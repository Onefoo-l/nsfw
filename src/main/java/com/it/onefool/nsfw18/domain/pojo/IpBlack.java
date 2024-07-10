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
    private static final List<String> IP_BLACK_CITY = List.of(
            "平壤"
    );
    public static Boolean containsCOUNTRY(String country){
        return IP_BLACK_COUNTRY.contains(country);
    }

    public static Boolean containsCITY(String city){
        return IP_BLACK_CITY.contains(city);
    }
}
