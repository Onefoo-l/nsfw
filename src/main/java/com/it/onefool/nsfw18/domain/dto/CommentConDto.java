package com.it.onefool.nsfw18.domain.dto;

import java.time.LocalDateTime;

/**
 * @Author linjiawei
 * @Date 2024/7/20 0:59
 */
public class CommentConDto {
    /**
     * 漫画id
     */
    private Integer cartoonId;
    /**
     * 章节id
     */
    private Integer chapterId;
    /**
     * 评论人写的内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
