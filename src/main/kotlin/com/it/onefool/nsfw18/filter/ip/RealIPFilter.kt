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

    /**
     * 操作日志需要用到的
     */
    override fun doFilter(operation: OperationLog) {
        doFilterVal(operation, true)
    }

    /**
     * servlet过滤器需要用到的
     */
    override fun doFilter(): Boolean {
        return if (doFilterVal(OperationLog(), false) == 1) true else false
    }

    override fun addOperation(operation: OperationLog) {
        LogQueue.addLog(operation)
    }

    /**
     * 1表示true 0表示false 2表示下一个过滤器
     */
    fun doFilterVal(k: OperationLog, v: Boolean): Int {
        if (v) {
            //未开启Ip过滤的话直接下一个
            if (!filterFactory.getAtomicBooleanValue()) return 2
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
                        k.userCity = d
                        k.success = 1
                        filterFactory.setAtomicBooleanValue(false)
                        addOperation(k)
                    } ?: run {
                        dataJson.getString("country")?.let { a ->
                            k.userCity = a
                            k.success = 1
                            filterFactory.setAtomicBooleanValue(false)
                            addOperation(k)
                        } ?: run {
                            return@launch
                        }
                    }
                }
            }
            return 2
        }
        return doFilterServlet()
    }

    fun doFilterServlet(): Int {
        var int = 3
        //未开启Ip过滤的话直接下一个
        if (!filterFactory.getAtomicBooleanValue()) return 2
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
                    if (d.startsWith("朝鲜")) {
                        int = 0
                        return@launch
                    }
                    filterFactory.setAtomicBooleanValue(false)
                } ?: run {
                    dataJson.getString("country")?.let { a ->
                        if (a.startsWith("朝鲜")) {
                            int = 0
                            return@launch
                        }
                        filterFactory.setAtomicBooleanValue(false)
                    } ?: run {
                        int = 2
                        return@let
                    }
                }
            }
        }
        return int
    }
}