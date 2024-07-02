package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName cartoon
 */
@TableName(value = "cartoon")
public class Cartoon implements Serializable {
    /**
     * 漫画id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 漫画标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 漫画头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 漫画描述
     */
    @TableField(value = "`describe`")
    private String describe;

    /**
     * 点赞数
     */
    @TableField(value = "nice_count")
    private Integer niceCount;

    /**
     * 阅读数
     */
    @TableField(value = "read_count")
    private Integer readCount;

    /**
     * 收藏数
     */
    @TableField(value = "collection_count")
    private Integer collectionCount;

    /**
     * 上架时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 漫画id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 漫画id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 漫画标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 漫画标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 漫画头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 漫画头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 漫画描述
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * 漫画描述
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * 点赞数
     */
    public Integer getNiceCount() {
        return niceCount;
    }

    /**
     * 点赞数
     */
    public void setNiceCount(Integer niceCount) {
        this.niceCount = niceCount;
    }

    /**
     * 阅读数
     */
    public Integer getReadCount() {
        return readCount;
    }

    /**
     * 阅读数
     */
    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    /**
     * 收藏数
     */
    public Integer getCollectionCount() {
        return collectionCount;
    }

    /**
     * 收藏数
     */
    public void setCollectionCount(Integer collectionCount) {
        this.collectionCount = collectionCount;
    }

    /**
     * 上架时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 上架时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
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
        Cartoon other = (Cartoon) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
                && (this.getDescribe() == null ? other.getDescribe() == null : this.getDescribe().equals(other.getDescribe()))
                && (this.getNiceCount() == null ? other.getNiceCount() == null : this.getNiceCount().equals(other.getNiceCount()))
                && (this.getReadCount() == null ? other.getReadCount() == null : this.getReadCount().equals(other.getReadCount()))
                && (this.getCollectionCount() == null
                ? other.getCollectionCount() == null
                : this.getCollectionCount().equals(other.getCollectionCount()))
            &&
        (this.getCreateTime() == null
                ? other.getCreateTime() == null
                : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null
                ? other.getUpdateTime() == null
                : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getDescribe() == null) ? 0 : getDescribe().hashCode());
        result = prime * result + ((getNiceCount() == null) ? 0 : getNiceCount().hashCode());
        result = prime * result + ((getReadCount() == null) ? 0 : getReadCount().hashCode());
        result = prime * result + ((getCollectionCount() == null)
                ? 0 : getCollectionCount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", avatar=").append(avatar);
        sb.append(", describe=").append(describe);
        sb.append(", niceCount=").append(niceCount);
        sb.append(", readCount=").append(readCount);
        sb.append(", collectionCount = ").append(collectionCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}