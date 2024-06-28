package com.it.onefool.nsfw18.domain.vo;

/**
 * @Author linjiawei
 * @Date 2024/6/24 21:13
 */
public class LoginUserVo {

    private Long id;
    private Long time;
    private String token;

    public LoginUserVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public LoginUserVo(Long id, Long time, String token) {
        this.id = id;
        this.time = time;
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginUserVo{" +
                "id=" + id +
                ", time=" + time +
                ", token='" + token + '\'' +
                '}';
    }
}
