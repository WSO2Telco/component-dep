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

import com.wso2telco.dep.datapublisher.DataPublisherClient;
import com.wso2telco.dep.datapublisher.DataPublisherConstants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


public class RequestHandlerMediator extends AbstractMediator {

    private DataPublisherClient publisherClient;

    /* (non-Javadoc)
     * @see org.apache.synapse.Mediator#mediate(org.apache.synapse.MessageContext)
     */
    public boolean mediate(MessageContext context) {

        log.info("This is handler mediator");
        String transactionId = UUID.randomUUID().toString();
        context.setProperty("uri.var.transactionId", transactionId);

        String restSubRequestPath = (String) context.getProperty("REST_SUB_REQUEST_PATH");
        String queryParamsPart = restSubRequestPath.substring(restSubRequestPath.indexOf('?') + 1);
        String[] queryParams = queryParamsPart.split("&");

        for(String queryParam : queryParams){
            String[] paramKeyValue = queryParam.split("=");
            context.setProperty("uri.var." + paramKeyValue[0], paramKeyValue[1]);
        }
        /*
        String operator = (String) context.getProperty("uri.var.operator");
        String url = "http://ids-host-name/oauth/authorize/operator/" + operator + "?transactionId=" + transactionId
                + queryParamsPart;

        publishRequestData(operator, url, null, context);
        */

        return true;
    }


    /**
     * Publish request data.
     *
     * @param operator
     *            the operator
     * @param url
     *            the url
     * @param requestStr
     *            the request str
     * @param messageContext
     *            the message context
     */
    private void publishRequestData(String operator, String url, String requestStr,
            MessageContext messageContext) {
        // set properties for request data publisher
        messageContext.setProperty(DataPublisherConstants.OPERATOR_ID, operator);
        messageContext.setProperty(DataPublisherConstants.SB_ENDPOINT, url);

        if (requestStr != null) {
            // get chargeAmount property for payment API request
            JSONObject paymentReq = null;
            try {
                paymentReq = new JSONObject(requestStr).optJSONObject("amountTransaction");
                if (paymentReq != null) {
                    String chargeAmount = paymentReq.getJSONObject("paymentAmount").getJSONObject("chargingInformation")
                            .optString("amount");
                    messageContext.setProperty(DataPublisherConstants.CHARGE_AMOUNT, chargeAmount);
                    String payCategory = paymentReq.getJSONObject("paymentAmount").getJSONObject("chargingMetaData")
                            .optString("purchaseCategoryCode");
                    messageContext.setProperty(DataPublisherConstants.PAY_CATEGORY, payCategory);
                }
            } catch (JSONException e) {
                log.error("Error in converting request to json. " + e.getMessage(), e);
            }
        }

        // publish data
        if (publisherClient == null) {
            publisherClient = new DataPublisherClient();
        }
        publisherClient.publishRequest(messageContext, requestStr);
    }

}
