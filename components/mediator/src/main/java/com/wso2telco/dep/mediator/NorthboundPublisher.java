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
package com.wso2telco.dep.mediator;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.wso2telco.dep.mediator.util.DataPublisherConstants;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.exceptions.PolicyException;
import com.wso2telco.dep.oneapivalidation.exceptions.RequestError;
import com.wso2telco.dep.oneapivalidation.exceptions.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class NorthboundPublisher.
 */
public class NorthboundPublisher {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(NorthboundPublisher.class);

	/** The enabled. */
	private boolean enabled = true;// SouthboundDataComponent.getApiMgtConfigReaderService().isEnabled();

	/** The publisher class. */
	private String publisherClass = "com.wso2telco.datapublisher.SouthboundDataPublisher";

	/**
	 * Publish nb error response data.
	 *
	 * @param ax
	 *            the ax
	 * @param retStr
	 *            the ret str
	 * @param messageContext
	 *            the message context
	 */
	public void publishNBErrorResponseData(CustomException ax, String retStr, MessageContext messageContext) {

		// set properties for response data publisher
		boolean isPaymentReq = false;

		if (retStr != null && !retStr.isEmpty()) {

			// get serverReferenceCode property for payment API response
			JSONObject paymentRes = null;
			// get exception property for exception response
			JSONObject exception = null;

			try {

				JSONObject response = new JSONObject(retStr);
				paymentRes = response.optJSONObject("amountTransaction");

				if (paymentRes != null) {

					if (paymentRes.has("serverReferenceCode")) {

						messageContext.setProperty(DataPublisherConstants.OPERATOR_REF,
								paymentRes.optString("serverReferenceCode"));
					} else if (paymentRes.has("originalServerReferenceCode")) {

						messageContext.setProperty(DataPublisherConstants.OPERATOR_REF,
								paymentRes.optString("originalServerReferenceCode"));
					}

					isPaymentReq = true;
				}
			} catch (JSONException e) {

				log.error("Error in converting response to json. " + e.getMessage(), e);
			}
		}

		String exMsg = ax.getErrmsg() + " " + Arrays.toString(ax.getErrvar()).replace("[", "").replace("]", "");
		log.info("exception id: " + ax.getErrcode());
		log.info("exception message: " + exMsg);
		messageContext.setProperty(DataPublisherConstants.EXCEPTION_ID, ax.getErrcode());
		messageContext.setProperty(DataPublisherConstants.EXCEPTION_MESSAGE, exMsg);

		buildErrorResponse(ax);


	}

	/**
	 * Builds the error response.
	 *
	 * @param ax
	 *            the ax
	 * @return the string
	 */
	private String buildErrorResponse(CustomException ax) {
		String exceptionString = ""; // Build custom exception to normal
										// exception here
		if (ax.getErrcode().length() > 3) {
			if (ax.getErrcode().substring(0, 3).equals("POL")) {
				// Policy Exception
				PolicyException pol = new PolicyException(ax.getErrcode(), ax.getErrmsg(),
						Arrays.toString(ax.getErrvar()).replace("[", "").replace("]", ""));
				RequestError error = new RequestError();
				error.setPolicyException(pol);
				exceptionString = getErrorJSONString(error);
			} else if (ax.getErrcode().substring(0, 3).equals("SVC")) {
				// Service Exception
				ServiceException svc = new ServiceException(ax.getErrcode(), ax.getErrmsg(),
						Arrays.toString(ax.getErrvar()).replace("[", "").replace("]", ""));
				RequestError error = new RequestError();
				error.setServiceException(svc);
				exceptionString = getErrorJSONString(error);
			} else {
				exceptionString = ax.toString();
			}
		}
		return exceptionString;
	}

	/**
	 * Gets the error json string.
	 *
	 * @param error
	 *            the error
	 * @return the error json string
	 */
	private static String getErrorJSONString(RequestError error) {

		String errorObjectString = "";
		try {
			errorObjectString = new JSONObject(error).toString();
			JSONObject innerObj = new JSONObject(error);
			if (innerObj.isNull("serviceException")) {

				innerObj.remove("serviceException");
			} else if (innerObj.isNull("policyException")) {

				innerObj.remove("policyException");
			}
			errorObjectString = innerObj.toString();
		} catch (Exception e) {

			log.error("ERROR IN getErrorJSONString : ", e);
		}
		return errorObjectString;
	}

}
