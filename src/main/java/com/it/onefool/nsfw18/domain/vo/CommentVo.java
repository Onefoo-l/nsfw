package com.it.onefool.nsfw18.domain.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/7/20 0:56
 */
public class CommentVo {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 评论人的昵称
     */
    private String nickName;

    /**
     * 评论人的头像
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
     * 0为自己的评论，1为别人的评论(别人评论是检举) 无状态说明用户并没有登录，只是查看评论
     */
    private Integer status;
    /**
     * 评论时间
     */
    private LocalDateTime createdTime;

    /**
     * 递归获取子评论
     */
    private List<CommentReplyVo> childrenCommentList;

    public List<CommentReplyVo> getChildrenCommentList() {
        return childrenCommentList;
    }

    public void setChildrenCommentList(List<CommentReplyVo> childrenCommentList) {
        this.childrenCommentList = childrenCommentList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
