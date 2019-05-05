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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Online banking service implement.
 *
 * @author zhaojun
 */
@Component
public final class OnlineBankingServiceImpl implements OnlineBankingService {
    
    private final DataSource dataSource;
    
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
    public void transferMoney() {
    
    }
    
    @Override
    public void checkDataConsistency() {
    
    }
}
