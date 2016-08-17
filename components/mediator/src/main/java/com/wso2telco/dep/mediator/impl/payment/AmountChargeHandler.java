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
package com.wso2telco.dep.mediator.impl.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.entity.OparatorEndPointSearchDTO;
import com.wso2telco.dep.mediator.internal.AggregatorValidator;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.PaymentService;
import com.wso2telco.dep.mediator.util.APIType;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidatePaymentCharge;
import com.wso2telco.subscriptionvalidator.util.ValidatorUtils;

/**
 *
 * @author WSO2telco
 */
public class AmountChargeHandler implements PaymentHandler {

	private Log log = LogFactory.getLog(AmountChargeHandler.class);

	private static final String API_TYPE = "payment";

	private OriginatingCountryCalculatorIDD occi;

	private PaymentService paymentService;

	private ResponseHandler responseHandler;

	private PaymentExecutor executor;
	
	private ApiUtils apiUtils;
	
	private PaymentUtil paymentUtil;

	public AmountChargeHandler(PaymentExecutor executor) {
		this.executor = executor;
		occi = new OriginatingCountryCalculatorIDD();
		paymentService = new PaymentService();
		responseHandler = new ResponseHandler();
		apiUtils = new ApiUtils();
		paymentUtil = new PaymentUtil();
	}

	public boolean handle(MessageContext context) throws Exception {

		String requestid = UID.getUniqueID(Type.PAYMENT.getCode(), context,	executor.getApplicationid());
		
		HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(context);
        OperatorEndpoint endpoint = null;
        String clientCorrelator = null;
        String requestResourceURL = executor.getResourceUrl();

        FileReader fileReader = new FileReader();
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();
        		
        String hub_gateway_id = mediatorConfMap.get("hub_gateway_id");
        log.debug("Hub / Gateway Id : " + hub_gateway_id);

        String appId = jwtDetails.get("applicationid");
        log.debug("Application Id : " + appId);
        String subscriber = jwtDetails.get("subscriber");
        log.debug("Subscriber Name : " + subscriber);
		JSONObject jsonBody = executor.getJsonBody();
		
				
		String endUserId = jsonBody.getJSONObject("amountTransaction").getString("endUserId");
		String msisdn = endUserId.substring(5);
		context.setProperty(MSISDNConstants.USER_MSISDN, msisdn);
		//OperatorEndpoint endpoint = null;

		if (ValidatorUtils.getValidatorForSubscription(context).validate(context)) {
			OparatorEndPointSearchDTO searchDTO = new OparatorEndPointSearchDTO();
			searchDTO.setApi(APIType.PAYMENT);
			searchDTO.setContext(context);
			searchDTO.setIsredirect(false);
			searchDTO.setMSISDN(endUserId);
			searchDTO.setOperators(executor.getValidoperators());
			searchDTO.setRequestPathURL(executor.getSubResourcePath());

			endpoint = occi.getOperatorEndpoint(searchDTO); /*
															 * occi.
															 * getAPIEndpointsByMSISDN
															 * (
															 * endUserId.replace
															 * ("tel:", ""),
															 * API_TYPE,
															 * executor
															 * .getSubResourcePath
															 * (), false,
															 * executor
															 * .getValidoperators
															 * ());
															 */
		}

		String sending_add = endpoint.getEndpointref().getAddress();
		log.info("sending endpoint found: " + sending_add);

		// Check if Spend Limits are exceeded
		//checkSpendLimit(msisdn, endpoint.getOperator(), context);
		paymentUtil.checkSpendLimit(msisdn, endpoint.getOperator(), context);
		// routeToEndpoint(endpoint, mc, sending_add);
		// apend request id to client co-relator

		/*JSONObject clientclr = jsonBody.getJSONObject("amountTransaction");
		clientclr.put("clientCorrelator", clientclr.getString("clientCorrelator") + ":" + requestid);*/
		 JSONObject objAmountTransaction = jsonBody.getJSONObject("amountTransaction");
		if (!objAmountTransaction.isNull("clientCorrelator")) {
			clientCorrelator = nullOrTrimmed(objAmountTransaction.get(
					"clientCorrelator").toString());
		}

		if (clientCorrelator == null || clientCorrelator.equals("")) {

			log.debug("clientCorrelator not provided by application and hub/plugin generating clientCorrelator on behalf of application");
			String hashString = apiUtils.getHashString(jsonBody.toString());
			log.debug("hashString : " + hashString);
			objAmountTransaction.put("clientCorrelator", hashString + "-"
					+ requestid + ":" + hub_gateway_id + ":" + appId);
		} else {

			log.debug("clientCorrelator provided by application");
			objAmountTransaction.put("clientCorrelator", clientCorrelator + ":"
					+ hub_gateway_id + ":" + appId);
		}
			 
		JSONObject chargingdmeta = objAmountTransaction.getJSONObject(
				"paymentAmount").getJSONObject("chargingMetaData");

		boolean isaggrigator = paymentUtil.isAggregator(context);

		if (isaggrigator) {
			//JSONObject chargingdmeta = objAmountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");
			if (!chargingdmeta.isNull("onBehalfOf")) {
				new AggregatorValidator().validateMerchant(
						Integer.valueOf(executor.getApplicationid()),
						endpoint.getOperator(), subscriber,
						chargingdmeta.getString("onBehalfOf"));
			}
		}

		if ((!chargingdmeta.isNull("purchaseCategoryCode"))
				&& (!chargingdmeta.getString("purchaseCategoryCode").isEmpty())) {

		}
		// validate payment categoreis
		List<String> validCategoris = paymentService.getValidPayCategories();
		//validatePaymentCategory(chargingdmeta, validCategoris);
		paymentUtil.validatePaymentCategory(chargingdmeta, validCategoris);
		
		String responseStr = executor.makeRequest(endpoint, sending_add, jsonBody.toString(), true, context, false);
		// Payment Error Exception Correction
		String base = str_piece(str_piece(responseStr, '{', 2), ':', 1);

		String errorReturn = "\"" + "requestError" + "\"";

		executor.removeHeaders(context);
		if (base.equals(errorReturn)) {
			executor.handlePluginException(responseStr);
		}

		//responseStr = responseHandler.makePaymentResponse(responseStr, clientCorrelator, requestResourceURL, requestid);
		responseStr = responseHandler.makePaymentResponseContext(context, responseStr, clientCorrelator, requestResourceURL, requestid);
		// set response re-applied
		executor.setResponse(context, responseStr);

		return true;

	}

