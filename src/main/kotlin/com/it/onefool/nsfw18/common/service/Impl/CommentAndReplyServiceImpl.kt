package com.it.onefool.nsfw18.common.service.Impl

import com.alibaba.fastjson2.JSON
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.common.service.CommentAndReplyService
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.domain.vo.CommentReplyVo
import com.it.onefool.nsfw18.utils.BeanUtils
import com.it.onefool.nsfw18.utils.LocalDateUtils
import com.it.onefool.nsfw18.utils.RetryTemplateUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/11/24 18:06
 * 评论的公共方法实现类
 */
@Component
class CommentAndReplyServiceImpl : CommentAndReplyService {
    companion object {
        private val logger = LoggerFactory.getLogger(CommentAndReplyServiceImpl::class.java)
    }

    @Autowired
    private lateinit var beanUtils: BeanUtils

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var retry: RetryTemplateUtil<Boolean>

    /**
     * 当前用户每插入一条评论，就往redis中另外存储一次评论记录(此方法并没有持久化，需要重试机制，重试时间指数式增加)
     *
     * key: CacheConstants.COMMENTS_USER_ID + r.userId
     *
     * value: CommentReplyVo对象
     *
     * score: 评论的时间戳
     */
    override fun addRedisUserComments(r: Comment) {
        val replyVo = CommentReplyVo()
        //一级评论的插入记录使用评论回复vo对象
        beanUtils.copyCommentAndCommentReplyVo(r, replyVo)
        val rv = JSON.toJSONString(replyVo)
        retry.executeWithRetry {
            val flag = redisTemplate.opsForZSet().add(
                CacheConstants.COMMENTS_USER_ID + r.userId,
                rv,
                LocalDateUtils.localDateToDouble(r.createdTime)
            )
            if (!flag!!) {
                logger.error("在redis中添加用户评论格外记录失败,重试中....")
                throw IllegalStateException("在redis中添加用户评论格外记录失败")
            }
        }
    }

    /**
     * 当前用户每插入一条评论，就往redis中另外存储一次评论记录(此方法并没有持久化，需要重试机制，重试时间指数式增加)
     *
     * key: CacheConstants.COMMENTS_USER_ID + r.userId
     *
     * value: CommentReplyVo对象
     *
     * score: 评论的时间戳
     */
    override fun addRedisUserComments(r: CommentReply) {
        val replyVo = CommentReplyVo()
        val rv = JSON.toJSONString(replyVo)
        //二级评论的插入记录使用评论回复vo对象
        beanUtils.copyCommentReplyAndCommentReplyVo(r, replyVo)
        retry.executeWithRetry {
            val flag = redisTemplate.opsForZSet().add(
                CacheConstants.COMMENTS_USER_ID + r.userId,
                rv,
                LocalDateUtils.localDateToDouble(r.createdTime)
            )
            if (!flag!!) {
                logger.error("在redis中添加用户评论格外记录失败,重试中...")
                throw IllegalStateException("在redis中添加用户评论格外记录失败")
            }
        }
    }

    override fun removeRedisUserComments(r: Comment) {
        val replyVo = CommentReplyVo()
        val rv = JSON.toJSONString(replyVo)
        //一级评论的删除记录使用评论回复vo对象
        beanUtils.copyCommentAndCommentReplyVo(r, replyVo)
        retry.executeWithRetry {
            val flag = redisTemplate.opsForZSet().remove(
                CacheConstants.COMMENTS_USER_ID + r.userId,
                rv
            )
            if (flag == null || flag <= 0) {
                logger.error("在redis中删除用户评论格外记录失败,重试中....")
                throw IllegalStateException("在redis中删除用户评论格外记录失败")
            }
        }
    }

    override fun removeRedisUserComments(r: CommentReply) {
        val replyVo = CommentReplyVo()
        val rv = JSON.toJSONString(replyVo)
        //二级评论的删除记录使用评论回复vo对象
        beanUtils.copyCommentReplyAndCommentReplyVo(r, replyVo)
        retry.executeWithRetry {
            val flag = redisTemplate.opsForZSet().remove(
                CacheConstants.COMMENTS_USER_ID + r.userId,
                rv
            )
            if (flag == null || flag <= 0) {
                logger.error("在redis中删除用户评论格外记录失败,重试中.....")
                throw IllegalStateException("在redis中删除用户评论格外记录失败")
            }
        }
    }
}