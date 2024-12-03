package com.it.onefool.nsfw18.schedule

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.domain.entry.CommentLike
import com.it.onefool.nsfw18.domain.entry.CommentReplyLike
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.service.CommentLikeService
import com.it.onefool.nsfw18.service.CommentReplyLikeService
import com.it.onefool.nsfw18.utils.LocalDateUtils
import com.it.onefool.nsfw18.utils.MinioUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime
import kotlin.math.log


/**
 * @Author linjiawei
 * @Date 2024/11/23 22:00
 * @Description redis的定时任务
 */
@Configuration
class RedisSchedule {
    companion object {
        private val logger = LoggerFactory.getLogger(RedisSchedule::class.java)
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
    @Scheduled(cron = "30 04 21 * * ?")
    fun commentLikeToDatabase() {
        var likeSizeSum = 0
        var likeReplySizeSum = 0
        //redis中的数据总量
        val likeSize = redisTemplate.opsForValue().operations.keys(
            CacheConstants.COMMENT_LIKE + "*"
        )
        val likeReplySize = redisTemplate.opsForValue().operations.keys(
            CacheConstants.COMMENT_REPLY_LIKE + "*"
        )
        if (likeSize != null) likeSizeSum = likeSize.size
        if (likeReplySize != null) likeReplySizeSum = likeReplySize.size
        logger.info("redis总量{}", likeSizeSum + likeReplySizeSum)
        //db中的数据总量
        val count = commentLikeService.count()
        val replyCount = commentReplyLikeService.count()
        logger.info("db中的数据总量{}", count + replyCount)

        // 获取 Redis 中所有点赞记录
        val redisKeys = redisTemplate.opsForValue().operations.keys(
            CacheConstants.COMMENT_LIKE + "*"
        ) ?: emptySet()
        val redisReplyKeys = redisTemplate.opsForValue().operations.keys(
            CacheConstants.COMMENT_REPLY_LIKE + "*"
        ) ?: emptySet()
        val redisLikes = mutableSetOf<Pair<Int, Int>>() // 使用 Pair 存储 (commentId, userId)
        val redisReplyLikes = mutableSetOf<Pair<Int, Int>>() // 使用 Pair 存储 (commentId, userId)

        redisKeys.forEach { key ->
            val commentId = key.split(":")[1].toInt() // 从 key 中提取评论 ID
            val userIds = redisTemplate.opsForList().range(
                CacheConstants.THUMBS_UP_USERS_KEY + "$commentId", 0, -1
            ) ?: emptyList()
            userIds.forEach { userId ->
                redisLikes.add(commentId to userId.toInt())
            }
        }
        redisReplyKeys.forEach { key ->
            val commentId = key.split(":")[1].toInt() // 从 key 中提取评论 ID
            val userIds = redisTemplate.opsForList().range(
                CacheConstants.THUMBS_UP_USERS_REPLY_KEY + "$commentId", 0, -1
            ) ?: emptyList()
            userIds.forEach { userId ->
                redisReplyLikes.add(commentId to userId.toInt())
            }
        }
        // 获取数据库中的所有点赞记录
        val dbLikes = commentLikeService.baseMapper.selectList(QueryWrapper<CommentLike>()).map {
            it?.commentId to it?.userId
        }.toSet()
        // 获取数据库中的所有点赞记录
        val dbReplyLikes = commentReplyLikeService.baseMapper.selectList(
            QueryWrapper<CommentReplyLike>()
        ).map {
            it?.replyId to it?.userId
        }.toSet()
        // 计算需要新增和删除的记录
        val toAdd = redisLikes - dbLikes // Redis 中有，数据库中没有(一级)
        val toAddReply = redisReplyLikes - dbReplyLikes //(二级)
        val toRemove = dbLikes - redisLikes // 数据库中有，Redis 中没有
        val toRemoveReply = dbReplyLikes - redisReplyLikes // (二级)
        transactionTemplate.execute {
            try {
                // 批量新增到数据库
                toAdd.chunked(1000).forEach { f ->
                    f.forEach { (commentId, userId) ->
                        commentLikeService.save(CommentLike().apply {
                            this.commentId = commentId
                            this.userId = userId
                        })
                    }
                }
                // 批量新增到数据库
                toAddReply.chunked(1000).forEach { f ->
                    f.forEach { (commentId, userId) ->
                        commentReplyLikeService.save(CommentReplyLike().apply {
                            this.replyId = commentId
                            this.userId = userId
                        })
                    }
                }

                // 批量从数据库中删除
                toRemove.chunked(1000).forEach { f ->
                    f.forEach { (commentId, userId) ->
                        commentLikeService.remove(
                            QueryWrapper<CommentLike>().eq("comment_id", commentId).eq("user_id", userId)
                        )
                    }
                }
                // 批量从数据库中删除
                toRemoveReply.chunked(1000).forEach { f ->
                    f.forEach { (commentId, userId) ->
                        commentReplyLikeService.remove(
                            QueryWrapper<CommentReplyLike>().eq("reply_id", commentId).eq("user_id", userId)
                        )
                    }
                }
            } catch (e: Exception) {
                logger.error("同步记录失败,原因::{}", e.message)
                it.setRollbackOnly()
                throw CustomizeException(StatusCode.UNAUTHORIZED.code(), e.message.toString())
            }

        }
        logger.info(
            "同步完成：新增一级 ${toAdd.size} 条，" +
                    "删除一级 ${toRemove.size} 条，" +
                    "新增二级 ${toAddReply.size} 条，" +
                    "删除二级 ${toRemoveReply.size} 条" +
                    "时间为{${LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())}}"
        )


        /*//说明redis增加了记录，同步到mysql中
            if (likeSizeSum - count >= 0) {
                logger.info(
                    "凌晨2点redis一级点赞记录增加开始同步数据库,时间为::{}",
                    LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                )
                //获取所有的key
                redisTemplate.opsForValue().operations.keys(CacheConstants.COMMENT_LIKE + "*")
                    ?.forEach { f ->
                        //每个key都对应一个评论id的点赞记录，一个评论存在被多个人点赞的情况下
                        //所以得确认评论id和点赞人的id才可以添加到数据库中(我们并不知道点赞评论的id)
                        val id = f.split(":")[1].toInt()
                        //评论人Key
                        val userKey = CacheConstants.THUMBS_UP_USERS_KEY + "$id"
                        //肯定是有用户id的，因为评论点赞记录存在，就会存入一条相对应的用户id记录
                        redisTemplate.opsForList().range(userKey, 0, -1)?.forEach { u ->
                            val qw = QueryWrapper<CommentLike>()
                            qw.eq("user_id", u)
                            qw.eq("comment_id", id)
                            //如果数据库没有记录，说明redis并没有同步到数据库中，则添加
                            transactionTemplate.execute { t ->
                                try {
                                    if (commentLikeService.getOne(qw) == null) {
                                        commentLikeService.save(CommentLike()
                                            .apply {
                                                this.userId = u.toInt()
                                                this.commentId = id
                                            })
                                    }
                                } catch (e: Exception) {
                                    logger.error(
                                        "redis同步到数据库失败，原因::{},时间为{}", e.printStackTrace(),
                                        LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                                    )
                                    t.setRollbackOnly()
                                }
                            }
                        }
                    }
            }
            if (likeReplySizeSum - replyCount >= 0) {
                logger.info(
                    "凌晨2点redis二级点赞记录增加开始同步数据库,时间为::{}",
                    LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                )
                redisTemplate.opsForValue().operations.keys(CacheConstants.COMMENT_REPLY_LIKE + "*")
                    ?.forEach { f ->
                        //每个key都对应一个评论id的点赞记录，一个评论存在被多个人点赞的情况下
                        //所以得确认评论id和点赞人的id才可以添加到数据库中(我们并不知道点赞评论的id)
                        val id = f.split(":")[1].toInt()
                        //评论人Key
                        val userKey = CacheConstants.THUMBS_UP_USERS_REPLY_KEY + "$id"
                        //肯定是有用户id的，因为评论点赞记录存在，就会存入一条相对应的用户id记录
                        redisTemplate.opsForList().range(userKey, 0, -1)?.forEach { u ->
                            val qw = QueryWrapper<CommentReplyLike>()
                            qw.eq("user_id", u)
                            qw.eq("reply_id", id)
                            //如果数据库没有记录，说明redis并没有同步到数据库中，则添加
                            transactionTemplate.execute { t ->
                                try {
                                    if (commentReplyLikeService.getOne(qw) == null) {
                                        commentReplyLikeService.save(
                                            CommentReplyLike().apply {
                                                this.userId = u.toInt()
                                                this.replyId = id
                                            })
                                    }
                                } catch (e: Exception) {
                                    logger.error(
                                        "redis同步数据库失败，原因{},时间{}", e.printStackTrace(),
                                        LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                                    )
                                    t.setRollbackOnly()
                                }
                            }
                        }
                    }
            }

            //db数据库一级评论不为空
            if ((count - likeSizeSum) >= 0) {
                logger.info(
                    "凌晨2点redis一级点赞记录取消开始同步数据库,时间为::{}",
                    LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                )
                val qw = QueryWrapper<CommentLike>()
                val list = commentLikeService.baseMapper.selectList(qw)
                list.forEach outer@{ f ->
                    val commentId = f!!.commentId
                    val id = f.userId
                    transactionTemplate.execute { t ->
                        try {
                            // 获取 Redis 中该评论 ID 对应的所有点赞用户
                            val redisUsers = redisTemplate.opsForList().range(
                                CacheConstants.THUMBS_UP_USERS_KEY
                                        + "$commentId", 0, -1
                            )
                            // 如果 Redis 没有记录该用户 ID，则从数据库中删除该记录
                            if (redisUsers == null || !redisUsers.contains(id.toString())) {
                                commentLikeService.baseMapper.deleteById(f.id)
                                logger.info("删除数据库中无效的点赞记录: commentId=$commentId, userId=$id")
                            }
                        } catch (e: Exception) {
                            logger.error(
                                "redis同步数据库失败，原因::{},时间::{}", e.message,
                                LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                            )
                            t.setRollbackOnly()
                        }
                    }
                }
            }
            if (replyCount- likeReplySizeSum >= 0) {
                logger.info(
                    "凌晨2点redis二级点赞记录取消开始同步数据库,时间为::{}",
                    LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                )
                val qw = QueryWrapper<CommentReplyLike>()
                commentReplyLikeService.baseMapper.selectList(qw).forEach outer@{ f ->
                    val replyId = f!!.replyId
                    val id = f.userId
                    transactionTemplate.execute { t ->
                        try {
                            // 获取 Redis 中该评论 ID 对应的所有点赞用户
                            val redisUsers = redisTemplate.opsForList().range(
                                CacheConstants.THUMBS_UP_USERS_REPLY_KEY + "$replyId", 0, -1
                            )
                            // 如果 Redis 没有记录该用户 ID，则从数据库中删除该记录
                            if (redisUsers == null || !redisUsers.contains(id.toString())) {
                                commentReplyLikeService.baseMapper.deleteById(f.id)
                                logger.info("删除数据库中无效回复的点赞记录: commentId=$replyId, userId=$id")
                            }
                        } catch (e: Exception) {
                            logger.error(
                                "redis同步数据库失败...，原因{},时间:::{}", e.message,
                                LocalDateUtils.dateTimeToMyTime(LocalDateTime.now())
                            )
                            t.setRollbackOnly()
                        }
                    }
                }
            }

        //如果两者相等，说明redis没有增加记录，不需要同步
        logger.info("不需要同步数据库,时间::{}", LocalDateUtils.dateTimeToMyTime(LocalDateTime.now()))*/
    }
}