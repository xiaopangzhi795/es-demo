/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy.impl;

import com.geek45.esdemo.commons.enums.SearchType;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: FuzzyOperatorStrategy
 * @Decription: 比较
 * @Author: rubik
 *  rubik create FuzzyOperatorStrategy.java of 2022/1/18 2:15 下午
 */
@Component
public class FuzzyOperatorStrategy implements OperatorStrategy {

    @Override
    public SearchType matchSearchType() {
        return SearchType.FUZZY;
    }

    @Override
    public void processingFieldBuilder(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        List<Object> list = fieldSearchDTO.getFieldValues();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Object value = list.get(0);
        builder.must(QueryBuilders
                .matchQuery(fieldSearchDTO.getField(), value)
                .fuzziness(Fuzziness.ZERO)
                .prefixLength(1)
                .operator(Operator.AND));
    }

}
