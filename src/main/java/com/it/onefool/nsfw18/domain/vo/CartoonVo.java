package com.it.onefool.nsfw18.domain.vo;

import com.it.onefool.nsfw18.domain.dto.ChapterDto;
import com.it.onefool.nsfw18.domain.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

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
     * 漫画章节
     */
    private List<ChapterDto> chapterDto;

    /**
     * 作品标签
     */
    private List<String> workDescription;

    /**
     * 标签
     */
    private List<String> tag;

    /**
     * 漫画作者
     */
    private List<String> author;

    /**
     * 评论列表
     */
    private List<CommentDto> commentDtoList;

    /**
     * 总图片数量
     */
    private Integer imgCount;

    /**
     * 上架时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Integer getImgCount() {
        return imgCount;
    }

    public void setImgCount(Integer imgCount) {
        this.imgCount = imgCount;
    }

    public List<CommentDto> getCommentDtoList() {
        return commentDtoList;
    }

    public void setCommentDtoList(List<CommentDto> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }

    public List<ChapterDto> getChapterDto() {
        return chapterDto;
    }

    public void setChapterDto(List<ChapterDto> chapterDto) {
        this.chapterDto = chapterDto;
    }

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

    public List<String> getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(List<String> workDescription) {
        this.workDescription = workDescription;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
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

    @Override
    public String toString() {
        return "CartoonVo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", avatar='" + avatar + '\'' +
                ", describe='" + describe + '\'' +
                ", niceCount=" + niceCount +
                ", readCount=" + readCount +
                ", collectionCount=" + collectionCount +
                ", chapterDto=" + chapterDto +
                ", workDescription=" + workDescription +
                ", tag=" + tag +
                ", author=" + author +
                ", commentDtoList=" + commentDtoList +
                ", totalPage=" + totalPage +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
