package com.it.onefool.nsfw18.aop

import com.alibaba.fastjson2.JSONObject
import com.it.onefool.nsfw18.domain.entry.OperationLog
import com.it.onefool.nsfw18.filter.ip.FilterFactory
import com.it.onefool.nsfw18.queue.LogQueue
import com.it.onefool.nsfw18.utils.JwtUtil
import kotlinx.coroutines.*
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @Author linjiawei
 * @Date 2024/6/25 19:07
 */
@Aspect
@Component
class LogAopOperation {
    companion object {
        private val log = LoggerFactory.getLogger(LoggerFactory::class.java)
    }

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var filterFactory: FilterFactory

    @Pointcut("@annotation(com.it.onefool.nsfw18.aop.Log)")
    fun LogOperation() {
    }

    // 后置通知
    @After("LogOperation()")
    fun afterLogOperation(joinPoint: JoinPoint) {
        // 获取用户请求
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request
        //获取到方法的签名
        val signature = joinPoint.signature as MethodSignature
        //获取到方法
        val method = signature.method
        //获取到方法的返回值类型   //获取注解内容
        val logAnnotationValue = method.getAnnotation(Log::class.java)?.value
        val operation = OperationLog()
        request?.let { v ->
            //获取token解析用户id
            val userDto = jwtUtil.getToken(v)
            //0 则为匿名用户
            val userID = userDto?.userId ?: 0
            //获取用户当前ip
            val ipAddress = v.remoteAddr ?: "0.0.0.0"

            operation.userId = userID
            operation.operationDescription = logAnnotationValue
            operation.userIp = ipAddress
            operation.createTime = LocalDateTime.now()
            operation.updateTime = LocalDateTime.now()

            filterFactory.builder(operation)


                    LogQueue.addLog(operation)


        }
    }
}
