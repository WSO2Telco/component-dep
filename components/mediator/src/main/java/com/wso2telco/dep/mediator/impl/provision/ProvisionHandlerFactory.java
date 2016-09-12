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
package com.wso2telco.dep.mediator.impl.provision;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;

public class ProvisionHandlerFactory {

	private static Log log = LogFactory.getLog(ProvisionHandlerFactory.class);

	public static ProvisionHandler createProvisionHandler(String resourceURL, ProvisionExecutor executor) {

		ProvisionHandler provisionHandler = null;
		String httpMethod = executor.getHttpMethod();

		log.debug("incoming json string in createProvisionHandler : " + executor.getJsonBody().toString());
		log.debug("incoming http method in createProvisionHandler : " + httpMethod);

		if (httpMethod.equalsIgnoreCase("post")) {

			log.debug("invoking provision service handler");
			provisionHandler = new ProvisionServiceHandler(executor);
		} else if (httpMethod.equalsIgnoreCase("get")) {

			if (resourceURL.contains("applicable")) {

				log.debug("invoking query applicable service handler");
				provisionHandler = new QueryApplicableServiceHandler(executor);
			} else if (resourceURL.contains("active")) {

				log.debug("invoking list service by customer handler");
				provisionHandler = new ListCustomerServiceHandler(executor);
			} else {

				log.debug("api type not found in createPaymentHandler");
				throw new CustomException("SVC0002", "", new String[] { null });
			}
		} else if (httpMethod.equalsIgnoreCase("delete")) {

			log.debug("invoking remove service handler");
			provisionHandler = new RemoveServiceHandler(executor);
		} else {

			log.debug("api type not found in createPaymentHandler");
			throw new CustomException("SVC0002", "", new String[] { null });
		}

		return provisionHandler;
	}
}
