package com.it.onefool.nsfw18.domain.vo;

import java.time.LocalDateTime;

/**
 * @Author linjiawei
 * @Date 2024/7/18 20:52
 */
public class CommentReplyVo {

    /**
     * 回复评论的id
     */
    private Integer id;

    /**
     * 写下回复的 用户的昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String headImage;

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

    /**
     * 回复的内容
     */
    private String content;

    /**
     * 点赞数（回复本身的点赞数量）
     */
    private Integer likes;

    /**
     * 0:为自己的评论可以设置为删除 1:为别人的评论可以设置为检举
     */
    private Integer status;

    /**
     * 0为一级评论，1为二级评论
     */
    private Integer level;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
