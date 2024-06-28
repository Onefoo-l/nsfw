package com.it.onefool.nsfw18.service.Impl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.mapper.ChapterMapper
import com.it.onefool.nsfw18.service.ChapterService
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【chapter】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:07
 */
@Service
class ChapterServiceImpl : ServiceImpl<ChapterMapper?, Chapter?>(), ChapterService{
    companion object{
        private val log = LoggerFactory.getLogger(ChapterServiceImpl::class.java)
    }


}
