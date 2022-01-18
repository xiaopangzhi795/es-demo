/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy.impl;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.model.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @ClassName: TermsOperatorStrategy
 * @Decription: 精确比较
 * @Author: rubik
 *  rubik create TermsOperatorStrategy.java of 2022/1/18 2:15 下午
 */
@Component
public class TermsOperatorStrategy implements OperatorStrategy {
    @Override
    public Set<OperatorEnum> supportMatchScene() {
        return Sets.newHashSet(OperatorEnum.EQ, OperatorEnum.NEQ);
    }

    @Override
    public void processingFieldBuilderOfMust(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        OperatorEnum operatorEnum = getOperatorEnum(fieldSearchDTO.getOperator());
        if (operatorEnum != OperatorEnum.EQ) {
            return;
        }
        TermsQueryBuilder termsQuery = termsQuery(fieldSearchDTO);
        builder.must(termsQuery);
    }

    @Override
    public void processingFieldBuilderOfMustNot(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        OperatorEnum operatorEnum = getOperatorEnum(fieldSearchDTO.getOperator());
        if (operatorEnum != OperatorEnum.NEQ) {
            return;
        }
        TermsQueryBuilder termsQuery = termsQuery(fieldSearchDTO);
        builder.mustNot(termsQuery);
    }
}
