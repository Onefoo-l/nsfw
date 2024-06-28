package com.it.onefool.nsfw18.service.Impl

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.entry.Cartoon
import com.it.onefool.nsfw18.domain.entry.Chapter
import com.it.onefool.nsfw18.domain.vo.CartoonVo
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.mapper.CartoonMapper
import com.it.onefool.nsfw18.service.CartoonService
import com.it.onefool.nsfw18.utils.BeanUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Service实现
 * @createDate 2024-06-25 02:49:19
 */
@Service
class CartoonServiceImpl : ServiceImpl<CartoonMapper?, Cartoon?>(), CartoonService {

    @Autowired
    private lateinit var beanUtils: BeanUtils

    companion object {
        private val log = LoggerFactory.getLogger(CartoonServiceImpl::class.java)
    }

    /**
     * 查看漫画id
     */
    override fun findId(id: Int?): Result<Any> {
        id?.let { i ->
            val cartoonVo = CartoonVo()
            val qwCartoon = QueryWrapper<Cartoon>()
            qwCartoon.eq("id", i)
            val cartoonOne = this.getOne(qwCartoon)?.let {
                // 尽量不用反射，线上影响性能
                beanUtils.copyCartoonAndCartoonVo(it, cartoonVo)
            } ?: run { throw CustomizeException(StatusCode.NOT_FOUND.code(), StatusCode.NOT_FOUND.message()) }
            val qwChapter = QueryWrapper<Chapter>()
            qwCartoon.eq("cartoon_id",i)
            return Result.ok(cartoonVo)
        }

        return Result.error()
    }


}
