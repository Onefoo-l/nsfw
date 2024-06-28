package com.it.onefool.nsfw18.utils

import com.it.onefool.nsfw18.domain.entry.Cartoon
import com.it.onefool.nsfw18.domain.vo.CartoonVo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @Author linjiawei
 * @Date 2024/6/28 17:12
 */
@Component
class BeanUtils {
    companion object{
        private val log = LoggerFactory.getLogger(BeanUtils::class.java)
    }

    fun copyCartoonAndCartoonVo(cartoon: Cartoon,cartoonVo:CartoonVo) : CartoonVo {
        cartoonVo.id = cartoon.id
        cartoonVo.title = cartoon.title
        cartoonVo.avatar = cartoon.avatar
        cartoonVo.describe = cartoon.describe
        cartoonVo.niceCount = cartoon.niceCount
        cartoonVo.readCount = cartoon.readCount
        cartoonVo.collectionCount = cartoon.collectionCount
        cartoonVo.createTime = cartoon.createTime
        cartoonVo.updateTime = cartoon.updateTime
        return cartoonVo
    }
}