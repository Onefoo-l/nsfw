package com.it.onefool.nsfw18.strategy.label

/**
 * @Author linjiawei
 * @Date 2024/7/9 0:30
 */
interface ILabelService {

    /**
     * @param id 漫画ID
     * @param str 标签
     */
    fun add(id: Int, str: String)
}