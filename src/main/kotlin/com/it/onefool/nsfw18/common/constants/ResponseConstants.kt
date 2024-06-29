package com.it.onefool.nsfw18.common.constants

/**
 * @Author linjiawei
 * @Date 2024/6/28 17:01
 * 返回值常量池
 */
class ResponseConstants {

    companion object{
        /**
         * 操作成功
         */
        val SUCCESS = "操作成功"
        /**
         * 参数异常
         */
        val PARAM_ERROR = "参数异常"
        /**
         * 未授权
         */
        val UNAUTHORIZED = "未授权"
        /**
         * 禁止访问
         */
        val FORBIDDEN = "禁止访问"
        /**
         * 授权过期
         */
        val LICENSE_EXPIRED = "授权过期"
        /**
         * 资源不存在
         */
        val NOT_FOUND ="资源不存在"
        /**
         * 系统异常
         */
        val FAILURE = "系统异常"
        /**
         * 自定义异常错误
         */
        val CUSTOM_FAILURE = "自定义异常错误"
        /**
         * 需要登录
         */
        val NEED_LOGIN = "需要登录"
        /**
         * 用户名或者密码错误
         */
        val USERNAME_OR_PASSWORD_ERROR = "用户名或者密码错误"
        /**
         * 你的网络有问题
         */
        val NETWORK_ERROR = "你的网络有问题"
        /**
         * 没有评论资源
         */
        val NOT_FOUND_COMMENT = "没有评论资源"
    }
}