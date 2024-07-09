package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @TableName comment_reply
 */
@TableName(value = "comment_reply")
public class CommentReply implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 写下回复的 用户的ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 写下回复的 用户的昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 头像
     */
    @TableField(value = "head_image")
    private String headImage;

    /**
     * 漫画id
     */
    @TableField(value = "cartoon_id")
    private Integer cartoonId;

    /**
     * 章节id
     */
    @TableField(value = "chapter_id")
    private Integer chapterId;

    /**
     * 针对的是哪条 评论id 进行回复
     */
    @TableField(value = "comment_id")
    private Integer commentId;

    /**
     * 回复的内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 点赞数（回复本身的点赞数量）
     */
    @TableField(value = "likes")
    private Integer likes;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private LocalDateTime updatedTime;

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

    /**
     *
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 写下回复的 用户的ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 写下回复的 用户的ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 写下回复的 用户的昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 写下回复的 用户的昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 头像
     */
    public String getHeadImage() {
        return headImage;
    }

    /**
     * 头像
     */
    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    /**
     * 针对的是哪条 评论id 进行回复
     */
    public Integer getCommentId() {
        return commentId;
    }

    /**
     * 针对的是哪条 评论id 进行回复
     */
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    /**
     * 回复的内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 回复的内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 点赞数（回复本身的点赞数量）
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * 点赞数（回复本身的点赞数量）
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * 创建时间
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * 创建时间
     */
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 更新时间
     */
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 更新时间
     */
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentReply that = (CommentReply) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(nickName, that.nickName)) return false;
        if (!Objects.equals(headImage, that.headImage)) return false;
        if (!Objects.equals(cartoonId, that.cartoonId)) return false;
        if (!Objects.equals(chapterId, that.chapterId)) return false;
        if (!Objects.equals(commentId, that.commentId)) return false;
        if (!Objects.equals(content, that.content)) return false;
        if (!Objects.equals(likes, that.likes)) return false;
        if (!Objects.equals(createdTime, that.createdTime)) return false;
        return Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + (headImage != null ? headImage.hashCode() : 0);
        result = 31 * result + (cartoonId != null ? cartoonId.hashCode() : 0);
        result = 31 * result + (chapterId != null ? chapterId.hashCode() : 0);
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (updatedTime != null ? updatedTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CommentReply{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", cartoonId=" + cartoonId +
                ", chapterId=" + chapterId +
                ", commentId=" + commentId +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}