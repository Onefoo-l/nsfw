package com.it.onefool.nsfw18.filter.ip

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/6/30 3:10
 * 真实Ip过滤器
 */
@Component
@Order(1)
class RealIPFilter : Filter(), InitializingBean {
    companion object{
        private val log = LoggerFactory.getLogger(RealIPFilter::class.java)
    }

    @Autowired
    private lateinit var filterFactory: FilterFactory

    override fun afterPropertiesSet() {
        filterFactory.setAtomicBooleanValue(true)
    }
}