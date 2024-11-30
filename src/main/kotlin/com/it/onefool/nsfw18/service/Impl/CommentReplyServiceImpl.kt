package com.it.onefool.nsfw18.service.Impl

import com.alibaba.fastjson2.JSON
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.common.service.CommentAndReplyService
import com.it.onefool.nsfw18.domain.dto.CommentReplyContentDto
import com.it.onefool.nsfw18.domain.dto.CommentReplyDto
import com.it.onefool.nsfw18.domain.dto.UserDto
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.domain.vo.CommentReplyVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CommentReplyMapper
import com.it.onefool.nsfw18.service.CommentReplyService
import com.it.onefool.nsfw18.utils.BeanUtils
import com.it.onefool.nsfw18.utils.JwtUtil
import com.it.onefool.nsfw18.utils.LocalDateUtils
import com.it.onefool.nsfw18.utils.RetryTemplateUtil
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.RuntimeException


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
    private lateinit var commentAndReplyService: CommentAndReplyService

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    private lateinit var beanUtils: BeanUtils

    @Autowired
    private lateinit var localDateUtils: LocalDateUtils

    @Autowired
    private lateinit var retry: RetryTemplateUtil<Boolean>


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
    override fun pageCommentReply(
        pageRequestDto: PageRequestDto<CommentReplyDto?>,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentReplyVo>> {
        pageRequestDto.body?.let { c ->
            c.commentId?.let {
                c.cartoonId?.let {
                    val userDto = jwtUtil.getToken(request)
                    //章节id为0时，说明没有章节id，则查询当前漫画详情下的所有评论
                    if (c.chapterId == null
                        || c.chapterId == 0
                    ) return getCommentReply(pageRequestDto, userDto)
                    return getCommentReplyChapter(pageRequestDto, userDto)
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
    ): Result<PageInfo<CommentReplyVo>> {
        val cartoonId = commentReply.cartoonId
        val commentId = commentReply.commentId
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
                return addCommentReplyInChapter(commentReply, userDto, request)
            }
        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }

    /**
     * 删除回复评论
     */
    override fun deleteCommentReply(id: Int) {
        val qw = QueryWrapper<CommentReply>()
        qw.eq("id", id)
        val comment = this.getOne(qw)
        transactionTemplate.execute {
            try {
                deleteCommentReplyRetry(id, comment)
                commentAndReplyService.removeRedisUserComments(comment!!)
            } catch (e: Exception) {
                logger.error("删除回复评论失败,失败原因,{}", e.printStackTrace())
                if (e.message.equals("SF")) {
                    val com = JSON.toJSONString(comment)
                    redisTemplate.opsForZSet().add(
                        CacheConstants.COMMENTS_REPLY
                                + comment!!.cartoonId
                                + comment.chapterId
                                + comment.commentId,
                        com, LocalDateUtils.localDateToDouble(comment.createdTime)
                    )
                    it.setRollbackOnly()
                } else if (e.message.equals("RF")) {
                    val com = JSON.toJSONString(comment)
                    redisTemplate.opsForZSet().rank(
                        CacheConstants.COMMENTS_REPLY
                                + comment!!.cartoonId
                                + comment.chapterId
                                + comment.commentId,
                        com
                    ) ?: {
                        val com = JSON.toJSONString(comment)
                        redisTemplate.opsForZSet().remove(
                            //重试删除回复评论
                            CacheConstants.COMMENTS_REPLY
                                    + comment.cartoonId
                                    + comment.chapterId
                                    + comment.commentId,
                            com
                        )
                    }
                }
                commentAndReplyService.removeRedisUserComments(comment!!)
                throw CustomizeException(
                    StatusCode.FORBIDDEN.code(), StatusCode.FORBIDDEN.message()
                )
            }
        }
    }

    /**
     * 提供给外部调用的方法
     */
    override fun getCommentsByPageMethod(
        commentReply: CommentReplyDto
    ): List<CommentReplyVo?> {
        return getCommentsByPage(commentReply).data.list
    }

    /**
     * 查看回复评论(如果redis没有数据需要进行缓存回填)
     */
    override fun findCommentReply(
        page: CommentReplyDto,
        request: HttpServletRequest
    ): Result<List<CommentReplyVo>> {
        var userId = 0
        val userDto = jwtUtil.getToken(request)
        if (userDto != null) userId = userDto.userId.toInt()
        val replyList = mutableListOf<CommentReplyVo>()
        val replyRedisCount = redisTemplate.opsForZSet().size(
            CacheConstants.COMMENTS_REPLY
                    + page.cartoonId
                    + page.chapterId
                    + page.commentId
        )
        val count = sqlReplyCount(page)
        var replyByRedis: Collection<Any>? = null
        if (replyRedisCount != 0L && replyRedisCount != null) {
            //redis的回复评论数量和数据库的回复评论数量相等，则从redis中获取
            if (replyRedisCount == count || replyRedisCount > count) {
                replyByRedis = redisTemplate.opsForZSet().range(
                    CacheConstants.COMMENTS_REPLY
                            + page.cartoonId
                            + page.chapterId
                            + page.commentId,
                    0,
                    -1
                )
                replyByRedis?.forEach { r ->
                    val c = r as CommentReply
                    val reply = CommentReplyVo().apply {
                        this.id = c.id
                        this.nickName = c.nickName
                        this.headImage = c.headImage
                        this.cartoonId = c.cartoonId
                        this.chapterId = c.chapterId
                        this.commentId = c.commentId
                        this.content = c.content
                        this.likes = c.likes
                        if (userId == c.userId) this.status = 0
                        else this.status = 1
                        this.level = 1
                        this.createdTime = c.createdTime
                        this.updatedTime = c.updatedTime
                    }
                    replyList.add(reply)
                }
            } else if (replyRedisCount < count) {
                return asyncRedisInsertReply(page, userId)
            }
        } else {
            //数据库与redis都无数据
            return if (count == 0L) Result.ok(replyList)
            else asyncRedisInsertReply(page, userId)
        }
        return Result.error()
    }

    /**
     * 异步缓存回填
     */
    private fun asyncRedisInsertReply(page: CommentReplyDto, userId: Int)
            : Result<List<CommentReplyVo>> {
        val replyBySql = sqlSelectReply(page, userId)
        //异步回填缓存
        CompletableFuture.runAsync({
            redisTemplate.opsForZSet().removeRange(
                CacheConstants.COMMENTS_REPLY
                        + page.cartoonId
                        + page.chapterId
                        + page.commentId,
                0,
                -1
            )
            //不需要担心主线程结束后数据丢失，因为jvm仍然在引用,不会回收
            replyBySql.data.forEach {
                val r = JSON.toJSONString(it)
                redisTemplate.opsForZSet().add(
                    CacheConstants.COMMENTS_REPLY
                            + page.cartoonId
                            + page.chapterId
                            + page.commentId,
                    r,
                    LocalDateUtils.localDateToDouble(it.createdTime)
                )
            }
        }, Executors.newSingleThreadExecutor())
        return replyBySql
    }

    /**
     * 从数据库中查出全部回复评论数量(只查询统计索引会更快速)
     */
    private fun sqlReplyCount(page: CommentReplyDto): Long {
        val qw = QueryWrapper<CommentReply>()
        qw.eq("cartoon_id", page.cartoonId)
        qw.eq("chapter_id", page.chapterId)
        qw.eq("comment_id", page.commentId)
        return this.count(qw)
    }

    /**
     * 从数据库中查询出全部回复评论列表
     */
    private fun sqlSelectReply(page: CommentReplyDto, userId: Int)
            : Result<List<CommentReplyVo>> {
        try {
            //说明当前没有章节id，则查询当前漫画详情下的所有评论
            if (page.chapterId == 0
                ||
                page.chapterId == null
            ) return Result.ok(getReplyAll(page, false, userId))
            else return Result.ok(getReplyAll(page, true, userId))
        } catch (e: Exception) {
            logger.error("从数据库中查询全部回复评论列表失败，失败具体信息{}", e.printStackTrace())
            throw CustomizeException(
                StatusCode.NOT_FOUND_COMMENT.code(),
                StatusCode.NOT_FOUND_COMMENT.message()
            )
        }
    }

    /**
     * 删除回复评论重试方法
     */
    private fun deleteCommentReplyRetry(id: Int, comment: CommentReply?) {
        val com = JSON.toJSONString(comment)
        val i = redisTemplate.opsForZSet().remove(
            CacheConstants.COMMENTS_REPLY
                    + comment!!.cartoonId
                    + comment.chapterId
                    + comment.commentId,
            com
        )
        if (i != null && i <= 0) throw RuntimeException("RF")
        val flag = this.baseMapper!!.deleteById(id)
        if (flag <= 0) throw RuntimeException("SF")
    }

    /**
     * 章节id为不为0，都是插入到回复评论表中，不需要分出两个方法
     */
    private fun addCommentReplyInChapter(
        commentReply: CommentReplyContentDto,
        userDto: UserDto,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentReplyVo>> {
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
        /*transactionTemplate.execute {
            try {
                redisInsertReply(reply)
                sqlInsertReply(reply)
            } catch (e: Exception) {
                it.setRollbackOnly()
                logger.error("回复评论事务插入失败，进行回滚,{}", e.message)
                redisTemplate.opsForZSet().remove(
                    CacheConstants.COMMENTS
                            + reply.cartoonId
                            + reply.chapterId
                            + reply.commentId,
                    reply
                )
                throw CustomizeException(
                    StatusCode.TRANSACTION_INSERTION_FAILED.code(),
                    StatusCode.TRANSACTION_INSERTION_FAILED.message()
                )
            }
        }*/
        //如果插入失败则抛出异常，不走以下流程
        sqlInsertReply(reply)
        redisInsertReply(reply)
        return pageCommentReply(
            PageRequestDto(1, 10, CommentReplyDto().apply {
                this.cartoonId = reply.cartoonId
                this.chapterId = reply.chapterId
                this.commentId = reply.commentId
            }), request
        )
    }

    /**
     * 往redis里面插入评论，插入失败则打印日志，但是不重试
     * 缓存回填则发生在查看数据库时进行
     */
    private fun redisInsertReply(r: CommentReply) {
        val cr = JSON.toJSONString(r)
        val add = redisTemplate.opsForZSet().add(
            CacheConstants.COMMENTS_REPLY
                    + r.cartoonId
                    + r.chapterId
                    + r.commentId,
            cr,
            LocalDateUtils.localDateToDouble(r.createdTime)
        )

        //转json存入redis,只存四个值，避免占用内存
        val com = CommentReply().apply {
            this.id = r.id
            this.userId = r.userId
            this.cartoonId = r.cartoonId
            this.chapterId = r.chapterId
        }
        val json = JSON.toJSONString(com)
        //插入评论数据,方便后面点赞数据在redis里面操作
        redisTemplate.opsForValue().set(CacheConstants.COMMENT_REPLY + r.id, json)

        if (add != null && add) commentAndReplyService.addRedisUserComments(r)
        else logger.error("redis插入评论数据失败")

    }

    /**
     * 往数据库插入评论
     */
    private fun sqlInsertReply(r: CommentReply) {
        transactionTemplate.execute {
            try {
                val flag = this.save(r)
                if (!flag) throw RuntimeException()
            } catch (e: Exception) {
                it.setRollbackOnly()
                logger.error("数据库插入二级评论失败")
                throw CustomizeException(
                    StatusCode.TRANSACTION_INSERTION_FAILED.code(),
                    StatusCode.TRANSACTION_INSERTION_FAILED.message()
                )
            }
        }
    }

    /**
     * 查询当前漫画下的章节的所有评论
     */
    private fun getCommentReplyChapter(
        p: PageRequestDto<CommentReplyDto?>,
        userDto: UserDto?
    )
            : Result<PageInfo<CommentReplyVo>> {
        //从redis里面查询回复评论
        val redisReply = getCommentsByPage(p.body!!)
        //如果redis里面没有评论记录就从mysql里面查询
        if (redisReply.data.list.isNullOrEmpty()) return getPageInfo(p, true, userDto)
        return redisReply
    }

    /**
     * 查询当前漫画的所有的回复评论(非章节id中的评论)
     */
    private fun getCommentReply(
        p: PageRequestDto<CommentReplyDto?>,
        userDto: UserDto?
    )
            : Result<PageInfo<CommentReplyVo>> {
        val redisReply = getCommentsByPage(p.body!!)
        if (redisReply.data.list.isNullOrEmpty()) return getPageInfo(p, false, userDto)
        return redisReply

    }

    /**
     * 通过反射分页查询数据库回复评论记录
     * false:查询当前漫画所有的评论 true:查询当前漫画下的章节所有的评论
     */
    private inline fun <reified T> getPageInfo(
        p: PageRequestDto<T>,
        b: Boolean,
        userDto: UserDto?
    )
            : Result<PageInfo<CommentReplyVo>> {
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
                //如果当前有章节id则查询当前章节id的评论
                if (b) qwReply.eq("chapter_id", chapterId)
            }
        }
        qwReply.orderByDesc("create_time")
        this.page(page, qwReply)
        val replyList = mutableListOf<CommentReplyVo>()
        beanUtils.copyCommentReplyAndReplyVo(page.records, replyList, userDto!!.userId.toInt())
        return Result.ok(
            PageInfo(
                page.current,
                page.size,
                page.total,
                page.pages,
                replyList
            )
        )
    }

    /**
     * 通过反射查询数据库回复评论记录
     * false:查询当前漫画所有的评论 true:查询当前漫画下的章节所有的评论
     */
    private inline fun <reified T> getReplyAll(
        p: T,
        b: Boolean,
        userId: Int
    )
            : List<CommentReplyVo> {
        val clazz = T::class.java
        val constructor = clazz.getConstructor()
        val instance = constructor.newInstance()
        val qwReply = QueryWrapper<CommentReply>()
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
                //如果当前有章节id则查询当前章节id的评论
                if (b) qwReply.eq("chapter_id", chapterId)
                else qwReply.eq("chapter_id", 0)
            }
        }
        qwReply.orderByDesc("create_time")
        val replyList = this.baseMapper!!.selectList(qwReply)
        val replyVoList = mutableListOf<CommentReplyVo>()
        if (replyList.isNullOrEmpty()) throw RuntimeException("F")
        beanUtils.copyCommentReplyAndCommentReplyVo(replyList, replyVoList, userId)
        return replyVoList
    }

    /**
     * 从redis里面查询回复评论
     */
    private fun getCommentsByPage(p: CommentReplyDto)
            : Result<PageInfo<CommentReplyVo>> {
        val replyList = mutableListOf<CommentReplyVo>()
        //从redis里面查询所有一级评论下的子评论
        val redisResult = redisTemplate.opsForZSet()
            .range(
                CacheConstants.COMMENTS_REPLY
                        + p.cartoonId
                        + p.chapterId
                        + p.commentId,
                0,
                -1
            )
        //总回复评论数
        val replyCount = redisTemplate.opsForZSet().size(
            CacheConstants.COMMENTS_REPLY
                    + p.cartoonId
                    + p.chapterId
                    + p.commentId
        )
        if (!redisResult.isNullOrEmpty() && replyCount != 0L) {
            beanUtils.copyCommentReplyAndReplyVo(redisResult, replyList, p.userId)
        }
        //-1代表无所谓值
        return Result.ok(
            PageInfo<CommentReplyVo>(
                -1,
                -1,
                replyCount,
                -1,
                replyList
            )
        )
    }


}
