/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.Set;

/**
 * @ClassName: OperatorStrategy
 * @Decription:
 * @Author: rubik
 *  rubik create OperatorStrategy.java of 2022/1/18 2:11 下午
 */
public interface OperatorStrategy {

    Set<OperatorEnum> supportMatchScene();

    /**
     * 构建字段
     * @param builder
     * @param fieldSearchDTO
     */
    default void processingFieldBuilderOfMust(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {}

    /**
     * 构建字段
     * @param builder
     * @param fieldSearchDTO
     */
    default void processingFieldBuilderOfMustNot(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {}

    /**
     * 构建时间
     */
    default void processingTimeBuilder(){}

    /**
     * 构建排序
     */
    default void processingSortBuilder(){}

    /**
     * 查询
     * @param fieldSearchDTO
     * @return
     */
    default TermsQueryBuilder termsQuery(FieldSearchDTO fieldSearchDTO) {
        return QueryBuilders.termsQuery(fieldSearchDTO.getField(), fieldSearchDTO.getFieldValues());
    }

    /**
     * 查询
     * @param fieldSearchDTO
     * @return
     */
    default RangeQueryBuilder rangeQuery(FieldSearchDTO fieldSearchDTO) {
        return QueryBuilders.rangeQuery(fieldSearchDTO.getField());
    }

    /**
     * 获取操作类型
     *
     * @param operator
     * @return
     */
    default OperatorEnum getOperatorEnum(String operator) {
        return OperatorEnum.valueOfOperator(operator);
    }

}
