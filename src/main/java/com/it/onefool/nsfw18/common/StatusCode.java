package com.it.onefool.nsfw18.common;

/**
 * 枚举类状态码
 *
 * @author Onefool
 * @version 1.0
 */
public enum StatusCode {
    //区分与http状态码
    SUCCESS(20000, "操作成功"),
    PARAM_ERROR(40000, "参数异常"),
    UNAUTHORIZED(40001, "未授权"),
    FORBIDDEN(40002, "禁止访问"),
    LICENSE_EXPIRED(40003, "授权过期"),
    NOT_FOUND(40004, "资源不存在"),
    FAILURE(50000, "系统异常"),
    CUSTOM_FAILURE(50001, "自定义异常错误"),
    NEED_LOGIN(50002, "需要登录");
    private final Integer code;

    private final String message;

    StatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    //获取状态码
    public Integer code() {
        return code;
    }

    //获取信息
    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}
