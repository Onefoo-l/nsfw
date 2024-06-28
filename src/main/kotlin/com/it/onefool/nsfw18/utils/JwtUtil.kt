package com.it.onefool.nsfw18.utils

import cn.hutool.core.util.IdUtil
import com.it.onefool.nsfw18.common.CacheConstants
import com.it.onefool.nsfw18.domain.dto.UserDto
import com.it.onefool.nsfw18.domain.entry.User
import com.it.onefool.nsfw18.domain.vo.LoginUserVo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @Author linjiawei
 * @Date 2024/6/24 20:31
 */
@Component
class JwtUtil {
    companion object {
        private val log = LoggerFactory.getLogger(JwtUtil::class.java)

        private val secret = "dsjfownelfknslkj"
    }

    @Autowired
    private lateinit var redisCacheUtil: RedisCacheUtil

    /**
     * 创建token
     */
    fun createToken(user: User): String {
        val token = IdUtil.simpleUUID()
        val loginUserVo = LoginUserVo()
        loginUserVo.token = token
        loginUserVo.time = System.currentTimeMillis()
        val userDto = UserDto()
        userDto.user = user
        BeanUtils.copyProperties(loginUserVo, userDto)
        val map = HashMap<String, Any>()
        map["token"] = token
        //缓存到redis中
        refreshToken(userDto)
        return Jwts.builder()
            .setClaims(map)
            .signWith(SignatureAlgorithm.ES512, secret)
            .compact()
    }

    /**
     * 解析token
     */
    fun parseToken(token: String): Claims {
        //解析token
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    fun getToken(request: HttpServletRequest): UserDto? {
        val oneToken = request.getHeader("Authorization") ?: return null
        val claims = parseToken(oneToken)
        val token = claims["token"] as String?
        val userDto : UserDto = redisCacheUtil.getCacheObject(
            CacheConstants.LOGIN_USER_KEY + token
        ) ?: return null
        //获取登录时间
        val time = userDto.time
        val currentTime = System.currentTimeMillis()
        //是否相差20分钟
        val mills = currentTime / 1000 / 60 - time / 1000 / 60
        if (mills >= 20) {
            refreshToken(userDto)
        }
        return userDto
    }

    //刷新token
    fun refreshToken(userDto: UserDto) {
        redisCacheUtil.setCacheObject(
            CacheConstants.LOGIN_USER_KEY + userDto.token,
            userDto,
            30,
            TimeUnit.MINUTES
        )
    }
}