package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.aop.log.Log
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.CommentConDto
import com.it.onefool.nsfw18.domain.dto.CommentReplyVoToDto
import com.it.onefool.nsfw18.domain.vo.CommentVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.service.CommentService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @Author linjiawei
 * @Date 2024/7/13 1:53
 * 一级评论
 */
@RestController
@RequestMapping("/comment")
class CommentController {

    @Autowired
    private lateinit var commentService: CommentService

    @Log("插入一级评论")
    @PostMapping("/add")
    fun addComment(
        @RequestBody commentConDto: CommentConDto?,
        request: HttpServletRequest
    ): Result<PageInfo<CommentVo>> {
        commentConDto?.let {
            return commentService.addComment(commentConDto, request)
        } ?: throw CustomizeException(
            StatusCode.NOT_FOUND_COMMENT.code(),
            StatusCode.NOT_FOUND_COMMENT.message()
        )
    }

    @Log("分页查询一级评论")
    @GetMapping("/page/{currentPage}/{pageSize}/{cartoonId}/{chapterId}")
    fun pageComment(
        @PathVariable currentPage: Long,
        @PathVariable pageSize: Long,
        @PathVariable cartoonId: Int,
        @PathVariable chapterId: Int
    ): Result<PageInfo<CommentVo>> {
        return commentService.pageComment(currentPage, pageSize, cartoonId, chapterId)
    }

    @Log("删除一级评论")
    @PostMapping("/delete/{id}")
    fun deleteComment(@PathVariable id: Int): Result<String>{
        return commentService.deleteComment(id)
    }
    //另外一项功能
    @Log("查看当前用户自己写的所有评论")
    @PostMapping("/findUserComment/{start}/{end}")
    fun findByUserId(
        request: HttpServletRequest,
        @PathVariable start: Int,
        @PathVariable end: Int
    ): Result<PageInfo<CommentVo>> {
        return commentService.findByUserComments(start, end, request)
    }

    @Log("删除当前用户自己的评论")
    @PostMapping("/deleteUserComment")
    fun findByUserId(
        commentReplyDto: CommentReplyVoToDto,
        request: HttpServletRequest
    ):
            Result<PageInfo<CommentVo>> {
        return commentService.deleteComments(request, commentReplyDto)
    }
}