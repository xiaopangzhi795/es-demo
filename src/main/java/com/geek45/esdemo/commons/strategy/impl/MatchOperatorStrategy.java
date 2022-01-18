/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy.impl;

import com.geek45.esdemo.commons.EsUtils;
import com.geek45.esdemo.commons.enums.SearchType;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: MatchOperatorStrategy
 * @Decription: 比较
 * @Author: rubik
 *  rubik create MatchOperatorStrategy.java of 2022/1/18 2:15 下午
 */
@Component
public class MatchOperatorStrategy implements OperatorStrategy {

    @Override
    public SearchType matchSearchType() {
        return SearchType.MATCH;
    }

    @Override
    public void processingFieldBuilderForList(BoolQueryBuilder builder, List<FieldSearchDTO> fieldSearchDTO, Boolean isBuild) {
        //外层每一条就是一个条件
        if (CollectionUtils.isEmpty(fieldSearchDTO)) {
            return;
        }
        BoolQueryBuilder orQueryBuilder = new BoolQueryBuilder();
        fieldSearchDTO.forEach(x -> {
            //内层组成一个新的条件
            List<Object> list = x.getFieldValues();
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            Object value = list.get(0);
            orQueryBuilder.should(QueryBuilders
                    .matchQuery(isBuild ? EsUtils.generatorField(x.getField()) : x.getField(),
                            isBuild ? EsUtils.joinLikeCondition(value) : value));
        });
        builder.must(orQueryBuilder);
    }


}
