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

/**
 * Request response.
 *
 * @author zhaojun
 */
public class RequestResult<T> {
    
    private String status;
    
    private T result;
    
    public RequestResult(final String status) {
        this.status = status;
    }
    
    /**
     * OK request result.
     *
     * @return request result
     */
    public static RequestResult ok() {
        return new RequestResult<>("OK");
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public T getResult() {
        return result;
    }
    
    public void setResult(final T result) {
        this.result = result;
    }
}
