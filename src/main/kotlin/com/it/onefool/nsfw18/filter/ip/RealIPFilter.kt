package com.it.onefool.nsfw18.filter.ip

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.it.onefool.nsfw18.domain.entry.OperationLog
import com.it.onefool.nsfw18.queue.LogQueue
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
 * @Date 2024/6/30 3:10
 * 真实Ip过滤器
 */
@Component
@Order(1)
class RealIPFilter : Filter(), InitializingBean {
    companion object {
        private val log = LoggerFactory.getLogger(RealIPFilter::class.java)
    }

    @Autowired
    private lateinit var filterFactory: FilterFactory

    override fun afterPropertiesSet() {
        //添加过滤器
        filterFactory.chainFilter(this)
        //开启过滤器
        filterFactory.setAtomicBooleanValue(true)
        log.info("添加真实ip过滤器并开启========>")
    }

    override fun doFilter(operation: OperationLog) {
        //未开启Ip过滤的话直接下一个
        if (!filterFactory.getAtomicBooleanValue()) return
        //创建协程的上下文
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            //调用第三方ip库获取用户城市
            val uri = URI.create("https://v4.ip.zxinc.org/info.php?type=json")
            val httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json") // 设置接受的内容类型
                .build()
            val res = HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString()).body()
            res?.let {
                val jsonRes = JSON.parseObject(res)
                val data = jsonRes.get("data")
                val dataJson = JSONObject.parseObject(data.toString())
                dataJson.getString("location")?.let { d ->
                    operation.userCity = d
                    filterFactory.setAtomicBooleanValue(false)
                    addOperation(operation)
                } ?: run {
                    dataJson.getString("country")?.let { a ->
                        operation.userCity = a
                        filterFactory.setAtomicBooleanValue(false)
                        addOperation(operation)
                    } ?: run {
                        return@let
                    }
                }
            }
        }
    }
    override fun addOperation(operation: OperationLog){
        LogQueue.addLog(operation)
    }
}