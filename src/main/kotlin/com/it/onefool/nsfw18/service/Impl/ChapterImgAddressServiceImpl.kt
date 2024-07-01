package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.ChapterImgAddress
import com.it.onefool.nsfw18.mapper.ChapterImgAddressMapper
import com.it.onefool.nsfw18.service.ChapterImgAddressService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【chapter_img_address】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:13
 */
@Service
class ChapterImgAddressServiceImpl : ServiceImpl<ChapterImgAddressMapper?, ChapterImgAddress?>(),
    ChapterImgAddressService {
    companion object {
        private val logger = LoggerFactory.getLogger(ChapterImgAddressServiceImpl::class.java)
    }
}
