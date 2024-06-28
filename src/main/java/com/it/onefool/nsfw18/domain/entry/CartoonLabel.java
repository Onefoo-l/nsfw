package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName cartoon_label
 */
@TableName(value ="cartoon_label")
public class CartoonLabel implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 漫画id
     */
    @TableField(value = "cartoon_id")
    private Integer cartoonId;

    /**
     * 标签id
     */
    @TableField(value = "label_id")
    private Integer labelId;

    /**
     * 
     */
    @TableField(value = "createTime")
    private LocalDateTime createTime;

    /**
     * 
     */
    @TableField(value = "updateTime")
    private LocalDateTime updateTime;


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
     * 标签id
     */
    public Integer getLabelId() {
        return labelId;
    }

    /**
     * 标签id
     */
    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
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
        CartoonLabel other = (CartoonLabel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCartoonId() == null ? other.getCartoonId() == null : this.getCartoonId().equals(other.getCartoonId()))
            && (this.getLabelId() == null ? other.getLabelId() == null : this.getLabelId().equals(other.getLabelId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCartoonId() == null) ? 0 : getCartoonId().hashCode());
        result = prime * result + ((getLabelId() == null) ? 0 : getLabelId().hashCode());
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
        sb.append(", cartoonId=").append(cartoonId);
        sb.append(", labelId=").append(labelId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}