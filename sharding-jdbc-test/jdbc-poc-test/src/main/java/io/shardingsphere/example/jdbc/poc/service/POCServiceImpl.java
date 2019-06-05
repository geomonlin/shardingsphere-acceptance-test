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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * POC service implement.
 *
 * @author zhaojun
 */
@Component
public class POCServiceImpl implements POCService {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public POCServiceImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public RequestResult initEnvironment() {
        jdbcTemplate.execute(SQLConstant.DROP_T_ORDER);
        jdbcTemplate.execute(SQLConstant.DROP_T_ORDER_ITEM);
        jdbcTemplate.execute(SQLConstant.CREATE_T_ORDER);
        jdbcTemplate.execute(SQLConstant.CREATE_T_ORDER_ITEM);
        return RequestResult.ok();
    }
    
    @Override
    public RequestResult cleanEnvironment() {
        jdbcTemplate.execute(SQLConstant.TRUNCATE_T_ORDER);
        jdbcTemplate.execute(SQLConstant.TRUNCATE_T_ORDER_ITEM);
        return RequestResult.ok();
    }
    
    @Override
    public RequestResult insert(final Order order, final OrderItem orderItem) {
        insertOrder(order);
        orderItem.setOrderId(order.getOrderId());
        insertItem(orderItem);
        return createRequestResult(orderItem);
    }
    
    @Override
    public RequestResult select(final String sql) {
        RequestResult<Order> result = new RequestResult<>("OK");
        result.getDetails().addAll(jdbcTemplate.queryForList(sql, Order.class));
        return result;
    }
    
    @Override
    public RequestResult delete(final String sql) {
        return RequestResult.ok();
    }
    
    @Override
    public RequestResult update(final String sql) {
        return RequestResult.ok();
    }
    
    @SuppressWarnings("unchecked")
    private RequestResult createRequestResult(final OrderItem orderItem) {
        Map<String, Object> newRecord = new HashMap<>();
        newRecord.put("order_id", orderItem.getOrderId());
        newRecord.put("order_item_id", orderItem.getOrderItemId());
        RequestResult<Map<String, Object>> result = RequestResult.ok();
        result.getDetails().add(newRecord);
        result.getSql().addAll(Arrays.asList(SQLConstant.INSERT_T_ORDER, SQLConstant.INSERT_T_ORDER_ITEM));
        return result;
    }
    
    private void insertOrder(final Order order) {
        final PreparedStatementCreator orderPrepareStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement result = connection.prepareStatement(SQLConstant.INSERT_T_ORDER, Statement.RETURN_GENERATED_KEYS);
                result.setInt(1, order.getUserId());
                result.setString(2, order.getStatus());
                return result;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(orderPrepareStatementCreator, holder);
        order.setOrderId(holder.getKey().longValue());
    }
    
    private void insertItem(final OrderItem orderItem) {
        final PreparedStatementCreator orderPrepareStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement result = connection.prepareStatement(SQLConstant.INSERT_T_ORDER_ITEM, Statement.RETURN_GENERATED_KEYS);
                result.setLong(1, orderItem.getOrderId());
                result.setInt(2, orderItem.getUserId());
                result.setString(3, orderItem.getStatus());
                return result;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(orderPrepareStatementCreator, holder);
        orderItem.setOrderItemId(holder.getKey().longValue());
    }
}