	@Override
	public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
		if (!httpMethod.equalsIgnoreCase("POST")) {
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		IServiceValidate validator = new ValidatePaymentCharge();
		validator.validateUrl(requestPath);
		validator.validate(jsonBody.toString());
		return true;
	}

	/**
	 * Check spend limit.
	 *
	 * @param msisdn
	 *            the msisdn
	 * @param operator
	 *            the operator
	 * @param mc
	 *            the mc
	 * @return true, if successful
	 * @throws AxataDBUtilException
	 *             the axata db util exception
	 */
	/*private boolean checkSpendLimit(String msisdn, String operator, MessageContext context) throws AxataDBUtilException {
		AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(context);
		String consumerKey = "";
		if (authContext != null) {
			consumerKey = authContext.getConsumerKey();
		}

		SpendLimitHandler spendLimitHandler = new SpendLimitHandler();
		if (spendLimitHandler.isMSISDNSpendLimitExceeded(msisdn)) {
			throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[] { "daily" });
		} else if (spendLimitHandler.isApplicationSpendLimitExceeded(consumerKey)) {
			throw new CustomException("POL1001","The %1 charging limit for this application has been exceeded",new String[] { "daily" });
		} else if (spendLimitHandler.isOperatorSpendLimitExceeded(operator)) {
			throw new CustomException("POL1001","The %1 charging limit for this operator has been exceeded",new String[] { "daily" });
		}
		return true;
	}*/

