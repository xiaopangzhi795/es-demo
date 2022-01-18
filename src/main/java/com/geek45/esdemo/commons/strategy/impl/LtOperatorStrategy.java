/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy.impl;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.model.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: LtOperatorStrategy
 * @Decription: 小于
 * @Author: rubik
 *  rubik create LtOperatorStrategy.java of 2022/1/18 2:15 下午
 */
@Component
public class LtOperatorStrategy implements OperatorStrategy {
    @Override
    public Set<OperatorEnum> supportMatchScene() {
        return Sets.newHashSet(OperatorEnum.LT, OperatorEnum.LTE, OperatorEnum.GT, OperatorEnum.GTE);
    }

    @Override
    public void processingFieldBuilderOfMust(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        List<Object> values = fieldSearchDTO.getFieldValues();
        if (CollectionUtils.isEmpty(values)) {
            return;
        }
        OperatorEnum operatorEnum = getOperatorEnum(fieldSearchDTO.getOperator());
        RangeQueryBuilder rangeQueryBuilder = rangeQuery(fieldSearchDTO);
        switch (operatorEnum) {
            case LT:
                values.forEach(x -> builder.must(rangeQueryBuilder.lt(x)));
                break;
            case LTE:
                values.forEach(x -> builder.must(rangeQueryBuilder.lte(x)));
                break;
            case GT:
                values.forEach(x -> builder.must(rangeQueryBuilder.gt(x)));
                break;
            case GTE:
                values.forEach(x -> builder.must(rangeQueryBuilder.gte(x)));
                break;
        }

    }
}
