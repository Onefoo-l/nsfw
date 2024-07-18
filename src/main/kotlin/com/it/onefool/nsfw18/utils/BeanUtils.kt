package com.it.onefool.nsfw18.utils

import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.po.CartoonPo
import com.it.onefool.nsfw18.domain.dto.ChapterDto
import com.it.onefool.nsfw18.domain.dto.CommentDto
import com.it.onefool.nsfw18.domain.entry.*
import com.it.onefool.nsfw18.domain.vo.CartoonVo
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
    fun copyCommentAndCartoonVo(comment: List<Comment?>, cartoonVo: CartoonVo) {
        val commentList = comment.map {
            CommentDto().apply {
                id = it?.id
                userId = it?.userId
                nickName = it?.nickName
                headImage = it?.headImage
                content = it?.content
                likes = it?.likes
                replys = it?.replys
                userTime = it?.createdTime
            }
        }
        cartoonVo.commentDtoList = commentList
    }

    /**
     * 漫画BO和漫画vo的复制
     */
    fun copyCartoonBoAndCartoonVo(cartoonBoList: List<CartoonPo?>, cartoonVoList: MutableList<CartoonVo>) {
        cartoonBoList.forEach {c ->
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

}