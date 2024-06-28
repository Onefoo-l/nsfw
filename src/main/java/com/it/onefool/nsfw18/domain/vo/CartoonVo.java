package com.it.onefool.nsfw18.domain.vo;

import java.time.LocalDateTime;

/**
 * @Author linjiawei
 * @Date 2024/6/26 4:25
 */
public class CartoonVo {

    /**
     * 漫画id
     */
    private Integer id;

    /**
     * 漫画标题
     */
    private String title;

    /**
     * 漫画头像
     */
    private String avatar;

    /**
     * 漫画描述
     */
    private String describe;

    /**
     * 漫画章节id
     */
    private Integer chapterId;

    /**
     * 章节标题
     */
    private String chapterName;

    /**
     * 作品标签
     */
    private String workDescription;

    /**
     * 标签
     */
    private String tag;

    /**
     * 漫画作者
     */
    private String author;
    /**
     * 点赞数
     */
    private Integer niceCount;

    /**
     * 阅读数
     */
    private Integer readCount;

    /**
     * 收藏数
     */
    private Integer collectionCount;

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

    /**
     * 评论时间
     */
    private LocalDateTime userTime;


    /**
     * 上架时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getNiceCount() {
        return niceCount;
    }

    public void setNiceCount(Integer niceCount) {
        this.niceCount = niceCount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(Integer collectionCount) {
        this.collectionCount = collectionCount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
