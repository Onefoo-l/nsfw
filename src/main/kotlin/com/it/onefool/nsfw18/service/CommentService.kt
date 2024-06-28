package com.it.onefool.nsfw18.service
import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.Comment

/**
 * @author 97436
 * @description 针对表【comment】的数据库操作Service
 * @createDate 2024-06-25 16:51:19
 */
interface CommentService : IService<Comment?>{
    fun pageComment(pageRequestDto: PageRequestDto<Comment>) : Result<PageInfo<Comment>>
}
