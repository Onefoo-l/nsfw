package com.it.onefool.nsfw18.exception

import com.it.onefool.nsfw18.common.Result
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @Author linjiawei
 * @Date 2024/6/24 20:17
 * 全局异常处理类
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    companion object{
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    //1. 方法用来针对 系统的异常处理 相当于代替之前的controller来给前端返回了
    @ExceptionHandler(Exception::class)
    fun handlerSystemException(e:Exception) : Result<Any>{
       log.error("后台出现异常，原因{}",e.message)
       return Result.errorMessage("你的网络有问题")
    }

    //2.方法用来针对 业务上的异常处理
    @ExceptionHandler(CustomizeException::class)
    fun handlerBException(e:CustomizeException):Result<Any>{
        log.error("业务异常,{}",e.message)
        return Result.errorMessage(e.message)
    }
}