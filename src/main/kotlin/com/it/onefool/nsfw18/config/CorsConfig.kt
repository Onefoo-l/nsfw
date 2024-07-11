package com.it.onefool.nsfw18.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


/**
 * @Author linjiawei
 * @Date 2024/7/12 1:17
 * 跨域配置
 */
@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOrigin("*") // 1 设置访问源地址
        corsConfiguration.addAllowedHeader("*") // 2 设置访问源请求头
        corsConfiguration.addAllowedMethod("*") // 3 设置访问源请求方法
        source.registerCorsConfiguration("/**", corsConfiguration) // 4 对接口配置跨域设置
        return CorsFilter(source)
    }
}