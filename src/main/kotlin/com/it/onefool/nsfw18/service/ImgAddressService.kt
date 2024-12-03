package com.it.onefool.nsfw18.service

import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.dto.ImgDto
import com.it.onefool.nsfw18.domain.entry.ImgAddress
import org.springframework.web.multipart.MultipartFile

/**
 * @author 97436
 * @description 针对表【img_address】的数据库操作Service
 * @createDate 2024-06-25 16:51:45
 */
interface ImgAddressService : IService<ImgAddress?> {
    /**
     * 根据图片id查询图片地址
     */
    fun findByImgId(list: List<Int>): List<ImgAddress>

    /**
     * 上传图片
     */
    fun upload(file: MultipartFile?): Result<String>

    /**
     * 下载图片
     */
    fun download(imgId: Int): Result<ByteArray>

    /**
     * 删除图片
     */
    fun delete(fileName: String): Result<String>

    /**
     * 上传多张图片
     */
    fun uploadList(files: List<MultipartFile>?,imgDto: ImgDto): Result<String>


}
