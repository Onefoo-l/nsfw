package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/7/13 2:03
 */
public class CommentReplyDto {

    /**
     * 漫画id
     */
    private Integer cartoonId;

    /**
     * 章节id
     */
    private Integer chapterId;

    /**
     * 针对的是哪条 评论id 进行回复
     */
    private Integer commentId;

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

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
}
