package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.aop.log.Log
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.CommentReplyContentDto
import com.it.onefool.nsfw18.domain.dto.CommentReplyDto
import com.it.onefool.nsfw18.domain.vo.CommentReplyVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.service.CommentReplyService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author linjiawei
 * @Date 2024/7/13 1:53
 * 回复评论
 */
@RestController
@RequestMapping("/reply")
class CommentReplyController {

    @Autowired
    private lateinit var commentReplyService: CommentReplyService

    /**
     * 分页查询回复评论（弃用，回复评论一同返回给前端）
     */
    @PostMapping("/page")
    @Deprecated("弃用")
    @Log("分页查询回复评论")
    fun pageCommentReply(
        @RequestBody page: PageRequestDto<CommentReplyDto?>,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentReplyVo>> {
        page.body?.let {
            return commentReplyService.pageCommentReply(page, request)
        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }

    /**
     * 查询回复评论
     */
    @PostMapping("/find")
    @Log("查询回复评论")
    fun findCommentReply(
        @RequestBody page: CommentReplyDto,
        request: HttpServletRequest
    )
            : Result<List<CommentReplyVo>> {
        if (page == null) throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
        return commentReplyService.findCommentReply(page, request)

    }

    /**
     * 插入回复评论
     */
    @PostMapping("/addCommentReply")
    @Log("插入回复评论")
    fun addCommentReply(
        @RequestBody commentReply: CommentReplyContentDto?,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentReplyVo>> {
        commentReply?.let {
            return commentReplyService.addCommentReply(commentReply, request)
        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }

    /**
     * 删除回复评论
     */
    @PostMapping("/deleteCommentReply/{id}")
    @Log("删除回复评论")
    fun deleteCommentReply(@PathVariable id: Int) {
        return commentReplyService.deleteCommentReply(id)
    }
}