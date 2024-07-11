package com.it.onefool.nsfw18.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * @Author linjiawei
 * @Date 2024/7/11 19:59
 * minio配置类
 */
@Configuration
class MinioConfig {

    /**
     * 访问地址
     */
    @Value("\${minio.url}")
    private val endpoint: String? = null

    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    @Value("\${minio.accessKey}")
    private val accessKey: String? = null

    /**
     * secretKey是你账户的密码
     */
    @Value("\${minio.secretKey}")
    private val secretKey: String? = null

    /**
     * 默认存储桶
     */
    @Value("\${minio.bucketName}")
    private val bucketName: String? = null

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build()
    }

    fun getBucketName(): String {
        return bucketName!!
    }
    fun getEndpoint(): String {
        return endpoint!!
    }

    fun getAccessKey(): String {
        return accessKey!!
    }
    fun getSecretKey(): String {
        return secretKey!!
    }
}