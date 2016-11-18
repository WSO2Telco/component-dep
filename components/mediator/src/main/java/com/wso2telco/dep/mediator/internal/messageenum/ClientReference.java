/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.mediator.internal.messageenum;

/**
 * Client reference represents the referent code types a message can have.
 */
public enum ClientReference {

    PAYMENT_REQUEST_REFCODE("referenceCode"),
    PAYMENT_RESPONSE_REFCODE("serverReferenceCode");

    private String refCode;

    private ClientReference(String code) {
        this.refCode = code;
    }

    public String getRefCode() {
        return refCode;

    }
}
