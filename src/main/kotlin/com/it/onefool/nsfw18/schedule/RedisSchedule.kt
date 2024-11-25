package com.it.onefool.nsfw18.schedule

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.Schedules

/**
 * @Author linjiawei
 * @Date 2024/11/23 22:00
 * @Description redis的定时任务
 */
@Configuration
class RedisSchedule {
    companion object{
        private val logger = LoggerFactory.getLogger(RedisSchedule::class.java)
    }

    /**
     * 每天凌晨2点从redis中的评论数据同步到数据库
     */
//    @Scheduled(cron = "0 0 2 * * ?")
    fun commentToDatabase(){

    }
}