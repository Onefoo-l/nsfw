package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.Label
import com.it.onefool.nsfw18.mapper.LabelMapper
import com.it.onefool.nsfw18.service.CartoonLabelService
import com.it.onefool.nsfw18.service.LabelService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【label】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:51
 */
@Service
class LabelServiceImpl : ServiceImpl<LabelMapper?, Label?>(), LabelService {
    companion object {
        private val logger = LoggerFactory.getLogger(LabelServiceImpl::class.java)
    }

    @Autowired
    private lateinit var cartoonLabelService: CartoonLabelService

    /**
     * 根据标签ID查询标签集合
     */
    override fun findById(id: List<Int?>): Result<List<Label?>> {
        val qwLabel = QueryWrapper<Label>()
        qwLabel.`in`("id", id)
        return Result.ok(this.list(qwLabel))
    }
}
