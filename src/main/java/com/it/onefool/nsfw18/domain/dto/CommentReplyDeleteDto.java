package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/7/18 20:50
 */
public class CommentReplyDeleteDto extends CommentReplyDto{

    /**
     * 回复id
     */
    private Integer replyId;
    /**
     * 用户id
     */
    private Integer userId;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
