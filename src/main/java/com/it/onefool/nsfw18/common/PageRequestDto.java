package com.it.onefool.nsfw18.common;

import java.io.Serializable;

/**
 * 分页查询的DTO类
 *
 * @author Onefool
 * @version 1.0
 */
public class PageRequestDto<T> implements Serializable {
    /**
     * //当前页码
     */
    private Long page = 1L;
    /**
     * //每页显示的行
     */
    private Long size = 10L;
    /**
     * //请求体实体对象
     */
    private T body;

    public PageRequestDto(Long page, Long size, T body) {
        this.page = page;
        this.size = size;
        this.body = body;
    }

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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
