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

package io.shardingsphere.example.jdbc.onlinebank.controller;

import io.shardingsphere.example.jdbc.onlinebank.service.OnlineBankingService;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * Online banking controller.
 *
 * @author zhaojun
 */
@RestController
@RequestMapping(value = "/jdbc")
public final class OnlineBankingController {
    
    private final OnlineBankingService onlineBankingService;
    
    @Autowired
    public OnlineBankingController(final OnlineBankingService onlineBankingService) {
        this.onlineBankingService = onlineBankingService;
    }
    
    /**
     * init.
     *
     * @return string
     * @throws SQLException SQL exception
     */
    @RequestMapping(value = "/init")
    public String init() throws SQLException {
        onlineBankingService.initEnvironment();
        return "ok";
    }
    
    /**
     * Transfer money.
     * @param type transaction type
     * @param count execute count
     * @return string
     * @throws SQLException SQL exception
     */
    @RequestMapping(value = "/transfer/{type}/{count}")
    public String transferMoney(final @PathVariable("count") int count, final @PathVariable("type") String type) throws SQLException {
        onlineBankingService.transferMoney(TransactionType.valueOf(type.toUpperCase()), count);
        return "ok";
    }
    
    @RequestMapping(value = "/check")
    public String checkData() throws SQLException {
        onlineBankingService.checkDataConsistency();
        return "ok";
    }
    
    /**
     * clean.
     *
     * @return string
     * @throws SQLException SQL exception
     */
    @RequestMapping(value = "/clean")
    public String clean() throws SQLException {
        onlineBankingService.cleanEnvironment();
        return "ok";
    }
}
