package com.it.onefool.nsfw18.domain.bo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/7/2 23:41
 */
public class CartoonBo {
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
     * 漫画作者
     */
    private List<String> authors;

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

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
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
