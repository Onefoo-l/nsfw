package com.it.onefool.nsfw18.service

import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.dto.CommentConDto
import com.it.onefool.nsfw18.domain.dto.CommentReplyVoToDto
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.vo.CommentVo
import jakarta.servlet.http.HttpServletRequest

/**
 * @author 97436
 * @description 针对表【comment】的数据库操作Service
 * @createDate 2024-06-25 16:51:19
 */
interface CommentService : IService<Comment?> {
    /**
     * 从漫画查询中去分页查询评论列表
     */
    fun pageComment(pageRequestDto: PageRequestDto<Comment?>): Result<PageInfo<CommentVo>>

    /**
     * 分页查询回复评论
     */
    fun pageComment(
        pageRequestDto: PageRequestDto<CommentConDto?>,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentVo>>

    /**
     * 分页查询回复评论
     */
    fun pageComment(
        currentPage: Long,
        pageSize: Long,
        cartoonId: Int,
        chapterId: Int
    )
            : Result<PageInfo<CommentVo>>
    /**
     * 删除一级评论
     */
    fun deleteComment(id: Int): Result<String>

    /**
     * 插入一级评论
     */
    fun addComment(
        comment: CommentConDto,
        request: HttpServletRequest
    ): Result<PageInfo<CommentVo>>

    /**
     * 删除评论记录
     */
    fun deleteComments(
        request: HttpServletRequest,
        commentReplyDto: CommentReplyVoToDto
    )
            : Result<PageInfo<CommentVo>>

    /**
     * 查询当前用户的所写评论
     */
    fun findByUserComments(start: Int, end: Int, request: HttpServletRequest)
            : Result<PageInfo<CommentVo>>
}
