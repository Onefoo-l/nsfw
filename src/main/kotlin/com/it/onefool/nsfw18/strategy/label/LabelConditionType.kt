package com.it.onefool.nsfw18.strategy.label

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.exception.CustomizeException
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/7/9 0:29
 */
@Component
class LabelConditionType {

    companion object {
        private val LABEL_TYPE_MAP: Map<Int, ILabelService> = mapOf(
            0 to WorkType(),
            1 to TagType(),
            2 to PeopleType(),
            3 to AuthorType()
        )
    }

    fun getLabelService(type: Int): ILabelService {
        return LABEL_TYPE_MAP[type] ?: throw CustomizeException(
            StatusCode.LABEL_PARAM_ERROR.code(),
            StatusCode.LABEL_PARAM_ERROR.message()
        )
    }
}