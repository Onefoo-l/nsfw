package com.it.onefool.nsfw18.service.Impl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.Label
import com.it.onefool.nsfw18.mapper.LabelMapper
import com.it.onefool.nsfw18.service.LabelService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【label】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:51
 */
@Service
class LabelServiceImpl : ServiceImpl<LabelMapper?, Label?>(), LabelService{
    companion object {
        private val log = LoggerFactory.getLogger(LabelServiceImpl::class.java)
    }
}
