/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.model;

import com.geek45.esdemo.commons.enums.SortType;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.model.dto.SortFieldDTO;
import com.geek45.esdemo.commons.model.dto.TimeSearchDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: EsSearchModel
 * @Decription: ES查询dto
 * @Author: rubik
 * rubik create EsSearchModel.java of 2022/1/18 11:57 上午
 */
public class EsSearchModel extends PageModel {

    /**
     * 时间格式
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间查询条件
     */
    private List<TimeSearchDTO> timeSearchList;
    /**
     * 字段精准查询条件
     */
    private List<FieldSearchDTO> fieldSearchList;
    /**
     * 字段模糊查询
     */
    private List<FieldSearchDTO> fuzzyFieldSearchList;
    /**
     * 通配符查询
     */
    private List<FieldSearchDTO> wildCardSearchList;
    /**
     * 或条件查询
     */
    private List<List<FieldSearchDTO>> orFieldSearchList;
    /**
     * 或条件查询，字段模糊匹配
     */
    private List<List<FieldSearchDTO>> orWildCardSearchList;
    /**
     * script脚本查询
     */
    private List<String> scriptList;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 包含字段
     */
    private String existsField;
    /**
     * 排序方式
     */
    private SortType sortType;
    /**
     * 按多个字段排序
     */
    private List<SortFieldDTO> sortFieldList;

    public void addFieldSearchList(List<FieldSearchDTO> fieldSearchList) {
        if (CollectionUtils.isNotEmpty(this.fieldSearchList)) {
            throw new RuntimeException("已存在条件，请清空后再添加条件列表");
        }
        this.fieldSearchList = new ArrayList<>(16);
        if (CollectionUtils.isNotEmpty(fieldSearchList)) {
            this.fieldSearchList.addAll(fieldSearchList);
        }
    }

    public void addFieldSearch(FieldSearchDTO fieldSearch) {
        if (null == this.fieldSearchList) {
            this.fieldSearchList = new ArrayList<>(16);
        }
        FieldSearchDTO dto = this.fieldSearchList
                .stream()
                .filter(x -> x.getField().equals(fieldSearch.getField()))
                .findAny()
                .orElse(null);
        if (null == dto) {
            this.fieldSearchList.add(fieldSearch);
        }else{
            dto.getFieldValues().addAll(fieldSearch.getFieldValues());
        }

    }

    public List<Object> getFieldSearchValuesByField(String field) {
        List<Object> fieldValues = new ArrayList<>(16);
        if (CollectionUtils.isEmpty(this.fieldSearchList)) {
            return new ArrayList<>();
        }
        this.fieldSearchList
                .stream()
                .filter(x -> x.getField().equals(field))
                .forEach(x -> fieldValues.addAll(x.getFieldValues()));
        return fieldValues;
    }

    public void removeFieldSearch(String field, List<Object> fieldValues) {
        if (CollectionUtils.isEmpty(this.fieldSearchList)) {
            return;
        }
        this.fieldSearchList
                .stream()
                .filter(x -> x.getField().equals(field))
                .forEach(x -> x.getFieldValues().removeAll(fieldValues));
    }

    public void removeFieldSearch(String field) {
        if (CollectionUtils.isEmpty(this.fieldSearchList)) {
            return;
        }
        List<FieldSearchDTO> list = new ArrayList<>(16);
        this.fieldSearchList
                .stream()
                .filter(x -> !x.getField().equals(field))
                .forEach(list::add);
        this.fieldSearchList = list;
    }

    public void addTimeSearch(TimeSearchDTO timeSearchDTO) {
        if (CollectionUtils.isEmpty(this.timeSearchList)) {
            this.timeSearchList = new ArrayList<>(16);
        }
        TimeSearchDTO dto = this.timeSearchList.stream().filter(x -> x.getTimeField().equals(timeSearchDTO.getTimeField()))
                .findAny().orElse(null);
        if (null != dto) {
            this.timeSearchList.remove(dto);
        }
        this.timeSearchList.add(timeSearchDTO);
    }

