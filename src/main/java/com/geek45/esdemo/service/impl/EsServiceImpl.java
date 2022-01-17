/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.geek45.esdemo.config.EsClient;
import com.geek45.esdemo.config.TestEsProperties;
import com.geek45.esdemo.service.EsService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName: EsServiceImpl
 * @Decription:
 * @Author: rubik
 *  rubik create EsServiceImpl.java of 2022/1/17 3:36 下午
 */
@Service
public class EsServiceImpl implements EsService {
    private static final Logger logger = LoggerFactory.getLogger(EsServiceImpl.class);

    private EsClient client;

    private TestEsProperties testEsProperties;

    @Override
    public void add(String text) throws IOException {
        IndexRequest indexRequest = new IndexRequest(testEsProperties.getIndex());
        indexRequest.source(text, XContentType.JSON);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        logger.info("index response is :{}", JSON.toJSONString(indexResponse));

    }

    @Override
    public void search(String query) throws IOException {
        SearchRequest request = new SearchRequest(testEsProperties.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder whereBuilder = QueryBuilders.boolQuery();

        whereBuilder.must(QueryBuilders.matchAllQuery());
        whereBuilder.must(QueryBuilders.matchQuery("test", query));
        sourceBuilder.query(whereBuilder);

        request.source(sourceBuilder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        logger.info("response is :{}", JSON.toJSONString(response));
    }

    @Autowired
    public void init(EsClient esClient, TestEsProperties testEsProperties) {
        this.client = esClient;
        this.testEsProperties = testEsProperties;
    }
}
