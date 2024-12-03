package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.dto.ImgDto
import com.it.onefool.nsfw18.service.ImgAddressService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
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
     * 上传单张图片(该接口不对用户开放，只用于管理员操作)
     */
    @PostMapping("/upload")
    fun upload(file: MultipartFile?): Result<String> {
        return imgAddressService.upload(file)
    }

    /**
     * 上传多张图片(该接口不对用户开放，只用于管理员操作)
     */
    @PostMapping("/uploadList")
    fun uploadList(files: List<MultipartFile>?,
        @RequestParam("cartoonId") cartoonId: Int,
        @RequestParam("chapterId") chapterId: Int
    ): Result<String> {
        return imgAddressService.uploadList(files, ImgDto().apply {
            this.cartoonId = cartoonId
            this.chapterId = chapterId
        })
    }


    /**
     * 下载图片(该接口不对用户开放，只用于管理员操作)
     */
    @PostMapping("/download/{imgId}")
    fun download(@PathVariable imgId: Int): Result<ByteArray> {
        return imgAddressService.download(imgId)
    }

    /**
     * 删除图片(该接口不对用户开放)
     */
    @PostMapping("/delete/{fileName}")
    fun delete(@PathVariable fileName: String): Result<String> {
        return imgAddressService.delete(fileName)
    }
}