    public void addFuzzySearch(FieldSearchDTO fuzzySearch) {
        if (CollectionUtils.isEmpty(this.fuzzyFieldSearchList)) {
            this.fuzzyFieldSearchList = new ArrayList<>(16);
        }
        FieldSearchDTO dto = this.fuzzyFieldSearchList.stream().filter(x -> x.getField().equals(fuzzySearch.getField()))
                .findAny().orElse(null);
        if (null != dto) {
            this.fuzzyFieldSearchList.remove(dto);
        }
        this.fuzzyFieldSearchList.add(fuzzySearch);
    }

    public void addSort(SortFieldDTO sortField) {
        if (CollectionUtils.isEmpty(this.sortFieldList)) {
            this.sortFieldList = new ArrayList<>(16);
        }
        SortFieldDTO dto = this.sortFieldList.stream().filter(x -> x.getSortField().equals(sortField.getSortField()))
                .findAny().orElse(null);
        if (null != dto) {
            this.sortFieldList.remove(dto);
        }
        this.sortFieldList.add(sortField);
    }

    public void addScript(String script) {
        if (StringUtils.isBlank(script)) {
            return;
        }

        if (CollectionUtils.isEmpty(this.scriptList)) {
            this.scriptList = new ArrayList<>(16);
        }
        this.scriptList.add(script);
    }

    public void addWildCardSearch(FieldSearchDTO wildCardSearch) {
        if (CollectionUtils.isEmpty(this.wildCardSearchList)) {
            this.wildCardSearchList = new ArrayList<>(16);
        }
        FieldSearchDTO dto = this.wildCardSearchList.stream().filter(x -> x.getField().equals(wildCardSearch.getField()))
                .findAny().orElse(null);
        if (null != dto) {
            this.wildCardSearchList.remove(dto);
        }
        this.wildCardSearchList.add(wildCardSearch);
    }

    public List<TimeSearchDTO> getTimeSearchList() {
        return timeSearchList;
    }

    public void setTimeSearchList(List<TimeSearchDTO> timeSearchList) {
        this.timeSearchList = timeSearchList;
    }

    public List<FieldSearchDTO> getFieldSearchList() {
        return fieldSearchList;
    }

    public void setFieldSearchList(List<FieldSearchDTO> fieldSearchList) {
        this.fieldSearchList = fieldSearchList;
    }

    public List<FieldSearchDTO> getFuzzyFieldSearchList() {
        return fuzzyFieldSearchList;
    }

    public void setFuzzyFieldSearchList(List<FieldSearchDTO> fuzzyFieldSearchList) {
        this.fuzzyFieldSearchList = fuzzyFieldSearchList;
    }

    public List<FieldSearchDTO> getWildCardSearchList() {
        return wildCardSearchList;
    }

    public void setWildCardSearchList(List<FieldSearchDTO> wildCardSearchList) {
        this.wildCardSearchList = wildCardSearchList;
    }

    public List<List<FieldSearchDTO>> getOrFieldSearchList() {
        return orFieldSearchList;
    }

    public void setOrFieldSearchList(List<List<FieldSearchDTO>> orFieldSearchList) {
        this.orFieldSearchList = orFieldSearchList;
    }

    public List<List<FieldSearchDTO>> getOrWildCardSearchList() {
        return orWildCardSearchList;
    }

    public void setOrWildCardSearchList(List<List<FieldSearchDTO>> orWildCardSearchList) {
        this.orWildCardSearchList = orWildCardSearchList;
    }

    public List<String> getScriptList() {
        return scriptList;
    }

    public void setScriptList(List<String> scriptList) {
        this.scriptList = scriptList;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getExistsField() {
        return existsField;
    }

    public void setExistsField(String existsField) {
        this.existsField = existsField;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public List<SortFieldDTO> getSortFieldList() {
        return sortFieldList;
    }

    public void setSortFieldList(List<SortFieldDTO> sortFieldList) {
        this.sortFieldList = sortFieldList;
    }

    public EsSearchModel() {
        super();
    }
}
