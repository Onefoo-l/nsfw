package com.it.onefool.nsfw18.schedule

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.domain.entry.CommentLike
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.domain.entry.CommentReplyLike
import com.it.onefool.nsfw18.mapper.CommentReplyLikeMapper
import com.it.onefool.nsfw18.service.CommentLikeService
import com.it.onefool.nsfw18.service.CommentReplyLikeService
import com.it.onefool.nsfw18.service.Impl.CommentLikeServiceImpl
import com.it.onefool.nsfw18.utils.LocalDateUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime

/**
 * @Author linjiawei
 * @Date 2024/11/23 22:00
 * @Description redis的定时任务
 */
@Configuration
class RedisSchedule {
    companion object {
        private val logger = LoggerFactory.getLogger(RedisSchedule::class.java)
        private val time = LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
    }

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var commentLikeService: CommentLikeService

    @Autowired
    private lateinit var commentReplyLikeService: CommentReplyLikeService

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    /**
     * 每天凌晨2点从redis中的点赞评论数据同步到数据库
     * (以redis为准，数据库只做持久化)
     */
    @Scheduled(cron = "0 0 2 * * ?")
    fun commentLikeToDatabase() {
        //redis中的数据总量
        val likeSize = redisTemplate.opsForValue().size(CacheConstants.COMMENT_LIKE + "*") ?: 0
        val likeReplySize = redisTemplate.opsForValue().size(
            CacheConstants.COMMENT_REPLY_LIKE + "*"
        ) ?: 0
        //db中的数据总量
        val count = commentLikeService.count()
        val replyCount = commentReplyLikeService.count()
        //说明redis增加了记录，同步到mysql中
        if (((likeSize + likeReplySize) - (count + replyCount)).toInt() > 0) {
            logger.info("凌晨2点redis点赞记录增加开始同步数据库,时间为::{}", time)
            if (likeSize != 0L) {
                redisTemplate.scan(
                    ScanOptions.scanOptions().match(CacheConstants.COMMENT_LIKE + "*").build()
                )
                    .forEach { f ->
                        //每个key都对应一个评论id的点赞记录，一个评论存在被多个人点赞的情况下
                        //所以得确认评论id和点赞人的id才可以添加到数据库中(我们并不知道点赞评论的id)
                        val id = f.split(":")[1].toInt()
                        //评论人Key
                        val userKey = CacheConstants.COMMENT_LIKE + "user:$id"
                        //肯定是有用户id的，因为评论点赞记录存在，就会存入一条相对应的用户id记录
                        redisTemplate.opsForSet().members(userKey)!!.forEach { u ->
                            val qw = QueryWrapper<CommentLike>()
                            qw.eq("user_id", u)
                            qw.eq("comment_id", id)
                            //如果数据库没有记录，说明redis并没有同步到数据库中，则添加
                            transactionTemplate.execute { t ->
                                try {
                                    commentLikeService.getOne(qw) ?: commentLikeService.save(CommentLike()
                                        .apply {
                                            this.userId = userId!!.toInt()
                                            this.commentId = id
                                        })
                                } catch (e: Exception) {
                                    logger.error("redis同步到数据库失败，原因::{},时间为{}", e.message, time)
                                    t.setRollbackOnly()
                                }
                            }
                        }
                    }
            } else if (likeReplySize != 0L) {
                redisTemplate.scan(
                    ScanOptions.scanOptions().match(CacheConstants.COMMENT_LIKE + "*").build()
                )
                    .forEach { f ->
                        //每个key都对应一个评论id的点赞记录，一个评论存在被多个人点赞的情况下
                        //所以得确认评论id和点赞人的id才可以添加到数据库中(我们并不知道点赞评论的id)
                        val id = f.split(":")[1].toInt()
                        //评论人Key
                        val userKey = CacheConstants.COMMENT_REPLY_LIKE + "user:$id"
                        //肯定是有用户id的，因为评论点赞记录存在，就会存入一条相对应的用户id记录
                        redisTemplate.opsForSet().members(userKey)!!.forEach { u ->
                            val qw = QueryWrapper<CommentReplyLike>()
                            qw.eq("user_id", u)
                            qw.eq("reply_id", id)
                            //如果数据库没有记录，说明redis并没有同步到数据库中，则添加
                            transactionTemplate.execute { t ->
                                try {
                                    commentReplyLikeService.getOne(qw) ?: commentReplyLikeService.save(
                                        CommentReplyLike().apply {
                                            this.userId = userId!!.toInt()
                                            this.replyId = id
                                        })
                                } catch (e: Exception) {
                                    logger.error("redis同步数据库失败，原因{},时间{}", e.message, time)
                                    t.setRollbackOnly()
                                }
                            }
                        }
                    }
            }
        } else if (((likeSize + likeReplySize) - (count + replyCount)).toInt() < 0) {
            logger.info("凌晨2点redis点赞记录取消开始同步数据库,时间为::{}", time)
            //db数据库一级评论不为空
            if (count.toInt() != 0) {
                val qw = QueryWrapper<CommentLike>()
                commentLikeService.baseMapper.selectList(qw).forEach outer@{ f ->
                    val commentId = f!!.commentId
                    val id = f.userId
                    transactionTemplate.execute { t ->
                        try {
                            redisTemplate.opsForSet().members(
                                CacheConstants.COMMENT_LIKE + "user:$commentId"
                            )?.forEach fr@{ u ->
                                var i = 0
                                if (u.toInt() != id) i++
                                else return@fr
                                //如果redis中该评论id对应的点赞用户id集合中，不存在该用户id，则从db中删除该条点赞记录
                                if (i.toLong() == redisTemplate.opsForSet().size(
                                        CacheConstants.COMMENT_LIKE + "user:$commentId"
                                    )
                                ) commentLikeService.baseMapper.deleteById(f.id)
                            } ?: commentLikeService.baseMapper.deleteById(f.id)
                        } catch (e: Exception) {
                            logger.error("redis同步数据库失败，原因::{},时间::{}", e.message, time)
                            t.setRollbackOnly()
                        }
                    }
                }
            } else if (replyCount.toInt() != 0) {
                val qw = QueryWrapper<CommentReplyLike>()
                commentReplyLikeService.baseMapper.selectList(qw).forEach outer@{ f ->
                    val replyId = f!!.replyId
                    val id = f.userId
                    transactionTemplate.execute { t ->
                        try {
                            redisTemplate.opsForSet().members(
                                CacheConstants.COMMENT_REPLY_LIKE + "user:$replyId"
                            )?.forEach fr@{ u ->
                                var i = 0
                                if (u.toInt() != id) i++
                                else return@fr
                                //如果redis中该评论id对应的点赞用户id集合中，不存在该用户id，则从db中删除该条点赞记录
                                if (i.toLong() == redisTemplate.opsForSet().size(
                                        CacheConstants.COMMENT_REPLY_LIKE + "user:$replyId"
                                    )
                                ) commentReplyLikeService.baseMapper.deleteById(f.id)
                            } ?: commentReplyLikeService.baseMapper.deleteById(f.id)
                        } catch (e: Exception) {
                            logger.error("redis同步数据库失败...，原因{},时间:::{}", e.message, time)
                            t.setRollbackOnly()
                        }
                    }
                }
            }
        }
        //如果两者相等，说明redis没有增加记录，不需要同步
        logger.info("不需要同步数据库,时间::{}", time)
    }
}