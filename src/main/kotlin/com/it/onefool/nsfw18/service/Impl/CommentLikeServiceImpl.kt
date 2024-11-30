package com.it.onefool.nsfw18.service.Impl

import com.alibaba.fastjson2.JSON
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.domain.dto.UserDto
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.entry.CommentLike
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CommentLikeMapper
import com.it.onefool.nsfw18.service.CommentLikeService
import com.it.onefool.nsfw18.utils.JwtUtil
import com.it.onefool.nsfw18.utils.LocalDateUtils
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * @author 97436
 * @description 针对表【comment_like】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:25
 */
@Service
class CommentLikeServiceImpl
    : ServiceImpl<CommentLikeMapper?, CommentLike?>(), CommentLikeService {

    companion object {
        private val logger = LoggerFactory.getLogger(CommentLikeServiceImpl::class.java)
    }

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    /**
     * 点赞评论(由于点赞可能处于高并发下，决定凌晨2点定时同步到数据库)
     * id: 评论id level: 评论级别
     */
    override fun addCommentLike(id: Int, level: Int, request: HttpServletRequest)
            : Result<String> {
        val userDto = jwtUtil.getToken(request) ?: throw CustomizeException(
            StatusCode.UNAUTHORIZED.code(), StatusCode.UNAUTHORIZED.message()
        )
        when (level) {
            0 -> {
                like(
                    CacheConstants.COMMENT_LIKE,
                    CacheConstants.COMMENTS,
                    id,
                    userDto,
                    Comment::class.java
                )
                return Result.ok()
            }

            1 -> {
                like(
                    CacheConstants.COMMENT_REPLY_LIKE,
                    CacheConstants.COMMENTS_REPLY,
                    id,
                    userDto,
                    CommentReply::class.java
                )
                return Result.ok()
            }

            else -> {
                return Result.error()
            }
        }

    }

    /**
     * 取消点赞评论
     * id: 评论id level: 评论级别
     */
    override fun deleteCommentLike(id: Int, level: Int, request: HttpServletRequest)
            : Result<String> {
        val userDto = jwtUtil.getToken(request) ?: throw CustomizeException(
            StatusCode.UNAUTHORIZED.code(), StatusCode.UNAUTHORIZED.message()
        )
        when (level) {
            0 -> {
                //一级评论取消点赞
                redisTemplate.opsForValue().decrement(CacheConstants.COMMENT_LIKE + id, 1)
                // 当前点赞记录
                val userKey = CacheConstants.COMMENT_LIKE + "user:$id"
                //取消点赞记录
                redisTemplate.opsForSet().remove(userKey,userDto.userId.toString())
                //从redis中获取评论详情后去修改评论列表中的点赞数量
                val json = redisTemplate.opsForValue().get(
                    CacheConstants.COMMENTS + id
                )
                //如果没有数据说明，添加评论的时候redis添加失败，又或者是淘汰策略删除了评论，导致评论没有
                //不能从db里面查，点赞全程redis操作
                if (json == null) {
                    redisTemplate.opsForValue().decrement(CacheConstants.COMMENT_LIKE + id, 1)
                    throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
                }
                val comment = JSON.parse(json.toString()) as Comment
                val redisResult = redisTemplate.opsForZSet().range(
                    CacheConstants.COMMENT + comment.cartoonId + comment.chapterId, 0, 1
                ) ?: throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
                redisResult.forEach { f ->
                    val com = JSON.parse(f) as Comment
                    if (com.id == comment.id) {
                        redisTemplate.opsForZSet().remove(
                            CacheConstants.COMMENT + comment.cartoonId + comment.chapterId, com
                        )
                        val likes = getLikes(CacheConstants.COMMENT_LIKE) ?: 0
                        com.likes = likes.toInt()
                        val comJson = JSON.toJSONString(com)
                        redisTemplate.opsForZSet().add(
                            CacheConstants.COMMENT + comment.cartoonId + comment.chapterId,
                            comJson,
                            LocalDateUtils.localDateToDouble(com.createdTime)
                        )
                    }
                }
                return Result.ok()
            }

            1 -> {

                //一级评论取消点赞
                redisTemplate.opsForValue().decrement(CacheConstants.COMMENT_REPLY_LIKE + id, 1)
                // 当前点赞记录
                val userKey = CacheConstants.COMMENT_REPLY_LIKE + "user:$id"
                //取消点赞记录
                redisTemplate.opsForSet().remove(userKey,userDto.userId.toString())
                //从redis中获取评论详情后去修改评论列表中的点赞数量
                val json = redisTemplate.opsForValue().get(
                    CacheConstants.COMMENTS_REPLY + id
                )
                //如果没有数据说明，添加评论的时候redis添加失败，又或者是淘汰策略删除了评论，导致评论没有
                //不能从db里面查，点赞全程redis操作
                if (json == null) {
                    redisTemplate.opsForValue().decrement(CacheConstants.COMMENT_REPLY_LIKE + id, 1)
                    throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
                }
                val commentR = JSON.parse(json.toString()) as CommentReply
                val redisResult = redisTemplate.opsForZSet().range(
                    CacheConstants.COMMENT_REPLY + commentR.cartoonId + commentR.chapterId, 0, 1
                ) ?: throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
                redisResult.forEach { f ->
                    val com = JSON.parse(f) as CommentReply
                    if (com.id == commentR.id) {
                        redisTemplate.opsForZSet().remove(
                            CacheConstants.COMMENTS_REPLY
                                    + commentR.cartoonId
                                    + commentR.chapterId, com
                        )
                        val likes = getLikes(CacheConstants.COMMENT_REPLY_LIKE) ?: 0
                        com.likes = likes.toInt()
                        val comJson = JSON.toJSONString(com)
                        redisTemplate.opsForZSet().add(
                            CacheConstants.COMMENTS_REPLY + commentR.cartoonId + commentR.chapterId,
                            comJson,
                            LocalDateUtils.localDateToDouble(com.createdTime)
                        )
                    }
                }
                return Result.ok()
            }

            else -> {
                return Result.error()
            }
        }
    }

    /**
     * 获取总点赞数
     */
    private fun getLikes(str: String): Long? {
        return redisTemplate.opsForValue().size(str)
    }

    /**
     * strLike: 点赞记录的前缀key
     * str: 评论的key
     * id: 评论的id
     * userDto: 用户信息
     */
    private fun <T> like(strLike: String, str: String, id: Int, userDto: UserDto, t: Class<T>) {
        //评论点赞 +1
        redisTemplate.opsForValue().increment(
            strLike + id, 1
        )
        // 当前点赞记录
        val userKey = strLike + "user:$id"
        //记录点赞人的 ID   一个评论点赞记录存在被多个人点赞的情况下
        redisTemplate.opsForSet().add(userKey, userDto.userId.toString())
        //从redis中获取评论详情后去修改评论列表中的点赞数量
        val json = redisTemplate.opsForValue().get(
            str + id
        )
        //如果没有数据说明，添加评论的时候redis添加失败，又或者是淘汰策略删除了评论，导致评论没有
        //不能从db里面查，点赞全程redis操作
        if (json == null) {
            redisTemplate.opsForValue().decrement(
                strLike + id, 1
            )
            throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
        }
        val commentReplyStr = JSON.parseObject(json.toString(), t)
        var cartoonId: Int = 0
        var chapterId: Int = 0
        //评论id
        var cid: Int = 0
        if (t == Comment::class.java) {
            val comment = commentReplyStr as Comment
            cartoonId = comment.cartoonId
            chapterId = comment.chapterId
            cid = comment.id
        } else {
            val commentReply = commentReplyStr as CommentReply
            cartoonId = commentReply.cartoonId
            chapterId = commentReply.chapterId
            cid = commentReply.id
        }
        val redisResult = redisTemplate.opsForZSet().range(
            str + cartoonId + chapterId, 0, 1
        )
        //更新点赞数量
        redisResult?.forEach { f ->
            var cCartoonId: Int = 0
            var cChapterId: Int = 0
            //评论id
            var cCid: Int = 0
            var cLikes = 0
            var comm = Any()
            if (t == Comment::class.java) {
                val comment = JSON.parseObject(f, t)
                cCartoonId = comment.cartoonId
                cChapterId = comment.chapterId
                cCid = comment.id
                cLikes = comment.likes
                comm = comment
            } else if (t == CommentReply::class.java) {
                val commentR = JSON.parseObject(f, t)
                cCartoonId = commentR.cartoonId
                cChapterId = commentR.chapterId
                cCid = commentR.id
                cLikes = commentR.likes
                comm = commentR
            }
            //可能会出现同一个时间戳有多个点赞记录，所以需要拿唯一值id去判断是否是之前的点赞记录
            //如果是同一个评论id的话说明是在同一个评论下进行操作的
            if (cCid == cid) {
                redisTemplate.opsForZSet().remove(
                    str + cCartoonId + cChapterId, f
                )
                val likeSize = getLikes(strLike + id)
                cLikes = likeSize!!.toInt()
                val comme = comm::class.java
                val field = comme.getDeclaredField("likes")
                field.isAccessible = true
                //设置点赞数
                field.set(comme.constructors, cLikes)
                val com = JSON.toJSONString(comm)
                redisTemplate.opsForZSet().add(
                    str + cCartoonId + cChapterId, com,
                    LocalDateUtils.localDateToDouble(
                        comm::class.java.getDeclaredField("createdTime")
                            .get(comme.constructors) as LocalDateTime
                    )
                )
            }
        }
    }
}
