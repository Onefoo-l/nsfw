package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/7/9 0:12
 * 添加标签
 */
public class LabelDto {

    /**
     * 漫画id
     */
    private Integer cartoonId;
    /**
     * 添加的标签类型
     * 0:作品标签 1:标签 2:作者 3:漫画作者
     */
    private Integer type;
    /**
     * 标签名称
     */
    private String name;

    public LabelDto(Integer cartoonId, Integer type, String name) {
        this.cartoonId = cartoonId;
        this.type = type;
        this.name = name;
    }

    public Integer getCartoonId() {
        return cartoonId;
    }

    public void setCartoonId(Integer cartoonId) {
        this.cartoonId = cartoonId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LabelDto{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
