package com.it.onefool.nsfw18.service.Impl

import cn.hutool.core.lang.UUID
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.config.MinioConfig
import com.it.onefool.nsfw18.domain.dto.ImgDto
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.domain.entry.ImgAddress
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.ImgAddressMapper
import com.it.onefool.nsfw18.service.ChapterService
import com.it.onefool.nsfw18.service.ImgAddressService
import io.minio.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StreamUtils
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime


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

    @Autowired
    @Lazy
    private lateinit var chapterService: ChapterService

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
    override fun upload(file: MultipartFile?): Result<String> {
        file ?: throw CustomizeException(
            StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message()
        )
        if (file.size > 1024 * 1024 * 10) throw CustomizeException(
            StatusCode.PARAM_ERROR.code(), StatusCode.PARAM_ERROR.message()
        )
        val fileName = file.originalFilename ?: UUID.randomUUID().toString().replace("-", "")
        val inputStream = file.inputStream
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .`object`(fileName)
                    .stream(inputStream, file.size, -1)
                    .contentType(file.contentType)
                    .build()
            )
            this.baseMapper!!.insert(ImgAddress().apply {
                this.imgRank = 1
                this.imgName = fileName
                this.address = minioConfig.getEndpoint() +
                        "/" + minioConfig.getBucketName() +
                        "/" + fileName
                this.createTime = LocalDateTime.now()
                this.updateTime = LocalDateTime.now()
            })
        } catch (e: Exception) {
            logger.error(e.message)
            throw CustomizeException(
                StatusCode.ERROR_UPLOAD_WITH_THE_FILE.code(),
                StatusCode.ERROR_UPLOAD_WITH_THE_FILE.message()
            )
        } finally {
            inputStream.close()
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

    /**
     * 删除图片
     */
    override fun delete(fileName: String): Result<String> {
        try {
            // 判断桶是否存在
            val res = minioClient.bucketExists(
                BucketExistsArgs
                    .builder()
                    .bucket(minioConfig.getBucketName())
                    .build()
            )
            if (res) {
                // 删除文件
                minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(minioConfig.getBucketName())
                        .`object`(fileName).build()
                )
            }
        } catch (e: Exception) {
            logger.error("删除文件失败,原因{}", e.message)
            return Result.error("删除文件失败")
        }
        return Result.ok()
    }

    /**
     * 上传多张图片
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun uploadList(
        files: List<MultipartFile>?,
        imgDto: ImgDto
    ): Result<String> {
        if (files!!.isNullOrEmpty()
            || imgDto.cartoonId == null
            || imgDto.chapterId == null
        ) throw CustomizeException(
            StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message()
        )
        val imgAddresses = mutableListOf<ImgAddress>()
        val uploadedFileNames = mutableListOf<String>()
        var v = 1
        val qw = QueryWrapper<Chapter>()
        qw.eq("cartoon_id", imgDto.cartoonId)
            .eq("chapter_id", imgDto.chapterId)
        val chapter = chapterService.list(qw)
        if (chapter.isNotEmpty()) chapterService.baseMapper.delete(qw)
        val chapterImg = chapter.map { it?.imgAddressId }.toList()
        this.removeByIds(chapterImg)
        for (file in files) {
            if (file.size > 1024 * 1024 * 10) throw CustomizeException(
                StatusCode.PARAM_ERROR.code(), StatusCode.PARAM_ERROR.message()
            )
            var fileName = file.originalFilename
                ?: UUID.randomUUID().toString().replace("-", "")
            val inputStream = file.inputStream
            try {
                var status = false
                try {
                    minioClient.statObject(
                        StatObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .`object`(fileName)
                            .build()
                    )
                } catch (e: Exception) {
                    status = true
                }
                if (!status) {
                    fileName = fileName
                        .split(".")[0] + UUID.randomUUID().toString()
                        .replace("-", "") + "." + fileName
                        .split(".")[1]
                }
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .`object`(fileName)
                        .stream(inputStream, file.size, -1)
                        .contentType(file.contentType)
                        .build()
                )
                val imgAddress = ImgAddress().apply {
                    this.imgRank = v++
                    this.imgName = fileName
                    this.address = "${minioConfig.getEndpoint()}/${minioConfig.getBucketName()}/$fileName"
                    this.createTime = LocalDateTime.now()
                    this.updateTime = LocalDateTime.now()
                }
                imgAddresses.add(imgAddress)
                uploadedFileNames.add(fileName)
            } catch (e: Exception) {
                logger.error(e.message)
                throw CustomizeException(
                    StatusCode.ERROR_UPLOAD_WITH_THE_FILE.code(),
                    StatusCode.ERROR_UPLOAD_WITH_THE_FILE.message()
                )
            } finally {
                inputStream.close()
            }
        }
        // 使用批量插入数据库
        val res = this.saveBatch(imgAddresses as Collection<ImgAddress?>?)
        if (!res) throw CustomizeException(
            StatusCode.FAILURE.code(), StatusCode.FAILURE.message()
        )
        val chapterList = mutableListOf<Chapter>()
        imgAddresses.forEach { i ->
            val chapter = Chapter().apply {
                this.cartoonId = imgDto.cartoonId
                this.chapterId = imgDto.chapterId
                this.imgAddressId = i.id
                this.chapterName = "第${imgDto.chapterId}章"
                this.createTime = LocalDateTime.now()
                this.updateTime = LocalDateTime.now()
            }
            chapterList.add(chapter)
        }
        chapterService.saveBatch(chapterList as Collection<Chapter?>?)
        return Result.ok()
    }
}
