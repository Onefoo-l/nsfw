package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.CommentDto
import com.it.onefool.nsfw18.domain.entry.Cartoon
import com.it.onefool.nsfw18.domain.entry.CartoonLabel
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.domain.entry.Label
import com.it.onefool.nsfw18.domain.vo.CartoonVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CartoonMapper
import com.it.onefool.nsfw18.service.*
import com.it.onefool.nsfw18.utils.BeanUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.reflect.Field
import kotlin.math.log

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Service实现
 * @createDate 2024-06-25 02:49:19
 */
@Service
class CartoonServiceImpl : ServiceImpl<CartoonMapper?, Cartoon?>(), CartoonService {
    companion object {
        private val logger = LoggerFactory.getLogger(CartoonServiceImpl::class.java)
    }

    @Autowired
    private lateinit var beanUtils: BeanUtils

    @Autowired
    private lateinit var chapterService: ChapterService

    @Autowired
    private lateinit var cartoonLabelService: CartoonLabelService

    @Autowired
    private lateinit var labelService: LabelService

    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var commentReplyService: CommentReplyService


    /**
     * 查看漫画id
     */
    override fun findId(id: Int?): Result<Any> {
        id?.let { i ->
            val cartoonVo = CartoonVo()
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.eq("id", i)
            this.getOne(qwCartoon)?.let {
                // 尽量不用反射，线上影响性能
                //漫画查询
                beanUtils.copyCartoonAndCartoonVo(it, cartoonVo)
            } ?: run { throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message()) }
            // 漫画章节查询
            chapterService.findByCartoonId(i).data?.let {
                beanUtils.copyChapterAndCartoonVo(it, cartoonVo)
            }
            //漫画标签查询
            cartoonLabelService.findByCartoonId(i).data?.let { c ->
                val labelIds = c.map { it?.labelId }
                labelService.findById(labelIds).data.let {
                    beanUtils.copyLabelAndCartoonVo(it, cartoonVo)
                }
            }
            // 调用评论业务逻辑层分页查询
            // 回复评论是建立在有评论的情况下
            reflexInfo(i,Comment())?.let {r->
                commentService.pageComment(r).data?.let {
                    beanUtils.copyCommentAndCartoonVo(it.list, cartoonVo)
                    //调用回复评论业务逻辑层分页查询
                    //TODD 表可以优化，以后重构优化
                    /*reflexInfo(i,CommentReply())?.let {
                        commentReplyService.findByCommentId(i).data?.let { c ->
                            commentReplyBuilder(c, cartoonVo)
                        }
                    }*/
                }
            }
            logger.info("查询漫画信息,{}",cartoonVo)
            return Result.ok(cartoonVo)
        }
        return Result.error()
    }

/*    *//**
     * 构建整个漫画的评论
     *//*
    fun commentReplyBuilder(reply: List<CommentReply?>, cartoonVo: CartoonVo) {
        val firstLevelComment = cartoonVo.commentDtoList
        //遍历一级评论
        firstLevelComment.forEach {
            val commentS = commentBuilderTree(reply,it)
            //设置二级评论
            cartoonVo.commentDtoList = commentS
        }
    }

    *//**
     * 构建回复评论列表
     *//*
    fun commentBuilderTree(reply: List<CommentReply?>,comment: CommentDto) : List<CommentDto> {
        val children = mutableListOf<CommentDto>()
        reply.forEach {
            // 二级评论
            if (it?.commentId == comment.id) {
                val commentDto = CommentDto().apply {
                    id = it?.id
                    userId = it?.userId
                    nickName = it?.nickName
                    headImage = it?.headImage
                    content = it?.content
                    likes = it?.likes
                    userTime = it?.createdTime
                }
                children.add(commentDto)
            }
            // 递归调用
            commentBuilderTree(reply,commentDto)
        }
    }*/

    /**
     * 简化代码，必要时刻可以使用反射
     */
    fun <T> reflexInfo(id: Int, t: T): PageRequestDto<T?>? {
        val clazz = t!!::class.java
        val fields = clazz.declaredFields
        fields.forEach {
            if (it.name == "cartoonId") {
                val clazzT = clazz.newInstance()
                val cId = clazz.getDeclaredField("cartoonId")
                cId.isAccessible = true
                cId.set(clazzT,id)
                return PageRequestDto(1, 5, clazzT)
            }
        }
        return PageRequestDto(1, 5, null)
    }
}
