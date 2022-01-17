/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @ClassName: EsClient
 * @Decription:
 * @Author: rubik
 * rubik create EsClient.java of 2022/1/17 3:22 下午
 */
@Component
public class EsClient implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(EsClient.class);

    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;

    private RestHighLevelClient client;

    public SearchResponse search(SearchRequest request, RequestOptions options) throws IOException {
        return client.search(request, options);
    }

    public IndexResponse index(IndexRequest request, RequestOptions options) throws IOException {
        return client.index(request, options);
    }

    public UpdateResponse update(UpdateRequest request, RequestOptions options) throws IOException {
        return client.update(request, options);
    }

    public DeleteResponse delete(DeleteRequest request, RequestOptions options) throws IOException {
        return client.delete(request, options);
    }

    @PreDestroy
    public void destroy() {
        if (null != client) {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("client close exception...", e);
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        final CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        RestClientBuilder builder = RestClient
                .builder(new HttpHost(host, port))
                .setHttpClientConfigCallback(
                        httpAsyncClientBuilder -> httpAsyncClientBuilder
                                .setDefaultCredentialsProvider(provider));
        client = new RestHighLevelClient(builder);
    }
}
