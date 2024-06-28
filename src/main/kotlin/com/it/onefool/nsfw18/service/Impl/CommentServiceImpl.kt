package com.it.onefool.nsfw18.service.Impl
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.domain.entry.Comment
import com.it.onefool.nsfw18.mapper.CommentMapper
import com.it.onefool.nsfw18.service.CommentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:19
 */
@Service
class CommentServiceImpl : ServiceImpl<CommentMapper?, Comment?>(), CommentService{
    companion object {
        private val log = LoggerFactory.getLogger(CommentServiceImpl::class.java)
    }

    /**
     * 分页查询评论
     */
    override fun pageComment(pageRequestDto: PageRequestDto<Comment>): PageInfo<Comment> {
        val pageSize = pageRequestDto.size
        val pageSum = pageRequestDto.page
        val comment = pageRequestDto.body
        val page = Page<Comment>(pageSum,pageSize)
        val qwComment = QueryWrapper<Comment>()
        qwComment
            .eq("cartoon_id",comment.cartoonId)
            .orderByAsc("create_time")
        this.baseMapper?.selectPage(page,qwComment)
        return PageInfo<Comment>(
            page.current,
            page.size,
            page.total,
            page.pages,
            page.records
        )
    }
}
