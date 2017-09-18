/*******************************************************************************
 * Copyright (c) 2015-2017, WSO2.Telco Inc. (http://www.wso2telco.com)
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
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;

import com.wso2telco.dep.apihandler.dto.AddNewSpDTO;
import com.wso2telco.dep.apihandler.dto.TokenDTO;
import com.wso2telco.dep.apihandler.util.APIManagerDBUtil;
import com.wso2telco.dep.apihandler.util.ReadPropertyFile;
import com.wso2telco.dep.apihandler.util.TokenPoolUtil;

public class ApiInvocationHandler extends AbstractHandler {

    private static final Log log = LogFactory.getLog(ApiInvocationHandler.class);
    public static final String NEW_LINE = System.getProperty("line.separator");
    private static final String TOKEN_POOL_ENABLED = "enable_token_pool";
    private static final String AUTH_ENDPOINT = "oauth2/authorize";
    private static final String TOKEN_ENDPOINT = "oauth2/token";
    private static final String USERINFO_ENDPOINT = "oauth2/userinfo";
    private static final String AUTH_HEADER = "Authorization";
    private static final String TEMP_AUTH_HEADER = "tempAuthVal";
    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public boolean handleRequest(MessageContext messageContext) {
        Map headerMap = (Map) ((Axis2MessageContext) messageContext).getAxis2MessageContext().getProperty(
                org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        String fullPath = (String) ((Axis2MessageContext) messageContext).getProperty("REST_FULL_REQUEST_PATH");
        String clientId = getClientIdForAuthorizeCall(fullPath);
        if (clientId == null)
            clientId = getClientId(fullPath, headerMap, messageContext);
        return processResponse(clientId, headerMap, fullPath);
    }

    private String getClientIdForAuthorizeCall(String fullPath) {
        String clientId = null;
        if (fullPath.contains(AUTH_ENDPOINT)) {
            if (log.isDebugEnabled()) {
                log.debug(AUTH_ENDPOINT);
            }
            clientId = fullPath.split("client_id=")[1].split("&")[0];

        }
        return clientId;
    }

    private String getClientId(String fullPath, Map headerMap, MessageContext messageContext) {
        String clientId = null;
        if (fullPath.contains(TOKEN_ENDPOINT) || fullPath.contains(USERINFO_ENDPOINT)) {
            String basicAuth = (String) headerMap.get(AUTH_HEADER);
            headerMap.remove(AUTH_HEADER);
            messageContext.setProperty(TEMP_AUTH_HEADER, basicAuth);
            clientId = getClientIdForTokenCall(fullPath, basicAuth);
            if (clientId == null)
                getClientIdForUserInfoCall(fullPath, basicAuth);

        }
        return clientId;

    }

    private String getClientIdForUserInfoCall(String fullPath, String basicAuth) {
        String accessToken = null;
        if (fullPath.contains(TOKEN_ENDPOINT)) {
            if (log.isDebugEnabled()) {
                log.debug(USERINFO_ENDPOINT);
            }
            accessToken = APIManagerDBUtil.getAccessTokenForUserInfo(basicAuth.split(" ")[1]);
        }
        return accessToken;

    }

    private String getClientIdForTokenCall(String fullPath, String basicAuth) {
        String clientId = null;
        if (fullPath.contains(TOKEN_ENDPOINT)) {
            if (log.isDebugEnabled()) {
                log.debug(TOKEN_ENDPOINT);
            }
            byte[] valueDecoded = Base64.decodeBase64(basicAuth.split(" ")[1].getBytes());
            String decodeString = new String(valueDecoded);
            clientId = decodeString.split(":")[0];
        }
        return clientId;
    }

    private boolean processResponse(String clientId, Map headerMap, String fullPath) {

        if (clientId == null)
            return false;

        else if (fullPath.contains(USERINFO_ENDPOINT)) {
            headerMap.put(AUTH_HEADER, TOKEN_TYPE + clientId);
            return true;

        }

        TokenDTO tokenDTO = APIManagerDBUtil.getTokenDetailsFromAPIManagerDB(clientId);
        headerMap.put(AUTH_HEADER, TOKEN_TYPE + tokenDTO.getAccessToken());
        handleByTokenPool(clientId, tokenDTO);
        return true;

    }

    private void handleByTokenPool(String clientId, TokenDTO tokenDTO) {
        Map<String, String> propertyMap = ReadPropertyFile.getPropertyFile();
        if (propertyMap.get(TOKEN_POOL_ENABLED).equals("true")) {
            if (log.isDebugEnabled()) {
                log.debug("TOken Pool Service Enabled for Insertion");
            }
            AddNewSpDTO newSpDto = new AddNewSpDTO();
            ArrayList<TokenDTO> tokenList = new ArrayList<TokenDTO>();

            newSpDto.setOwnerId(clientId);
            tokenDTO.setTokenAuth(getbase64EncodedTokenAouth(tokenDTO.getTokenAuth()));
            tokenList.add(tokenDTO);
            newSpDto.setSpTokenList(tokenList);
            callTokenPool(newSpDto);
        }
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }

    private void callTokenPool(final AddNewSpDTO newSpDto) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.execute(new Runnable() {
            public void run() {
                log.debug("Calling Token Pool Endpoint");

                TokenPoolUtil.callTokenPoolToAddSpToken(newSpDto);
            }
        });
        executorService.shutdown();
    }

    private String getbase64EncodedTokenAouth(String plainTextAouth) {
        byte[] bytesEncoded = Base64.encodeBase64(plainTextAouth.getBytes());
        String base64EncodedAouthString = new String(bytesEncoded);
        log.debug("ecncoded value is " + base64EncodedAouthString);
        return base64EncodedAouthString;
    }
}
