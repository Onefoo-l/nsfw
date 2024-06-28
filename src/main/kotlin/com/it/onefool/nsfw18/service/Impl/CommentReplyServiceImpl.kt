package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
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
        private val log = LoggerFactory.getLogger(CommentReplyServiceImpl::class.java)
    }
}
