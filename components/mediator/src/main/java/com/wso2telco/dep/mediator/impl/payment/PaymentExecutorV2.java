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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;
import com.wso2telco.core.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.RequestExecutor;
import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.internal.AggregatorValidator;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Base64Coder;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.PaymentService;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.impl.payment.ValidatePaymentCharge;
import com.wso2telco.dep.subscriptionvalidator.util.ValidatorUtils;

public class PaymentExecutorV2 extends RequestExecutor {

    private Log log = LogFactory.getLog(PaymentExecutor.class);

    private static final String API_TYPE = "payment-v2";
    private OriginatingCountryCalculatorIDD occi;
    private PaymentService dbservice;
    private ResponseHandler responseHandler;
    private ApiUtils apiUtils;
    private PaymentUtil paymentUtil;

    public PaymentExecutorV2() {
        occi = new OriginatingCountryCalculatorIDD();
        dbservice = new PaymentService();
        responseHandler = new ResponseHandler();
        apiUtils = new ApiUtils();
        paymentUtil = new PaymentUtil();
    }

    @Override
    public boolean execute(MessageContext context) throws CustomException, AxisFault, Exception {
        try {

            return chargeUserExec(context);
        } catch (JSONException e) {

            log.error(e.getMessage());
            throw new CustomException("SVC0001", "", new String[]{"Request is missing required URI components"});
        }
        //return false;
    }

    @Override
    public boolean validateRequest(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {

        if (!httpMethod.equalsIgnoreCase("POST")) {

            ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
            throw new Exception("Method not allowed");
        }

        ValidatePaymentCharge validator = new ValidatePaymentCharge();
        validator.validateUrl(requestPath);
        validator.validate(jsonBody.toString());

        return true;
    }

    private boolean chargeUserExec(MessageContext mc) throws Exception {

        String requestid = UID.getUniqueID(Type.PAYMENT.getCode(), mc, getApplicationid());
        HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(mc);

        JSONObject jsonBody = getJsonBody();
        String endUserId = jsonBody.getJSONObject("amountTransaction").getString("endUserId");
        String msisdn = endUserId.substring(5);
        String amount = jsonBody.getJSONObject("amountTransaction").getJSONObject("paymentAmount").getJSONObject("chargingInformation").getString("amount");
        mc.setProperty(MSISDNConstants.USER_MSISDN, msisdn);
        OperatorEndpoint endpoint = null;
        String clientCorrelator = null;
        String requestResourceURL = this.getResourceUrl();

        FileReader fileReader = new FileReader();
        String file = CarbonUtils.getCarbonConfigDirPath() + File.separator + FileNames.MEDIATOR_CONF_FILE.getFileName();
		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);
        
        String hub_gateway_id = mediatorConfMap.get("hub_gateway_id");
        log.debug("Hub / Gateway Id : " + hub_gateway_id);

        String appId = jwtDetails.get("applicationid");
        log.debug("Application Id : " + appId);
        String subscriber = jwtDetails.get("subscriber");
        log.debug("Subscriber Name : " + subscriber);

        if (ValidatorUtils.getValidatorForSubscription(mc).validate(mc)) {

            endpoint = occi.getAPIEndpointsByMSISDN(endUserId.replace("tel:", ""), API_TYPE, getSubResourcePath(), false,
                    getValidoperators());
        }

        String sending_add = endpoint.getEndpointref().getAddress();
        log.info("sending endpoint found: " + sending_add);

        // Check if Spend Limits are exceeded
     // checkSpendLimit(msisdn, endpoint.getOperator(), mc ,amount);

        //routeToEndpoint(endpoint, mc, sending_add);
        //apend request id to client co-relator
        JSONObject objAmountTransaction = jsonBody.getJSONObject("amountTransaction");

        if (!objAmountTransaction.isNull("clientCorrelator")) {

            clientCorrelator = nullOrTrimmed(objAmountTransaction.get("clientCorrelator").toString());
        }

        if (clientCorrelator == null || clientCorrelator.equals("")) {

            log.debug("clientCorrelator not provided by application and hub/plugin generating clientCorrelator on behalf of application");
            String hashString = apiUtils.getHashString(jsonBody.toString());
            log.debug("hashString : " + hashString);
            objAmountTransaction.put("clientCorrelator", hashString + "-" + requestid + ":" + hub_gateway_id + ":" + appId);
        } else {

            log.debug("clientCorrelator provided by application");
            objAmountTransaction.put("clientCorrelator", clientCorrelator + ":" + hub_gateway_id + ":" + appId);
        }

        //objAmountTransaction.put("clientCorrelator", objAmountTransaction.getString("clientCorrelator") + ":" + requestid);
        JSONObject chargingdmeta = objAmountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");

        boolean isaggrigator = isAggregator(mc);

        if (isaggrigator) {
            
            //JSONObject chargingdmeta = objAmountTransaction.getJSONObject("paymentAmount").getJSONObject("chargingMetaData");
            if (!chargingdmeta.isNull("onBehalfOf")) {
            	new AggregatorValidator().validateMerchant(Integer.valueOf(getApplicationid()), endpoint.getOperator(), subscriber, chargingdmeta.getString("onBehalfOf"));            }
        }

        //validate payment categoreis
        List<String> validCategoris = dbservice.getValidPayCategories();
        validatePaymentCategory(chargingdmeta, validCategoris);

        String responseStr = makeRequest(endpoint, sending_add, jsonBody.toString(), true, mc, false);

        // Payment Error Exception Correction
        String base = str_piece(str_piece(responseStr, '{', 2), ':', 1);

        String errorReturn = "\"" + "requestError" + "\"";

        removeHeaders(mc);

        if (base.equals(errorReturn)) {
            handlePluginException(responseStr);
        }

        responseStr = responseHandler.makePaymentResponse(responseStr, clientCorrelator, requestResourceURL, requestid);

        //set response re-applied
        setResponse(mc, responseStr);

        return true;
    }

    /*private String storeSubscription(MessageContext context) throws AxisFault {
     String subscription = null;

     org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
     .getAxis2MessageContext();
     Object headers = axis2MessageContext.getProperty(
     org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
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
    private boolean isAggregator(MessageContext context) throws AxisFault {
        boolean aggregator = false;

        try {
            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
                    .getAxis2MessageContext();
            Object headers = axis2MessageContext.getProperty(
                    org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
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

    private void validatePaymentCategory(JSONObject chargingdmeta, List<String> lstCategories) throws JSONException {
        boolean isvalid = false;
        String chargeCategory = "";
        if ((!chargingdmeta.isNull("purchaseCategoryCode")) && (!chargingdmeta.getString("purchaseCategoryCode")
                .isEmpty())) {

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
            throw new CustomException("POL0001", "A policy error occurred. Error code is %1", new String[]{"Invalid "
                + "purchaseCategoryCode : " + chargeCategory});
        }
    }

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
     * Ensure the input value is either a null value or a trimmed string
     */
    public static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }
}
