package com.it.onefool.nsfw18.filter

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.pojo.IpBlack
import com.jthinking.common.util.ip.IPInfoUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.math.log


/**
 * @Author linjiawei
 * @Date 2024/7/11 3:14
 * ip黑名单过滤器
 * 有条件可以购买防火墙，直接在网络层上拦截，在过滤器中进行过滤也是会对服务器造成压力
 */
//@Component
class IpBlacklistFilter : AbstractCustomFilter() {
    companion object{
        private val log = LoggerFactory.getLogger(IpBlacklistFilter::class.java)
    }
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (!isEnabled()) {
            chain!!.doFilter(request, response)
            return
        }
        log.info("ip过滤器==========>")
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val ip = httpRequest.remoteAddr
        val ipInfo = IPInfoUtils.getIpInfo(ip)
        if (IpBlack.containsCOUNTRY(ipInfo.country) || IpBlack.containsCITY(ipInfo.address)) {
            httpResponse.sendError(
                StatusCode.REQUEST_FORBIDDEN.code(),
                StatusCode.REQUEST_FORBIDDEN.message()
            )
            return
        }

        chain!!.doFilter(request, response)
    }
}