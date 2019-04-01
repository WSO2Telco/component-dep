/*******************************************************************************
 * Copyright (c) 2015-2019, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.apihandler;

import com.wso2telco.dep.apihandler.util.APIManagerDBUtil;
import com.wso2telco.dep.apihandler.util.ServicesHolder;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.transport.passthru.PassThroughConstants;
import org.wso2.carbon.apimgt.gateway.handlers.Utils;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.OAuthAdminService;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.config.RealmConfiguration;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.xml.bind.DatatypeConverter;
import java.util.Map;

public class ApiInvocationHandler extends AbstractHandler {

    private static final Log log = LogFactory.getLog(ApiInvocationHandler.class);
    private UserStoreManager userStoreManager;
    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer ";
    private static final String TOKEN_TYPE_BASIC = "Basic ";
    private static final String APP_CONSUMER_KEY = "app_consumer_key";
    private static final String SP_TOKEN_CACHE = "spTokenCache";

    public ApiInvocationHandler() {
        try {
            RealmConfiguration config = new RealmConfiguration();
            userStoreManager = ServicesHolder.getInstance().getRealmService().getUserRealm(config).getUserStoreManager();
        } catch (UserStoreException e) {
            String errorMsg = "Error while initiating UserStoreManager";
            log.error(errorMsg, e);
        }
    }

    @Override
    public boolean handleRequest(org.apache.synapse.MessageContext messageContext) {
        Map headerMap = (Map) ((Axis2MessageContext) messageContext).getAxis2MessageContext()
                .getProperty("TRANSPORT_HEADERS");

        if (!headerMap.containsKey(AUTH_HEADER)) {
            return true;
        }

        if (headerMap.get(AUTH_HEADER).toString().contains("Bearer")) {
            return true;
        }

        try {
            handleAPIWise(messageContext);
        } catch (UserStoreException e) {
            log.error(getFaultPayload(), e);
            handleAuthFailure(messageContext, e);
            return false;
        } catch (Exception ex) {
            log.error("Authentication Failure");
        }
        return true;
    }

    private void handleAPIWise(org.apache.synapse.MessageContext messageContext) throws UserStoreException {
        Map headerMap = (Map) ((Axis2MessageContext) messageContext).getAxis2MessageContext()
                .getProperty("TRANSPORT_HEADERS");

        handleAuthRequest(headerMap.get(AUTH_HEADER), headerMap, messageContext);
    }

    private void handleAuthRequest(Object authorization, Map headerMap, MessageContext messageContext) throws UserStoreException {

        String username = getUserNamePassword((String) authorization, 0);
        String password = getUserNamePassword((String) authorization, 1);
        String consumerKey;
        boolean authStatus;

        try {
            PrivilegedCarbonContext.startTenantFlow();
            authStatus = userStoreManager.authenticate(username, password);

            if (authStatus) {
                PrivilegedCarbonContext.getThreadLocalCarbonContext().setUsername(username);
                PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain("carbon.super");
                PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(-1234);
                OAuthAdminService oAuthAdminService = new OAuthAdminService();
                OAuthConsumerAppDTO[] oauthapps = oAuthAdminService.getAllOAuthApplicationData();

                if (oauthapps != null && oauthapps.length > 0) {
                    consumerKey = oauthapps[0].getOauthConsumerKey();
                    PrivilegedCarbonContext.endTenantFlow();
                    messageContext.setProperty(APP_CONSUMER_KEY, consumerKey);
                    processTokenResponse(headerMap, consumerKey);
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("No Application Found for User" + username);
                    }
                }
            } else {
                throw new UserStoreException("Invalid Credentials");
            }
        } catch (IdentityOAuthAdminException e) {
            log.debug("Service Error");
        }

    }

    private String getUserNamePassword(String basicAuth, int index) {
        byte[] valueDecoded = null;
        if (basicAuth.startsWith(TOKEN_TYPE_BASIC)) {
            valueDecoded = DatatypeConverter.parseBase64Binary(basicAuth.split(TOKEN_TYPE_BASIC)[1]);
        }
        String decodeString = new String(valueDecoded);
        return decodeString.split(":")[index];
    }

    private void processTokenResponse(Map headerMap, String clientId) {
        String token = null;

        Cache spToken = Caching.getCacheManager(APIConstants.API_MANAGER_CACHE_MANAGER).getCache(SP_TOKEN_CACHE);

        if (spToken.containsKey(clientId)) {
            token = (String) spToken.get(clientId);
        } else {
            token = APIManagerDBUtil.getTokenDetailsFromAPIManagerDB(clientId).getAccessToken();
            if (token != null && !token.isEmpty()) {
                spToken.put(clientId, token);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Bearer Token :  " + token);
        }

        headerMap.put(AUTH_HEADER, TOKEN_TYPE + token);
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }

    private void handleAuthFailure(MessageContext messageContext, Exception e) {
        messageContext.setProperty(SynapseConstants.ERROR_CODE, e);
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, e);
        messageContext.setProperty(SynapseConstants.ERROR_EXCEPTION, e);

        org.apache.axis2.context.MessageContext axis2MC = ((Axis2MessageContext) messageContext).
                getAxis2MessageContext();
        Mediator sequence = messageContext.getSequence(APISecurityConstants.API_AUTH_FAILURE_HANDLER);
        // Invoke the custom error handler specified by the user
        if (sequence != null && !sequence.mediate(messageContext)) {
            // If needed user should be able to prevent the rest of the fault handling
            // logic from getting executed
            return;
        }
        // This property need to be set to avoid sending the content in pass-through pipe (request message)
        // as the response.
        axis2MC.setProperty(PassThroughConstants.MESSAGE_BUILDER_INVOKED, Boolean.TRUE);
        axis2MC.setProperty(Constants.Configuration.MESSAGE_TYPE, "application/soap+xml");
        int status;

        status = HttpStatus.SC_UNAUTHORIZED;

        if (messageContext.isDoingPOX() || messageContext.isDoingGET()) {
            Utils.setFaultPayload(messageContext, getFaultPayload());
        } else {
            Utils.setSOAPFault(messageContext, "Client", "Authentication Failure", e.getMessage());
        }
        axis2MC.setProperty("messageType", "application/json");
        Utils.sendFault(messageContext, status);
    }

    private OMElement getFaultPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace("Failed", "failed");
        OMElement payload = fac.createOMElement("fault", ns);

        OMElement errorCode = fac.createOMElement("code", ns);
        errorCode.setText("401");
        OMElement errorMessage = fac.createOMElement("message", ns);
        errorMessage.setText("Authentication failure");
        OMElement errorDetail = fac.createOMElement("description", ns);
        errorDetail.setText("Authentication failure");

        payload.addChild(errorCode);
        payload.addChild(errorMessage);
        payload.addChild(errorDetail);
        return payload;
    }
}
