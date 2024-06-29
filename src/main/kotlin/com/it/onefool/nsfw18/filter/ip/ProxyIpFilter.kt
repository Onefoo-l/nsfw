package com.it.onefool.nsfw18.filter.ip

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/6/30 2:56
 * 代理ip过滤器
 */
@Component
@Order(2)
class ProxyIpFilter : Filter() ,InitializingBean{
    companion object{
        private val log = LoggerFactory.getLogger(ProxyIpFilter::class.java)
    }

    @Autowired
    private lateinit var filterFactory: FilterFactory

    override fun afterPropertiesSet() {
        filterFactory.setAtomicBooleanValue(true)
    }


}