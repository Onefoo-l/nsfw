package com.it.onefool.nsfw18.aop.log

import com.it.onefool.nsfw18.domain.entry.OperationLog
import com.it.onefool.nsfw18.queue.LogQueue
import com.jthinking.common.util.ip.IPInfoUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.lang.StringBuilder

/**
 * @Author linjiawei
 * @Date 2024/6/30 3:10
 * 真实Ip检测
 */
@Component
class RealIPDetection : InitializingBean {
    companion object {
        private val log = LoggerFactory.getLogger(RealIPDetection::class.java)
    }

    override fun afterPropertiesSet() {
        //初始化ip库
        IPInfoUtils.init()
        log.info("添加真实ip========>")
    }
    fun builder(operation: OperationLog){
        doFilterVal(operation)
    }
    private fun addOperation(operation: OperationLog) {
        LogQueue.addLog(operation)
    }

    private fun doFilterVal(k: OperationLog) {
        val str = StringBuilder()
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

    }
}