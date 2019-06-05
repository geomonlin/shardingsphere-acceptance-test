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

/**
 * SQL constant.
 *
 * @author zhaojun
 */
class SQLConstant {
    
    public static final String CREATE_T_ORDER = "CREATE TABLE IF NOT EXISTS t_order (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id))";
    
    public static final String CREATE_T_ORDER_ITEM = "CREATE TABLE IF NOT EXISTS t_order_item "
        + "(order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id))";
    
    public static final String INSERT_T_ORDER = "INSERT INTO t_order (user_id, status) VALUES (?, ?)";
    
    public static final String INSERT_T_ORDER_ITEM = "INSERT INTO t_order_item (order_id, user_id, status) VALUES (?, ?, ?)";
    
    public static final String DELETE_T_ORDER = "DELETE FROM t_order WHERE order_id=?";
    
    public static final String DELETE_T_ORDER_ITEM = "DELETE FROM t_order_item WHERE order_item_id=?";
    
    public static final String DROP_T_ORDER = "DROP TABLE IF EXISTS t_order";
    
    public static final String DROP_T_ORDER_ITEM = "DROP TABLE IF EXISTS t_order_item";
    
    public static final String TRUNCATE_T_ORDER = "TRUNCATE TABLE t_order";
    
    public static final String TRUNCATE_T_ORDER_ITEM = "TRUNCATE TABLE t_order_item";
}
