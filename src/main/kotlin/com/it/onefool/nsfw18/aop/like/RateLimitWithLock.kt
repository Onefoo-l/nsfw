
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RateLimitWithLock(
    /**
     * Redis 锁的 Key 前缀
     */
    val lockPrefix: String = "lock:like:",
    /**
     * Redis 限流的 Key 前缀
     */
    val rateLimitPrefix: String = "rate:limit:",
    /**
     * 初始冷却时间（毫秒）
     */
    val initialCooldown: Int = 1000,
    /**
     * 最大冷却时间（毫秒）
     */
    val maxCooldown: Int = 10000,
    /**
     * 每秒允许的最大操作次数
     */
    val maxAttempts: Int = 5,
    /**
     * 锁的过期时间（毫秒）
     */
    val lockExpireTime: Int = 100
)