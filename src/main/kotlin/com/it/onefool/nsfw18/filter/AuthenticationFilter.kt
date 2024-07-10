package com.it.onefool.nsfw18.filter

import com.it.onefool.nsfw18.utils.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/6/24 18:54
 * 校验的过滤器
 */
@Component
class AuthenticationFilter : AbstractCustomFilter() {
    companion object {
        private val log = LoggerFactory.getLogger(AuthenticationFilter::class.java)
    }

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    //进行校验
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {
        if (!isEnabled()) {
            chain?.doFilter(request, response)
            return
        }
        log.info("登录过滤器==========>")
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        // 登录校验逻辑
        val loggedIn: Boolean = checkLogin(httpRequest)
        if (loggedIn) {
            chain?.doFilter(request, response)
        } else {
//            httpResponse.sendRedirect("/login")
            log.info("未登录无法使用某些功能")
        }
    }

    private fun checkLogin(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        //以后还有很多逻辑校验
        jwtUtil.getToken(request) ?: run {
            if (path.contains("/comment") ||
                path.contains("/commentReply")
            ) {
                return false
            }
        }
        return true
    }
}