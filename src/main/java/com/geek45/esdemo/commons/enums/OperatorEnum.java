/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.enums;

import java.util.Arrays;

/**
 * @ClassName: OperatorEnum
 * @Decription:
 * @Author: rubik
 * rubik create OperatorEnum.java of 2022/1/18 12:28 下午
 */
public enum OperatorEnum {

    EQ("="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    NEQ("!="),
    LIKE("like"),
    EXISTS("exists"),
    NOT_EXISTS("not exists"),
    ;

    private String operator;

    OperatorEnum(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public static OperatorEnum valueOfOperator(String operator) {
        return Arrays.stream(OperatorEnum.values()).filter(x -> x.getOperator().equals(operator)).findAny().orElse(null);
    }

}
