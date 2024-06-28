package com.it.onefool.nsfw18.common;

import java.io.Serializable;
import java.util.List;

/**
 * 分页相关的封装对象
 *
 * @author Onefool
 * @version 1.0
 */

public class PageInfo<T> implements Serializable {
    /**
     * //当前页码
     */
    private Long page;
    /**
     * //每页显示行
     */
    private Long size;
    /**
     * //总记录数
     */
    private Long total;
    /**
     * //总页数
     */
    private Long totalPages;
    /**
     * //当前页记录
     */
    private List<T> list;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageInfo(Long page, Long size, Long total, Long totalPages, List<T> list) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = totalPages;
        this.list = list;
    }
}
