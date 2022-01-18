/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.enums.SortType;
import com.geek45.esdemo.commons.model.EsSearchModel;
import com.geek45.esdemo.commons.model.FieldSearchDTO;
import com.geek45.esdemo.commons.model.TimeSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import com.geek45.esdemo.commons.strategy.StrategyFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.function.Function;

/**
 * @ClassName: EsUtils
 * @Decription:
 * @Author: rubik
 *  rubik create EsUtils.java of 2022/1/18 11:56 上午
 */
public class EsUtils {

    public static SearchRequest generatorRequest(String esDocIndex, EsSearchModel model) {
        SearchSourceBuilder source = generatorSource(model);

        SearchRequest request = new SearchRequest(esDocIndex);
        request.source(source);
        return request;
    }

    /**
     * 生成source
     * @param model
     * @return
     */
    public static SearchSourceBuilder generatorSource(EsSearchModel model) {
        SearchSourceBuilder source = new SearchSourceBuilder();
        BoolQueryBuilder query = generatorWhereBuilder(model);
        source.query(query);

        //分页
        source.from(model.getStart());
        source.size(model.getLimit());

        //多字段排序
        sortByModel(source, model);
        return source;
    }


    /**
     * 排序
     * @param source
     * @param model
     */
    public static void sortByModel(SearchSourceBuilder source, EsSearchModel model) {
        //多字段排序
        if (CollectionUtils.isNotEmpty(model.getSortFieldList())) {
            model.getSortFieldList().forEach(x -> {
                if (x.getSortByText()) {
                    source.sort(new FieldSortBuilder(generatorField(x.getSortField())).order(converterSortOrder(x.getSortType())));
                    return;
                }
                source.sort(new FieldSortBuilder(x.getSortField()).order(converterSortOrder(x.getSortType())));
                return;
            });
            return;
        }

        if (StringUtils.isBlank(model.getSortField())) {
            return;
        }

        //单字段排序
        source.sort(new FieldSortBuilder(generatorField(model.getSortField())).order(converterSortOrder(model.getSortType())));
    }

    /**
     * sortType 转为 es可识别的SortOrder
     * 默认正序
     * @param sortType
     * @return
     */
    public static SortOrder converterSortOrder(SortType sortType) {
        if (null == sortType) {
            return SortOrder.ASC;
        }
        if (sortType == SortType.ASC) {
            return SortOrder.ASC;
        }
        return SortOrder.DESC;
    }


    /**
     * 生成where查询条件
     * @param searchModel
     * @return
     */
    public static BoolQueryBuilder generatorWhereBuilder(EsSearchModel searchModel) {
        BoolQueryBuilder builder = new BoolQueryBuilder();

        //匹配所有文档
        builder.must(QueryBuilders.matchAllQuery());

        //精确查询相关条件
        builderEqCondition(builder, searchModel.getFieldSearchList(), OperatorEnum.EQ);

        //模糊查询条件
        builderFuzzyCondition(builder, searchModel.getFuzzyFieldSearchList());

        //通配符搜索
        builderWildcardCondition(builder, searchModel.getWildCardSearchList());

        //或 搜索
        builderOrCondition(builder, searchModel.getOrFieldSearchList(), x -> x, x -> x);

        //或 通配符 搜索
        builderOrCondition(builder, searchModel.getOrWildCardSearchList(), EsUtils::generatorField, EsUtils::joinLikeCondition);

        //时间搜搜
        builderTimeCondition(builder, searchModel.getTimeSearchList(), EsUtils::generatorField);

        //script 脚本搜索
        if (CollectionUtils.isNotEmpty(searchModel.getScriptList())) {
            searchModel.getScriptList().forEach(x -> builder.must(QueryBuilders.scriptQuery(new Script(x))));
        }

        //字段
        if (StringUtils.isNotBlank(searchModel.getExistsField())) {
            BoolQueryBuilder query = new BoolQueryBuilder();
            query.filter(QueryBuilders.existsQuery(searchModel.getExistsField()));
            builder.must(query);
        }

        return builder;
    }


