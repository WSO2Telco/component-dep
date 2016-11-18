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
 * Message type represents the types of payment requests handled by the hub.
 */
public enum MessageType {

    PAYMENT_REQUEST(1, "Payment Request"),
    PAYMENT_RESPONSE(2, "Payment Response"),
    REFUND_REQUEST(3, "Refundrequest"),
    REFUND_RESPONSE(4, "RefundResponse");

    private int messageDid;
    private String desc;

    private MessageType(final int messageType, final String desc) {
        this.messageDid = messageType;
        this.desc = desc;
    }

    public int getMessageDid() {
        return messageDid;
    }

    public String getDesc() {
        return desc;
    }
}
