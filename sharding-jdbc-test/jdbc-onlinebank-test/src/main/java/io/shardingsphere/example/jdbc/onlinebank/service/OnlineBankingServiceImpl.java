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

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Online banking service implement.
 *
 * @author zhaojun
 */
@Component
public final class OnlineBankingServiceImpl implements OnlineBankingService {
    
    private final DataSource dataSource;
    
    private final AtomicLong id = new AtomicLong();
    
    private final SnowflakeShardingKeyGenerator keyGenerator = new SnowflakeShardingKeyGenerator();
    
    @Autowired
    public OnlineBankingServiceImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void initEnvironment() throws SQLException {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(SQLConstant.createCustomer);
            statement.execute(SQLConstant.createAccount);
            statement.execute(SQLConstant.createJournal);
            statement.execute(SQLConstant.createBill);
        }
    }
    
    @Override
    public void cleanEnvironment() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SQLConstant.dropCustomer);
            statement.execute(SQLConstant.dropAccount);
            statement.execute(SQLConstant.dropBill);
            statement.execute(SQLConstant.dropJournal);
        }
    }
    
    @Override
    public void transferMoney(final int count) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Map<String, Object> accounts = getAccountsPair(connection);
            for (int i = 0; i < count; i++) {
                Long flowNo = insertJournal(connection, accounts);
                updateAccount(connection, accounts);
                insertBill(connection, flowNo, accounts);
                updateJournal(connection, flowNo, accounts);
            }
        }
    }
    
    private Map<String, Object> getAccountsPair(final Connection connection) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        Long customerNo = insertCustomer(connection);
        result.put("debit_customer_no", customerNo);
        result.put("debit_account_no", insertAccount(connection, customerNo));
        customerNo = insertCustomer(connection);
        result.put("credit_customer_no", customerNo);
        result.put("credit_account_no", insertAccount(connection, customerNo));
        return result;
    }
    
    private Long insertCustomer(final Connection connection) throws SQLException {
        Long result = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLConstant.insertCustomer, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, id.incrementAndGet());
            preparedStatement.setObject(2, keyGenerator.generateKey());
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getLong(1);
                }
            }
        }
        return result;
    }
    
    private Long insertAccount(final Connection connection, final Long customerNo) throws SQLException {
        Long result = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLConstant.insertAccount, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, customerNo);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getLong(1);
                }
            }
        }
        return result;
    }
    
    private Long insertJournal(final Connection connection, final Map<String, Object> accounts) throws SQLException {
        Long result = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLConstant.insertJournal, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, accounts.get("debit_account_no"));
            preparedStatement.setObject(2, accounts.get("credit_account_no"));
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getLong(1);
                }
            }
        }
        return result;
    }
    
    private void updateAccount(final Connection connection, final Map<String, Object> accounts) throws SQLException {
        try (PreparedStatement debitStatement = connection.prepareStatement(SQLConstant.updateDebitAccount);
            PreparedStatement creditStatement = connection.prepareStatement(SQLConstant.updateCreditAccount)) {
            debitStatement.setObject(1, accounts.get("debit_account_no"));
            debitStatement.setObject(2, accounts.get("debit_customer_no"));
            debitStatement.execute();
            creditStatement.setObject(1, accounts.get("credit_account_no"));
            creditStatement.setObject(2, accounts.get("credit_customer_no"));
            creditStatement.execute();
        }
    }
    
    private void insertBill(final Connection connection, final Long flowNo, final Map<String, Object> accounts) throws SQLException {
        try (PreparedStatement debitStatement = connection.prepareStatement(SQLConstant.insertDebitBill);
             PreparedStatement creditStatement = connection.prepareStatement(SQLConstant.insertCreditBill)) {
            debitStatement.setObject(1, flowNo);
            debitStatement.setObject(2, accounts.get("debit_customer_no"));
            debitStatement.setObject(3, accounts.get("debit_customer_no"));
            debitStatement.execute();
            creditStatement.setObject(1, flowNo);
            creditStatement.setObject(2, accounts.get("credit_account_no"));
            creditStatement.setObject(3, accounts.get("credit_customer_no"));
            creditStatement.execute();
        }
    }
    
    private void updateJournal(final Connection connection, final Long flowNo, final Map<String, Object> accounts) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLConstant.updateJournal)) {
            preparedStatement.setObject(1, flowNo);
            preparedStatement.setObject(2, accounts.get("debit_account_no"));
            preparedStatement.setObject(3, accounts.get("credit_account_no"));
            preparedStatement.execute();
        }
    }
    
    @Override
    public void checkDataConsistency() {
    
    }
}
