package com.it.onefool.nsfw18.domain.vo;

import com.it.onefool.nsfw18.domain.dto.ImgDto;

import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/7/9 1:28
 * 返回给前端的章节图片信息
 */
public class ChapterImgVo {

    private Integer cartoonId;
    private Integer chapterId;
    private String chapterName;
    private List<ImgDto> imgDtoS;


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

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public List<ImgDto> getImgDtoS() {
        return imgDtoS;
    }

    public void setImgDtoS(List<ImgDto> imgDtoS) {
        this.imgDtoS = imgDtoS;
    }

    @Override
    public String toString() {
        return "ChapterImgVo{" +
                "cartoonId=" + cartoonId +
                ", chapterId=" + chapterId +
                ", chapterName='" + chapterName + '\'' +
                ", imgDtoS=" + imgDtoS +
                '}';
    }
}
