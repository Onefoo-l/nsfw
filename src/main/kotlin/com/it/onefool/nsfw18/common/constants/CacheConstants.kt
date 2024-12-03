package com.it.onefool.nsfw18.common.constants

/**
 * @Author linjiawei
 * @Date 2024/6/24 21:28
 */
class CacheConstants {
    companion object {
        //认证后的用户key前缀
        val LOGIN_USER_KEY = "login_key:"

        //存入redis的一级评论Key  评论详情
        val COMMENTS = "comments:"
        //存入redis的二级评论Key  评论详情
        val COMMENTS_REPLY = "comments_reply:"

        //存入redis的评论人id
        val COMMENTS_USER_ID = "comments_userId:"

        //存入redis的评论点赞 存储点赞数量
        val COMMENT_LIKE = "comment_like:"
        //存入redis的回复点赞  存储点赞数量
        val COMMENT_REPLY_LIKE = "comment_reply_like:"

        //一条评论id对应的多人的key
        val THUMBS_UP_USERS_KEY = "Thumbs_up_users_key:"
        val THUMBS_UP_USERS_REPLY_KEY = "Thumbs_up_users_reply_key:"

        //存入redis中一级评论少量值的key 评论id对应少量评论内容
        val COMMENT = "comment:"
        //存入redis中二级评论少量值的key  评论id对应少量评论内容
        val COMMENT_REPLY = "comment_reply:"
    }

}