package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @TableName comment
 */
@TableName(value = "comment")
public class Comment implements Serializable {
    /**
     * 评论id
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
     * 章节id
     */
    @TableField(value = "chapter_id")
    private Integer chapterId;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(userId, comment.userId) && Objects.equals(nickName, comment.nickName) && Objects.equals(headImage, comment.headImage) && Objects.equals(cartoonId, comment.cartoonId) && Objects.equals(chapterId, comment.chapterId) && Objects.equals(content, comment.content) && Objects.equals(likes, comment.likes) && Objects.equals(replys, comment.replys) && Objects.equals(createdTime, comment.createdTime) && Objects.equals(updatedTime, comment.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, nickName, headImage, cartoonId, chapterId, content, likes, replys, createdTime, updatedTime);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", cartoonId=" + cartoonId +
                ", chapterId=" + chapterId +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", replys=" + replys +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}