package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.UserCartoon
import com.it.onefool.nsfw18.mapper.UserCartoonMapper
import com.it.onefool.nsfw18.service.UserCartoonService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【user_cartoon】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:54
 */
@Service
class UserCartoonServiceImpl : ServiceImpl<UserCartoonMapper?, UserCartoon?>(), UserCartoonService {
    companion object {
        private val logger = LoggerFactory.getLogger(UserCartoonServiceImpl::class.java)
    }
}
