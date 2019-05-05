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

package io.shardingsphere.example.jdbc.onlinebank.service;

/**
 * SQL constant.
 *
 * @author zhaojun
 */
public class SQLConstant {
    
    public static final String createCustomer = "CREATE TABLE IF NOT EXISTS customer (" +
        "customer_no int primary key not null comment '客户号'," +
        "customer_name varchar(200) not null          comment '户名'," +
        "paper_type char(2) not null              comment '证件类型'," +
        "paper_no varchar(18) not null            comment '证件号码'," +
        "phone_no varchar(11) not null          comment '手机号'," +
        "addr varchar(500)                     comment '地址'," +
        "customer_state char(2) default '01'          comment '客户状态'," +
        "gmt_create datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6)  comment '创建时间'," +
        "gmt_modified datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)      comment '修改时间'," +
        "reserve1 varchar(500)                 comment '备用字段1'," +
        "reserve2 varchar(500)                 comment '备用字段2'," +
        "reserve3 varchar(500)                 comment '备用字段3'," +
        "reserve4 varchar(500)                 comment '备用字段4'," +
        "KEY idx_customer(customer_name)" +
        ") ";
    
    public static final String createAccount = "CREATE TABLE IF NOT EXISTS account (" +
        "account_no int not null comment '账号'," +
        "account_state char(1) default '1' not null  comment '账户状态'," +
        "customer_no int  not null              comment '客户号'," +
        "realtimeremain decimal(18,2) default 0 not null  comment '实时余额'," +
        "currency char(3) not null              comment '币种'," +
        "rate decimal(13,5) not null  default 1 comment '利率'," +
        "accnature char(1) default '1' not null comment '账号性质'," +
        "gmt_create datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6)  comment '创建时间'," +
        "gmt_modified datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)      comment '修改时间'," +
        "reserve1 varchar(500)                  comment '备用字段1'," +
        "reserve2 varchar(500)                  comment '备用字段2'," +
        "reserve3 varchar(500)                  comment '备用字段3'," +
        "reserve4 varchar(500)                  comment '备用字段4'," +
        "PRIMARY KEY(account_no, customer_no)," +
        "KEY idx_customer(customer_no) " +
        ")";
    
    public static final String createBill = "CREATE TABLE IF NOT EXISTS bill (" +
        "flowno varchar(19) not null             comment '流水号'," +
        "flowdate date not null                  comment '日期'," +
        "account_no varchar(16) not null              comment '账号'," +
        "debitamount decimal(18,2) not null      comment '借方发生额'," +
        "credityield decimal(18,2) not null      comment '贷方发生额'," +
        "customer_no int not null               comment '客户号'," +
        "abscode char(2)                         comment '摘要码'," +
        "note varchar(200)                       comment '附言'," +
        "gmt_create datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6)  comment '创建时间'," +
        "gmt_modified datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)      comment '修改时间'," +
        "reserve1 varchar(500)                   comment '备用字段1'," +
        "reserve2 varchar(500)                   comment '备用字段2'," +
        "reserve3 varchar(500)                   comment '备用字段3'," +
        "reserve4 varchar(500)                   comment '备用字段4'," +
        "PRIMARY KEY (account_no, flowno,customer_no)" +
        ")";
    
    public static final String createJournal = "CREATE TABLE IF NOT EXISTS journal (" +
        "flowno varchar(19)      not null      comment '流水号'," +
        "flowdate date not null                comment '日期'," +
        "amount decimal(18,2) not null         comment '发生额'," +
        "debitacc int not null         comment '借方账号'," +
        "creditacc int not null        comment '贷方账号'," +
        "errcode char(4)             comment '错误码'," +
        "state char(1)                         comment '状态'," +
        "gmt_create datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6)  comment '创建时间'," +
        "gmt_modified datetime(6)  not NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)      comment '修改时间'," +
        "reserve1 varchar(500)                 comment '备用字段1'," +
        "reserve2 varchar(500)                 comment '备用字段2'," +
        "reserve3 varchar(500)                 comment '备用字段3'," +
        "reserve4 varchar(500)                 comment '备用字段4'," +
        "PRIMARY KEY (flowno)" +
        ")";
    
    public static final String truncateCustomer = "truncate table customer";
    
    public static final String truncateAccount = "truncate table account";
    
    public static final String truncateBill = "truncate table bill";
    
    public static final String truncateJournal = "truncate table journal";
    
    public static final String dropCustomer = "drop table if exists customer";
    
    public static final String dropAccount = "drop table if exists account";
    
    public static final String dropBill = "drop table if exists bill";
    
    public static final String dropJournal = "drop table if exists journal";
}
