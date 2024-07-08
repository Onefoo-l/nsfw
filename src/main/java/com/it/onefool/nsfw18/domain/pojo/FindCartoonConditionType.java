package com.it.onefool.nsfw18.domain.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author linjiawei
 * @Date 2024/7/8 20:56
 * 0:全站查询  1:根据漫画名称查询  2:根据漫画作者查询  3:根据标签查询  4:根据漫画人物查询
 */
public enum FindCartoonConditionType {
    ALL(0),
    CARTOON_NAME(1),
    CARTOON_AUTHOR(2),
    TAG(3),
    CARTOON_PEOPLE(4);

    private final int type;
    private static final Map<Integer, FindCartoonConditionType> map = new HashMap<>();

    static {
        for (FindCartoonConditionType ct : FindCartoonConditionType.values()) {
            map.put(ct.getType(), ct);
        }
    }

    private FindCartoonConditionType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * 根据整型值获取枚举名称
     *
     * @param type 整型值
     * @return 枚举名称或 null 如果找不到对应的枚举
     */
    public static FindCartoonConditionType fromInt(int type) {
        return map.get(type);
    }
}
