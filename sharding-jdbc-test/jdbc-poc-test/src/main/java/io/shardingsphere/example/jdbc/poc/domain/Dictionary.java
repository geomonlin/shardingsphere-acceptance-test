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

import java.io.Serializable;

public class Dictionary implements Serializable {
    
    private static final long serialVersionUID = 661434701950670670L;
    
    private long dictId;
    
    private String code;
    
    private String codeName;
    
    private String remark;
    
    public long getDictId() {
        return dictId;
    }
    
    public void setDictId(long dictId) {
        this.dictId = dictId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCodeName() {
        return codeName;
    }
    
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}

