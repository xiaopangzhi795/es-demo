/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.commons.strategy;

import com.geek45.esdemo.commons.enums.OperatorEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: StrategyFactory
 * @Decription: 策略工厂
 * @Author: rubik
 *  rubik create StrategyFactory.java of 2022/1/18 2:27 下午
 */
@Component
public class StrategyFactory {

    private final static StrategyFactory INSTANCE = new StrategyFactory();

    private List<OperatorStrategy> operatorStrategyList;

    /**
     * 通过操作符获取实现类
     * @param operatorEnum
     * @return
     */
    public static OperatorStrategy getOperatorStrategy(OperatorEnum operatorEnum) {
        List<OperatorStrategy> list = INSTANCE.getOperatorStrategyList();
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("没有找到该实现");
        }
        return list.stream().filter(x -> x.supportMatchScene().contains(operatorEnum)).findAny().orElseGet(() -> {
            throw new RuntimeException("没有找到该实现");
        });
    }

    public List<OperatorStrategy> getOperatorStrategyList() {
        return operatorStrategyList;
    }

    @Autowired
    public void init(List<OperatorStrategy> operatorStrategyList) {
        getInstance().operatorStrategyList = operatorStrategyList;
    }
    private StrategyFactory() {
    }
    public static final StrategyFactory getInstance() {
        return INSTANCE;
    }
}
