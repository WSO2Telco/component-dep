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

import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.RequestExecutor;
import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.internal.AggregatorValidator;
import com.wso2telco.dep.mediator.internal.Base64Coder;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateChargeReservation;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateListTransactions;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidatePaymentCharge;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateRefund;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateReleaseReservation;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateReserveAdditional;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateReserveAmount;
import com.wso2telco.publisheventsdata.handler.SpendLimitHandler;
import com.wso2telco.subscriptionvalidator.util.ValidatorUtils;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class PaymentExecutor.
 */
public class PaymentExecutor extends RequestExecutor {

	/** The log. */
	private Log log = LogFactory.getLog(PaymentExecutor.class);

	/** The Constant API_TYPE. */
	private static final String API_TYPE = "payment";

	/** The occi. */
	private OriginatingCountryCalculatorIDD occi;

	/** The dbservice. */
	private AxiataDbService dbservice;

	/** The response handler. */
	private ResponseHandler responseHandler;

	/**
	 * Instantiates a new payment executor.
	 */
	public PaymentExecutor() {
		occi = new OriginatingCountryCalculatorIDD();
		dbservice = new AxiataDbService();
		responseHandler = new ResponseHandler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.mediator.RequestExecutor#execute(org.apache.synapse.
	 * MessageContext)
	 */
	@Override
	public boolean execute(MessageContext context) throws CustomException, AxisFault, Exception {
		try {
			return chargeUserExec(context);
		} catch (JSONException e) {
			log.error(e.getMessage());
			throw new CustomException("SVC0001", "", new String[] { "Request is missing required URI components" });
		}
		// return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wso2telco.mediator.RequestExecutor#validateRequest(java.lang.String,
	 * java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
	 */
	@Override
	public boolean validateRequest(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context)
			throws Exception {
		if (!httpMethod.equalsIgnoreCase("POST")) {
			((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
			throw new Exception("Method not allowed");
		}

		int apiType = findPaymentAPIType(requestPath, jsonBody.toString());
		// Payment API types are as follow,
		// 100 - Charge, 101 - Refund, 102 - Reserve amount, 103 - Reserve
		// additional amount, 104 - Charge against reservation, 105 - Release
		// Reservation,
		// 106 - List transactions, 0 - Unsupported

		context.setProperty(DataPublisherConstants.OPERATION_TYPE, apiType);
		if (apiType == 100) {
			ValidatePaymentCharge validator = new ValidatePaymentCharge();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		} else if (apiType == 101) {
			ValidateRefund validator = new ValidateRefund();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		} else if (apiType == 102) {
			ValidateReserveAmount validator = new ValidateReserveAmount();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		} else if (apiType == 103) {
			ValidateReserveAdditional validator = new ValidateReserveAdditional();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		} else if (apiType == 104) {
			ValidateChargeReservation validator = new ValidateChargeReservation();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		} else if (apiType == 105) {
			ValidateReleaseReservation validator = new ValidateReleaseReservation();
			validator.validateUrl(requestPath);
			validator.validate(jsonBody.toString());
		} else if (apiType == 106) {
			ValidateListTransactions validator = new ValidateListTransactions();
			validator.validateUrl(requestPath);
			if (requestPath.startsWith("/")) {
				requestPath = requestPath.substring(1);
			}
			String[] requestParts = requestPath.split("/");
			validator.validate(requestParts);
		} else {
			throw new Exception("API Type Not found");
		}

		String categoryCode = "";
		String subCategory = "";
		String merchantId = "";

		JSONObject chargingdmeta = jsonBody.getJSONObject("amountTransaction").getJSONObject("paymentAmount")
				.getJSONObject("chargingMetaData");

		if ((!chargingdmeta.isNull("purchaseCategoryCode"))
				&& (!chargingdmeta.getString("purchaseCategoryCode").isEmpty())) {
			categoryCode = chargingdmeta.getString("purchaseCategoryCode");
		}
		if ((!chargingdmeta.isNull("onBehalfOf")) && (!chargingdmeta.getString("onBehalfOf").isEmpty())) {
			merchantId = chargingdmeta.getString("onBehalfOf");
		}
		// set merchantId and PaymentCategory default
		context.setProperty(DataPublisherConstants.MERCHANT_ID, merchantId);
		context.setProperty(DataPublisherConstants.CATEGORY, categoryCode);
		context.setProperty(DataPublisherConstants.SUB_CATEGORY, subCategory);

		if ((categoryCode.length() == 12) && (categoryCode.matches("[-+]?\\d*\\.?\\d+"))) {
			if (categoryCode.substring(0, 3).equals("000")) {
				context.setProperty(DataPublisherConstants.MERCHANT_ID, "");
			} else {
				context.setProperty(DataPublisherConstants.MERCHANT_ID, categoryCode.substring(0, 3));
			}

			if (categoryCode.substring(3, 6).equals("000")) {
				context.setProperty(DataPublisherConstants.CATEGORY, "");
			} else {
				context.setProperty(DataPublisherConstants.CATEGORY, categoryCode.substring(3, 6));
			}

			if (categoryCode.substring(6, 9).equals("000")) {
				context.setProperty(DataPublisherConstants.SUB_CATEGORY, "");
			} else {
				context.setProperty(DataPublisherConstants.SUB_CATEGORY, categoryCode.substring(6, 9));
			}
		}

		return true;
	}

	/**
	 * Charge user exec.
	 *
	 * @param mc
	 *            the mc
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean chargeUserExec(MessageContext mc) throws Exception {
		String requestid = UID.getUniqueID(Type.PAYMENT.getCode(), mc, getApplicationid());
		JSONObject jsonBody = getJsonBody();
		String endUserId = jsonBody.getJSONObject("amountTransaction").getString("endUserId");
		String msisdn = endUserId.substring(5);
		mc.setProperty(MSISDNConstants.USER_MSISDN, msisdn);
		OperatorEndpoint endpoint = null;
		if (ValidatorUtils.getValidatorForSubscription(mc).validate(mc)) {
			endpoint = occi.getAPIEndpointsByMSISDN(endUserId.replace("tel:", ""), API_TYPE, getSubResourcePath(),
					false, getValidoperators());
		}
		String sending_add = endpoint.getEndpointref().getAddress();
		log.info("sending endpoint found: " + sending_add);

		// Check if Spend Limits are exceeded

		// ##################
		// checkSpendLimit(msisdn, endpoint.getOperator(), mc);

		// routeToEndpoint(endpoint, mc, sending_add);
		// apend request id to client co-relator
		JSONObject clientclr = jsonBody.getJSONObject("amountTransaction");
		clientclr.put("clientCorrelator", clientclr.getString("clientCorrelator") + ":" + requestid);

		JSONObject chargingdmeta = clientclr.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");

		String subscriber = storeSubscription(mc);
		boolean isaggrigator = isAggregator(mc);

		if (isaggrigator) {
			// JSONObject chargingdmeta =
			// clientclr.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");
			if (!chargingdmeta.isNull("onBehalfOf")) {
				new AggregatorValidator().validateMerchant(Integer.valueOf(getApplicationid()), endpoint.getOperator(),
						subscriber, chargingdmeta.getString("onBehalfOf"));
				mc.setProperty(DataPublisherConstants.MERCHANT_ID, chargingdmeta.getString("onBehalfOf"));
			}
		}

		if ((!chargingdmeta.isNull("purchaseCategoryCode"))
				&& (!chargingdmeta.getString("purchaseCategoryCode").isEmpty())) {

			mc.setProperty(DataPublisherConstants.CATEGORY, chargingdmeta.getString("purchaseCategoryCode"));
		}
		// validate payment categoreis
		List<String> validCategoris = dbservice.getValidPayCategories();
		validatePaymentCategory(chargingdmeta, validCategoris);

		String responseStr = makeRequest(endpoint, sending_add, jsonBody.toString(), true, mc);
		// Payment Error Exception Correction
		String base = str_piece(str_piece(responseStr, '{', 2), ':', 1);

		String errorReturn = "\"" + "requestError" + "\"";

		removeHeaders(mc);

		if (base.equals(errorReturn)) {
			handlePluginException(responseStr);
		}

		responseStr = responseHandler.makePaymentResponse(responseStr, requestid);

		// set response re-applied
		setResponse(mc, responseStr);

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
	private boolean checkSpendLimit(String msisdn, String operator, MessageContext mc) throws AxataDBUtilException {
		AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(mc);
		String consumerKey = "";
		if (authContext != null) {
			consumerKey = authContext.getConsumerKey();
		}

		SpendLimitHandler spendLimitHandler = new SpendLimitHandler();
		if (spendLimitHandler.isMSISDNSpendLimitExceeded(msisdn)) {
			throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded",
					new String[] { "daily" });
		} else if (spendLimitHandler.isApplicationSpendLimitExceeded(consumerKey)) {
			throw new CustomException("POL1001", "The %1 charging limit for this application has been exceeded",
					new String[] { "daily" });
		} else if (spendLimitHandler.isOperatorSpendLimitExceeded(operator)) {
			throw new CustomException("POL1001", "The %1 charging limit for this operator has been exceeded",
					new String[] { "daily" });
		}
		return true;
	}

	/**
	 * Store subscription.
	 *
	 * @param context
	 *            the context
	 * @return the string
	 * @throws AxisFault
	 *             the axis fault
	 */
	private String storeSubscription(MessageContext context) throws AxisFault {
		String subscription = null;

		org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
				.getAxis2MessageContext();
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
	}

	/**
	 * Checks if is aggregator.
	 *
	 * @param context
	 *            the context
	 * @return true, if is aggregator
	 * @throws AxisFault
	 *             the axis fault
	 */
	private boolean isAggregator(MessageContext context) throws AxisFault {
		boolean aggregator = false;

		try {
			org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
					.getAxis2MessageContext();
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
	}

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
	private void validatePaymentCategory(JSONObject chargingdmeta, List<String> lstCategories) throws JSONException {
		boolean isvalid = false;
		String chargeCategory = "";
		if ((!chargingdmeta.isNull("purchaseCategoryCode"))
				&& (!chargingdmeta.getString("purchaseCategoryCode").isEmpty())) {

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
			throw new CustomException("POL0001", "A policy error occurred. Error code is %1",
					new String[] { "Invalid " + "purchaseCategoryCode : " + chargeCategory });
		}
	}

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

	/**
	 * Find payment api type.
	 *
	 * @param requestURL
	 *            the request url
	 * @param jsonPayload
	 *            the json payload
	 * @return the int
	 */
	private int findPaymentAPIType(String requestURL, String jsonPayload) {
		String transactionOperationStatus = null;
		// String requestString = null;
		int apiType = 0;

		if (requestURL.contains("amountReservation")) {
			String parts[] = requestURL.split("/transactions/");
			String urlParts[] = parts[1].split("/");

			if (urlParts.length == 1) {
				// apiType = "reserveAmount";
				apiType = 102;
			} else if (urlParts.length == 2) {
				JSONObject objJSONObject;
				try {
					objJSONObject = new JSONObject(jsonPayload);
					JSONObject objAmountReservationTransaction = (JSONObject) objJSONObject
							.get("amountReservationTransaction");

					if (objAmountReservationTransaction.get("transactionOperationStatus") != null) {
						transactionOperationStatus = nullOrTrimmed(
								objAmountReservationTransaction.get("transactionOperationStatus").toString());
						if (transactionOperationStatus.equalsIgnoreCase("Reserved")) {
							// apiType = "reserveAdditionalAmount";
							apiType = 103;
						} else if (transactionOperationStatus.equalsIgnoreCase("Charged")) {
							// apiType = "chargeAgainstReservation";
							apiType = 104;
						} else if (transactionOperationStatus.equalsIgnoreCase("Released")) {
							// apiType = "releaseReservation";
							apiType = 105;
						} else {
							// throw new Exception("API Type Not found");
							apiType = 0;
						}
					} else {
						// throw new Exception("API Type Not found");
						apiType = 0;
					}
				} catch (Exception ex) {
					// logger.error("Sandbox controller - Manipulating recived
					// JSON Object: " + ex);
					// throw new Exception("API Type Not found");
					throw new CustomException("SVC0002", "", new String[] { null });
				}
			} else {
				// throw new Exception("API Type Not found");
				apiType = 0;
			}
		} else if (requestURL.contains("amount")) {

			try {
				JSONObject objJSONObject = new JSONObject(jsonPayload);
				JSONObject objAmountTransaction = (JSONObject) objJSONObject.get("amountTransaction");

				if (objAmountTransaction.get("transactionOperationStatus") != null) {
					transactionOperationStatus = nullOrTrimmed(
							objAmountTransaction.get("transactionOperationStatus").toString());
					if (transactionOperationStatus.equalsIgnoreCase("Charged")) {
						// apiType = "charge";
						apiType = 100;
					} else if (transactionOperationStatus.equalsIgnoreCase("Refunded")) {
						// apiType = "refund";
						apiType = 101;
					} else {
						apiType = 0;
					}
				} else {
					// throw new Exception("API Type Not found");
					apiType = 0;
				}
			} catch (Exception e) {
				// logger.error("Sandbox controller - Manipulating recived JSON
				// Object: " + e);
				// throw new Exception("API Type Not found");
				throw new CustomException("SVC0002", "", new String[] { null });
			}
		} else if (requestURL.contains("transactions")) {
			// apiType = "listTransactions";
			apiType = 106;
		} else {
			// throw new Exception("API Type Not found");
			apiType = 0;
		}
		return apiType;
	}

	/**
	 * Null or trimmed.
	 *
	 * @param s
	 *            the s
	 * @return the string
	 */
	private static String nullOrTrimmed(String s) {
		String rv = null;
		if (s != null && s.trim().length() > 0) {
			rv = s.trim();
		}
		return rv;
	}
}
