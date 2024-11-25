package com.it.onefool.nsfw18.common.service

import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.entry.CommentReply

/**
 * @Author linjiawei
 * @Date 2024/11/24 18:05
 * 评论类的公共方法接口
 */
interface CommentAndReplyService {
    /**
     * 当前用户每插入一条评论，就往redis中另外存储一次评论记录
     *
     * key: CacheConstants.COMMENTS_USER_ID + r.userId
     *
     * value: CommentReplyVo对象
     *
     * score: 评论的时间戳
     */
    fun addRedisUserComments(r: Comment)
    /**
     * 当前用户每插入一条评论，就往redis中另外存储一次评论记录
     *
     * key: CacheConstants.COMMENTS_USER_ID + r.userId
     *
     * value: CommentReplyVo对象
     *
     * score: 评论的时间戳
     */
    fun addRedisUserComments(r: CommentReply)

    /**
     * 当前用户每插入一条评论，就往redis中另外存储一次评论记录
     *
     * key: CacheConstants.COMMENTS_USER_ID + r.userId
     *
     * value: CommentReplyVo对象
     *
     * score: 评论的时间戳
     */
    fun removeRedisUserComments(r: Comment)

    /**
     * 当前用户每插入一条评论，就往redis中另外存储一次评论记录
     *
     * key: CacheConstants.COMMENTS_USER_ID + r.userId
     *
     * value: CommentReplyVo对象
     *
     * score: 评论的时间戳
     */
    fun removeRedisUserComments(r: CommentReply)
}