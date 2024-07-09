package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.ImgDto
import com.it.onefool.nsfw18.domain.entry.ChapterImgAddress
import com.it.onefool.nsfw18.domain.vo.ChapterImgVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.ChapterImgAddressMapper
import com.it.onefool.nsfw18.service.ChapterImgAddressService
import com.it.onefool.nsfw18.service.ImgAddressService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【chapter_img_address】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:13
 */
@Service
class ChapterImgAddressServiceImpl : ServiceImpl<ChapterImgAddressMapper?, ChapterImgAddress?>(),
    ChapterImgAddressService {
    companion object {
        private val logger = LoggerFactory.getLogger(ChapterImgAddressServiceImpl::class.java)
    }

    @Autowired
    private lateinit var imgAddressService: ImgAddressService

    /**
     * 根据章节id查询图片id
     */
    override fun findByChapterId(id: Int): Result<ChapterImgVo> {
        val chapterImgVo = ChapterImgVo()
        val qwChapterImg = QueryWrapper<ChapterImgAddress>()
        qwChapterImg.eq("chapter_id", id)
        val listImg = this.list(qwChapterImg)
        if (listImg.isNullOrEmpty()) throw CustomizeException(
            StatusCode.NOT_FOUND.code(),
            StatusCode.NOT_FOUND.message()
        )
        val list = listImg.map { it!!.imgAddressId }
        val imgAddressList = imgAddressService.findByImgId(list)
        imgAddressList.forEach {
            val imgDto = ImgDto()
            imgDto.imgId = it.id
            imgDto.imgUrl = it.address
            chapterImgVo.imgDtoS.add(imgDto)
        }
        return Result.ok(chapterImgVo)
    }

    /**
     * 查询出当前漫画的所有图片数量
     */
    override fun findImgCount(chapterIds: List<Int?>): Int {
        if (chapterIds.isEmpty()) throw CustomizeException(
            StatusCode.NOT_FOUND.code(),
            StatusCode.NOT_FOUND.message()
        )
        val qwChapterIds = QueryWrapper<ChapterImgAddress>()
        qwChapterIds.`in`("chapter_id", chapterIds)
        return this.count(qwChapterIds).toInt()
    }
}
