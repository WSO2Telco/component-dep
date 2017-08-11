package com.wso2telco.dep.authorize.token.handler;


import com.wso2telco.dep.authorize.token.handler.dto.AddNewSpDto;
import com.wso2telco.dep.authorize.token.handler.dto.TokenDTO;
import com.wso2telco.dep.authorize.token.handler.util.APIManagerDBUtil;
import com.wso2telco.dep.authorize.token.handler.util.TokenPoolUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sheshan on 8/4/17.
 */
public class AuthorizeTokenHandler extends AbstractHandler {

    private static final Log log = LogFactory.getLog(AuthorizeTokenHandler.class);

    @Override
    public boolean handleRequest(MessageContext messageContext) {
        ArrayList<TokenDTO> tokenList = new ArrayList<TokenDTO>();
        Map headerMap = (Map) ((Axis2MessageContext) messageContext).getAxis2MessageContext().
                getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        String[] message = messageContext.toString().split("client_id=");
        String[] clientIdString = message[1].split("&");
        String clientId = clientIdString[0];
        if(!clientId.equals(null)){
            AddNewSpDto newSpDto = new AddNewSpDto();
            newSpDto.setOwnerId(clientId);
            TokenDTO tokenDTO = APIManagerDBUtil.getTokenDetailsFromAPIManagerDB(newSpDto.getOwnerId());
            String base64EncodedAouthString = getbase64EncodedTokenAouth(tokenDTO.getTokenAuth());
            tokenDTO.setTokenAuth(base64EncodedAouthString);
            tokenList.add(tokenDTO);
            newSpDto.setSpTokenList(tokenList);
            headerMap.put("Authorization", "Bearer " + tokenDTO.getTokenAuth());
            callTokenPool(newSpDto);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        return true;
    }


    private void callTokenPool(final AddNewSpDto newSpDto){
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.execute(new Runnable() {
            public void run() {
                log.debug("Calling Token Pool Endpoint");
                TokenPoolUtil.callTokenPoolToAddSpToken(newSpDto);
            }
        });
        executorService.shutdown();
    }

    private String getbase64EncodedTokenAouth(String plainTextAouth){
        byte[]   bytesEncoded = Base64.encodeBase64(plainTextAouth .getBytes());
        String base64EncodedAouthString = new String(bytesEncoded );
        log.debug("ecncoded value is " + base64EncodedAouthString);
        return base64EncodedAouthString;
    }
}
