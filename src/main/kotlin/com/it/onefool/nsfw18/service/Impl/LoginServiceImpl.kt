package com.it.onefool.nsfw18.service.Impl

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.crypto.digest.DigestAlgorithm
import cn.hutool.crypto.digest.Digester
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.constants.CacheConstants
import com.it.onefool.nsfw18.domain.dto.LoginDto
import com.it.onefool.nsfw18.domain.dto.RegisterDto
import com.it.onefool.nsfw18.domain.dto.UserDto
import com.it.onefool.nsfw18.domain.entry.User
import com.it.onefool.nsfw18.domain.vo.CodeVo
import com.it.onefool.nsfw18.domain.vo.LoginUserVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.LoginMapper
import com.it.onefool.nsfw18.service.LoginService
import com.it.onefool.nsfw18.utils.JwtUtil
import com.it.onefool.nsfw18.utils.RedisCacheUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @Author linjiawei
 * @Date 2024/7/12 1:00
 */
@Service
class LoginServiceImpl : LoginService {
    companion object {
        private val log = LoggerFactory.getLogger(LoginServiceImpl::class.java)
    }

    @Autowired
    private lateinit var redisCacheUtil: RedisCacheUtil

    @Autowired
    private lateinit var loginMapper: LoginMapper

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    /**
     * 获取验证码
     */
    override fun sendCode(): Result<CodeVo> {
        return generate()
    }

    /**
     * 注册相关逻辑
     */
    override fun register(register: RegisterDto): Result<String> {
        val username = register.username
        val password = register.password
        val code = register.code
        val codeKey = register.codeKey
        if (username.isNullOrBlank()
            || password.isNullOrBlank()
            || code.isNullOrBlank()
            || codeKey.isNullOrBlank()
        )
            paramError()
        val booleanUsername = isValidUsername(username)
        val booleanPassword = isValidPassword(password)
        if (!booleanUsername || !booleanPassword) paramError()
        loginMapper.findUserByUsername(username)?.let {
            if (!it.username.isNullOrBlank())
                throw CustomizeException(
                    StatusCode.USERNAME_IS_EXIST.code(),
                    StatusCode.USERNAME_IS_EXIST.message()
                )
        }
        checkMsgCode(codeKey, code)
        val pwd = encryptionPassword(password)
        val user = User().apply {
            this.username = username
            this.nickname = username
            this.password = pwd
            this.email = register.email
            this.sex = register.sex
            this.createTime = LocalDateTime.now()
            this.updateTime = LocalDateTime.now()
            this.status = 0
            this.deleted = 0
        }
        transactionTemplate.execute {
            try {
                loginMapper.register(user)
            } catch (e: Exception) {
                //事务回滚
                it.setRollbackOnly()
            }
        }
        return Result.ok()
    }

    /**
     * 登录
     */
    override fun login(loginDto: LoginDto): Result<LoginUserVo> {
        val username = loginDto.username
        val password = loginDto.password
        var loginUserVo: LoginUserVo? = null
        if (username.isNullOrBlank() || password.isNullOrBlank()) paramError()
        loginMapper.findUserByUsername(username)?.let { u ->
            if (u.username.isNotBlank()
                && u.password.isNotBlank()
                && u.id != 0L
            ) {
                if (!checkPassword(password, u.password)) nameOrPwdError()
                loginUserVo = responseLoginUserVo(u)
            }
            nameOrPwdError()
        } ?: run {
            nameOrPwdError()
        }
        return Result.ok(loginUserVo)
    }


    /**
     * 验证码生成器
     */
    private fun generate(): Result<CodeVo> {
        //生成验证码图片， 定义图形验证码的长、宽、验证码字符数（这里采用的是动图验证码）
        val captcha = CaptchaUtil.createGifCaptcha(300, 100, 4)
        //生成验证码唯一标识，这里主要是为了临时的区分用户端的，验证码唯一标识这里采用的是uuid生成
        val captchaKey: String = UUID.randomUUID().toString().replace("-", "")
        //获得图片，base64格式
        val image = captcha.imageBase64
        //获取验证码图片上的字母，即验证码的答案
        val captchaValue = captcha.getCode()
        //将验证码信息存入redis
        redisCacheUtil.setCacheObject(
            captchaKey,
            captchaValue,
            120,
            TimeUnit.SECONDS
        )
        //封装对象，返回数据
        return Result.ok(
            CodeVo().apply {
                this.code = image
                this.key = captchaKey
            }
        )

    }

    /**
     * 检查验证码的正确性
     *
     * @param captchaKey 当前用户的验证码的key
     * @param userCaptchaValue 用户填写的验证码信息
     */
    private fun checkMsgCode(captchaKey: String?, userCaptchaValue: String?) {
        if (captchaKey.isNullOrBlank() && userCaptchaValue.isNullOrBlank()) { //非空判断
            try {
                val trueCaptchaValue = redisCacheUtil
                    .getCacheObject<String>(captchaKey) //根据key获取真正的验证码信息
                redisCacheUtil.deleteObject(captchaKey) //删除验证码信息，此条验证码作废
                if (trueCaptchaValue.isNullOrBlank()
                    && userCaptchaValue!!.compareTo(
                        trueCaptchaValue, ignoreCase = true
                    ) == 0
                ) {
                    //验证码正确，退出
                    return
                }
            } catch (e: Exception) {
                //忽略异常，
            }
        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(), StatusCode.PARAM_ERROR.message()
        ) //验证码不正确，抛出异常
    }

    /**
     * 检测空格
     */
    private fun isValidUsername(username: String): Boolean {
        return (username == username.trim()) && (!username.contains(" "))
    }

    /**
     * 检测空格以及密码长度
     */
    private fun isValidPassword(password: String): Boolean {
        return !(password.length < 6
                || password.length > 20
                || (password != password.trim())
                || (password.contains(" ")))
    }

    /**
     * SHA-256的加密算法
     */
    private fun encryptionPassword(password: String): String {
        val sha = Digester(DigestAlgorithm.SHA256)
        return sha.digestHex(password)
    }

    /**
     * 验证密码
     * @param password 用户输入的密码
     * @param passwordDB 数据库的密码
     */
    private fun checkPassword(password: String, passwordDB: String): Boolean {
        return encryptionPassword(password) == passwordDB
    }

    private fun responseLoginUserVo(u: User): LoginUserVo {
        val loginUserVo = LoginUserVo()
        val token = jwtUtil.createToken(u)
        val userDto = redisCacheUtil
            .getCacheObject<UserDto>(CacheConstants.LOGIN_USER_KEY + token)
        userDto?.let {
            loginUserVo.time = it.time
            loginUserVo.token = token
            loginUserVo.id = it.id
            return loginUserVo
        } ?: run {
            throw CustomizeException(
                StatusCode.FAILURE.code(),
                StatusCode.FAILURE.message()
            )
        }
    }

    private fun nameOrPwdError() {
        throw CustomizeException(
            StatusCode.USERNAME_OR_PASSWORD_ERROR.code(),
            StatusCode.USERNAME_OR_PASSWORD_ERROR.message()
        )
    }

    private fun paramError() {
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }
}