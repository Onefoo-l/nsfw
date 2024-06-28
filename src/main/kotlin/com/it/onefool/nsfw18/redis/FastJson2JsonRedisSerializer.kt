package com.it.onefool.nsfw18.redis

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONReader
import com.alibaba.fastjson2.JSONWriter
import org.springframework.data.redis.serializer.RedisSerializer
import java.nio.charset.Charset

/**
 * @Author linjiawei
 * @Date 2024/6/24 20:36
 */
class FastJson2JsonRedisSerializer<T> : RedisSerializer<T> {
    companion object {
        val DEFAULT_CHARSET: Charset = Charset.forName("UTF-8")
    }

    private lateinit var clazz: Class<T>

    constructor(clazz: Class<T>) {
        this.clazz = clazz
    }

    override fun serialize(value: T?): ByteArray? {
        return value?.let {
            JSON.toJSONString(
                value,
                JSONWriter.Feature.WriteClassName
            ).toByteArray(DEFAULT_CHARSET)
        } ?: ByteArray(0)
    }

    override fun deserialize(bytes: ByteArray?): T? {
        return bytes?.let {
            val str = String(bytes, DEFAULT_CHARSET)
            return JSON.parseObject(str,
                clazz,
                JSONReader.Feature.SupportAutoType)
        } ?: null
    }
}