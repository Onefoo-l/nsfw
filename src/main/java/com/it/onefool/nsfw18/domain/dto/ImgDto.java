package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/7/9 2:22
 */
public class ImgDto {
    private Integer imgId;
    private String imgUrl;

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "ImgDto{" +
                "imgId=" + imgId +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
