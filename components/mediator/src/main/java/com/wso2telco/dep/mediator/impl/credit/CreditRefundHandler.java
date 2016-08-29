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

package com.wso2telco.dep.mediator.impl.credit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.utils.CarbonUtils;

import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.ResponseHandler;
import com.wso2telco.dep.mediator.internal.ApiUtils;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.service.impl.ValidateCreditRefund;
import com.wso2telco.dep.subscriptionvalidator.util.ValidatorUtils;

public class CreditRefundHandler implements CreditHandler {
	
	private Log log = LogFactory.getLog(CreditRefundHandler.class);
	
    private static final String API_TYPE = "credit";
    private OriginatingCountryCalculatorIDD occi;
    private ResponseHandler responseHandler;
    private CreditExecutor executor;
    private ApiUtils apiUtils;

    public CreditRefundHandler(CreditExecutor executor) {
        this.executor = executor;
        occi = new OriginatingCountryCalculatorIDD();
        responseHandler = new ResponseHandler();
        apiUtils = new ApiUtils();
    }

    @Override
    public boolean handle(MessageContext context) throws Exception {

    	String requestid = UID.getUniqueID(Type.CREDIT.getCode(), context, executor.getApplicationid());
        HashMap<String, String> jwtDetails = apiUtils.getJwtTokenDetails(context);
        OperatorEndpoint endpoint = null;
        String clientCorrelator = null;
        String requestResourceURL = executor.getResourceUrl();

        FileReader fileReader = new FileReader();
        String file = CarbonUtils.getCarbonConfigDirPath() + File.separator + FileNames.MEDIATOR_CONF_FILE.getFileName();
		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);
        String hub_gateway_id = mediatorConfMap.get("hub_gateway_id");
        log.debug("Hub / Gateway Id : " + hub_gateway_id);

        String appId = jwtDetails.get("applicationid");
        log.debug("Application Id : " + appId);
        String subscriber = jwtDetails.get("subscriber");
        log.debug("Subscriber Name : " + subscriber);
        JSONObject jsonBody = executor.getJsonBody();
        
        JSONObject objAmountTransaction = jsonBody.getJSONObject("refundRequest");
        
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
                
        String[] params = executor.getSubResourcePath().split("/");
        String endUserId= params[2];
        context.setProperty(MSISDNConstants.USER_MSISDN,endUserId);

        if (ValidatorUtils.getValidatorForSubscription(context).validate(context)) {

            endpoint = occi.getAPIEndpointsByMSISDN(endUserId.replace("tel:", ""), API_TYPE, executor.getSubResourcePath(), false,
                    executor.getValidoperators());
        }
        
        String sending_add = endpoint.getEndpointref().getAddress();
        log.info("sending endpoint found: " + sending_add);

        String responseStr = executor.makeCreditRequest(endpoint, sending_add, jsonBody.toString(), true, context, false);

        // Error Exception Correction
        String base = str_piece(str_piece(responseStr, '{', 2), ':', 1);

        String errorReturn = "\"" + "requestError" + "\"";

        executor.removeHeaders(context);

        if (base.equals(errorReturn)) {
            executor.handlePluginException(responseStr);
       }

        responseStr = responseHandler.creditRefundResponseContext(context, responseStr, clientCorrelator, requestResourceURL, requestid);
        
        //set response re-applied
        executor.setResponse(context, responseStr);

        return true;
    }

    @Override
    public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
        if (!httpMethod.equalsIgnoreCase("POST")) {
            ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 405);
            throw new Exception("Method not allowed");
        }

        IServiceValidate validator = new ValidateCreditRefund();
        validator.validateUrl(requestPath);
        validator.validate(jsonBody.toString());
        return true;
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
