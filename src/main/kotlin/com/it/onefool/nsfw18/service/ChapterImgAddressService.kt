package com.it.onefool.nsfw18.service
import com.baomidou.mybatisplus.extension.service.IService
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.ChapterImgAddress
import com.it.onefool.nsfw18.domain.vo.ChapterImgVo

/**
 * @author 97436
 * @description 针对表【chapter_img_address】的数据库操作Service
 * @createDate 2024-06-25 16:51:13
 */
interface ChapterImgAddressService : IService<ChapterImgAddress?> {
    /**
     * 根据章节id查询图片id
     */
    fun findByChapterId(id: Int) : Result<ChapterImgVo>

    /**
     * 查询出当前漫画的所有图片数量
     */
    fun findImgCount(chapterIds: List<Int?>): Int
}
