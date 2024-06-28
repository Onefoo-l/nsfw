package com.it.onefool.nsfw18.domain.dto;

import com.it.onefool.nsfw18.domain.entry.User;
import com.it.onefool.nsfw18.domain.vo.LoginUserVo;

/**
 * @Author linjiawei
 * @Date 2024/6/26 3:19
 */
public class UserDto extends LoginUserVo {
    private User user;

    public String getPassWord() {
        return user.getPassword();
    }

    // 要先设置
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUserName() {
        return user.getUsername();
    }

    public Long getUserId() {
        return user.getId();
    }
}
