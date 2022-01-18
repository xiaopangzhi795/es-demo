package com.geek45.esdemo;

import com.alibaba.fastjson.JSONObject;
import com.geek45.esdemo.commons.enums.OperatorEnum;
import com.geek45.esdemo.commons.model.EsSearchModel;
import com.geek45.esdemo.commons.model.dto.FieldSearchDTO;
import com.geek45.esdemo.commons.strategy.StrategyFactory;
import com.geek45.esdemo.service.EsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
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

    @Test
    public void testSearchEsByCondition() throws IOException {
        EsSearchModel model = new EsSearchModel();
        //模糊搜索
        model.addFuzzySearch(FieldSearchDTO.createFieldModelByOperator("test", "测试", OperatorEnum.LIKE.getOperator()));
        esService.search(model);

        EsSearchModel model2 = new EsSearchModel();
        //精确搜索
        model2.addFuzzySearch(FieldSearchDTO.createFieldModelByOperator("test", "测试测试测试", OperatorEnum.EQ.getOperator()));
        esService.search(model2);

        EsSearchModel model3 = new EsSearchModel();
        //精确搜索
        model3.addFuzzySearch(FieldSearchDTO.createFieldModelByOperator("test", "测试测试测试", OperatorEnum.LIKE.getOperator()));
        esService.search(model3);
    }

    @Test
    public void testStrategy() {
        StrategyFactory.getOperatorStrategy(OperatorEnum.EQ);

    }

    @Autowired
    public void init(EsService esService) {
        this.esService = esService;
    }

}
