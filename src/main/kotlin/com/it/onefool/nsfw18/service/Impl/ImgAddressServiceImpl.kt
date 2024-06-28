package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.ImgAddress
import com.it.onefool.nsfw18.mapper.ImgAddressMapper
import com.it.onefool.nsfw18.service.ImgAddressService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【img_address】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:45
 */
@Service
class ImgAddressServiceImpl
    : ServiceImpl<ImgAddressMapper?, ImgAddress?>(), ImgAddressService {
    companion object {
        private val log = LoggerFactory.getLogger(ImgAddressServiceImpl::class.java)
    }
}
