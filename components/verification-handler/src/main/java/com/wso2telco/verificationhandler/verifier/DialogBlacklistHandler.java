/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.verificationhandler.verifier;


import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.wso2.carbon.apimgt.gateway.handlers.Utils;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;

import com.wso2telco.datapublisher.DataPublisherConstants;
import com.wso2telco.datapublisher.NorthboundDataPublisherClient;

import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


// TODO: Auto-generated Javadoc
//This is the Handler class for Blacklist Numbers Module
/**
 * The Class DialogBlacklistHandler.
 */
//Handlers should extend AbstractHandler Class
public class DialogBlacklistHandler extends AbstractHandler implements ManagedLifecycle {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(DialogBlacklistHandler.class);
    
    /** The is initialized. */
    private static boolean isInitialized = false;
    
    /** The blacklist numbers. */
    private List<String> blacklistNumbers;
    
    /** The subscription list. */
    private List<String> subscriptionList;
    
    /** The data publisher client. */
    private NorthboundDataPublisherClient dataPublisherClient;
    
   
    //Initialize BlackList Numbers
    //Read Blacklist Numbers from database and store in memory
    /**
     * Intialize.
     *
     * @throws SQLException the SQL exception
     */
    //Initialization happens only once
    public void intialize() throws SQLException{
        
        
    }
    
    /* (non-Javadoc)
     * @see org.apache.synapse.rest.Handler#handleRequest(org.apache.synapse.MessageContext)
     */
    //Entry point for the blacklist Module
    public boolean handleRequest(MessageContext messageContext) {
        
        String resourceUrl=(String) messageContext.getProperty("REST_FULL_REQUEST_PATH");
        
        String acr = null;

        String api = APIUtil.getAPI(messageContext);

        if (api.equals(APINameConstant.PAYMENT)){
           acr = str_piece(resourceUrl,'/',4); 

        }
        
	
        else if(api.equals(APINameConstant.LOCATION)){

            //Retriveing MSISDN from the incoming request
        acr = str_piece(str_piece(resourceUrl, '=', 2),'&',1);

        }
       
        else{
            //nop
        }
        
 
        String userMSISDN = ACRModule.getMSISDNFromACR(acr);
        
        log.info(userMSISDN);
        
        try {
            //If blacklisted number error response is sent in the response


            if( isBlackListNumber(userMSISDN,api) ){
                hadleBlakListResponse(messageContext);
            }else{
                //This will let the request to go further down to backends
                //This will let the request to go further down to backends
            }
        } catch (SQLException ex) {
            Logger.getLogger(DialogBlacklistHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(DialogBlacklistHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    /**
     * Hadle blak list response.
     *
     * @param messageContext the message context
     */
    //Sending the Response back to Client 
    private void hadleBlakListResponse(MessageContext messageContext) {
        messageContext.setProperty(SynapseConstants.ERROR_CODE, "500");
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, "Internal Server Error. Blacklisted Number");
        int status = 500;
        OMElement faultPayload = getFaultPayload();

        org.apache.axis2.context.MessageContext axis2MC = ((Axis2MessageContext) messageContext).getAxis2MessageContext();
        try {
            RelayUtils.buildMessage(axis2MC);
        } catch (IOException e) {
            log.error("Error occurred while building the message", e);
        } catch (XMLStreamException e) {
            log.error("Error occurred while building the message", e);
        }
        axis2MC.setProperty(Constants.Configuration.MESSAGE_TYPE, "application/soap+xml");
        if (messageContext.isDoingPOX() || messageContext.isDoingGET()) {
            Utils.setFaultPayload(messageContext, faultPayload);
        } else {
            Utils.setSOAPFault(messageContext, "Client", "Authentication Failure", "Not a whitelisted Number");
        }

        messageContext.setProperty("error_message_type", "application/json");
        publishResponseData(messageContext, faultPayload);
        Utils.sendFault(messageContext, status);
    }

    /**
     * Checks if is black list number.
     *
     * @param msisdn the msisdn
     * @param apiName the api name
     * @return true, if is black list number
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    private boolean isBlackListNumber(String msisdn,String apiName) throws SQLException, NamingException{       
    	if (blacklistNumbers!=null) {
    		blacklistNumbers.clear();
		}
    	
    	blacklistNumbers = DatabaseUtils.ReadBlacklistNumbers(apiName);

        return  blacklistNumbers.contains(msisdn);
    }

    /**
     * Gets the fault payload.
     *
     * @return the fault payload
     */
    //Constructing the Payload
    private OMElement getFaultPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(APISecurityConstants.API_SECURITY_NS,
                APISecurityConstants.API_SECURITY_NS_PREFIX);
        OMElement payload = fac.createOMElement("fault", ns);

        OMElement errorCode = fac.createOMElement("code", ns);
        errorCode.setText("500");
        OMElement errorMessage = fac.createOMElement("message", ns);
        errorMessage.setText("Blacklisted Number");
        OMElement errorDetail = fac.createOMElement("description", ns);
        errorDetail.setText("Blacklisted Number");

        payload.addChild(errorCode);
        payload.addChild(errorMessage);
        payload.addChild(errorDetail);
        return payload;
    }
    
    /* (non-Javadoc)
     * @see org.apache.synapse.rest.Handler#handleResponse(org.apache.synapse.MessageContext)
     */
    public boolean handleResponse(MessageContext messageContext) {
        
        return true;
    }
    
    /**
     * Str_piece.
     *
     * @param str the str
     * @param separator the separator
     * @param index the index
     * @return the string
     */
    private static String str_piece(String str, char separator, int index) {
        String str_result = "";
        int count = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == separator) {
                count++;
                if(count == index) {
                    break;
                }
            }
            else {
                if(count == index-1) {
                    str_result += str.charAt(i);
                }
            }
        }
        return str_result;
    }

    /**
     * Publish response data.
     *
     * @param messageContext the message context
     * @param faultPayload the fault payload
     */
    private void publishResponseData(MessageContext messageContext, OMElement faultPayload) {
        if (dataPublisherClient == null) {
            dataPublisherClient = new NorthboundDataPublisherClient();
        }
        String xmlPayload = faultPayload.toString();
        String jsonBody = "";
        //convert xml response to json
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(xmlPayload);
            jsonBody = xmlJSONObj.toString(2);
        } catch (JSONException je) {
            log.info(je.toString());
        }
        //publish data to BAM
        messageContext.setProperty(DataPublisherConstants.MSISDN, messageContext.getProperty("UserMSISDN"));
        dataPublisherClient.publishResponse(messageContext, jsonBody);
    }
    

    /* (non-Javadoc)
     * @see org.apache.synapse.ManagedLifecycle#init(org.apache.synapse.core.SynapseEnvironment)
     */
    public void init(SynapseEnvironment synapseEnvironment) {
        
        
    }

    /* (non-Javadoc)
     * @see org.apache.synapse.ManagedLifecycle#destroy()
     */
    public void destroy() {
        
    }

}
