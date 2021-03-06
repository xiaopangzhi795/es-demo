/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy.impl;

import com.geek45.esdemo.commons.EsUtils;
import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.enums.SearchType;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: WildcardOperatorStrategy
 * @Decription: 比较
 * @Author: rubik
 *  rubik create WildcardOperatorStrategy.java of 2022/1/18 2:15 下午
 */
@Component
public class WildcardOperatorStrategy implements OperatorStrategy {

    @Override
    public SearchType matchSearchType() {
        return SearchType.WILDCARD;
    }

    @Override
    public Set<OperatorEnum> supportMatchScene() {
        return Sets.newHashSet(OperatorEnum.LIKE);
    }

    @Override
    public void processingFieldBuilder(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        List<Object> list = fieldSearchDTO.getFieldValues();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Object value = list.get(0);
        builder.must(QueryBuilders
                .wildcardQuery(fieldSearchDTO.getField(), EsUtils.joinLikeCondition(value)));
        //TODO 此处为什么只需要放进去一个？
//        list.forEach(x -> builder.must(QueryBuilders.wildcardQuery(fieldSearchDTO.getField(), EsUtils.joinLikeCondition(x))));
    }

}
