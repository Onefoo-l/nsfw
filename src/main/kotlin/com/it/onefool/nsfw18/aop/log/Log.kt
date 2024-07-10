package com.it.onefool.nsfw18.aop.log

/**
 * @Author linjiawei
 * @Date 2024/6/25 18:44
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Log(val value : String = "")
