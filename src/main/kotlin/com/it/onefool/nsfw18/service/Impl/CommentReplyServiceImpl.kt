package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.domain.dto.CommentReplyContentDto
import com.it.onefool.nsfw18.domain.dto.CommentReplyDto
import com.it.onefool.nsfw18.domain.dto.UserDto
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CommentReplyMapper
import com.it.onefool.nsfw18.service.CommentReplyService
import com.it.onefool.nsfw18.utils.JwtUtil
import com.it.onefool.nsfw18.utils.RedisCacheUtil
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime


/**
 * @author 97436
 * @description 针对表【comment_reply】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:32
 */
@Service
class CommentReplyServiceImpl
    : ServiceImpl<CommentReplyMapper?, CommentReply?>(), CommentReplyService {
    companion object {
        private val logger = LoggerFactory.getLogger(CommentReplyServiceImpl::class.java)
    }

    @Autowired
    private lateinit var redisCacheUtil: RedisCacheUtil

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate


    /**
     * 根据漫画id查询当前漫画回复评论
     * (此处需考虑内存溢出情况，暂时先不考虑，等线上做起来后要么扩充内存，要么优化表，反正最后都得扩充内存)
     * (做大做强就得加硬件设施)
     */
    override fun findByCommentId(id: Int): Result<List<CommentReply?>?> {
        val qwReply = QueryWrapper<CommentReply>()
        qwReply.eq("cartoon_id", id)
        return Result.ok(this.list(qwReply))
    }

    /**
     * 分页查询回复评论
     */
    override fun pageCommentReply(pageRequestDto: PageRequestDto<CommentReplyDto?>)
            : Result<PageInfo<CommentReply>> {
        pageRequestDto.body?.let { c ->
            c.commentId?.let {
                c.cartoonId?.let {
                    //章节id为0时，说明没有章节id，则查询当前漫画详情下的所有评论
                    if (c.chapterId == null
                        || c.chapterId == 0
                    ) return getCommentReply(pageRequestDto)
                    return getCommentReplyChapter(pageRequestDto)
                }
            }
        }
        throw CustomizeException(
            StatusCode.NOT_FOUND_COMMENT.code(),
            StatusCode.NOT_FOUND_COMMENT.message()
        )
    }

    /**
     * 插入回复评论
     */
    override fun addCommentReply(
        commentReply: CommentReplyContentDto,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentReply>> {
        val cartoonId = commentReply.cartoonId
        val commentId = commentReply.commentId
        val chapterId = commentReply.chapterId
        val content = commentReply.content
        cartoonId?.let {
            commentId?.let {
                // 评论不能为空
                if (content.isNullOrBlank()) throw CustomizeException(
                    StatusCode.COMMENTS_CANNOT_BE_EMPTY.code(),
                    StatusCode.COMMENTS_CANNOT_BE_EMPTY.message()
                )
                val userDto = jwtUtil.getToken(request) ?: throw CustomizeException(
                    StatusCode.UNAUTHORIZED.code(),
                    StatusCode.UNAUTHORIZED.message()
                )
                return addCommentReplyInChapter(commentReply, userDto)
            }
        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }

    /**
     * 章节id为不为0，都是插入到回复评论表中，不需要分出两个方法
     */
    private fun addCommentReplyInChapter(
        commentReply: CommentReplyContentDto,
        userDto: UserDto
    )
            : Result<PageInfo<CommentReply>> {
        val cartoonId = commentReply.cartoonId
        val commentId = commentReply.commentId
        val chapterId = commentReply.chapterId
        val content = commentReply.content
        val reply = CommentReply().apply {
            this.userId = userDto.userId.toInt()
            this.nickName = userDto.user.nickname
            this.headImage = userDto.user.avatar
            this.likes = 0
            this.cartoonId = cartoonId
            this.commentId = commentId
            this.chapterId = chapterId
            this.content = content
        }
        transactionTemplate.execute {
            try {
                redisInsertReply(reply)
                sqlInsertReply(reply)
            } catch (e: Exception) {
                it.setRollbackOnly()
                logger.error("事务插入失败，进行回滚", e)
                redisTemplate.opsForZSet().remove(CacheConstants.COMMENTS, reply)
                throw CustomizeException(
                    StatusCode.TRANSACTION_INSERTION_FAILED.code(),
                    StatusCode.TRANSACTION_INSERTION_FAILED.message()
                )
            }
        }

        return Result.ok()
    }

    /**
     * 往redis里面插入评论
     */
    private fun redisInsertReply(r: CommentReply) {
        val add = redisTemplate.opsForZSet().add(
            CacheConstants.COMMENTS
                    + r.cartoonId
                    + r.chapterId
                    + r.commentId,
            r,
            localDateUtil(r.createdTime)
        )
        if (add == null || !add) {
            logger.error("Failed to insert reply into Redis")
            throw CustomizeException(
                StatusCode.TRANSACTION_INSERTION_FAILED.code(),
                StatusCode.TRANSACTION_INSERTION_FAILED.message()
            )
        }
    }

    /**
     * 往数据库插入评论
     */
    private fun sqlInsertReply(r: CommentReply) {
        this.save(r)
    }

    private fun getCommentReplyChapter(p: PageRequestDto<CommentReplyDto?>)
            : Result<PageInfo<CommentReply>> {
        val redisReply = getCommentsByPage(p)
        if (redisReply.data.list.isNullOrEmpty()) return getPageInfo(p, true)
        return redisReply
    }

    /**
     * 不在章节内的回复评论
     */
    private fun getCommentReply(p: PageRequestDto<CommentReplyDto?>)
            : Result<PageInfo<CommentReply>> {
        return getPageInfo(p, false)

    }

    private inline fun <reified T> getPageInfo(
        p: PageRequestDto<T>,
        b: Boolean
    )
            : Result<PageInfo<CommentReply>> {
        val pageSize = p.size
        val pageCurrent = p.page
        val clazz = T::class.java
        val constructor = clazz.getConstructor()
        val instance = constructor.newInstance()
        val qwReply = QueryWrapper<CommentReply>()
        val page = Page<CommentReply>(pageCurrent, pageSize)
        clazz.declaredFields.forEach {
            it.setAccessible(true)
            if (it.name == "commentId") {
                val commentId = it.get(instance) as Int
                qwReply.eq("comment_id", commentId)
            }
            if (it.name == "cartoonId") {
                val cartoonId = it.get(instance) as Int
                qwReply.eq("cartoon_id", cartoonId)
            }
            if (it.name == "chapterId") {
                val chapterId = it.get(instance) as Int
                if (b) qwReply.eq("chapter_id", chapterId)
            }
        }
        qwReply.orderByDesc("create_time")
        this.page(page, qwReply)
        return Result.ok(
            PageInfo(
                page.current,
                page.size,
                page.total,
                page.pages,
                page.records
            )
        )
    }

    /**
     * 将LocalDateTime转换成Double
     */
    private fun localDateUtil(l: LocalDateTime): Double {
        // 将LocalDateTime转换为ZonedDateTime，指定时区
        val zonedDateTime: ZonedDateTime = l.atZone(ZoneId.systemDefault())
        // 将ZonedDateTime转换为Instant
        val instant = zonedDateTime.toInstant()
        // 获取自Unix纪元以来的毫秒数
        val millisecondsSinceEpoch = instant.toEpochMilli()
        // 将毫秒数转换为double
        return millisecondsSinceEpoch.toDouble()
    }

    /**
     * 从redis里面查询评论
     */
    private fun getCommentsByPage(p: PageRequestDto<CommentReplyDto?>)
            : Result<PageInfo<CommentReply>> {
        val pageCurrent = p.page
        val pageSize = p.size
        val data = p.body
        val start = (pageCurrent - 1) * pageSize.toLong()
        val end = start + pageSize.toLong() - 1
        var redisResult = redisTemplate.opsForZSet()
            .reverseRangeByScore(
                CacheConstants.COMMENTS
                        + data!!.cartoonId
                        + data.chapterId
                        + data.commentId,
                0.0,
                System.currentTimeMillis().toDouble(),
                start,
                end
            )
            ?.map { it as CommentReply }
        val replyCount = redisTemplate.opsForZSet().size(
            CacheConstants.COMMENTS
                    + data.cartoonId
                    + data.chapterId
                    + data.commentId
        )
        if (redisResult.isNullOrEmpty() || replyCount == 0L) redisResult = emptyList()
        return Result.ok(
            PageInfo<CommentReply>(
                -1,
                -1,
                replyCount,
                -1,
                redisResult
            )
        )
    }
}
