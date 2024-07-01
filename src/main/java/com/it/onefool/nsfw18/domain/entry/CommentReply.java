package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName comment_reply
 */
@TableName(value ="comment_reply")
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
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CommentReply other = (CommentReply) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getHeadImage() == null ? other.getHeadImage() == null : this.getHeadImage().equals(other.getHeadImage()))
            && (this.getCommentId() == null ? other.getCommentId() == null : this.getCommentId().equals(other.getCommentId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getLikes() == null ? other.getLikes() == null : this.getLikes().equals(other.getLikes()))
            && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()))
            && (this.getUpdatedTime() == null ? other.getUpdatedTime() == null : this.getUpdatedTime().equals(other.getUpdatedTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getHeadImage() == null) ? 0 : getHeadImage().hashCode());
        result = prime * result + ((getCommentId() == null) ? 0 : getCommentId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getLikes() == null) ? 0 : getLikes().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdatedTime() == null) ? 0 : getUpdatedTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", nickName=").append(nickName);
        sb.append(", headImage=").append(headImage);
        sb.append(", commentId=").append(commentId);
        sb.append(", content=").append(content);
        sb.append(", likes=").append(likes);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append("]");
        return sb.toString();
    }
}