package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.domain.entry.CartoonLabel
import com.it.onefool.nsfw18.mapper.CartoonLabelMapper
import com.it.onefool.nsfw18.service.CartoonLabelService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @description 针对表【cartoon_label】的数据库操作Service实现
 * @createDate 2024-06-25 16:50:54
 */
@Service
class CartoonLabelServiceImpl :
    ServiceImpl<CartoonLabelMapper?, CartoonLabel?>(), CartoonLabelService {
    companion object {
        private val log = LoggerFactory.getLogger(CartoonLabelServiceImpl::class.java)
    }
}
