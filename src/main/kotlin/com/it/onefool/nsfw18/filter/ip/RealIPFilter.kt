package com.it.onefool.nsfw18.filter.ip

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.it.onefool.nsfw18.domain.entry.OperationLog
import com.it.onefool.nsfw18.queue.LogQueue
import com.it.onefool.nsfw18.utils.IpCityFilterUtil
import com.jthinking.common.util.ip.IPInfoUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.lang.StringBuilder
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
        //开启过滤器(日志)
        filterFactory.setAtomicBooleanValue(true)
        //开启过滤器(servlet拦截使用)
        filterFactory.setAtomicBooleanServletValue1(true)
//        filterFactory.setAtomicBooleanServletValue2(true)
//        filterFactory.setAtomicBooleanServletValue3(true)
        //初始化ip库
        IPInfoUtils.init()
        log.info("添加真实ip过滤器并开启========>")
    }

    /**
     * 操作日志需要用到的
     */
    override fun doFilter(operation: OperationLog) {
        doFilterVal(operation)
    }

    /**
     * servlet过滤器需要用到的
     */
    override fun doFilter(k: String): Int {
        return doFilterServlet(k)
    }

    override fun addOperation(operation: OperationLog) {
        LogQueue.addLog(operation)
    }

    /**
     * 1表示true 0表示false 2表示下一个过滤器
     */
    fun doFilterVal(k: OperationLog) {
        val str = StringBuilder()
        //未开启Ip过滤的话直接下一个  需加锁保证线程安全
        if (!filterFactory.getAtomicBooleanValue()) return
        IPInfoUtils.getIpInfo(k.userIp)?.let { i ->
            i.country?.let {
                str.append(it)
            }
            i.address?.let {
                if (it != str.toString()) str.append(it)
            }
            k.userCity = str.toString()
            k.success = 1
            addOperation(k)
        } ?: run {
            k.userCity = "未知"
            k.success = 1
        }
        /*//创建协程的上下文
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
        }*/
    }

    /**
     * 1表示true 0表示false 2表示下一个过滤器
     */
    fun doFilterServlet(k: String): Int {
        var int = 2
        //未开启Ip过滤的话直接下一个 需加锁保证线程安全
        if (!filterFactory.getAtomicBooleanServletValue1()) return 2
        IPInfoUtils.getIpInfo(k)?.let { i ->
            i.country?.let { c ->
                if (c == "朝鲜") int = 0
            } ?: run {
                i.address?.let {
                    IpCityFilterUtil.listKor.forEach { l ->
                        if (it == l)  int = 0
                    }
                }
            }
        }
        /*        //创建协程的上下文
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
                                filterFactory.setAtomicBooleanServletValue2(false)
                                return@launch
                            }
                        } ?: run {
                            dataJson.getString("country")?.let { a ->
                                if (a.startsWith("朝鲜")) {
                                    int = 0
                                    filterFactory.setAtomicBooleanServletValue2(false)
                                    return@launch
                                }
                            } ?: run {
                                int = 2
                                filterFactory.setAtomicBooleanServletValue2(true)
                                return@launch
                            }
                        }

                    }
                }*/
        return int
    }
}