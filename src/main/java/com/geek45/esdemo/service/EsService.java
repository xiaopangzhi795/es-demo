/**
 * From geek45.com
 * Email to : rubixgeek795@gmail.com
 */
package com.geek45.esdemo.service;

import com.geek45.esdemo.commons.model.EsSearchModel;

import java.io.IOException;

/**
 * @ClassName: EsService
 * @Decription:
 * @Author: rubik
 * rubik create EsService.java of 2022/1/17 3:36 下午
 */
public interface EsService {

    /**
     * 查询
     * @param query
     */
    void search(String query) throws IOException;


    /**
     * 条件查询
     * @param model
     */
    void search(EsSearchModel model) throws IOException;

    /**
     * 新增
     *
     * @param text
     */
    void add(String text) throws IOException;
}
