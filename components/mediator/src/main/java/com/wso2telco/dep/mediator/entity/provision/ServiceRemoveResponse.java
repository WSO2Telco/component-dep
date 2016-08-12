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
package com.wso2telco.dep.mediator.entity.provision;

public class ServiceRemoveResponse {

	private String serviceCode;
	private String clientCorrelator;
	private String clientReferenceCode;
	private String serverReferenceCode;
	private CallbackReference callbackReference;
	private String transactionStatus;

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getClientCorrelator() {
		return clientCorrelator;
	}

	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}

	public String getClientReferenceCode() {
		return clientReferenceCode;
	}

	public void setClientReferenceCode(String clientReferenceCode) {
		this.clientReferenceCode = clientReferenceCode;
	}

	public String getServerReferenceCode() {
		return serverReferenceCode;
	}

	public void setServerReferenceCode(String serverReferenceCode) {
		this.serverReferenceCode = serverReferenceCode;
	}

	public CallbackReference getCallbackReference() {
		return callbackReference;
	}

	public void setCallbackReference(CallbackReference callbackReference) {
		this.callbackReference = callbackReference;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
}
