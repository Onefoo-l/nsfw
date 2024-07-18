package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.CommentReplyContentDto
import com.it.onefool.nsfw18.domain.dto.CommentReplyDto
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.service.CommentReplyService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
     * 分页查询回复评论
     */
    @PostMapping("/page")
    fun pageCommentReply(@RequestBody page: PageRequestDto<CommentReplyDto?>)
            : Result<PageInfo<CommentReply>> {
        page.body?.let {
            return commentReplyService.pageCommentReply(page)
        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }

    /**
     * 插入回复评论
     */
    @PostMapping("/addCommentReply")
    fun addCommentReply(
        @RequestBody commentReply: CommentReplyContentDto?,
        request: HttpServletRequest
    )
            : Result<PageInfo<CommentReply>> {
        commentReply?.let {
            return commentReplyService.addCommentReply(commentReply,request)
        }
        throw CustomizeException(
            StatusCode.PARAM_ERROR.code(),
            StatusCode.PARAM_ERROR.message()
        )
    }
}