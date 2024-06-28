package com.it.onefool.nsfw18.filter
import cn.hutool.core.util.ObjectUtil
import javax.servlet.*
import javax.servlet.annotation.WebFilter

/**
 * @Author linjiawei
 * @Date 2024/6/24 18:54
 */
//@WebFilter("/*")
//class AuthenticationFilter : Filter {
//
//    //用户登录时进行校验
//    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
//        val token = request?.getAttribute("token") ?: response?.writer?.println()
//
//    }
//}