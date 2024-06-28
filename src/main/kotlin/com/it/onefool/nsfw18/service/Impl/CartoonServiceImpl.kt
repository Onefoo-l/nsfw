package com.it.onefool.nsfw18.service.Impl
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.domain.entry.Cartoon
import com.it.onefool.nsfw18.domain.vo.CartoonVo
import com.it.onefool.nsfw18.mapper.CartoonMapper
import com.it.onefool.nsfw18.service.CartoonService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author 97436
 * @description 针对表【cartoon】的数据库操作Service实现
 * @createDate 2024-06-25 02:49:19
 */
@Service
class CartoonServiceImpl : ServiceImpl<CartoonMapper?, Cartoon?>(), CartoonService{
    companion object {
        private val log = LoggerFactory.getLogger(CartoonServiceImpl::class.java)
    }

    override fun findId(id: Int?): Result<Any> {
        id?.let {i ->
            val cartoonVo = CartoonVo()
            val lqw = LambdaQueryWrapper<Cartoon>()
            lqw.eq(Cartoon::getId, i)
            val cartoonOne = this.getOne(lqw)


            return Result.ok(cartoonVo)
        }

        return Result.error()
    }
}
