package com.it.onefool.nsfw18.strategy.cartoon

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.pojo.FindCartoonConditionType
import com.it.onefool.nsfw18.exception.CustomizeException
import org.springframework.beans.factory.InitializingBean
import kotlin.math.cbrt

/**
 * @Author linjiawei
 * @Date 2024/7/8 21:12
 */
abstract class AbstractCartoonTemplate : ICartoonService, InitializingBean {

    abstract fun support(): FindCartoonConditionType

    /**
     * 检查枚举类中的值是否和当前类中所管理的值一致
     */
    override fun afterPropertiesSet() {
        val supportValue = support()
        FindCartoonConditionType.values().find { it == supportValue }
            ?: throw CustomizeException(
                StatusCode.PARAM_ERROR.code(),
                StatusCode.PARAM_ERROR.message()
            )
    }
}