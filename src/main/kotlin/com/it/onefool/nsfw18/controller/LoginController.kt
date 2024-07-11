package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.LoginDto
import com.it.onefool.nsfw18.domain.dto.RegisterDto
import com.it.onefool.nsfw18.domain.vo.CodeVo
import com.it.onefool.nsfw18.domain.vo.LoginUserVo
import com.it.onefool.nsfw18.service.LoginService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author linjiawei
 * @Date 2024/7/12 0:26
 * 登录相关的控制层
 */
@RestController
@RequestMapping("/login")
class LoginController{
    companion object{
        private val log = LoggerFactory.getLogger(LoginController::class.java)
    }

    @Autowired
    private lateinit var loginService: LoginService

    /**
     * 注册
     */
    @PostMapping("/register")
    fun register(@RequestBody registerDto: RegisterDto?): Result<String>{
        registerDto?.let {
            return loginService.register(registerDto)
        }?: run {
            return Result.error(StatusCode.PARAM_ERROR)
        }
    }


    /**
     * 登录
     */
    @PostMapping()
    fun login(@RequestBody loginDto: LoginDto?): Result<LoginUserVo>{
        loginDto?.let {
            return loginService.login(loginDto)
        }?: run {
            return Result.error(StatusCode.PARAM_ERROR)
        }
    }

    /**
     * 获取验证码
     */
    @GetMapping("/sendCode")
    fun sendEmailCode(): Result<CodeVo>{
        return loginService.sendCode()
    }
}