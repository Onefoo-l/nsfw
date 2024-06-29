package com.it.onefool.nsfw18.filter.ip

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @Author linjiawei
 * @Date 2024/6/30 3:12
 */
@Component
class FilterFactory {
    companion object {
        private val list = mutableListOf<Filter>()

        //是否开启过滤器
        private val atomicBooleanValue = AtomicBoolean(false)
    }

    @Autowired
    private lateinit var proxyIpFilter: ProxyIpFilter

    @Autowired
    private lateinit var realIPFilter: RealIPFilter


    /**
     * 构建责任链
     */
    fun chainFilter() {
        list.add(realIPFilter)
        list.add(proxyIpFilter)
        list.sortBy { l ->
            l.javaClass.getAnnotation(Order::class.java).value
        }
    }

    fun getAtomicBooleanValue(): Boolean {
        return atomicBooleanValue.get()
    }

    fun setAtomicBooleanValue(boolean: Boolean) {
        atomicBooleanValue.set(boolean)
    }
}