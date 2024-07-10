package com.it.onefool.nsfw18.controller

import com.it.onefool.nsfw18.aop.log.Log
import com.it.onefool.nsfw18.common.StatusCode
import com.it.onefool.nsfw18.domain.dto.LabelDto
import com.it.onefool.nsfw18.exception.CustomizeException
import com.it.onefool.nsfw18.service.LabelService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author linjiawei
 * @Date 2024/7/9 0:09
 * 标签控制层
 */
@RestController
@RequestMapping("/label")
class LabelController {
    companion object {
        private val log = LoggerFactory.getLogger(LabelController::class.java)
    }

    @Autowired
    private lateinit var labelService: LabelService

    /**
     * 添加标签
     */
    @Log("添加标签")
    @PostMapping("/add")
    fun addLabel(@RequestBody labelDto: LabelDto) {
        if (labelDto.name.isNullOrEmpty()
            || labelDto.type == null
            || labelDto.cartoonId == null
        ) throw CustomizeException(
            StatusCode.LABEL_PARAM_ERROR.code(),
            StatusCode.LABEL_PARAM_ERROR.message()
        )
        return labelService.addLabel(labelDto)
    }
}