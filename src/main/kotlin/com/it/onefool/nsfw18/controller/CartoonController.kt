package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.common.Result
import com.it.onefool.nsfw18.service.CartoonService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author linjiawei
 * @Date 2024/6/26 3:52
 * 漫画控制层
 */
@RestController
@RequestMapping("/cartoon")
class CartoonController {
    companion object {
        private val log = LoggerFactory.getLogger(CartoonController::class.java)
    }

    @Autowired
    private lateinit var cartoonService: CartoonService

    /**
     * 查看漫画id
     */
    @GetMapping("/findId/{id}")
    fun findId(@PathVariable id: Int?): Result<Any> {
        return cartoonService.findId(id)
    }
}