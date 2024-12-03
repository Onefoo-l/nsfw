package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.aop.log.Log
import com.it.onefool.nsfw18.common.PageInfo
import com.it.onefool.nsfw18.common.PageRequestDto
import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.common.controller.AbstractCoreController
import com.it.onefool.nsfw18.domain.dto.CartoonDto
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
    @Log("查看漫画id")
    fun findId(@PathVariable id: Int?): Result<CartoonVo> {
        return cartoonService.findId(id)
    }

    /**
     * 查询最近更新的漫画
     */
    @PostMapping("/findNew")
    @Log("查询最近更新的漫画")
    fun findByUpdateTime(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findByUpdateTime(pageCarton)
    }

    /**
     * 查询最多人观看的漫画
     */
    @PostMapping("/findRead")
    @Log("查询最多人观看的漫画")
    fun findManyRead(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findManyRead(pageCarton)
    }

    /**
     * 查询最多人收藏的漫画
     */
    @PostMapping("/findCollection")
    @Log("查询最多人收藏的漫画")
    fun findManyCollection(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findManyCollection(pageCarton)
    }

    /**
     * 查询最多人点赞的漫画
     */
    @PostMapping("/findNice")
    @Log("查询最多人点赞的漫画")
    fun findManyNice(
        @RequestBody pageCarton: PageRequestDto<Cartoon>
    ): Result<PageInfo<Cartoon>> {
        return cartoonService.findManyNice(pageCarton)
    }

    /**
     * 多条件查询(优先级 1.漫画名称 2.漫画作者 3.标签 4.漫画人物)
     * type 0:全站查询  1:根据漫画名称查询  2:根据漫画作者查询  3:根据标签查询  4:根据漫画人物查询
     */
    @GetMapping("/findCondition/{str}/{pages}/{size}/{type}")
    @Log("多条件查询")
    fun findByCondition(
        @PathVariable str: String?,
        @PathVariable pages: Long?, // 页码
        @PathVariable size: Long?, // 每页条数
        @PathVariable type: Int?
    ): Result<PageInfo<CartoonVo>> {
        val sum = if (pages == null || pages.toInt() == 0) 1 else pages
        val sizes = if (size == null || size.toInt() == 0) 1 else size
        str?.let {
            type?.let {
                return cartoonService.findByCondition(str,sum,sizes,type)
            }
        }
        return Result.error(StatusCode.PARAM_ERROR)
    }

    /**
     * 添加漫画信息
     */
    @PostMapping("/add")
    @Log("添加漫画信息")
    fun addCartoon(cartoonDto: CartoonDto?): Result<String>{
        return cartoonService.addCartoon(cartoonDto)
    }

    /**
     * 修改漫画信息
     */
    @PostMapping("/update")
    @Log("/修改漫画信息")
    fun updateCartoon(cartoonDto: CartoonDto?): Result<String>{
        return cartoonService.updateCartoon(cartoonDto)
    }
}