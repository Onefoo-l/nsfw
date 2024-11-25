package com.it.onefool.nsfw18.utils

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.po.CartoonPo
import com.it.onefool.nsfw18.domain.dto.ChapterDto
import com.it.onefool.nsfw18.domain.dto.CommentDto
import com.it.onefool.nsfw18.domain.dto.UserDto
import com.it.onefool.nsfw18.domain.entry.*
import com.it.onefool.nsfw18.domain.vo.CartoonVo
import com.it.onefool.nsfw18.domain.vo.CommentReplyVo
import com.it.onefool.nsfw18.domain.vo.CommentVo
import com.it.onefool.nsfw18.exception.CustomizeException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/6/28 17:12
 */
@Component
class BeanUtils {
    companion object {
        private val log = LoggerFactory.getLogger(BeanUtils::class.java)
    }

    /**
     * 漫画和漫画vo的复制
     */
    fun copyCartoonAndCartoonVo(cartoon: Cartoon, cartoonVo: CartoonVo) {
        cartoonVo.id = cartoon.id
        cartoonVo.title = cartoon.title
        cartoonVo.avatar = cartoon.avatar
        cartoonVo.describe = cartoon.describe
        cartoonVo.niceCount = cartoon.niceCount
        cartoonVo.readCount = cartoon.readCount
        cartoonVo.collectionCount = cartoon.collectionCount
        cartoonVo.createTime = cartoon.createTime
        cartoonVo.updateTime = cartoon.updateTime
    }

    /**
     * 章节和漫画vo的复制
     */
    fun copyChapterAndCartoonVo(chapter: List<Chapter?>, cartoonVo: CartoonVo) {
        val list = mutableListOf<ChapterDto>()
        chapter.forEach { c ->
            val chapterDto = ChapterDto().apply {
                this.chapterId = c!!.chapterId
                this.chapterName = c.chapterName
            }
            list.add(chapterDto)
        }
        cartoonVo.chapterDto = list
    }

    /**
     * 标签和漫画vo的复制
     */
    fun copyLabelAndCartoonVo(label: List<Label?>, cartoonVo: CartoonVo) {
        val listW = mutableListOf<String>()
        val listT = mutableListOf<String>()
        val listA = mutableListOf<String>()
        label.forEach {
            it?.workDescription?.let { w -> listW.add(w) }
            it?.tag?.let { w -> listT.add(w) }
            it?.author?.let { w -> listA.add(w) }
        }
        cartoonVo.workDescription = listW
        cartoonVo.tag = listT
        cartoonVo.author = listA
    }

    /**
     * 评论和漫画vo的复制
     */
    fun copyCommentAndCartoonVo(comment: List<CommentVo?>, cartoonVo: CartoonVo) {
        val commentList = comment.map {
            CommentDto().apply {
//                this.userId = it?.userId
                this.nickName = it?.nickName
                this.headImage = it?.headImage
                this.content = it?.content
                this.likes = it?.likes
                this.replys = it?.replys
                this.userTime = it?.createdTime
            }
        }
        cartoonVo.commentDtoList = commentList
    }

    /**
     * 漫画BO和漫画vo的复制
     */
    fun copyCartoonBoAndCartoonVo(cartoonBoList: List<CartoonPo?>, cartoonVoList: MutableList<CartoonVo>) {
        cartoonBoList.forEach { c ->
            c?.let {
                val cartoonVo = CartoonVo().apply {
                    id = it.id
                    title = it.title
                    avatar = it.avatar
                    niceCount = it.niceCount
                    readCount = it.readCount
                    collectionCount = it.collectionCount
                    createTime = it.createTime
                    updateTime = it.updateTime
                    author = it.authors
                }
                cartoonVoList.add(cartoonVo)
            } ?: run {
                throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
            }
        }
    }

    /**
     * 评论回复和评论回复vo的复制
     */
    fun copyCommentReplyAndReplyVo(
        redisResult: Collection<Any>,
        replyList: MutableList<CommentReplyVo>,
        userId: Int
    ) {
        redisResult.forEach { r ->
            val reply = r as CommentReply
            var status = 0
            //0是自己的评论，1是别人的评论。可以设置为删除或者是检举
            if (userId != reply.userId) status = 1
            val replyVo = CommentReplyVo().apply {
                this.id = reply.id
                this.nickName = reply.nickName
                this.headImage = reply.headImage
                this.cartoonId = reply.cartoonId
                this.chapterId = reply.chapterId
                this.commentId = reply.commentId
                this.content = reply.content
                this.likes = reply.likes
                this.status = status
                this.createdTime = reply.createdTime
                this.updatedTime = reply.updatedTime
            }
            replyList.add(replyVo)
        }
    }

