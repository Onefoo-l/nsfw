package com.it.onefool.nsfw18.service.Impl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
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
}
