package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.entry.ImgAddress
import com.it.onefool.nsfw18.exception.CustomizeException
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
        private val logger = LoggerFactory.getLogger(ImgAddressServiceImpl::class.java)
    }

    /**
     * 根据图片id查询图片地址
     */
    override fun findByImgId(list: List<Int>) : List<ImgAddress>{
        val qwImg = QueryWrapper<ImgAddress>()
        qwImg.`in`("id", list)
        val imgAddress = this.list(qwImg)
        if (imgAddress.isNullOrEmpty()) throw CustomizeException(
            StatusCode.NOT_FOUND.code(),
            StatusCode.NOT_FOUND.message()
        )
        return imgAddress.filterNotNull()
    }
}
