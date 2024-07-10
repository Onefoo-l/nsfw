package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.domain.entry.UserCartoon
import com.it.onefool.nsfw18.domain.vo.ChapterImgVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.ChapterMapper
import com.it.onefool.nsfw18.service.ChapterImgAddressService
import com.it.onefool.nsfw18.service.ChapterService
import com.it.onefool.nsfw18.service.UserCartoonService
import com.it.onefool.nsfw18.utils.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import kotlin.Exception


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

    @Autowired
    private lateinit var userCartoonService: UserCartoonService

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    private lateinit var jwtUtil: JwtUtil

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
    override fun findByChapter(
        cartoonId: Int,
        chapterId: Int,
        request: HttpServletRequest
    ): Result<ChapterImgVo> {
        val userId = parseToken(request)
        val qwChapter = QueryWrapper<Chapter>()
        qwChapter.eq("cartoon_id", cartoonId)
        qwChapter.eq("chapter_id", chapterId)
        val chapter = this.getOne(qwChapter)
            ?: throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message())
        // 匿名用户无需记录浏览记录，前端做缓存匿名用户浏览记录，后端只做真实用户浏览记录
        if (userId.toInt() != 0){
            val userChapter = UserCartoon().apply {
                this.cartoonId = cartoonId
                this.chapterId = chapterId
                this.userId = userId.toInt()
            }
            //事务控制
            transactionTemplate.execute {
                try {
                    userCartoonService.save(userChapter)
                } catch (e: Exception) {
                    it.setRollbackOnly()
                    throw CustomizeException(
                        StatusCode.TRANSACTION_INSERTION_FAILED.code(),
                        StatusCode.TRANSACTION_INSERTION_FAILED.message()
                    )
                }
            }
        }
        val result = chapterImgAddressService.findByChapterId(chapter.id)
        result.data.cartoonId = cartoonId
        result.data.chapterName = chapter.chapterName
        return result
    }

    private fun parseToken(request: HttpServletRequest): Long {
        val user = jwtUtil.getToken(request)?: return 0
        return user.user.id
    }
}