    /**
     * 构建时间查询条件
     *
     * @param builder
     * @param timeSearch
     */
    public static void builderTimeCondition(BoolQueryBuilder builder, List<TimeSearchDTO> timeSearch, Function<String, String> buildName) {
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


    /**
     * 构建 或 查询条件
     *
     * @param builder
     * @param orFieldSearch
     */
    public static void builderOrCondition(BoolQueryBuilder builder, List<List<FieldSearchDTO>> orFieldSearch, Function<String, String> builderName, Function<Object, Object> builderValue) {
        if (CollectionUtils.isEmpty(orFieldSearch)) {
            return;
        }
        orFieldSearch.forEach(x -> {
            //外层每一条就是一个条件
            if (CollectionUtils.isEmpty(x)) {
                return;
            }
            BoolQueryBuilder orQueryBuilder = new BoolQueryBuilder();
            x.forEach(j -> {
                //内层组成一个新的条件
                List<Object> list = j.getFieldValues();
                if (CollectionUtils.isEmpty(list)) {
                    return;
                }
                Object value = list.get(0);
                orQueryBuilder.should(QueryBuilders.matchQuery(builderName.apply(j.getField()), builderValue.apply(value)));
            });
            builder.must(orQueryBuilder);
        });
    }

    /**
     * 构建通配符查询条件
     *
     * @param builder
     * @param wildcardFieldSearch
     */
    public static void builderWildcardCondition(BoolQueryBuilder builder, List<FieldSearchDTO> wildcardFieldSearch) {
        if (CollectionUtils.isEmpty(wildcardFieldSearch)) {
            return;
        }
        wildcardFieldSearch.forEach(x -> {
            List<Object> list = x.getFieldValues();
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            Object value = list.get(0);
            builder.must(QueryBuilders
                    .wildcardQuery(x.getField(), joinLikeCondition(value)));
        });
    }

    /**
     * 构建模糊查询条件
     *
     * @param builder
     * @param fuzzyFieldSearch
     */
    public static void builderFuzzyCondition(BoolQueryBuilder builder, List<FieldSearchDTO> fuzzyFieldSearch) {
        if (CollectionUtils.isEmpty(fuzzyFieldSearch)) {
            return;
        }
        fuzzyFieldSearch.forEach(x -> {
            List<Object> list = x.getFieldValues();
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            Object value = list.get(0);
            builder.must(QueryBuilders
                    .matchQuery(x.getField(), value)
                    .fuzziness(Fuzziness.ZERO)
                    .prefixLength(1)
                    .operator(Operator.AND));
        });
    }

    /**
     * 构建精准查询条件
     *
     * @param builder
     * @param fieldSearchDTOS
     */
    public static void builderEqCondition(BoolQueryBuilder builder, List<FieldSearchDTO> fieldSearchDTOS, OperatorEnum operator) {
        if (CollectionUtils.isEmpty(fieldSearchDTOS)) {
            return;
        }
        fieldSearchDTOS.forEach(x -> {
            processingFieldValues(x, operator);
            prepareWhereSqlByOperator(builder, x);
        });
    }

    public static void prepareWhereSqlByOperator(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        OperatorEnum operatorEnum = OperatorEnum.valueOfOperator(fieldSearchDTO.getOperator());
        if (null == operatorEnum) {
            throw new RuntimeException("操作符不对，请检查操作符");
        }
        OperatorStrategy strategy = StrategyFactory.getOperatorStrategy(operatorEnum);
        strategy.processingFieldBuilderOfMust(builder, fieldSearchDTO);
        strategy.processingFieldBuilderOfMustNot(builder, fieldSearchDTO);
    }

    /**
     * 特殊处理条件
     *
     * @param fieldSearchDTO
     */
    public static void processingFieldValues(FieldSearchDTO fieldSearchDTO, OperatorEnum operator) {
        if (StringUtils.isNotBlank(fieldSearchDTO.getOperator())) {
            return;
        }
        fieldSearchDTO.setOperator(operator.getOperator());
        fieldSearchDTO.setField(generatorField(fieldSearchDTO.getField()));
    }

    /**
     * 生成field字段名，后续有可能加上后缀
     * @param field
     * @return
     */
    public static String generatorField(String field) {
        return field;
    }

    /**
     * like查询条件前后增加通配符
     *
     * @param value
     * @return
     */
    public static String joinLikeCondition(Object value) {
        if (null == value) {
            return null;
        }
        return StringUtils.join("*", value, "*");
    }

}
