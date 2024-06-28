package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
public class Comment implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论人的ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 评论人的昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 评论人的头像
     */
    @TableField(value = "head_image")
    private String headImage;

    /**
     * 漫画id
     */
    @TableField(value = "cartoon_id")
    private Integer cartoonId;

    /**
     * 评论人写的内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 总的点赞数
     */
    @TableField(value = "likes")
    private Integer likes;

    /**
     * 总的回复数
     */
    @TableField(value = "replys")
    private Integer replys;

    /**
     * 评论时间
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
     * 评论人的ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 评论人的ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 评论人的昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 评论人的昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 评论人的头像
     */
    public String getHeadImage() {
        return headImage;
    }

    /**
     * 评论人的头像
     */
    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    /**
     * 漫画id
     */
    public Integer getCartoonId() {
        return cartoonId;
    }

    /**
     * 漫画id
     */
    public void setCartoonId(Integer cartoonId) {
        this.cartoonId = cartoonId;
    }

    /**
     * 评论人写的内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 评论人写的内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 总的点赞数
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * 总的点赞数
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * 总的回复数
     */
    public Integer getReplys() {
        return replys;
    }

    /**
     * 总的回复数
     */
    public void setReplys(Integer replys) {
        this.replys = replys;
    }

    /**
     * 评论时间
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * 评论时间
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
        Comment other = (Comment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getHeadImage() == null ? other.getHeadImage() == null : this.getHeadImage().equals(other.getHeadImage()))
            && (this.getCartoonId() == null ? other.getCartoonId() == null : this.getCartoonId().equals(other.getCartoonId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getLikes() == null ? other.getLikes() == null : this.getLikes().equals(other.getLikes()))
            && (this.getReplys() == null ? other.getReplys() == null : this.getReplys().equals(other.getReplys()))
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
        result = prime * result + ((getCartoonId() == null) ? 0 : getCartoonId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getLikes() == null) ? 0 : getLikes().hashCode());
        result = prime * result + ((getReplys() == null) ? 0 : getReplys().hashCode());
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
        sb.append(", cartoonId=").append(cartoonId);
        sb.append(", content=").append(content);
        sb.append(", likes=").append(likes);
        sb.append(", replys=").append(replys);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append("]");
        return sb.toString();
    }
}