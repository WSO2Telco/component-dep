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

package com.wso2telco.dep.mediator.impl.ussd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.SubscriptionResponse;
import com.wso2telco.dep.mediator.entity.smsmessaging.CallbackReference;
import com.wso2telco.dep.mediator.entity.ussd.*;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.service.USSDService;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;

public class NBUSSDSubscriptionHandler implements USSDHandler {
	
	private Log log = LogFactory.getLog(NBUSSDSubscriptionHandler.class);
    private static final String API_TYPE ="ussd";
    private OriginatingCountryCalculatorIDD occi;
    private USSDExecutor executor;
    private USSDService ussdService;

    public NBUSSDSubscriptionHandler(USSDExecutor ussdExecutor){

        occi = new OriginatingCountryCalculatorIDD();
        this.executor = ussdExecutor;
        ussdService = new USSDService();
    }


    @Override
    public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
        return false;
    }

    @Override
    public boolean handle(MessageContext context) throws Exception {

        UID.getUniqueID(Type.MO_USSD.getCode(), context, executor.getApplicationid());
        JSONObject jsonBody = executor.getJsonBody();
        String notifyUrl = jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").getString("notifyURL");
        Gson gson = new GsonBuilder().serializeNulls().create();

        FileReader fileReader = new FileReader();
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();

        //Integer subscriptionId = ussdService.ussdRequestEntry(notifyUrl);
        AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(context);
        String consumerKey = "";
        String userId="";
        if (authContext != null) {
            consumerKey = authContext.getConsumerKey();
            userId=authContext.getUsername();
        }

        //Integer subscriptionId = ussdService.ussdRequestEntry(notifyUrl,consumerKey);
        String operatorId="";
        Integer subscriptionId = ussdService.ussdRequestEntry(notifyUrl,consumerKey,operatorId,userId);
        log.info("created axiataId  -  " + subscriptionId);

        String subsEndpoint = mediatorConfMap.get("ussdGatewayEndpoint")+subscriptionId;
        log.info("Subsendpoint - " +subsEndpoint);

        jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").put("notifyURL", subsEndpoint);

        List<OperatorEndpoint> endpoints = occi.getAPIEndpointsByApp(API_TYPE, executor.getSubResourcePath(),
                executor.getValidoperators());

        executor.removeHeaders(context);

        String responseStr ="";

        List<OperatorSubscriptionDTO> operatorsubses = new ArrayList<OperatorSubscriptionDTO>();
        JSONObject jObject = null ;
        SubscriptionHubRequest subscriptionHubRequest = gson.fromJson(jsonBody.toString(),SubscriptionHubRequest.class);
        ShortCodes[] shortCodes = subscriptionHubRequest.getSubscription().getShortCodes();

        SubscriptionGatewayRequest subscriptionGatewayRequest = new SubscriptionGatewayRequest();
        SubscriptionGatewayRequestDTO subscriptionGatewayRequestDTO = new SubscriptionGatewayRequestDTO();
        CallbackReference callbackReference = new CallbackReference();
        SubscriptionHubResponse subscriptionHubResponse = new SubscriptionHubResponse();
        Subscription subscription = new Subscription();
        SubscriptionResponse subscription_rsponse = null;
        Map<ShortCodes,Boolean> endpointToOperator=new HashMap<ShortCodes,Boolean>();

                for (OperatorEndpoint endpoint : endpoints) {

                    for (ShortCodes shortCodeObj:shortCodes){
                        endpointToOperator.put(shortCodeObj,false);

                        if (shortCodeObj.getOperatorCode().equalsIgnoreCase(endpoint.getOperator())){

                            endpointToOperator.put(shortCodeObj,true);
                            subscriptionGatewayRequestDTO.setClientCorrelator(jsonBody.getJSONObject("subscription").getString("clientCorrelator"));
                            subscriptionGatewayRequestDTO.setKeyword(shortCodeObj.getKeyword());
                            subscriptionGatewayRequestDTO.setShortCode(shortCodeObj.getShortCode());
                            callbackReference.setCallbackData(jsonBody.getJSONObject("subscription").getJSONObject("callbackReference").getString("callbackData"));
                            callbackReference.setNotifyURL(subsEndpoint);
                            subscriptionGatewayRequestDTO.setCallbackReference(callbackReference);
                            subscriptionGatewayRequest.setSubscription(subscriptionGatewayRequestDTO);
                            String jsonb = gson.toJson(subscriptionGatewayRequest);

                            operatorId=ussdService.getOperatorIdByOperator(endpoint.getOperator());
                            ussdService.updateOperatorIdBySubscriptionId(subscriptionId,operatorId);
                            
                            responseStr = executor.makeRequest(endpoint, endpoint.getEndpointref().getAddress(), jsonb, true, context, false);
                            executor.handlePluginException(responseStr);

                            if(responseStr != null){

                                subscription_rsponse = gson.fromJson(responseStr, SubscriptionResponse.class);
                                operatorsubses.add(new OperatorSubscriptionDTO(endpoint.getOperator(), subscription_rsponse.getSubscription().getResourceURL()));
                                callbackReference.setCallbackData(subscription_rsponse.getSubscription().getCallbackReference().getCallbackData());
                                callbackReference.setNotifyURL(notifyUrl);
                                subscription.setCallbackReference(callbackReference);

                                if(subscription_rsponse.getSubscription() != null){
                                    shortCodeObj.setStatus("Created");
                                    shortCodeObj.setShortCode(subscription_rsponse.getSubscription().getShortCode());
                                } else {
                                    shortCodeObj.setStatus("Not Created");
                                }
                            } else {
                                shortCodeObj.setStatus("failed:");
                            }
                            subscription.setShortCodeses(shortCodes);
                        }
                    }
            }

        for ( Map.Entry<ShortCodes,Boolean> entry : endpointToOperator.entrySet()) {
            if (entry.getValue()!= true) {
                ShortCodes shortcode = entry.getKey();
                shortcode.setStatus("Not Provisioned");
                subscription.setShortCodeses(shortCodes);
            }
        }

        subscription.setClientCorrelator(subscription_rsponse.getSubscription().getClientCorrelator());
        subscription.setResourceURL(mediatorConfMap.get("hubGateway")+executor.getResourceUrl()+"/"+subscriptionId);
        subscriptionHubResponse.setSubscription(subscription);
        jObject  = new JSONObject(subscriptionHubResponse);

        ussdService.moUssdSubscriptionEntry(operatorsubses, subscriptionId);

        String responseobj =jObject.toString();
        executor.setResponse(context, responseobj);
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");

        return true;
    }



}
