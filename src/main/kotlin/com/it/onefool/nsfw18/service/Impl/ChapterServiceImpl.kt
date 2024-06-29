package com.it.onefool.nsfw18.service.Impl
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.mapper.ChapterMapper
import com.it.onefool.nsfw18.service.ChapterService
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【chapter】的数据库操作Service实现
 * @createDate 2024-06-25 16:51:07
 */
@Service
class ChapterServiceImpl : ServiceImpl<ChapterMapper?, Chapter?>(), ChapterService{
    companion object{
        private val log = LoggerFactory.getLogger(ChapterServiceImpl::class.java)
    }

    /**
     * 根据漫画id查询章节信息
     */
    override fun findByCartoonId(id: Int): Result<List<Chapter?>> {
        val qwChapter = QueryWrapper<Chapter>()
        qwChapter.eq("cartoon_id", id)
        return Result.ok(this.list(qwChapter) as List<Chapter?>)
    }


}
