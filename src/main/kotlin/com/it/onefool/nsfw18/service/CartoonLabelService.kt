package com.it.onefool.nsfw18.service
import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.CartoonLabel

/**
 * @author 97436
 * @description 针对表【cartoon_label】的数据库操作Service
 * @createDate 2024-06-25 16:50:54
 */
interface CartoonLabelService : IService<CartoonLabel?>{
    /**
     * 根据漫画ID查询标签ID
     */
    fun findByCartoonId(id: Int): Result<List<CartoonLabel?>?>

    /**
     * 插入漫画id和标签id
     */
    fun addCartoonIdAndLabelId(cartoonId: Int,labelId: Int)
}
