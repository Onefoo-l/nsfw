package com.it.onefool.nsfw18.common.constants

/**
 * @Author linjiawei
 * @Date 2024/6/28 17:01
 * 返回值常量池
 */
class ResponseConstants {

    companion object{
        val SUCCESS = "操作成功"
        val PARAM_ERROR = "参数异常"
        val UNAUTHORIZED = "未授权"
        val FORBIDDEN = "禁止访问"
        val LICENSE_EXPIRED = "授权过期"
        val NOT_FOUND ="资源不存在"
        val FAILURE = "系统异常"
        val CUSTOM_FAILURE = "自定义异常错误"
        val NEED_LOGIN = "需要登录"
        val USERNAME_OR_PASSWORD_ERROR = "用户名或者密码错误"
        val NETWORK_ERROR = "你的网络有问题"
    }
}