/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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

package com.wso2telco.dep.datapublisher;


class JsonPOJOforMO {
	private InboundSMSMessageNotification inboundSMSMessageNotification;

	public InboundSMSMessageNotification getInboundSMSMessageNotification() {
		return inboundSMSMessageNotification;
	}

	public void setInboundSMSMessageNotification(InboundSMSMessageNotification inboundSMSMessageNotification) {
		this.inboundSMSMessageNotification = inboundSMSMessageNotification;
	}



	@Override
	public String toString() {
		return "ClassPojoMO";
	}


	class InboundSMSMessageNotification {
	    private String callbackData = "";
	    
	    private InboundSMSMessage inboundSMSMessage;
	    
	    public String getcallbackData() {
	           return callbackData;
	    }
	
	    public void setCallbackData(String callbackData) {
	            this.callbackData = callbackData;
	    }
	   
	    public InboundSMSMessage getInboundSMSMessage() {
	            return inboundSMSMessage;
	    }
	    
	    public void setInboundSMSMessage (InboundSMSMessage inboundSMSMessage) {
	            this.inboundSMSMessage = inboundSMSMessage;
	    }
	    
	}
}