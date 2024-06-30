package com.it.onefool.nsfw18.filter.ip

import com.it.onefool.nsfw18.domain.entry.OperationLog
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.log

/**
 * @Author linjiawei
 * @Date 2024/6/30 3:12
 */
@Component
class FilterFactory {
    companion object {
        private val log = LoggerFactory.getLogger(FilterFactory::class.java)
        private val listFilter = mutableListOf<Filter>()

        //是否开启过滤器
        private val atomicBooleanValue = AtomicBoolean(false)
    }

    /**
     * 构建责任链
     */
    fun chainFilter(filter: Filter) {
        listFilter.add(filter)
        listFilter.sortBy { l ->
            l.javaClass.getAnnotation(Order::class.java).value
        }
    }

    fun getAtomicBooleanValue(): Boolean {
        return atomicBooleanValue.get()
    }

    fun setAtomicBooleanValue(boolean: Boolean) {
        atomicBooleanValue.set(boolean)
    }

    fun builder(operation : OperationLog) {
        listFilter.forEach {
            it.doFilter(operation)
        }
    }
}