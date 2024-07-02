package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.controller.AbstractCoreController
import com.it.onefool.nsfw18.domain.entry.Cartoon
import com.it.onefool.nsfw18.domain.vo.CartoonVo
import com.it.onefool.nsfw18.service.CartoonService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author linjiawei
 * @Date 2024/6/26 3:52
 * 漫画控制层
 */
@RestController
@RequestMapping("/cartoon")
class CartoonController : AbstractCoreController<Cartoon>() {
    companion object {
        private val log = LoggerFactory.getLogger(CartoonController::class.java)
    }


    private lateinit var cartoonService: CartoonService

    @Autowired
    fun CartoonController(cartoonService: CartoonService) {
        super.setCoreService(cartoonService)
        this.cartoonService = cartoonService
    }

    /**
     * 查看漫画id
     */
    @GetMapping("/findId/{id}")
    fun findId(@PathVariable id: Int?): Result<CartoonVo> {
        return cartoonService.findId(id)
    }

    /**
     * 查询最近更新的漫画
     */
    @PostMapping("/findNew")
    fun findByUpdateTime(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findByUpdateTime(pageCarton)
    }

    /**
     * 查询最多人观看的漫画
     */
    @PostMapping("/findRead")
    fun findManyRead(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findManyRead(pageCarton)
    }

    /**
     * 查询最多人收藏的漫画
     */
    @PostMapping("/findCollection")
    fun findManyCollection(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findManyCollection(pageCarton)
    }

    /**
     * 查询最多人点赞的漫画
     */
    @PostMapping("/findNice")
    fun findManyNice(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findManyNice(pageCarton)
    }

    /**
     * 多条件查询(优先级 1.漫画名称 2.漫画作者 3.标签 4.漫画人物)
     */
    @GetMapping("/findCondition/{str}/{pageSize}/{pageSum}")
    fun findByCondition(
        @PathVariable str: String?,
        @PathVariable pageSize: Long?,
        @PathVariable pageSum: Long?
    ): Result<PageInfo<CartoonVo>> {
        val size = pageSize ?: 10
        val sum = pageSum ?: 1
        str?.let {
            return cartoonService.findByCondition(str,size,sum)
        }
        return Result.error(StatusCode.PARAM_ERROR)
    }
}