package com.it.onefool.nsfw18.utils

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @Author linjiawei
 * @Date 2024/11/24 18:47
 * 重试机制工具类
 */
@Component
class RetryTemplateUtil<T> {

    companion object {
        private val logger = LoggerFactory.getLogger(RetryTemplateUtil::class.java)
    }

    //最大重试次数
    private val maxAttempts = 3

    //每次重试之间的延迟时间
    private val delayMillis = 500

    fun <T> executeWithRetry(action: Supplier<T>): T {
        for (attempt in 1..maxAttempts) {
            try {
                return action.get() // 执行传入的操作
            } catch (e: Exception) {
                // 打印重试信息
                logger.error("Attempt" + attempt + " failed:" + e.message)
                // 达到最大重试次数，退出循环(暂时不记录失败日志，以后可以记录，需加表)
                if (attempt == maxAttempts) break
                // 延时重试
                Thread.sleep(calculateExponentialBackoff(attempt))
            }
        }
        throw IllegalStateException("Unexpected error in retry mechanism") // 这一行理论上不会执行，但为了确保编译通过
    }

    private fun calculateExponentialBackoff(attempt: Int): Long {
        return 500L * (1 shl (attempt - 1)) // 500ms, 1s, 2s...
    }
}