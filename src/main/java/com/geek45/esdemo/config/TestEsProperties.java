/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: TestEsConfig
 * @Decription:
 * @Author: rubik
 * rubik create TestEsConfig.java of 2022/1/17 3:38 下午
 */
@ConfigurationProperties("es.config")
public class TestEsProperties {
    private String type;
    private String index;

    public String getType() {
        return type;
    }

    public String getIndex() {
        return index;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
