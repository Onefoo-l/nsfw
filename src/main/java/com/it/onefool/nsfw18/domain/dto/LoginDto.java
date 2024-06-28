package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/6/24 19:07
 */
public class LoginDto {

    //可以是用户名或者是短信或者是邮箱
    private String username;
    //密码
    private String password;
    //验证码
    private String code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
