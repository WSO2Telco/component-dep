/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.operatorservice.model;

// TODO: Auto-generated Javadoc

/**
 * The Class Operator.
 */
public class Operator {

    /**
     * The operator id.
     */
    private int operatorId;

    /**
     * The operator name.
     */
    private String operatorName;


    /**
     * The refreshtoken.
     */
    String refreshtoken;

    /**
     * The tokenvalidity.
     */
    long tokenvalidity;

    /**
     * The tokentime.
     */
    long tokentime;

    /**
     * The token.
     */
    String token;

    /**
     * The tokenurl.
     */
    String tokenurl;

    /**
     * The tokenauth.
     */
    String tokenauth;


    /**
     * The operator description.
     */
    private String operatorDescription;

    /**
     * Gets the operator id.
     *
     * @return the operator id
     */
    public int getOperatorId() {
        return operatorId;
    }

    /**
     * Sets the operator id.
     *
     * @param operatorId the new operator id
     */
    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * Gets the operator name.
     *
     * @return the operator name
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * Sets the operator name.
     *
     * @param operatorName the new operator name
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * Gets the operator description.
     *
     * @return the operator description
     */
    public String getOperatorDescription() {
        return operatorDescription;
    }

    /**
     * Sets the operator description.
     *
     * @param operatorDescription the new operator description
     */
    public void setOperatorDescription(String operatorDescription) {
        this.operatorDescription = operatorDescription;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public long getTokenvalidity() {
        return tokenvalidity;
    }

    public void setTokenvalidity(long tokenvalidity) {
        this.tokenvalidity = tokenvalidity;
    }

    public long getTokentime() {
        return tokentime;
    }

    public void setTokentime(long tokentime) {
        this.tokentime = tokentime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenurl() {
        return tokenurl;
    }

    public void setTokenurl(String tokenurl) {
        this.tokenurl = tokenurl;
    }

    public String getTokenauth() {
        return tokenauth;
    }

    public void setTokenauth(String tokenauth) {
        this.tokenauth = tokenauth;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ \"operator\" : \"" + operatorName + "\",");
        builder.append("\"id\" : " + operatorId);
        builder.append("}");
        return builder.toString();
    }
}

