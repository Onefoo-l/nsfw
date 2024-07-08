package com.it.onefool.nsfw18.strategy.cartoon

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.pojo.FindCartoonConditionType
import com.it.onefool.nsfw18.exception.CustomizeException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.util.EnumMap

/**
 * @Author linjiawei
 * @Date 2024/7/4 11:24
 * 类型工厂类
 */
@Component
class ConditionTypeFactory : ApplicationContextAware {
    companion object {
        private val cartoonServices =
            EnumMap<FindCartoonConditionType, ICartoonService>(FindCartoonConditionType::class.java)
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        val types = applicationContext.getBeansOfType(AbstractCartoonTemplate::class.java)
        types.values.forEach {
            val support = it.support()
            cartoonServices[support] = it
        }
    }

    /**
     * 根据类型获取对应的服务
     */
    fun getCartoonService(type: FindCartoonConditionType): ICartoonService {
        return cartoonServices[type] ?: throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }
}