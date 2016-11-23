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
package com.wso2telco.dep.mediator.model;

import com.wso2telco.dep.mediator.internal.messageenum.ClientReference;

public class MessageDTO {
    private int msgId;
    private String mdtrequestId;
    private String clienString;
    private String message;
    private ClientReference refcode;
    private String refval;
    private long reportedTime;

    public String getMdtrequestId() {
        return mdtrequestId;
    }

    public void setMdtrequestId(String mdtrequestId) {
        this.mdtrequestId = mdtrequestId;
    }

    public String getClienString() {
        return clienString;
    }

    public void setClienString(String clienString) {
        this.clienString = clienString;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClientReference getRefcode() {
        return refcode;
    }

    public void setRefcode(ClientReference refcode) {
        this.refcode = refcode;
    }

    public String getRefval() {
        return refval;
    }

    public void setRefval(String refval) {
        this.refval = refval;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public long getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(long reportedTime) {
        this.reportedTime = reportedTime;
    }
}
