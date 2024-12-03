package com.it.onefool.nsfw18.domain.dto;

import com.it.onefool.nsfw18.domain.entry.Cartoon;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/12/1 16:02
 */
public class CartoonDto extends Cartoon {

    /**
     * 漫画章节
     */
    private Integer chapterId;

    /**
     * 漫画章节名字
     */
    private String chapterName;

    /**
     * 漫画图片
     */
    private List<MultipartFile> file;

    /**
     * 登场人物
     */
    private List<String> comePeople;

    /**
     * 作者
     */
    private List<String> author;

    /**
     * 作品标签
     */
    private List<String> workDescription;


    @Override
    public String toString() {
        return "CartoonDto{" +
                "chapterId=" + chapterId +
                ", file=" + file +
                ", comePeople=" + comePeople +
                ", author=" + author +
                ", workDescription=" + workDescription +
                '}';
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public List<String> getComePeople() {
        return comePeople;
    }

    public void setComePeople(List<String> comePeople) {
        this.comePeople = comePeople;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public List<String> getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(List<String> workDescription) {
        this.workDescription = workDescription;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public List<MultipartFile> getFile() {
        return file;
    }

    public void setFile(List<MultipartFile> file) {
        this.file = file;
    }

}
