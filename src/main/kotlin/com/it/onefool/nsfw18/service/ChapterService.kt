package com.it.onefool.nsfw18.service

import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.domain.vo.ChapterImgVo
import jakarta.servlet.http.HttpServletRequest

/**
 * @author 97436
 * @description 针对表【chapter】的数据库操作Service
 * @createDate 2024-06-25 16:51:07
 */
interface ChapterService : IService<Chapter?> {
    /**
     * 根据漫画id查询章节信息
     */
    fun findByCartoonId(id: Int): Result<List<Chapter?>>

    /**
     * 根据漫画id和章节id查询章节信息
     */
    fun findByChapter(cartoonId: Int, chapterId: Int, request: HttpServletRequest): Result<ChapterImgVo>
}
