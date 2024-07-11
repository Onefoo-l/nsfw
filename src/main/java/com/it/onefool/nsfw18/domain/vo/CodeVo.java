package com.it.onefool.nsfw18.domain.vo;

/**
 * @Author linjiawei
 * @Date 2024/7/12 1:37
 * 返回给前端的验证码
 */
public class CodeVo {

    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码key
     */
    private String key;

    public CodeVo() {
    }

    public CodeVo(String code, String key) {
        this.code = code;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
