package com.it.onefool.nsfw18.filter.ip

import com.alibaba.fastjson2.JSONObject
import com.it.onefool.nsfw18.domain.entry.OperationLog
import com.it.onefool.nsfw18.queue.LogQueue
import com.it.onefool.nsfw18.utils.IpCityFilterUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

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
        //添加过滤器
        filterFactory.chainFilter(this)
        //开启过滤器
        filterFactory.setAtomicBooleanValue(true)
        log.info("添加代理ip过滤器并开启========>")
    }

    /**
     * 操作日志需要用到的
     */
    override fun doFilter(operation: OperationLog) {
        doFilterVal(operation, true)

    }
    /**
     * 1表示true 0表示false 2表示下一个过滤器
     */
     fun doFilterVal(operation: OperationLog, b: Boolean) : Int{
        if (b){
            //未开启Ip过滤的话直接下一个
            if (!filterFactory.getAtomicBooleanValue()) return 2
            //创建协程的上下文
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                //调用第三方ip库获取用户城市  https://whatismyipaddress.com/ip/183.12.223.67
                val uri = URI.create("https://cmp.inmobi.com/geoip")
                val httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json") // 设置接受的内容类型
                    .build()
                val res = HttpClient.newHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString()).body()
                res?.let {
                    operation.userCity = JSONObject.parseObject(it).getString("city") ?: "未知"
                    filterFactory.setAtomicBooleanValue(true)
                    addOperation(operation)
                } ?: run {
                    operation.userCity = "未知"
                    filterFactory.setAtomicBooleanValue(true)
                    addOperation(operation)
                }
            }
            return 2
        }
         return doFilterServlet()
    }

    /**
     * servlet过滤器需要用到的
     */
    private fun doFilterServlet(): Int {
        var int = 2
        //未开启Ip过滤的话直接下一个
        if (!filterFactory.getAtomicBooleanValue()) return 2
        //创建协程的上下文
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            //调用第三方ip库获取用户城市  https://whatismyipaddress.com/ip/183.12.223.67
            val uri = URI.create("https://cmp.inmobi.com/geoip")
            val httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json") // 设置接受的内容类型
                .build()
            val res = HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString()).body()
            res?.let { r ->
                JSONObject.parseObject(r).getString("city")?.let {t->
                    val list = IpCityFilterUtil.listKor.map { m -> m.toLowerCase() }
                    if (list.contains(t.toLowerCase())) {
                        int = 0
                        filterFactory.setAtomicBooleanValue(true)
                        return@launch
                    }
                } ?: run {
                    int= 1
                    filterFactory.setAtomicBooleanValue(true)
                    return@launch
                }
            }
        }
        return int
    }

    override fun doFilter(): Boolean {
        return if (doFilterVal(OperationLog(), false) == 1) true else false
    }

    override fun addOperation(operation: OperationLog) {
        LogQueue.addLog(operation)
    }


}