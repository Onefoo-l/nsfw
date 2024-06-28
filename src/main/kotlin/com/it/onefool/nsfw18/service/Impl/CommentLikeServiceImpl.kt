package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.CommentLike
import com.it.onefool.nsfw18.mapper.CommentLikeMapper
import com.it.onefool.nsfw18.service.CommentLikeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【comment_like】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:25
 */
@Service
class CommentLikeServiceImpl
    : ServiceImpl<CommentLikeMapper?, CommentLike?>(), CommentLikeService {
    companion object {
        private val log = LoggerFactory.getLogger(CommentLikeServiceImpl::class.java)
    }
}
