package com.it.onefool.nsfw18;

import cn.hutool.core.collection.CollectionUtil;
import com.it.onefool.nsfw18.common.constants.CacheConstants;
import com.it.onefool.nsfw18.domain.entry.CommentLike;
import com.it.onefool.nsfw18.domain.entry.CommentReplyLike;
import com.it.onefool.nsfw18.service.CommentLikeService;
import com.it.onefool.nsfw18.service.CommentReplyLikeService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@SpringBootTest
class Nsfw18ApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(Nsfw18ApplicationTests.class);
    @Autowired
    private CommentLikeService commentLike;

    @Autowired
    private CommentReplyLikeService commentReplyLike;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void test() {
        var start11111111 = LocalDateTime.now();
        var start = LocalDateTime.now();
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 100000; j++) {
                var commentLikes = new CommentLike();
                commentLikes.setUserId(i);
                commentLikes.setCommentId(j);
                commentLike.getBaseMapper().insert(commentLikes);
            }
        }
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        System.out.println("一级评论添加耗时::{}" + seconds);

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 15000; j++) {
                var commentReplyLikes = new CommentReplyLike();
                commentReplyLikes.setUserId(i);
                commentReplyLikes.setReplyId(j);
                commentReplyLike.getBaseMapper().insert(commentReplyLikes);

            }
        }
        LocalDateTime end2 = LocalDateTime.now();
        Duration duration2 = Duration.between(start, end2);
        long seconds2 = duration2.getSeconds();
        System.out.println("一级评论添加耗时::{}" + seconds2);
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 100; j++) {
                //评论被点赞了五百下
                redisTemplate.opsForValue().increment(
                        CacheConstants.Companion.getCOMMENT_LIKE() + j, 1);
                var userKey = CacheConstants.Companion.getTHUMBS_UP_USERS_KEY() + j;
                //value是用户的id
                redisTemplate.opsForList().rightPush(userKey, i + "");


            }

        }
        // 记录结束时间
        LocalDateTime end3 = LocalDateTime.now();
        // 计算耗时
        Duration duration3 = Duration.between(start, end3);
        long seconds3 = duration3.getSeconds();
        System.out.println("redis一级评论添加耗时::{}" + seconds3);
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 300; j++) {
                //评论被点赞了8百下
                redisTemplate.opsForValue().increment(
                        CacheConstants.Companion.getCOMMENT_REPLY_LIKE() + j, 1);
                var userKey = CacheConstants.Companion.getTHUMBS_UP_USERS_REPLY_KEY() + j;
                //value是用户的id
                redisTemplate.opsForList().rightPush(userKey, i + "");
            }

        }

        // 记录结束时间
        LocalDateTime end5 = LocalDateTime.now();
        // 计算耗时
        Duration duration5 = Duration.between(start11111111, end5);
        long seconds5 = duration5.getSeconds();
        System.out.println("总耗时==============================>" + seconds5);
    }

    @Test
    void test2() {
        Set<String> keys = redisTemplate.opsForValue().getOperations().keys(
                CacheConstants.Companion.getCOMMENT_LIKE() + "*");
        if (CollectionUtil.isNotEmpty(keys)) keys.forEach(System.out::println);
    }

    @Test
    void test3() {
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 10000; j++) {
                //评论被点赞了五百下
                redisTemplate.opsForValue().increment(
                        CacheConstants.Companion.getCOMMENT_LIKE() + j, 1);
                var userKey = CacheConstants.Companion.getCOMMENT_LIKE() + "user:" + j;
                //value是用户的id
                redisTemplate.opsForList().rightPush(userKey, i + "");
            }
        }
    }
}
