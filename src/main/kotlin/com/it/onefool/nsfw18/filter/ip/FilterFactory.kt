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
 * 过滤器工厂类
 */
@Component
class FilterFactory {
    companion object {
        private val log = LoggerFactory.getLogger(FilterFactory::class.java)
        private val listFilter = mutableListOf<Filter>()
        //是否开启过滤器（日志）
        private val atomicBooleanValue = AtomicBoolean(false)
        private val lockLog = Any()
        //是否开启过滤器（servlet拦截使用） 每个类一个共享变量
        private val atomicBooleanServletValue1 = AtomicBoolean(false)
        private val atomicBooleanServletValue2 = AtomicBoolean(false)
        private val atomicBooleanServletValue3 = AtomicBoolean(false)
        private val lockServlet = Any()
    }
    fun getLockLog():Any{
        return lockLog
    }

    fun getLockServlet() : Any{
        return lockServlet
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

    fun getAtomicBooleanServletValue3() : Boolean{
        return atomicBooleanServletValue3.get()
    }
    fun setAtomicBooleanServletValue3(boolean: Boolean){
        atomicBooleanServletValue3.set(boolean)
    }
    fun getAtomicBooleanServletValue2() : Boolean{
        return atomicBooleanServletValue2.get()
    }
    fun setAtomicBooleanServletValue2(boolean: Boolean){
        atomicBooleanServletValue2.set(boolean)
    }
    fun getAtomicBooleanServletValue1() : Boolean{
        return atomicBooleanServletValue1.get()
    }
    fun setAtomicBooleanServletValue1(boolean: Boolean){
        atomicBooleanServletValue1.set(boolean)
    }
    fun getAtomicBooleanValue(): Boolean {
        return atomicBooleanValue.get()
    }
    fun setAtomicBooleanValue(boolean: Boolean) {
        atomicBooleanValue.set(boolean)
    }


    fun builder(operation : OperationLog) {
        synchronized(lockLog){
            listFilter.forEach {
                it.doFilter(operation)
            }
        }
    }
    //servlet过滤器使用
    /**
     * 1表示true 0表示false 2表示下一个过滤器(只要是0则屏蔽掉)
     */
    fun builder(k:String) : List<Int>{
        synchronized(lockServlet){
            val list = mutableListOf<Int>()
            listFilter.forEach {
                list.add(it.doFilter(k))
            }
            return list
        }
    }
}