	/**
	 * Store subscription.
	 *
	 * @param context
	 *            the context
	 * @return the string
	 * @throws AxisFault
	 *             the axis fault
	 */
	/*private String storeSubscription(MessageContext context) throws AxisFault {
		String subscription = null;

		org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context).getAxis2MessageContext();
		Object headers = axis2MessageContext.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
		if (headers != null && headers instanceof Map) {
			try {
				Map headersMap = (Map) headers;
				String jwtparam = (String) headersMap.get("x-jwt-assertion");
				String[] jwttoken = jwtparam.split("\\.");
				String jwtbody = Base64Coder.decodeString(jwttoken[1]);
				JSONObject jwtobj = new JSONObject(jwtbody);
				subscription = jwtobj.getString("http://wso2.org/claims/subscriber");

			} catch (JSONException ex) {
				throw new AxisFault("Error retriving application id");
			}
		}

		return subscription;
	}*/

	/**
	 * Checks if is aggregator.
	 *
	 * @param context
	 *            the context
	 * @return true, if is aggregator
	 * @throws AxisFault
	 *             the axis fault
	 */
	/*private boolean isAggregator(MessageContext context) throws AxisFault {
		boolean aggregator = false;

		try {
			org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context).getAxis2MessageContext();
			Object headers = axis2MessageContext.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
			if (headers != null && headers instanceof Map) {
				Map headersMap = (Map) headers;
				String jwtparam = (String) headersMap.get("x-jwt-assertion");
				String[] jwttoken = jwtparam.split("\\.");
				String jwtbody = Base64Coder.decodeString(jwttoken[1]);
				JSONObject jwtobj = new JSONObject(jwtbody);
				String claimaggr = jwtobj.getString("http://wso2.org/claims/role");
				if (claimaggr != null) {
					String[] allowedRoles = claimaggr.split(",");
					for (int i = 0; i < allowedRoles.length; i++) {
						if (allowedRoles[i].contains(MSISDNConstants.AGGRIGATOR_ROLE)) {
							aggregator = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("Error retrive aggregator");
		}

		return aggregator;
	}*/

	/**
	 * Validate payment category.
	 *
	 * @param chargingdmeta
	 *            the chargingdmeta
	 * @param lstCategories
	 *            the lst categories
	 * @throws JSONException
	 *             the JSON exception
	 */
	/*private void validatePaymentCategory(JSONObject chargingdmeta, List<String> lstCategories) throws JSONException {
		boolean isvalid = false;
		String chargeCategory = "";
		if ((!chargingdmeta.isNull("purchaseCategoryCode"))	&& (!chargingdmeta.getString("purchaseCategoryCode").isEmpty())) {

			chargeCategory = chargingdmeta.getString("purchaseCategoryCode");
			for (String d : lstCategories) {
				if (d.equalsIgnoreCase(chargeCategory)) {
					isvalid = true;
					break;
				}
			}
		} else {
			isvalid = true;
		}

		if (!isvalid) {
			throw new CustomException("POL0001","A policy error occurred. Error code is %1",new String[] { "Invalid " + "purchaseCategoryCode : "+ chargeCategory });
		}
	}
*/
	/**
	 * Str_piece.
	 *
	 * @param str
	 *            the str
	 * @param separator
	 *            the separator
	 * @param index
	 *            the index
	 * @return the string
	 */
	private String str_piece(String str, char separator, int index) {
		String str_result = "";
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == separator) {
				count++;
				if (count == index) {
					break;
				}
			} else {
				if (count == index - 1) {
					str_result += str.charAt(i);
				}
			}
		}
		return str_result;
	}
	
	public static String nullOrTrimmed(String s) {
		String rv = null;
		if (s != null && s.trim().length() > 0) {
			rv = s.trim();
		}
		return rv;
	}
	 

}
