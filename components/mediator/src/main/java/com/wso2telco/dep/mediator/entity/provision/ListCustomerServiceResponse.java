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

public class ListCustomerServiceResponse {

	private ServiceList serviceList;

	public ServiceList getServiceList() {
		return serviceList;
	}

	public void setServiceList(ServiceList serviceList) {
		this.serviceList = serviceList;
	}

	public static class ServiceList {

		private ServiceInfo serviceInfo;
		private String resourceURL;

		public ServiceInfo getServiceInfo() {
			return serviceInfo;
		}

		public void setServiceInfo(ServiceInfo serviceInfo) {
			this.serviceInfo = serviceInfo;
		}

		public String getResourceURL() {
			return resourceURL;
		}

		public void setResourceURL(String resourceURL) {
			this.resourceURL = resourceURL;
		}

		public static class ServiceInfo {

			private String serviceCode;
			private String description;
			private String timestamp;

			public String getServiceCode() {
				return serviceCode;
			}

			public void setServiceCode(String serviceCode) {
				this.serviceCode = serviceCode;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getTimestamp() {
				return timestamp;
			}

			public void setTimestamp(String timestamp) {
				this.timestamp = timestamp;
			}
		}
	}
}
