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

package io.shardingsphere.example.jdbc.poc.domain;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Request response.
 *
 * @author zhaojun
 */
public class RequestResult<T> {
    
    private String status;
    
    private Collection<String> sql = new LinkedHashSet<>();
    
    private Collection<T> details = new LinkedList<>();
    
    public RequestResult(final String status) {
        this.status = status;
    }
    
    @SuppressWarnings("unchecked")
    public void add(final RequestResult requestResult) {
        sql.addAll(requestResult.getSql());
        details.addAll(requestResult.getDetails());
    }
    
    /**
     * OK request result.
     *
     * @return request result
     */
    public static RequestResult ok() {
        return new RequestResult("OK");
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public Collection<T> getDetails() {
        return details;
    }
    
    public Collection<String> getSql() {
        return sql;
    }
}
