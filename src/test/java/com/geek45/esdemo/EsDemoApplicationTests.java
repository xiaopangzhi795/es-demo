package com.geek45.esdemo;

import com.alibaba.fastjson.JSONObject;
import com.geek45.esdemo.config.EsClient;
import com.geek45.esdemo.config.TestEsProperties;
import com.geek45.esdemo.service.EsService;
import com.geek45.esdemo.service.impl.EsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
@ComponentScan({"com.geek45"})
public class EsDemoApplicationTests {

    private EsService esService;

    private String mockJSON() {
        JSONObject object = new JSONObject();
        object.put("test", "测试测试测试");
        return object.toJSONString();
    }

    @Test
    public void testAddEs() throws IOException {
        esService.add(mockJSON());
    }

    @Test
    public void testSearchEs() throws IOException {
        esService.search("测试");
    }

    @Autowired
    public void init(EsService esService) {
        this.esService = esService;
    }

}
