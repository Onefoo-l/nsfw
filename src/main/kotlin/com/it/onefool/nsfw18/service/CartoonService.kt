package com.it.onefool.nsfw18.service
import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.Cartoon

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Service
 * @createDate 2024-06-25 02:49:19
 */
interface CartoonService : IService<Cartoon?> {
    /**
     * 查看漫画id
     */
    fun findId(id: Int?): Result<Any>
}
