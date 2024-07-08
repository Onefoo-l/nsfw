package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.domain.vo.ChapterImgVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.ChapterMapper
import com.it.onefool.nsfw18.service.ChapterImgAddressService
import com.it.onefool.nsfw18.service.ChapterService
import com.it.onefool.nsfw18.service.ImgAddressService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【chapter】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:07
 */
@Service
class ChapterServiceImpl : ServiceImpl<ChapterMapper?, Chapter?>(), ChapterService {
    companion object {
        private val logger = LoggerFactory.getLogger(ChapterServiceImpl::class.java)
    }

    @Autowired
    private lateinit var chapterImgAddressService: ChapterImgAddressService

    /**
     * 根据漫画id查询章节信息
     */
    override fun findByCartoonId(id: Int): Result<List<Chapter?>> {
        val qwChapter = QueryWrapper<Chapter>()
        qwChapter.eq("cartoon_id", id)
        return Result.ok(this.list(qwChapter) as List<Chapter?>)
    }

    /**
     * 根据漫画id和章节id查询章节信息
     */
    override fun findByChapter(cartoonId: Int, chapterId: Int): Result<ChapterImgVo> {
        val qwChapter = QueryWrapper<Chapter>()
        qwChapter.eq("cartoon_id", cartoonId)
        qwChapter.eq("chapter_id", chapterId)
        val chapter = this.getOne(qwChapter)
            ?: throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
        val result = chapterImgAddressService.findByChapterId(chapter.id)
        result.data.cartoonId = cartoonId
        result.data.chapterName = chapter.chapterName
        return result
    }


}
