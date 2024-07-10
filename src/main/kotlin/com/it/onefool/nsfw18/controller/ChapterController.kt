package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.aop.log.Log
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.vo.ChapterImgVo
import com.it.onefool.nsfw18.service.ChapterService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @Author linjiawei
 * @Date 2024/7/9 1:25
 * 漫画章节控制层
 */
@RestController
@RequestMapping("/chapter")
class ChapterController {
    companion object {
        private val log = LoggerFactory.getLogger(ChapterController::class.java)
    }

    @Autowired
    private lateinit var chapterService: ChapterService

    /**
     * 根据漫画id和章节id查询章节信息
     */
    @PostMapping("/findChapter")
    @Log("查询章节信息")
    fun findById(
        @RequestParam(name = "cartoonId", required = true) cartoonId: Int,
        @RequestParam(name = "chapterId", required = true) chapterId: Int,
        request: HttpServletRequest
    ): Result<ChapterImgVo> {
        return chapterService.findByChapter(cartoonId, chapterId, request)
    }
}