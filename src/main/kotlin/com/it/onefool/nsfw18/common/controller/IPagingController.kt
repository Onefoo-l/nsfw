package com.it.onefool.nsfw18.common.controller

import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result

/***
 * 描述
 * @author Onefool
 * @version 1.0
 */
interface IPagingController<T> {
    /**
     * 根据查询条件 requestDto 分页查询
     * @return
     */
    fun findByPage(requestDto: PageRequestDto<T>): Result<PageInfo<T>>
}
