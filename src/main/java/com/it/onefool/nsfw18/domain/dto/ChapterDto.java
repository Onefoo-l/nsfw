package com.it.onefool.nsfw18.domain.dto;

/**
 * @Author linjiawei
 * @Date 2024/6/28 18:54
 */
public class ChapterDto {
    /**
     * 漫画章节id
     */
    private Integer chapterId;

    /**
     * 章节标题
     */
    private String chapterName;

    /**
     * 是否曾经浏览过
     */
    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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
}
