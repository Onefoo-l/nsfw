package com.it.onefool.nsfw18.utils

import cn.hutool.core.codec.Base64
import io.minio.*
import io.minio.http.Method
import io.minio.messages.Bucket
import io.minio.messages.DeleteObject
import io.minio.messages.Item
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Exception
import kotlin.Int
import kotlin.Long
import kotlin.Result
import kotlin.String
import kotlin.Throws
import kotlin.byteArrayOf


/**
 * @Author linjiawei
 * @Date 2024/7/11 20:03
 * minio工具类
 */
@Component
class MinioUtils {
    companion object {
        private val log = LoggerFactory.getLogger(MinioUtils::class.java)
    }

    @Autowired
    private lateinit var minioClient: MinioClient

    /**
     * 启动SpringBoot容器的时候初始化Bucket
     * 如果没有Bucket则创建
     *
     * @param bucketName
     */
    @Throws(Exception::class)
    private fun createBucket(bucketName: String) {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
        }
    }

    /**
     * 判断Bucket是否存在，true：存在，false：不存在
     *
     * @param bucketName
     * @return
     */
    @Throws(Exception::class)
    fun bucketExists(bucketName: String?): Boolean {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
    }

    /**
     * 获得Bucket的策略
     *
     * @param bucketName
     * @return
     */
    @Throws(Exception::class)
    fun getBucketPolicy(bucketName: String?): String {
        return minioClient.getBucketPolicy(
            GetBucketPolicyArgs
                .builder()
                .bucket(bucketName)
                .build()
        )
    }

    /**
     * 获得所有Bucket列表
     *
     * @return
     */
    @Throws(Exception::class)
    fun getAllBuckets(): List<Bucket> {
        return minioClient.listBuckets()
    }

    /**
     * 根据bucketName获取其相关信息
     *
     * @param bucketName
     * @return
     */
    @Throws(Exception::class)
    fun getBucket(bucketName: String?): Optional<Bucket> {
        return getAllBuckets().stream().filter(Predicate<Bucket> { b: Bucket ->
            b.name().equals(bucketName)
        }).findFirst()
    }

    /**
     * 根据bucketName删除Bucket，true：删除成功； false：删除失败，文件或已不存在
     *
     * @param bucketName
     * @throws Exception
     */
    @Throws(Exception::class)
    fun removeBucket(bucketName: String?) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build())
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    fun isObjectExist(bucketName: String?, objectName: String?): Boolean {
        var exist = true
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(objectName)
                    .build()
            )
        } catch (e: Exception) {
            log.error("[Minio工具类]>>>> 判断文件是否存在, 异常：", e)
            exist = false
        }
        return exist
    }

    /**
     * 判断文件夹是否存在
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    fun isFolderExist(bucketName: String?, objectName: String): Boolean {
        var exist = false
        try {
            val results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(objectName)
                    .recursive(false)
                    .build()
            )
            for (result in results) {
                val item: Item = result.get()
                if (item.isDir && objectName == item.objectName()) {
                    exist = true
                }
            }
        } catch (e: Exception) {
            log.error("[Minio工具类]>>>> 判断文件夹是否存在，异常：", e)
            exist = false
        }
        return exist
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName 存储桶
     * @param prefix     前缀
     * @param recursive  是否使用递归查询
     * @return MinioItem 列表
     */
    @Throws(Exception::class)
    fun getAllObjectsByPrefix(
        bucketName: String?,
        prefix: String?,
        recursive: Boolean
    ): List<Item> {
        val list: MutableList<Item> = ArrayList()
        val objectsIterator = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(recursive)
                .build()
        )
        if (objectsIterator != null) {
            for (o in objectsIterator) {
                val item: Item = o.get()
                list.add(item)
            }
        }
        return list
    }

    /**
     * 获取文件流
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @return 二进制流
     */
    @Throws(Exception::class)
    fun getObject(
        bucketName: String?,
        objectName: String?
    ): InputStream {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .build()
        )
    }

    /**
     * 断点下载
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return 二进制流
     */
    @Throws(Exception::class)
    fun getObject(
        bucketName: String?,
        objectName: String?,
        offset: Long,
        length: Long
    ): InputStream {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .offset(offset)
                .length(length)
                .build()
        )
    }

    /**
     * 获取路径下文件列表
     *
     * @param bucketName 存储桶
     * @param prefix     文件名称
     * @param recursive  是否递归查找，false：模拟文件夹结构查找
     * @return 二进制流
     */
    fun listObjects(bucketName: String?, prefix: String?, recursive: Boolean):
            MutableIterable<io.minio.Result<Item>>? {
        return minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(recursive)
                .build()
        )
    }

    /**
     * 使用MultipartFile进行文件上传
     *
     * @param bucketName  存储桶
     * @param file        文件名
     * @param objectName  对象名
     * @param contentType 类型
     * @return
     */
    @Throws(Exception::class)
    fun uploadFile(
        bucketName: String?,
        file: MultipartFile,
        objectName: String?,
        contentType: String?
    ): ObjectWriteResponse {
        val inputStream = file.inputStream
        return minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .contentType(contentType)
                .stream(inputStream, inputStream.available().toLong(), -1)
                .build()
        )
    }

    /**
     * 图片上传
     * @param bucketName
     * @param imageBase64
     * @param imageName
     * @return
     */
    fun uploadImage(
        bucketName: String?,
        imageBase64: String,
        imageName: String
    ): ObjectWriteResponse? {
        if (!StringUtils.isEmpty(imageBase64)) {
            val `in` = base64ToInputStream(imageBase64)
            val newName = System.currentTimeMillis().toString() + "_" + imageName + ".jpg"
            val year: String = java.lang.String.valueOf(Date().getYear())
            val month: String = java.lang.String.valueOf(Date().getMonth())
            return uploadFile(bucketName, "$year/$month/$newName", `in`)
        }
        return null
    }

    // BASE64Decoder在jdk8以上的版本移除了，报错最简单解决换成jdk8就行了
    fun base64ToInputStream(base64: String): InputStream? {
        var stream: ByteArrayInputStream? = null
        try {
            val bytes: ByteArray = Base64.decode(base64.trim { it <= ' ' })
            stream = ByteArrayInputStream(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stream
    }


    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     * @return
     */
    @Throws(Exception::class)
    fun uploadFile(
        bucketName: String?,
        objectName: String?,
        fileName: String?
    ): ObjectWriteResponse {
        return minioClient.uploadObject(
            UploadObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .filename(fileName)
                .build()
        )
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     * @return
     */
    @Throws(Exception::class)
    fun uploadFile(
        bucketName: String?,
        objectName: String?,
        inputStream: InputStream?
    ): ObjectWriteResponse {
        return minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .stream(inputStream, inputStream!!.available().toLong(), -1)
                .build()
        )
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     * @return
     */
    @Throws(Exception::class)
    fun createDir(bucketName: String?, objectName: String?): ObjectWriteResponse {
        return minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .stream(ByteArrayInputStream(byteArrayOf()), 0, -1)
                .build()
        )
    }

    /**
     * 获取文件信息, 如果抛出异常则说明文件不存在
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     * @return
     */
    @Throws(Exception::class)
    fun getFileStatusInfo(bucketName: String?, objectName: String?): String {
        return minioClient.statObject(
            StatObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .build()
        ).toString()
    }

    /**
     * 拷贝文件
     *
     * @param bucketName    存储桶
     * @param objectName    文件名
     * @param srcBucketName 目标存储桶
     * @param srcObjectName 目标文件名
     */
    @Throws(Exception::class)
    fun copyFile(
        bucketName: String?,
        objectName: String?,
        srcBucketName: String?,
        srcObjectName: String?
    ): ObjectWriteResponse {
        return minioClient.copyObject(
            CopyObjectArgs.builder()
                .source(CopySource.builder().bucket(bucketName).`object`(objectName).build())
                .bucket(srcBucketName)
                .`object`(srcObjectName)
                .build()
        )
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     */
    @Throws(Exception::class)
    fun removeFile(bucketName: String?, objectName: String?) {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .build()
        )
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶
     * @param keys       需要删除的文件列表
     * @return
     */
    fun removeFiles(bucketName: String?, keys: List<String?>) {
        val objects: MutableList<DeleteObject> = LinkedList()
        keys.forEach(Consumer<String?> { s: String? ->
            objects.add(DeleteObject(s))
            try {
                removeFile(bucketName, s)
            } catch (e: Exception) {
                log.error("[Minio工具类]>>>> 批量删除文件，异常：", e)
            }
        })
    }

    /**
     * 获取文件外链
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @param expires    过期时间 <=7 秒 （外链有效时间（单位：秒））
     * @return url
     */
    @Throws(Exception::class)
    fun getPresignedObjectUrl(bucketName: String?, objectName: String?, expires: Int?): String {
        val args = GetPresignedObjectUrlArgs.builder().expiry(expires!!).bucket(bucketName).`object`(objectName).build()
        return minioClient.getPresignedObjectUrl(args)
    }

    /**
     * 获得文件外链
     *
     * @param bucketName
     * @param objectName
     * @return url
     */
    @Throws(Exception::class)
    fun getPresignedObjectUrl(bucketName: String?, objectName: String?): String {
        val args = GetPresignedObjectUrlArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .method(Method.GET).build()
        return minioClient.getPresignedObjectUrl(args)
    }

    /**
     * 将URLDecoder编码转成UTF8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    @Throws(UnsupportedEncodingException::class)
    fun getUtf8ByURLDecoder(str: String): String {
        val url = str.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
        return URLDecoder.decode(url, "UTF-8")
    }
}