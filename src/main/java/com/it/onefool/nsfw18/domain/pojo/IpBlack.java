package com.it.onefool.nsfw18.domain.pojo;

import java.util.Arrays;
import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/7/11 3:18
 */
public class IpBlack {
    private static final List<String> IP_BLACK_COUNTRY = Arrays.asList(
            "朝鲜", "古巴", "印度尼西亚"
    );
    private static final List<String> IP_BLACK_CITY = Arrays.asList(
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
    );

    public static Boolean containsCOUNTRY(String country) {
        return IP_BLACK_COUNTRY.contains(country);
    }

    public static Boolean containsCITY(String city) {
        return IP_BLACK_CITY.contains(city);
    }
}
