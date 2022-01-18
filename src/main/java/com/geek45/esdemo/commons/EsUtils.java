/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.enums.SearchType;
import com.geek45.esdemo.commons.enums.SortType;
import com.geek45.esdemo.commons.model.EsSearchModel;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.OperatorStrategy;
import com.geek45.esdemo.commons.strategy.StrategyFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

/**
 * @ClassName: EsUtils
 * @Decription:
 * @Author: rubik
 *  rubik create EsUtils.java of 2022/1/18 11:56 上午
 */
public class EsUtils {

    /**
     * 生成查询条件
     * @param esDocIndex
     * @param model
     * @return
     */
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
     * 生成where查询条件
     * @param searchModel
     * @return
     */
    public static BoolQueryBuilder generatorWhereBuilder(EsSearchModel searchModel) {
        BoolQueryBuilder builder = new BoolQueryBuilder();

        //匹配所有文档
        builder.must(QueryBuilders.matchAllQuery());

        //精确查询相关条件
        builderEqCondition(builder, searchModel.getFieldSearchList());

        //模糊查询条件
        builderFuzzyCondition(builder, searchModel.getFuzzyFieldSearchList());

        //通配符搜索
        builderWildcardCondition(builder, searchModel.getWildCardSearchList());

        //或 搜索
        builderOrCondition(builder, searchModel.getOrFieldSearchList(), Boolean.FALSE);

        //或 通配符 搜索
        builderOrCondition(builder, searchModel.getOrWildCardSearchList(), Boolean.TRUE);

        //时间搜搜
        getStrategy(OperatorEnum.LT).processingTimeBuilder(builder,
                searchModel.getTimeSearchList(),
                EsUtils::generatorField);

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
     * 构建 或 查询条件
     *
     * @param builder
     * @param orFieldSearch
     */
    public static void builderOrCondition(BoolQueryBuilder builder, List<List<FieldSearchDTO>> orFieldSearch, Boolean isBuild) {
        if (CollectionUtils.isEmpty(orFieldSearch)) {
            return;
        }
        orFieldSearch.forEach(x -> getStrategy(SearchType.MATCH).processingFieldBuilderForList(builder, x, isBuild));
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
        wildcardFieldSearch.forEach(x -> getStrategy(SearchType.WILDCARD).processingFieldBuilder(builder, x));
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
        fuzzyFieldSearch.forEach(x -> getStrategy(SearchType.FUZZY).processingFieldBuilder(builder, x));
    }

    /**
     * 构建精准查询条件
     *
     * @param builder
     * @param fieldSearchDTOS
     */
    public static void builderEqCondition(BoolQueryBuilder builder, List<FieldSearchDTO> fieldSearchDTOS) {
        if (CollectionUtils.isEmpty(fieldSearchDTOS)) {
            return;
        }
        fieldSearchDTOS.forEach(x -> {
            processingFieldValues(x, OperatorEnum.EQ);
            prepareWhereSqlByOperator(builder, x);
        });
    }

    /**
     * 获取策略
     * @param operatorEnum
     * @return
     */
    public static OperatorStrategy getStrategy(OperatorEnum operatorEnum) {
        return StrategyFactory.getOperatorStrategy(operatorEnum);
    }

    /**
     * 获取策略
     * @param searchType
     * @return
     */
    public static OperatorStrategy getStrategy(SearchType searchType) {
        return StrategyFactory.getOperatorStrategyBySearchType(searchType);
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
                    source.sort(createSortBuild(generatorField(x.getSortField()), model));
                    return;
                }
                source.sort(createSortBuild(x.getSortField(), model));
                return;
            });
            return;
        }
        if (StringUtils.isBlank(model.getSortField())) {
            return;
        }

        //单字段排序
        source.sort(createSortBuild(generatorField(model.getSortField()), model));
    }

    /**
     * 创建排序构造器
     * @param field
     * @param model
     * @return
     */
    public static FieldSortBuilder createSortBuild(String field, EsSearchModel model) {
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(field);
        return fieldSortBuilder.order(converterSortOrder(model.getSortType()));
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
     * 转换为条件
     * @param builder
     * @param fieldSearchDTO
     */
    public static void prepareWhereSqlByOperator(BoolQueryBuilder builder, FieldSearchDTO fieldSearchDTO) {
        OperatorEnum operatorEnum = OperatorEnum.valueOfOperator(fieldSearchDTO.getOperator());
        getStrategy(operatorEnum).processingFieldBuilder(builder, fieldSearchDTO);
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
