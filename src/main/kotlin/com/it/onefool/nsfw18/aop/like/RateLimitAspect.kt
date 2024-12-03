import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.exception.CustomizeException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

@Aspect
@Component
class RateLimitAspect {

    companion object {
        private val logger = LoggerFactory.getLogger(RateLimitAspect::class.java)
    }

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Around("@annotation(rateLimitWithLock)")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint, rateLimitWithLock: RateLimitWithLock): Any {
        // 生成 Redis 锁的 Key
        val lockKey = generateLockKey(joinPoint, rateLimitWithLock.lockPrefix)
        val lockValue: String = UUID.randomUUID().toString().replace("-","") // 锁唯一标识

        // 动态限流的 Redis Key
        val rateLimitKey = generateRateLimitKey(
            joinPoint, rateLimitWithLock.rateLimitPrefix
        )

        try {
            // 获取 Redis 分布式锁
            val isLocked = redisTemplate.opsForValue()
                .setIfAbsent(
                    lockKey,
                    lockValue,
                    rateLimitWithLock.lockExpireTime.toLong(),
                    TimeUnit.MILLISECONDS
                )
            //条件为false才会抛出言语异常
            check(java.lang.Boolean.FALSE != isLocked) { "操作过于频繁，请稍后再试" }

            // 动态限流逻辑
            checkRateLimit(rateLimitKey, rateLimitWithLock)

            // 执行目标方法（点赞逻辑）
            return joinPoint.proceed()
        } finally {
            // 释放 Redis 锁
            releaseLock(lockKey, lockValue)
        }
    }

    /**
     * 检查是否需要限流
     * @param rateLimitKey 动态限流 Redis Key
     * @param config 动态限流配置
     */
    private fun checkRateLimit(rateLimitKey: String, config: RateLimitWithLock) {
        val currentTime = System.currentTimeMillis()
        val rateData = redisTemplate.opsForHash<Any, Any>().entries(rateLimitKey)
        //尝试次数
        var attempts =
            if (rateData.getOrDefault("attempts", "0") is String)
                    (rateData["attempts"] as String?)!!.toInt() else 0
        //下一次执行的时间
        var nextAllowTime = if (rateData.getOrDefault(
                "nextAllowTime",
                "0"
            ) is String
        ) (rateData["nextAllowTime"] as String?)!!.toLong() else 0
        //冷却时间
        var cooldown = if (rateData.getOrDefault(
                "cooldown",
                config.initialCooldown.toString()
            ) is String
        ) (rateData["cooldown"] as String?)!!.toInt() else config.initialCooldown

        // 检查是否需要限流
        if (currentTime < nextAllowTime) {
            val waitTime = (nextAllowTime - currentTime) / 1000
            logger.error("操作过于频繁，请等待 $waitTime 秒后重试")
            throw CustomizeException(
                StatusCode.FAILURE.code(),
                "操作过于频繁，请等待 $waitTime 秒后重试"
            )
        }

        // 更新限流信息
        attempts++
        val withinLimit = attempts <= config.maxAttempts
        if (!withinLimit) {
            cooldown = min((cooldown * 2).toDouble(), config.maxCooldown.toDouble()).toInt() // 冷却时间指数增长
            nextAllowTime = currentTime + cooldown
            attempts = 0 // 重置尝试次数
        } else {
            if (cooldown > config.initialCooldown) {
                cooldown =
                    max((cooldown / 2).toDouble(), config.initialCooldown.toDouble()).toInt() // 冷却时间递减
            }
            nextAllowTime = currentTime // 重置允许时间
        }

        val newRateData: MutableMap<String, String> = HashMap()
        newRateData["attempts"] = attempts.toString()
        newRateData["nextAllowTime"] = nextAllowTime.toString()
        newRateData["cooldown"] = cooldown.toString()

        redisTemplate.opsForHash<Any, Any>().putAll(rateLimitKey, newRateData)
        redisTemplate.expire(rateLimitKey, 1, TimeUnit.MINUTES)
    }

    /**
     * 根据评论id和评论等级生成的key
     */
    private fun generateLockKey(
        joinPoint: ProceedingJoinPoint,
        lockPrefix: String
    ): String {
        val args = joinPoint.args
        val commentId = args[0].toString()
        val userId = args[1].toString()
        return "$lockPrefix$commentId:$userId"
    }

    /**
     * 动态限流 Redis Key 生成 (前缀配上等级)
     */
    private fun generateRateLimitKey(
        joinPoint: ProceedingJoinPoint,
        rateLimitPrefix: String
    ): String {
        val args = joinPoint.args
        val levelId = args[1].toString()
        return rateLimitPrefix + levelId
    }

    private fun releaseLock(lockKey: String, lockValue: String) {
        redisTemplate.execute { connection: RedisConnection ->
            val redisKey: ByteArray = lockKey.toByteArray(StandardCharsets.UTF_8)
            val redisValue: ByteArray = lockValue.toByteArray(StandardCharsets.UTF_8)

            val currentValue = connection[redisKey]
            if (currentValue != null && Arrays.equals(currentValue, redisValue)) {
                connection.del(*arrayOf(redisKey))
            }
            true
        }
    }
}