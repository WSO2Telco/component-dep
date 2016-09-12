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

import java.util.List;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.internal.Type;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.service.USSDService;
import com.wso2telco.dep.operatorservice.model.OperatorSubscriptionDTO;

public class StopMOUSSDSubscriptionHandler implements USSDHandler  {

    private USSDExecutor executor;
    private USSDService dbService;

    public StopMOUSSDSubscriptionHandler(USSDExecutor ussdExecutor) {

        this.executor = ussdExecutor;
        dbService = new USSDService();
    }

    @Override
    public boolean validate(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext context) throws Exception {
        return false;
    }

    @Override
    public boolean handle(MessageContext context) throws Exception {
        if (executor.getHttpMethod().equalsIgnoreCase("DELETE")) {
            return deleteSubscriptions(context);
        }
        return false;

    }
    private boolean deleteSubscriptions(MessageContext context) throws Exception {
        UID.getUniqueID(Type.DELRETSUB.getCode(), context, executor.getApplicationid());

        String requestPath = executor.getSubResourcePath();
        String subid = requestPath.substring(requestPath.lastIndexOf("/") + 1);


        Integer axiataid = Integer.parseInt(subid.replaceFirst("sub", ""));
        List<OperatorSubscriptionDTO> domainsubs = (dbService.moUssdSubscriptionQuery(Integer.valueOf(axiataid)));

        String resStr = "";

        if(!domainsubs.isEmpty() && domainsubs != null){
            for (OperatorSubscriptionDTO subs : domainsubs) {
                 executor.makeDeleteRequest(new OperatorEndpoint(new EndpointReference(subs.getDomain()), subs
                        .getOperator()), subs.getDomain(), null, true, context, false);
            }
            new USSDService().moUssdSubscriptionDelete(Integer.valueOf(axiataid));
        }



        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("HTTP_SC", 204);
        executor.removeHeaders(context);

        return true;
    }
}