    /**
     * 评论和评论vo的复制
     */
    fun copyCommentAndCommentVo(records: List<Comment>, commentList: MutableList<CommentVo>) {
        records.forEach { r ->
            val commentVo = CommentVo().apply {
                this.id = r.id
                this.nickName = r.nickName
                this.headImage = r.headImage
                this.cartoonId = r.cartoonId
                this.chapterId = r.chapterId
                this.content = r.content
                this.likes = r.likes
                this.replys = r.replys
                this.createdTime = r.createdTime
            }
            commentList.add(commentVo)
        }
    }

    /**
     * 评论和评论vo的复制
     */
    fun copyCommentAndCommentVo(
        comment: Comment,
        commentVo: CommentVo,
        userDto: UserDto?
    ) {
        var status = 0
        //为空或者当前用户id不等于自己说明是别人的评论
        if (userDto == null || userDto.userId.toInt() != comment.userId) status = 1
        commentVo.id = comment.id
        commentVo.nickName = comment.nickName
        commentVo.headImage = comment.headImage
        commentVo.cartoonId = comment.cartoonId
        commentVo.chapterId = comment.chapterId
        commentVo.content = comment.content
        commentVo.likes = comment.likes
        commentVo.status = status
        commentVo.replys = comment.replys
        commentVo.createdTime = comment.createdTime
    }

    /**
     * 评论回复和评论vo的复制
     */
    fun copyCommentReplyAndCommentVo(
        commentReplyList: List<CommentReply>,
        commentLists: MutableList<CommentVo>
    ) {
        commentReplyList.forEach {
            val commentVo = CommentVo().apply {
                this.id = it.id
                this.nickName = it.nickName
                this.headImage = it.headImage
                this.cartoonId = it.cartoonId
                this.chapterId = it.chapterId
                this.content = it.content
                this.likes = it.likes
                this.createdTime = it.createdTime
                this.status = 0
            }
            commentLists.add(commentVo)
        }
    }

    /**
     * 评论和评论回复vo的复制
     */
    fun copyCommentAndCommentReplyVo(r: Comment, replyVo: CommentReplyVo) {
        //一级评论的id成为该对象的id
        replyVo.id = r.id
        replyVo.nickName = r.nickName
        replyVo.headImage = r.headImage
        replyVo.cartoonId = r.cartoonId
        replyVo.chapterId = r.chapterId
        replyVo.content = r.content
        replyVo.likes = r.likes
        replyVo.createdTime = r.createdTime
        replyVo.updatedTime = r.updatedTime
        //因为是一级评论，所以并没有针对回复评论的id
        replyVo.commentId = 0
        //0:一级评论 1:二级评论以下评论
        replyVo.level = 0
        replyVo.status = 0
    }

    /**
     * 评论回复和评论回复vo的复制
     */
    fun copyCommentReplyAndCommentReplyVo(
        r: CommentReply,
        replyVo: CommentReplyVo
    ) {
        //二级级评论的id成为该对象的id
        replyVo.id = r.id
        replyVo.nickName = r.nickName
        replyVo.headImage = r.headImage
        replyVo.cartoonId = r.cartoonId
        replyVo.chapterId = r.chapterId
        replyVo.content = r.content
        replyVo.likes = r.likes
        replyVo.createdTime = r.createdTime
        replyVo.updatedTime = r.updatedTime
        //因为是二级评论，所以这里有一级评论的id
        replyVo.commentId = r.commentId
        //0:一级评论 1:二级评论以下评论
        replyVo.level = 1
        replyVo.status = 0
    }

    /**
     * 评论回复和评论回复vo的复制
     */
    fun copyCommentReplyAndCommentReplyVo(
        r: List<CommentReply?>,
        replyVo: MutableList<CommentReplyVo>,
        userId: Int
    ) {
        r.forEach { i ->
            val commentReplyVo = CommentReplyVo().apply {
                this.id = i!!.id
                this.nickName = i.nickName
                this.headImage = i.headImage
                this.cartoonId = i.cartoonId
                this.chapterId = i.chapterId
                this.commentId = i.commentId
                this.content = i.content
                this.likes = i.likes
                if (userId == i.userId) this.status = 0
                else this.status = 1
                this.level = 1
                this.createdTime = i.createdTime
                this.updatedTime = i.updatedTime

            }
            replyVo.add(commentReplyVo)
        }
    }

}