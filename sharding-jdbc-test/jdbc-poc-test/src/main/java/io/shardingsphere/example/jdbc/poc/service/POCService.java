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

package io.shardingsphere.example.jdbc.poc.service;

import io.shardingsphere.example.jdbc.poc.domain.Order;
import io.shardingsphere.example.jdbc.poc.domain.OrderItem;
import io.shardingsphere.example.jdbc.poc.domain.RequestResult;

import java.sql.SQLException;

/**
 * POC service.
 *
 * @author zhaojun
 */
public interface POCService {
    
    RequestResult initEnvironment() throws SQLException;
    
    RequestResult initDict() throws SQLException;
    
    RequestResult cleanEnvironment() throws SQLException;
    
    RequestResult insert(final Order order, final OrderItem item);
    
    RequestResult select(final String sql);
    
    RequestResult delete(final String sql);
    
    RequestResult update(final String sql);
}
