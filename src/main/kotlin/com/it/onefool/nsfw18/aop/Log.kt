package com.it.onefool.nsfw18.aop

/**
 * @Author linjiawei
 * @Date 2024/6/25 18:44
 */
@Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target
annotation class Log(val value : String = "")
