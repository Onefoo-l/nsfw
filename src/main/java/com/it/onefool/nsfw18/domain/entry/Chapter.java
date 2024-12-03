package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName chapter
 */
@TableName(value = "chapter")
public class Chapter implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 漫画章节id
     */
    @TableField(value = "chapter_id")
    private Integer chapterId;

    /**
     * 章节标题
     */
    @TableField(value = "chapter_name")
    private String chapterName;

    /**
     * 对应的漫画id
     */
    @TableField(value = "cartoon_id")
    private Integer cartoonId;

    /**
     * 漫画图片id
     */
    @TableField(value = "img_address_id")
    private Integer imgAddressId;
    /**
     *
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;


    /**
     * id
     */
    public Integer getId() {
        return id;
    }

    /**
     * id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 漫画章节id
     */
    public Integer getChapterId() {
        return chapterId;
    }

    /**
     * 漫画章节id
     */
    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getImgAddressId() {
        return imgAddressId;
    }

    public void setImgAddressId(Integer imgAddressId) {
        this.imgAddressId = imgAddressId;
    }

    /**
     * 章节标题
     */
    public String getChapterName() {
        return chapterName;
    }

    /**
     * 章节标题
     */
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    /**
     * 对应的漫画id
     */
    public Integer getCartoonId() {
        return cartoonId;
    }

    /**
     * 对应的漫画id
     */
    public void setCartoonId(Integer cartoonId) {
        this.cartoonId = cartoonId;
    }

    /**
     *
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     *
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     *
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     *
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
        Chapter other = (Chapter) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getChapterId() == null ? other.getChapterId() == null : this.getChapterId().equals(other.getChapterId()))
                && (this.getChapterName() == null ? other.getChapterName() == null : this.getChapterName().equals(other.getChapterName()))
                && (this.getCartoonId() == null ? other.getCartoonId() == null : this.getCartoonId().equals(other.getCartoonId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getChapterId() == null) ? 0 : getChapterId().hashCode());
        result = prime * result + ((getChapterName() == null) ? 0 : getChapterName().hashCode());
        result = prime * result + ((getCartoonId() == null) ? 0 : getCartoonId().hashCode());
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
        sb.append(", chapterId=").append(chapterId);
        sb.append(", chapterName=").append(chapterName);
        sb.append(", cartoonId=").append(cartoonId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}