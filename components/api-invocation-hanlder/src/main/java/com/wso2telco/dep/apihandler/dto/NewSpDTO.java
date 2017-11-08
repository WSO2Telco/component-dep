/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.apihandler.dto;
import java.util.List;

public class NewSpDTO {

    private String ownerId;
    private static final String TOKEN_URL = "https://localhost:8243/token";
    private static final long DEFAULT_CONNECTION_RESET_TIME = 4000;
    private static final int RETRY_ATTEMPT = 3;
    private static final int RETRYMAX = 10;
    private static final int RETRYDELAY = 20000;
    private List<TokenDTO> spTokenList;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTokenUrl() {
        return TOKEN_URL;
    }

    public long getDefaultconnectionresettime() {
        return DEFAULT_CONNECTION_RESET_TIME;
    }

    public int getRetryAttmpt() {
        return RETRY_ATTEMPT;
    }

    public int getRetrymax() {
        return RETRYMAX;
    }

    public int getRetrydelay() {
        return RETRYDELAY;
    }

    public List<TokenDTO> getSpTokenList() {
        return spTokenList;
    }

    public void setSpTokenList(List<TokenDTO> spTokenList) {
        this.spTokenList = spTokenList;
    }
}
