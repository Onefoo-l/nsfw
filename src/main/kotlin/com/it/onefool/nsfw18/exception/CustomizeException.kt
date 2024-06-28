package com.it.onefool.nsfw18.exception

/**
 * @Author linjiawei
 * @Date 2024/6/24 20:08
 * 自定义异常类
 */
class CustomizeException(code: Int, message: String) : RuntimeException() {

    //错误的状态码
    private var code: Int = code

    //错误信息
    private var customizeMessage: String = message

    fun getCode(): Int {
        return code
    }

    fun getCustomizeMessage(): String {
        return customizeMessage
    }

    fun setCustomizeMessage(customizeMessage: String) {
        this.customizeMessage = customizeMessage
    }

    fun setCode(code: Int) {
        this.code = code
    }
}