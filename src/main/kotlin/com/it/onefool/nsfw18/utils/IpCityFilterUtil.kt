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
            "Pyongyang",
            "Hamhung",
            "Chongjin",
            "Nampo",
            "Wonsan",
            "Sinuiju",
            "Kaesong",
            "Sariwon",
            "Haeju",
            "Kimchaek",
            "Hyesan",
            "Rason",
            "Ryanggang",
            "Kanggye",
            "Samjiyon",
            "Kilju",
            "Anju",
            "Kimhyongjik",
            "Kusong",
            "Munchon",
            "Riwon",
            "Sinpo",
            "Songrim",
            "Sunchon",
            "Tanchon",
            "Tokchon",
            "Hyesan",
            "Kumya",
            "Musan",
            "Namsan",
            "Pakchon",
            "Pukchang",
            "Pyongsong",
            "Sinpyong",
            "Sokam",
            "Songnim",
            "Taedong",
            "Tongsin",
            "Unchon",
            "Wonsan",
            "Yomju"
        )
    }
}