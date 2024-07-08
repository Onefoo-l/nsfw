package com.it.onefool.nsfw18.service

import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.dto.LabelDto
import com.it.onefool.nsfw18.domain.entry.Label

/**
 * @author 97436
 * @description 针对表【label】的数据库操作Service
 * @createDate 2024-06-25 16:51:51
 */
interface LabelService : IService<Label?> {
    /**
     * 根据标签ID集合查询标签集合
     */
    fun findById(id: List<Int?>): Result<List<Label?>>

    /**
     * 添加标签
     */
    fun addLabel(labelDto: LabelDto)


}
