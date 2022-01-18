/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy.impl;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.enums.SearchType;
import com.geek45.esdemo.commons.model.EsSearchModel;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.model.dto.TimeSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @ClassName: RangeOperatorStrategy
 * @Decription: 小于
 * @Author: rubik
 *  rubik create RangeOperatorStrategy.java of 2022/1/18 2:15 下午
 */
@Component
public class RangeOperatorStrategy implements OperatorStrategy {

    @Override
    public SearchType matchSearchType() {
        return SearchType.RANGE;
    }

    @Override
    public Set<OperatorEnum> supportMatchScene() {
        return Sets.newHashSet(OperatorEnum.LT, OperatorEnum.LTE, OperatorEnum.GT, OperatorEnum.GTE);
    }

    @Override
    public void processingFieldBuilder(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
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


    @Override
    public void processingTimeBuilder(BoolQueryBuilder builder, List<TimeSearchDTO> timeSearch, Function<String, String> buildName) {
        if (CollectionUtils.isEmpty(timeSearch)) {
            return;
        }
        timeSearch.forEach(x -> {
            if (null == x) {
                return;
            }
            if (StringUtils.isBlank(x.getStartTime()) && StringUtils.isBlank(x.getEndTime())) {
                return;
            }

            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(x.getTimeField());
            if (StringUtils.isNotBlank(x.getStartTime())) {
                rangeQueryBuilder = rangeQueryBuilder.gt(x.getStartTime());
            }

            if (StringUtils.isNotBlank(x.getEndTime())) {
                rangeQueryBuilder = rangeQueryBuilder.lt(buildName.apply(x.getEndTime()));
            }
            builder.must(rangeQueryBuilder.format(EsSearchModel.DATE_TIME_FORMAT));
        });
    }
}
