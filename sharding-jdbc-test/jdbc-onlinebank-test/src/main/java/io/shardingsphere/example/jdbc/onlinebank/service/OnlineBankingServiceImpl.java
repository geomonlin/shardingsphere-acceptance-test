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
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
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
import java.util.TreeMap;
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
            statement.execute(SQLConstant.dropCustomer);
            statement.execute(SQLConstant.dropAccount);
            statement.execute(SQLConstant.dropBill);
            statement.execute(SQLConstant.dropJournal);
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
            statement.execute(SQLConstant.truncateCustomer);
            statement.execute(SQLConstant.truncateAccount);
            statement.execute(SQLConstant.truncateBill);
            statement.execute(SQLConstant.truncateJournal);
        }
    }
    
    @Override
    public void transferMoney(final int count) throws SQLException {
        Map<String, Object> accounts = prepareAccountPair();
        TransactionTypeHolder.set(TransactionType.XA);
        try (Connection connection = dataSource.getConnection()) {
            for (int i = 0; i < count; i++) {
                connection.setAutoCommit(false);
                Long flowNo = insertJournal(connection, accounts);
                updateAccount(connection, accounts);
                insertBill(connection, flowNo, accounts);
                updateJournal(connection, flowNo, accounts);
                connection.commit();
            }
        }
    }
    
    private Map<String, Object> prepareAccountPair() throws SQLException {
        Map<String, Object> result = new HashMap<>();
        TransactionTypeHolder.set(TransactionType.LOCAL);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Long customerNo = insertCustomer(connection);
            result.put("debit_customer_no", customerNo);
            result.put("debit_account_no", insertAccount(connection, customerNo));
            customerNo = insertCustomer(connection);
            result.put("credit_customer_no", customerNo);
            result.put("credit_account_no", insertAccount(connection, customerNo));
            connection.commit();
        }
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
            debitStatement.setObject(2, accounts.get("debit_account_no"));
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
    public void checkDataConsistency() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Map<Long, Double> debitJournal = queryJournalAccount(connection, "select debitacc, sum(amount) from journal GROUP BY debitacc");
            Map<Long, Double> creditJournal = queryJournalAccount(connection, "select creditacc, sum(amount) from journal GROUP BY creditacc");
            if (checkDebitAccount(debitJournal, connection) && checkCreditAccount(creditJournal, connection)) {
                System.out.println("All check PASSED !!");
            } else {
                System.out.println("Check FAILED, some account data is not consistency !!");
            }
        }
    }
    
    private boolean checkDebitAccount(final Map<Long, Double> journalAccounts, final Connection connection) throws SQLException {
        boolean result = true;
        for (Map.Entry<Long, Double> entry : journalAccounts.entrySet()) {
            Double debitAmount = getAmount(entry, connection, "select sum(debitamount) from bill where account_no=?");
            Double accountRealAmount = getAmount(entry, connection, "select realtimeremain from account where account_no=?");
            if (debitAmount.equals(entry.getValue()) && accountRealAmount.equals(1000000 + debitAmount)) {
                System.out.println(String.format("check debit account [%s] PASSED", entry.getKey()));
            } else {
                System.out.println(String.format("check debit account [%s] is FAILED, amount:[%s]", entry.getKey(), entry.getValue()));
                result = false;
            }
        }
        return result;
    }
    
    private boolean checkCreditAccount(final Map<Long, Double> journalAccounts, final Connection connection) throws SQLException {
        boolean result = true;
        for (Map.Entry<Long, Double> entry : journalAccounts.entrySet()) {
            Double creditAmount = getAmount(entry, connection, "select sum(credityield) from bill where account_no=?");
            Double accountRealAmount = getAmount(entry, connection, "select realtimeremain from account where account_no=?");
            if (creditAmount.equals(entry.getValue()) && accountRealAmount.equals(1000000 - creditAmount)) {
                System.out.println(String.format("check credit account [%s] PASSED", entry.getKey()));
            } else {
                System.out.println(String.format("check credit account [%s] is FAILED, amount:[%s]", entry.getKey(), entry.getValue()));
                result = false;
            }
        }
        return result;
    }
    
    private Double getAmount(final Map.Entry<Long, Double> entry, final Connection connection, final String sql) throws SQLException {
        Double result = (double) 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(entry.getKey()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getDouble(1);
            }
        }
        return result;
    }
    
    private Map<Long, Double> queryJournalAccount(final Connection connection, final String sql) throws SQLException {
        Map<Long, Double> result = new TreeMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                result.put(resultSet.getLong(1), resultSet.getDouble(2));
            }
        }
        return result;
    }
    
}
