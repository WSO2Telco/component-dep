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
package com.wso2telco.dep.mediator.mediationrule;

import java.util.HashMap;

import org.apache.axis2.addressing.EndpointReference;


// TODO: Auto-generated Javadoc
/**
 * The Class OriginatingCountryCalculatorGeoLocation.
 */
public class OriginatingCountryCalculatorGeoLocation extends OriginatingCountryCalculator{

	/* (non-Javadoc)
	 * @see com.wso2telco.mediator.mediationrule.OriginatingCountryCalculator#initialize()
	 */
	public void initialize(){
		
		
	    
	    apiEprMap = new HashMap<String, EndpointReference>();
	    
	    apiEprMap.put("Dialog sms",
                             new EndpointReference(DialogSmsApiEndpoint));
	    apiEprMap.put("Dialog payment", new EndpointReference(DialogPaymentApiEndpoint));
            apiEprMap.put("Dialog location", new EndpointReference(DialogLocationApiEndpoint));
	    
	    apiEprMap.put("Celcom sms",
             new EndpointReference(CelcomSmsApiEndpoint));
	    
	    apiEprMap.put("Celcom payment", new EndpointReference(CelcomPaymentApiEndpoint));
            apiEprMap.put("Celcom location", new EndpointReference(CelcomLocationApiEndpoint));
            
            apiEprMap.put("Robi sms", new EndpointReference(RobiSmsApiEndpoint));	    
	    apiEprMap.put("Robi payment", new EndpointReference(RobiPaymentApiEndpoint));
            apiEprMap.put("Robi location", new EndpointReference(RobiLocationApiEndpoint));
 }
	
}
