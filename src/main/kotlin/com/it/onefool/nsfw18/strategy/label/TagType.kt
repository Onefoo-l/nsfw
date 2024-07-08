package com.it.onefool.nsfw18.strategy.label

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.entry.Label
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.LabelMapper
import com.it.onefool.nsfw18.service.CartoonLabelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * @Author linjiawei
 * @Date 2024/7/9 0:32
 */
@Component
class TagType: ILabelService {

    @Autowired
    private lateinit var labelMapper: LabelMapper

    @Autowired
    private lateinit var cartoonLabelService: CartoonLabelService

    /**
     * @param id 漫画ID
     * @param str 标签
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun add(id: Int, str: String) {
        val label = Label()
        label.tag = str
        val record = labelMapper.insert(label)
        if (record <= 0) throw CustomizeException(
            StatusCode.ADD_LABEL_ERROR.code(),
            StatusCode.ADD_LABEL_ERROR.message()
        )
        cartoonLabelService.addCartoonIdAndLabelId(id,label.id)
    }
}