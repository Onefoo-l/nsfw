package com.it.onefool.nsfw18.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/6/28 20:01
 */
public class CommentDto {
    /**
     * 评论ID
     */
    private Integer id;

    /**
     * 评论人的ID
     */
    private Integer userId;

    /**
     * 评论人的昵称
     */
    private String nickName;

    /**
     * 评论人的头像
     */
    private String headImage;

    /**
     * 评论人写的内容
     */
    private String content;

    /**
     * 总的点赞数
     */
    private Integer likes;

    /**
     * 总的回复数
     */
    private Integer replys;

    /**
     * 评论时间
     */
    private LocalDateTime userTime;

    private List<CommentDto> childrenCommentList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getReplys() {
        return replys;
    }

    public void setReplys(Integer replys) {
        this.replys = replys;
    }

    public LocalDateTime getUserTime() {
        return userTime;
    }

    public void setUserTime(LocalDateTime userTime) {
        this.userTime = userTime;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", replys=" + replys +
                ", userTime=" + userTime +
                '}';
    }
}
