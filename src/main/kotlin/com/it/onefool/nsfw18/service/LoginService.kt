package com.it.onefool.nsfw18.service

import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.dto.LoginDto
import com.it.onefool.nsfw18.domain.dto.RegisterDto
import com.it.onefool.nsfw18.domain.vo.CodeVo
import com.it.onefool.nsfw18.domain.vo.LoginUserVo

/**
 * @Author linjiawei
 * @Date 2024/7/12 1:00
 */
interface LoginService {
    /**
     * 获取验证码
     */
    fun sendCode(): Result<CodeVo>

    /**
     * 注册
     */
    fun register(register: RegisterDto): Result<String>

    /**
     * 登录
     */
    fun login(loginDto: LoginDto): Result<LoginUserVo>




}