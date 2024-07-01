package com.it.onefool.nsfw18.utils

import org.slf4j.LoggerFactory

/**
 * @Author linjiawei
 * @Date 2024/6/30 18:54
 * 禁止某些城市或者国家访问
 */
class IpCityFilterUtil {
    companion object {
        private val log = LoggerFactory.getLogger(IpCityFilterUtil::class.java)
        val listKor = listOf(
            "平壤",
            "咸兴",
            "清津",
            "南浦",
            "元山",
            "新义州",
            "开城",
            "沙里院",
            "海州",
            "金策",
            "惠山",
            "罗先",
            "兩江",
            "江界",
            "三池渊",
            "吉州",
            "安州",
            "金亨稷",
            "龟城",
            "文川",
            "利原",
            "新浦",
            "松林",
            "顺川",
            "端川",
            "德川",
            "惠山",
            "金野",
            "茂山",
            "南山",
            "博川",
            "北仓",
            "平城",
            "新坪",
            "石岩",
            "松林",
            "大同",
            "统信",
            "云川",
            "元山",
            "廉州"
        )
    }
}