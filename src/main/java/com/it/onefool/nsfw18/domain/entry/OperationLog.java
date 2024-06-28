package com.it.onefool.nsfw18.domain.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName operation_log
 */
@TableName(value ="operation_log")
public class OperationLog implements Serializable {
    /**
     * 日志id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id，为0则为匿名用户，其他则为实际用户
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 操作说明
     */
    @TableField(value = "operation_description")
    private String operationDescription;

    /**
     * 用户ip,如果查询不到ip默认0.0.0.0
     */
    @TableField(value = "user_ip")
    private String userIp;

    /**
     * 用户ip城市
     */
    @TableField(value = "user_city")
    private String userCity;

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
     * 日志id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 日志id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户id，为0则为匿名用户，其他则为实际用户
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户id，为0则为匿名用户，其他则为实际用户
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 操作说明
     */
    public String getOperationDescription() {
        return operationDescription;
    }

    /**
     * 操作说明
     */
    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    /**
     * 用户ip,如果查询不到ip默认0.0.0.0
     */
    public String getUserIp() {
        return userIp;
    }

    /**
     * 用户ip,如果查询不到ip默认0.0.0.0
     */
    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    /**
     * 用户ip城市
     */
    public String getUserCity() {
        return userCity;
    }

    /**
     * 用户ip城市
     */
    public void setUserCity(String userCity) {
        this.userCity = userCity;
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
        OperationLog other = (OperationLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOperationDescription() == null ? other.getOperationDescription() == null : this.getOperationDescription().equals(other.getOperationDescription()))
            && (this.getUserIp() == null ? other.getUserIp() == null : this.getUserIp().equals(other.getUserIp()))
            && (this.getUserCity() == null ? other.getUserCity() == null : this.getUserCity().equals(other.getUserCity()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOperationDescription() == null) ? 0 : getOperationDescription().hashCode());
        result = prime * result + ((getUserIp() == null) ? 0 : getUserIp().hashCode());
        result = prime * result + ((getUserCity() == null) ? 0 : getUserCity().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", operationDescription=").append(operationDescription);
        sb.append(", userIp=").append(userIp);
        sb.append(", userCity=").append(userCity);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}