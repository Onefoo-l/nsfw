package com.it.onefool.nsfw18.filter

import com.it.onefool.nsfw18.filter.*
import jakarta.servlet.Filter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    @Autowired
    private lateinit var authenticationFilter: AuthenticationFilter

    @Autowired
    private lateinit var ipBlacklistFilter: IpBlacklistFilter

    @Bean
    fun filterChainManager(): FilterChainManager {
        val filterChainManager = FilterChainManager()
        filterChainManager.addFilter(ipBlacklistFilter)
        filterChainManager.addFilter(authenticationFilter)
        return filterChainManager
    }

    @Bean
    fun filterRegistrationBean(filterChainManager: FilterChainManager): FilterRegistrationBean<*> {
        val registrationBean = FilterRegistrationBean<FilterChainManager>()
        registrationBean.filter = filterChainManager()
        registrationBean.addUrlPatterns("/*")
        return registrationBean
    }
}