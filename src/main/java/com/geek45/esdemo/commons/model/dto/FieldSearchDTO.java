/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.model.dto;

import com.geek45.esdemo.commons.enums.OperatorEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FieldSearchDTO
 * @Decription: 字段查询模型
 * @Author: rubik
 *  rubik create FieldSearchDTO.java of 2022/1/18 12:25 下午
 */
public class FieldSearchDTO implements Serializable {

    private static final long serialVersionUID = 4217884994365780157L;
    /**
     * the field of search
     */
    private String field;
    /**
     * the operator of search
     */
    private String operator;
    /**
     * the field value of search
     */
    private List<Object> fieldValues;

    public FieldSearchDTO() {
    }

    /**
     * create operator query conditions
     * @param field
     * @param fieldValue
     * @param operator
     * @return
     */
    public static FieldSearchDTO createFieldModelByOperator(String field, Object fieldValue, String operator) {
        FieldSearchDTO model = new FieldSearchDTO();
        model.setField(field);
        model.addField(fieldValue);
        model.setOperator(operator);
        return model;
    }

    /**
     * create eq query conditions
     * @param field
     * @param fieldValue
     * @return
     */
    public static FieldSearchDTO createFieldByEq(String field, Object fieldValue) {
        return createFieldModelByOperator(field, fieldValue, OperatorEnum.EQ.getOperator());
    }

    /**
     * add a query condition
     * @param fieldValue
     */
    public void addField(Object fieldValue) {
        initField();
        this.fieldValues.add(fieldValue);
    }

    /**
     * add a query condition
     * @param fieldValues
     */
    public void addFieldValues(List<Object> fieldValues) {
        initField();
        this.fieldValues.addAll(fieldValues);
    }

    /**
     * init field
     */
    private void initField() {
        if (null == this.fieldValues) {
            this.fieldValues = new ArrayList<>(16);
        }
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        if (null == this.operator) {
            this.operator = OperatorEnum.EQ.getOperator();
        }
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<Object> getFieldValues() {
        if (null == this.fieldValues) {
            this.fieldValues = new ArrayList<>(16);
        }
        return this.fieldValues;
    }

    public void setFieldValues(List<Object> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
