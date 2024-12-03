package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 
 * @TableName img_address
 */
@TableName(value ="img_address")
public class ImgAddress implements Serializable {
    /**
     * 图片id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片名字
     */
    @TableField(value = "img_name")
    private String imgName;

    /**
     * 图片地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 图片的排名
     */
    @TableField(value = "img_rank")
    private Integer imgRank;
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

    public Integer getImgRank() {
        return imgRank;
    }

    public void setImgRank(Integer imgRank) {
        this.imgRank = imgRank;
    }

    /**
     * 图片id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 图片id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    /**
     * 图片地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 图片地址
     */
    public void setAddress(String address) {
        this.address = address;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImgAddress that = (ImgAddress) o;
        return Objects.equals(id, that.id) && Objects.equals(imgName, that.imgName) && Objects.equals(address, that.address) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(imgName);
        result = 31 * result + Objects.hashCode(address);
        result = 31 * result + Objects.hashCode(createTime);
        result = 31 * result + Objects.hashCode(updateTime);
        return result;
    }

    @Override
    public String toString() {
        return "ImgAddress{" +
                "id=" + id +
                ", imgName='" + imgName + '\'' +
                ", address='" + address + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}