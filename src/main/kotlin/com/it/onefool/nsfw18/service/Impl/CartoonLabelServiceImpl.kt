package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.entry.CartoonLabel
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CartoonLabelMapper
import com.it.onefool.nsfw18.service.CartoonLabelService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @description 针对表【cartoon_label】的数据库操作Service实现
 * @createDate 2024-06-25 16:50:54
 */
@Service
class CartoonLabelServiceImpl :
    ServiceImpl<CartoonLabelMapper?, CartoonLabel?>(), CartoonLabelService {
    companion object {
        private val logger = LoggerFactory.getLogger(CartoonLabelServiceImpl::class.java)
    }

    /**
     * 根据漫画ID查询标签ID
     */
    override fun findByCartoonId(id: Int): Result<List<CartoonLabel?>?> {
        val qwCartoonLabel = QueryWrapper<CartoonLabel>()
        qwCartoonLabel.eq("cartoon_id", id)
        return Result.ok(this.list(qwCartoonLabel))
    }

    /**
     * 插入漫画id和标签id
     */
    override fun addCartoonIdAndLabelId(cartoonId: Int, labelId: Int) {
        val cartoonLabel = CartoonLabel()
        cartoonLabel.cartoonId = cartoonId
        cartoonLabel.labelId = labelId
        val record = this.save(cartoonLabel)
        if (!record) throw CustomizeException(
            StatusCode.ADD_LABEL_ERROR.code(),
            StatusCode.ADD_LABEL_ERROR.message()
        )
    }
}
