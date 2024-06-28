package com.it.onefool.nsfw18.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * @Author linjiawei
 * @Date 2024/6/24 20:34
 */
@Configuration
class RedisConfig {

    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<Any,Any>{
        val template = RedisTemplate<Any,Any>()
        template.connectionFactory = connectionFactory
        val serializer = FastJson2JsonRedisSerializer(Any::class.java)
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = serializer
        // Hash的key也采用StringRedisSerializer的序列化方式
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = serializer
        template.afterPropertiesSet()
        return template
    }
}