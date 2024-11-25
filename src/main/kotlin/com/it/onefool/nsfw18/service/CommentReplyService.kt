package com.it.onefool.nsfw18.service

import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.dto.CommentReplyContentDto
import com.it.onefool.nsfw18.domain.dto.CommentReplyDto
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.domain.vo.CommentReplyVo
import jakarta.servlet.http.HttpServletRequest

/**
 * @author 97436
 * @description 针对表【comment_reply】的数据库操作Service
 * @createDate 2024-06-25 16:51:32
 */
interface CommentReplyService : IService<CommentReply?> {

    /**
     * 根据漫画id查询当前漫画回复评论
     * (此处需考虑内存溢出情况，暂时先不考虑，等线上做起来后要么扩充内存，要么优化表，反正最后都得扩充内存)
     */
    fun findByCommentId(id: Int): Result<List<CommentReply?>?>

    /**
     * 分页查询回复评论（弃用，无需要分页查询，全部查询后返回给前端）
     */
    fun pageCommentReply(
        pageRequestDto: PageRequestDto<CommentReplyDto?>,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentReplyVo>>

    /**
     * 插入回复评论
     */
    fun addCommentReply(
        commentReply: CommentReplyContentDto,
        request: HttpServletRequest
    ): Result<PageInfo<CommentReplyVo>>

    /**
     * 删除回复评论
     */
    fun deleteCommentReply(id: Int)


    /**
     * 提供给外部调用的方法
     */
    fun getCommentsByPageMethod(
        commentReply: CommentReplyDto
    ): List<CommentReplyVo?>

    /**
     * 查询回复评论
     */
    fun findCommentReply(
        page: CommentReplyDto,
        request: HttpServletRequest
    ): Result<List<CommentReplyVo>>
}
