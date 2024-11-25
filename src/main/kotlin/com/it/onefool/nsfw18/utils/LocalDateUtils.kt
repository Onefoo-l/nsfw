package com.it.onefool.nsfw18.utils

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * @Author linjiawei
 * @Date 2024/11/24 18:17
 * 时间转换的工具类
 */
@Component
class LocalDateUtils {
    companion object {
        private val logger = LoggerFactory.getLogger(LoggerFactory::class.java)
    }

    /**
     * 将LocalDateTime转换成Double
     */
    fun localDateToDouble(l: LocalDateTime): Double {
        // 将LocalDateTime转换为ZonedDateTime，指定时区
        val zonedDateTime: ZonedDateTime = l.atZone(ZoneId.systemDefault())
        // 将ZonedDateTime转换为Instant
        val instant = zonedDateTime.toInstant()
        // 获取自Unix纪元以来的毫秒数
        val millisecondsSinceEpoch = instant.toEpochMilli()
        // 将毫秒数转换为double
        return millisecondsSinceEpoch.toDouble()
    }
}