/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shardingsphere.example.jdbc.poc.controller;

import io.shardingsphere.example.jdbc.poc.domain.Dictionary;
import io.shardingsphere.example.jdbc.poc.domain.Order;
import io.shardingsphere.example.jdbc.poc.domain.OrderItem;
import io.shardingsphere.example.jdbc.poc.domain.RequestResult;
import io.shardingsphere.example.jdbc.poc.service.POCService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * POC controller.
 *
 * @author zhaojun
 */
@RestController
@RequestMapping(value = "/poc")
public final class POCController {
    
    private final POCService pocService;
    
    @Autowired
    public POCController(final POCService pocService) {
        this.pocService = pocService;
    }
    
    /**
     * init.
     *
     * @return string
     * @throws SQLException SQL exception
     */
    @RequestMapping(value = "/init")
    public RequestResult init() throws SQLException {
        return pocService.initEnvironment();
    }
    
    @RequestMapping(value = "/insert/{count}")
    @SuppressWarnings("unchecked")
    public RequestResult insert(@PathVariable("count") int count) {
        RequestResult result = new RequestResult("OK");
        for (int i = 0; i < count; i++) {
            Order order = new Order();
            order.setUserId(i);
            order.setStatus("01");
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(i);
            orderItem.setStatus("01");
            result.add(pocService.insert(order, orderItem));
        }
        return result;
    }
    
    @RequestMapping(value = "/init/dict")
    @SuppressWarnings("unchecked")
    public RequestResult initDict() {
        RequestResult result = new RequestResult("OK");
        for (int i = 0; i < 10; i++) {
            Dictionary dictionary = new Dictionary();
            dictionary.setCode(StringUtils.leftPad(String.valueOf(i), 2, "0"));
            dictionary.setCodeName("name" + i);
            result.add(pocService.insert(dictionary));
        }
        return result;
    }
    
    @RequestMapping(value = "/query")
    public RequestResult query(final String sql, final boolean masterOnly) {
        if (masterOnly) {
            try (HintManager hintManager = HintManager.getInstance()) {
                hintManager.setMasterRouteOnly();
                return pocService.select(sql);
            }
        }
        return pocService.select(sql);
    }
    
    @RequestMapping(value = "/delete")
    public RequestResult delete(String sql) {
        return pocService.delete(sql);
    }
    
    @RequestMapping(value = "/update")
    public RequestResult update(String sql) {
        return pocService.update(sql);
    }
    
    @RequestMapping(value = "/clean")
    public RequestResult clear() throws SQLException {
        return pocService.cleanEnvironment();
    }
}
