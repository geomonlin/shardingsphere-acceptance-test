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
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public RequestResult insert() {
        return RequestResult.ok();
    }
    
    @Override
    public RequestResult select(final String sql) {
        return RequestResult.ok();
    }
    
    @Override
    public RequestResult delete(final String sql) {
        return RequestResult.ok();
    }
    
    @Override
    public RequestResult update(final String sql) {
        return RequestResult.ok();
    }
    
    final void insertItem(final PreparedStatement preparedStatement, final OrderItem orderItem) throws SQLException {
        preparedStatement.setLong(1, orderItem.getOrderId());
        preparedStatement.setInt(2, orderItem.getUserId());
        preparedStatement.setString(3, orderItem.getStatus());
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (resultSet.next()) {
                orderItem.setOrderItemId(resultSet.getLong(1));
            }
        }
    }
    
    final void insertOrder(final PreparedStatement preparedStatement, final Order order) throws SQLException {
        preparedStatement.setInt(1, order.getUserId());
        preparedStatement.setString(2, order.getStatus());
        preparedStatement.executeUpdate();
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (resultSet.next()) {
                order.setOrderId(resultSet.getLong(1));
            }
        }
    }
}
