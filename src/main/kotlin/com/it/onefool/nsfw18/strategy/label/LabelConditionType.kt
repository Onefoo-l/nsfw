package com.it.onefool.nsfw18.strategy.label

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.exception.CustomizeException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/7/9 0:29
 */
@Component
class LabelConditionType {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    fun getLabelService(type: Int): ILabelService {
        return when (type) {
            0 -> applicationContext.getBean(WorkType::class.java)
            1 -> applicationContext.getBean(TagType::class.java)
            2 -> applicationContext.getBean(PeopleType::class.java)
            3 -> applicationContext.getBean(AuthorType::class.java)
            else -> throw CustomizeException(
                StatusCode.LABEL_PARAM_ERROR.code(),
                StatusCode.LABEL_PARAM_ERROR.message()
            )
        }
    }
}