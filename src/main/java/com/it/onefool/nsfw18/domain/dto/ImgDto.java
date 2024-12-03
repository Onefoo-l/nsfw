package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/7/9 2:22
 */
public class ImgDto {
    private Integer cartoonId;

    private Integer chapterId;



    public Integer getCartoonId() {
        return cartoonId;
    }

    public void setCartoonId(Integer cartoonId) {
        this.cartoonId = cartoonId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public String toString() {
        return "ImgDto{" +
                "cartoonId=" + cartoonId +
                ", chapterId=" + chapterId +
                '}';
    }
}
