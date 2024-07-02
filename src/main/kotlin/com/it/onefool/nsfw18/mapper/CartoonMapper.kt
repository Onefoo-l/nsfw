package com.it.onefool.nsfw18.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.it.onefool.nsfw18.domain.bo.CartoonBo
import com.it.onefool.nsfw18.domain.entry.Cartoon
import org.apache.ibatis.annotations.Param

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Mapper
 * @createDate 2024-06-25 02:49:19
 * @Entity generator.domain.Cartoon
 */
interface CartoonMapper : BaseMapper<Cartoon?> {
    fun findByCondition(
        @Param("str") str: String,
        @Param("start") start: Long,
        @Param("size") size: Long
    ): List<Int>?

    /**
     * 根据漫画id查询漫画信息
     */
    fun findById(ids: List<Int>): List<CartoonBo?>

    /**
     * 根据条件查询漫画总数
     */
    fun findByConditionCount(str: String): Int




}
