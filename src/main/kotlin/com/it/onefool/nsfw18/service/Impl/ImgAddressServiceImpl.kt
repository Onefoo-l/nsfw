package com.it.onefool.nsfw18.service.Impl

import cn.hutool.core.lang.UUID
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.config.MinioConfig
import com.it.onefool.nsfw18.domain.entry.ImgAddress
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.ImgAddressMapper
import com.it.onefool.nsfw18.service.ImgAddressService
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

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

    @Autowired
    private lateinit var minioClient: MinioClient

    @Autowired
    private lateinit var minioConfig: MinioConfig

    /**
     * 根据图片id查询图片地址
     */
    override fun findByImgId(list: List<Int>): List<ImgAddress> {
        val qwImg = QueryWrapper<ImgAddress>()
        qwImg.`in`("id", list)
        val imgAddress = this.list(qwImg)
        if (imgAddress.isNullOrEmpty()) throw CustomizeException(
            StatusCode.NOT_FOUND.code(),
            StatusCode.NOT_FOUND.message()
        )
        return imgAddress.filterNotNull()
    }

    /**
     * 上传图片
     */
    override fun upload(uploadFile: MultipartFile?): Result<String> {
        uploadFile ?: throw CustomizeException(
            StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message()
        )
        if (uploadFile.size > 1024 * 1024 * 10) throw CustomizeException(
            StatusCode.PARAM_ERROR.code(), StatusCode.PARAM_ERROR.message()
        )
        val fileName = uploadFile.originalFilename ?: UUID.randomUUID().toString().replace("-", "")
        val inputStream = uploadFile.inputStream
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .`object`(fileName)
                    .stream(inputStream, uploadFile.size, -1)
                    .contentType(uploadFile.contentType)
                    .build()
            )
            inputStream.close()
        } catch (e: Exception) {
            throw CustomizeException(
                StatusCode.ERROR_UPLOAD_WITH_THE_FILE.code(),
                StatusCode.ERROR_UPLOAD_WITH_THE_FILE.message()
            )
        }
        return Result.ok()
    }

    /**
     * 下载图片
     * 通过字节流下载
     */
    override fun download(imgId: Int): Result<ByteArray> {
        val imgAddress = this.getById(imgId) ?: throw CustomizeException(
            StatusCode.NOT_FOUND.code(),
            StatusCode.NOT_FOUND.message()
        )
        try {
            val input = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .`object`(imgAddress.imgName)
                    .build()
            )
            val buffer = StreamUtils.copyToByteArray(input)
            input.read(buffer)
            input.close()
            return Result.ok(buffer)
        } catch (e: Exception) {
            throw CustomizeException(
                StatusCode.ERROR_DOW_WITH_THE_FILE.code(),
                e.message!!
            )
        }
    }


}
