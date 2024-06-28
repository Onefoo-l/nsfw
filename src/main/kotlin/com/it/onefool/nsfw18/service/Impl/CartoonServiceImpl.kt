package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
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

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Service实现
 * @createDate 2024-06-25 02:49:19
 */
@Service
class CartoonServiceImpl : ServiceImpl<CartoonMapper?, Cartoon?>(), CartoonService {

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

    companion object {
        private val log = LoggerFactory.getLogger(CartoonServiceImpl::class.java)
    }

    /**
     * 查看漫画id
     */
    override fun findId(id: Int?): Result<Any> {
        id?.let { i ->
            val cartoonVo = CartoonVo()
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.eq("id", i)
            val cartoonOne = this.getOne(qwCartoon)?.let {
                // 尽量不用反射，线上影响性能
                beanUtils.copyCartoonAndCartoonVo(it, cartoonVo)
            } ?: run { throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message()) }

            val qwChapter = QueryWrapper<Chapter>()
            qwCartoon.eq("cartoon_id", i)
            chapterService.list(qwChapter)?.let {
                beanUtils.copyChapterAndCartoonVo(it, cartoonVo)
            }
            val qwCartoonLabel = QueryWrapper<CartoonLabel>()
            qwCartoonLabel.eq("cartoon_id", i)
            cartoonLabelService.list(qwCartoonLabel)?.let { c ->
                val labelIds = c.map { it?.labelId }
                val qwLabel = QueryWrapper<Label>()
                qwLabel.`in`("id", labelIds)
                labelService.list(qwLabel)?.let {
                    beanUtils.copyLabelAndCartoonVo(it, cartoonVo)
                }
            }
            // 调用评论业务逻辑层分页查询
            val pageDto = PageRequestDto(1, 5, Comment().apply { cartoonId = i })
            commentService.pageComment(pageDto).let {
                beanUtils.copyCommentAndCartoonVo(it.data.list, cartoonVo)
            }
            val qwReply = QueryWrapper<CommentReply>()
            qwReply.eq("cartoon_id", i)
            commentReplyService.list(qwReply)?.let { c ->
                commentReplyBuilder(cartoonVo)
            }
            return Result.ok(cartoonVo)
        }

        return Result.error()
    }

    /**
     * 构建整个漫画的评论
     */
    fun commentReplyBuilder(cartoonVo: CartoonVo) {

    }
}
