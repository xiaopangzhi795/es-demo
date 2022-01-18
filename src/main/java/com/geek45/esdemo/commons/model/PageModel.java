/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.model;

import java.io.Serializable;

/**
 * @ClassName: PageModel
 * @Decription:
 * @Author: rubik
 *  rubik create PageModel.java of 2022/1/18 11:58 上午
 */
public class PageModel implements Serializable {
    private static final long serialVersionUID = 2316304415265684630L;

    /**
     * The largest number of page size
     */
    public static final Integer MAX_PAGE_SIZE = 999;
    /**
     * The default number of page size
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    /**
     * the default number of pages
     */
    public static final Integer DEFAULT_PAGE = 1;

    /**
     * the number of pages
     */
    private Integer page;
    /**
     * the page size
     */
    private Integer pageSize;
    /**
     * the pagination
     */
    private Boolean pagination;

    public PageModel() {
        this.pagination = Boolean.TRUE;
    }

    public Integer getPage() {
        return null == this.page ? DEFAULT_PAGE : this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return null == this.pageSize ? DEFAULT_PAGE_SIZE : this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean isPagination() {
        return this.pagination;
    }

    public void setPagination(Boolean pagination) {
        this.pagination = pagination;
    }

    public Integer getLimit() {
        if (null == this.pageSize) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        if (this.pageSize > MAX_PAGE_SIZE) {
            this.pageSize = MAX_PAGE_SIZE;
        }
        return this.pageSize;
    }

    public Integer getStart() {
        if (null == this.pageSize) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        if (null == this.page) {
            return 0;
        }
        return this.page <= 0 ? 0 : (this.page - 1) * this.pageSize;
    }
}
