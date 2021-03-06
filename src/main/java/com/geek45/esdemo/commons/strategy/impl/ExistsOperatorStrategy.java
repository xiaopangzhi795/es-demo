/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy.impl;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.enums.SearchType;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @ClassName: ExistsOperatorStrategy
 * @Decription: 精确比较
 * @Author: rubik
 *  rubik create ExistsOperatorStrategy.java of 2022/1/18 2:15 下午
 */
@Component
public class ExistsOperatorStrategy implements OperatorStrategy {

    @Override
    public SearchType matchSearchType() {
        return SearchType.EXISTS;
    }

    @Override
    public Set<OperatorEnum> supportMatchScene() {
        return Sets.newHashSet(OperatorEnum.EXISTS, OperatorEnum.NOT_EXISTS);
    }

    @Override
    public void processingFieldBuilder(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        OperatorEnum operatorEnum = getOperatorEnum(fieldSearchDTO.getOperator());
        if (operatorEnum == OperatorEnum.EXISTS) {
            builder.must(QueryBuilders.existsQuery(fieldSearchDTO.getField()));
        }

        if (operatorEnum == OperatorEnum.NOT_EXISTS) {
            builder.mustNot(QueryBuilders.existsQuery(fieldSearchDTO.getField()));
        }

    }

}
