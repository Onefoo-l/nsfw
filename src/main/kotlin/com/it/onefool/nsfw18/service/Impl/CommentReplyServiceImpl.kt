package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.constants.ResponseConstants
import com.it.onefool.nsfw18.domain.entry.CommentReply
import com.it.onefool.nsfw18.mapper.CommentReplyMapper
import com.it.onefool.nsfw18.service.CommentReplyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【comment_reply】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:32
 */
@Service
class CommentReplyServiceImpl
    : ServiceImpl<CommentReplyMapper?, CommentReply?>(), CommentReplyService {
    companion object {
        private val logger = LoggerFactory.getLogger(CommentReplyServiceImpl::class.java)
    }

    /**
     * 根据漫画id查询当前漫画回复评论
     * (此处需考虑内存溢出情况，暂时先不考虑，等线上做起来后要么扩充内存，要么优化表，反正最后都得扩充内存)
     * (做大做强就得加硬件设施)
     */
    override fun findByCommentId(id: Int): Result<List<CommentReply?>?> {
        val qwReply = QueryWrapper<CommentReply>()
        qwReply.eq("cartoon_id", id)
        return Result.ok(this.list(qwReply))
    }

    /**
     * 分页查询回复评论
     */
    override fun pageCommentReply(pageRequestDto: PageRequestDto<CommentReply?>): Result<PageInfo<CommentReply>> {
        val pageSize = pageRequestDto.size
        val pageCurrent = pageRequestDto.page
        pageRequestDto.body?.let {
            val page = Page<CommentReply>(pageSize, pageCurrent)
            val qwReply = QueryWrapper<CommentReply>()
            qwReply.eq("cartoon_id", it.commentId)
                .orderByDesc("createdTime")
            this.page(page, qwReply)
            return Result.ok(
                PageInfo<CommentReply>(
                    page.current,
                    page.size,
                    page.total,
                    page.pages,
                    page.records
                )
            )
        }
        return Result.error(StatusCode.NOT_FOUND_COMMENT)
    }
}
