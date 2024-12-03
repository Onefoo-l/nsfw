package com.it.onefool.nsfw18.controller

import RateLimitWithLock
import com.it.onefool.nsfw18.aop.log.Log
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.service.CommentLikeService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @Author linjiawei
 * @Date 2024/11/25 22:54
 * 评论点赞的控制层
 */
@RestController
@RequestMapping("/commentLike")
class CommentLikeController {

    companion object {
        private val logger = LoggerFactory.getLogger(CommentLikeController::class.java)
    }

    @Autowired
    private lateinit var commentLikeService: CommentLikeService

    /**
     * 点赞评论
     * @param id: 点赞评论的id
     * @param level: 评论等级 是否是一级评论或者是二级评论
     */
    @Log("点赞评论")
    @GetMapping("/add")
    @RateLimitWithLock("lock:like",
        "rate:limit",
        500,
        3000,
        5,
        100)
    fun addCommentLike(
        @RequestParam id: Int,
        @RequestParam level: Int,
        request: HttpServletRequest
    ): Result<String> {
        return commentLikeService.addCommentLike(id, level, request)
    }

    /**
     * 取消点赞评论
     * @param id: 点赞评论的id
     * @param level: 评论等级 是否是一级评论或者是二级评论
     */
    @Log("取消点赞评论")
    @GetMapping("/delete")
    @RateLimitWithLock("lock:dislike",
        "rate:disLimit",
        500,
        3000,
        5,
        100)
    fun deleteCommentLike(
        @RequestParam id: Int,
        @RequestParam level: Int,
        request: HttpServletRequest
    ): Result<String> {
        return commentLikeService.deleteCommentLike(id,level,request)
    }
}