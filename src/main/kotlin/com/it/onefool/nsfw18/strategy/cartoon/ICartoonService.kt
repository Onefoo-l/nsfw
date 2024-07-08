package com.it.onefool.nsfw18.strategy.cartoon

/**
 * @Author linjiawei
 * @Date 2024/7/8 21:02
 * 根据条件进行通用查询
 */
interface ICartoonService {
    /**
     * 根据条件进行通用查询
     * 0:全站查询  1:根据漫画名称查询  2:根据漫画作者查询  3:根据标签查询  4:根据漫画人物查询
     */
    fun findByConditionType(str: String, start: Long, size: Long) : List<Int?>
}