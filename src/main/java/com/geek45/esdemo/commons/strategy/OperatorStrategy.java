/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.enums.SearchType;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.model.dto.TimeSearchDTO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @ClassName: OperatorStrategy
 * @Decription:
 * @Author: rubik
 *  rubik create OperatorStrategy.java of 2022/1/18 2:11 下午
 */
public interface OperatorStrategy {

    /**
     * 操作符类型
     * @return
     */
    default Set<OperatorEnum> supportMatchScene(){
        return Collections.emptySet();
    }

    /**
     * 查询类型
     * @return
     */
    SearchType matchSearchType();

    /**
     * 构建字段
     * @param builder
     * @param fieldSearchDTO
     */
    default void processingFieldBuilder(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {}

    /**
     * 构建字段
     *
     * @param builder
     * @param fieldSearchDTO
     * @param isBuild 是否构建特殊话字段
     */
    default void processingFieldBuilderForList(BoolQueryBuilder builder, List<FieldSearchDTO> fieldSearchDTO, Boolean isBuild) {}

    /**
     * 构建时间
     */
    default void processingTimeBuilder(BoolQueryBuilder builder, List<TimeSearchDTO> timeSearch, Function<String, String> buildName) {}


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
