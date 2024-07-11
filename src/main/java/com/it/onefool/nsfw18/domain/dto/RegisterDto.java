package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/7/12 2:12
 */
public class RegisterDto {

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private Integer sex;

    /**
     * 密码
     */
    private String password;

    //验证码
    private String code;
    //验证码Key
    private String codeKey;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    @Override
    public String toString() {
        return "RegisterDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                ", codeKey='" + codeKey + '\'' +
                '}';
    }
}
