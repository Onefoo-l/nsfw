package com.it.onefool.nsfw18.mapper

import com.it.onefool.nsfw18.domain.entry.User
import org.apache.ibatis.annotations.Param

/**
 * @Author linjiawei
 * @Date 2024/7/12 1:58
 */
interface LoginMapper {

    fun findUserByUsername(@Param("username")username: String): User?

    fun register(user: User): Int
}