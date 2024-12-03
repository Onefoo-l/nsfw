package com.it.onefool.nsfw18.service

import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.dto.CartoonDto
import com.it.onefool.nsfw18.domain.entry.Cartoon
import com.it.onefool.nsfw18.domain.vo.CartoonVo

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Service
 * @createDate 2024-06-25 02:49:19
 */
interface CartoonService : IService<Cartoon> {
    /**
     * 查看漫画id
     */
    fun findId(id: Int?): Result<CartoonVo>

    /**
     * 查询最近更新的漫画
     */
    fun findByUpdateTime(pageCarton: PageRequestDto<Cartoon>): Result<PageInfo<Cartoon>>

    /**
     * 查询最多人观看的漫画
     */
    fun findManyRead(pageCarton: PageRequestDto<Cartoon>): Result<PageInfo<Cartoon>>

    /**
     * 查询最多人收藏的漫画
     */
    fun findManyCollection(pageCarton: PageRequestDto<Cartoon>): Result<PageInfo<Cartoon>>

    /**
     * 查询最多人点赞的漫画
     */
    fun findManyNice(pageCarton: PageRequestDto<Cartoon>): Result<PageInfo<Cartoon>>

    /**
     * 多条件查询(优先级 1.漫画名称 2.漫画作者 3.标签 4.漫画人物)
     */
    fun findByCondition(str: String, pages: Long, size: Long, type: Int): Result<PageInfo<CartoonVo>>

    /**
     * 添加漫画信息
     */
    fun addCartoon(cartoonDto: CartoonDto?): Result<String>

    /**
     * 修改漫画信息
     */
    fun updateCartoon(cartoonDto: CartoonDto?): Result<String>
}
