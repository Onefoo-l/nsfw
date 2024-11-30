package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.service.ImgAddressService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * @Author linjiawei
 * @Date 2024/11/30 14:40
 * minio的控制层,不对外开放，只用于管理员上传下载图片数据
 */
@RequestMapping("/imgAddress")
@RestController
class ImgAddressController {

    companion object {
        private val logger = LoggerFactory.getLogger(ImgAddressController::class.java)
    }

    @Autowired
    private lateinit var imgAddressService: ImgAddressService

    /**
     * 上传图片(该接口不对用户开放，只用于管理员操作)
     */
    @PostMapping("/upload")
    fun upload(uploadFile: MultipartFile?): Result<String> {
        return imgAddressService.upload(uploadFile)
    }


    /**
     * 下载图片(该接口不对用户开放，只用于管理员操作)
     */
    @PostMapping("/download")
    fun download(imgId: Int): Result<ByteArray> {
        return imgAddressService.download(imgId)
    }
}