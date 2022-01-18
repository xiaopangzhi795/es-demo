/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.model;

import com.geek45.esdemo.commons.enums.SortType;

import java.io.Serializable;

/**
 * @ClassName: SortFieldDTO
 * @Decription: 排序实体
 * @Author: rubik
 *  rubik create SortFieldDTO.java of 2022/1/18 12:44 下午
 */
public class SortFieldDTO implements Serializable {

    private static final long serialVersionUID = -5641007149671669273L;

    /**
     * sort field of search
     */
    private String sortField;
    /**
     * sort type of search
     */
    private SortType sortType;
    /**
     * Whether to sort by text
     */
    private Boolean sortByText;

    public SortFieldDTO() {
        this.sortByText = Boolean.TRUE;
    }

    public static SortFieldDTO createAscSortByText(String sortField) {
        SortFieldDTO field = new SortFieldDTO();
        field.setSortField(sortField);
        field.setSortType(SortType.ASC);
        return field;
    }

    public static SortFieldDTO createDescSortByText(String sortField) {
        SortFieldDTO field = new SortFieldDTO();
        field.setSortField(sortField);
        field.setSortType(SortType.DESC);
        return field;
    }

    public static SortFieldDTO createSort(String sortField, SortType sortType, Boolean sortByText) {
        SortFieldDTO sortFieldDTO = new SortFieldDTO();
        sortFieldDTO.setSortField(sortField);
        sortFieldDTO.setSortType(sortType);
        sortFieldDTO.setSortByText(sortByText);
        return sortFieldDTO;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public Boolean getSortByText() {
        return sortByText;
    }

    public void setSortByText(Boolean sortByText) {
        this.sortByText = sortByText;
    }
}
