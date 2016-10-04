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
package com.wso2telco.dep.verificationhandler.verifier;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.rest.AbstractHandler;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.XML;


// TODO: Auto-generated Javadoc
//Handlers should extend AbstractHandler Class
/**
 * The Class DialogPaymentHandler.
 */
//This is the Handler class for Payment Module
public class PaymentHandler extends AbstractHandler implements ManagedLifecycle {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(PaymentHandler.class);
    
    /* (non-Javadoc)
     * @see org.apache.synapse.rest.Handler#handleRequest(org.apache.synapse.MessageContext)
     */
    //Entry point for the Payment Module
    public boolean handleRequest(MessageContext messageContext) {
        try {
            
            //Building JSON Payload
            Mediator sequence = messageContext.getSequence("_build_");
            sequence.mediate(messageContext);

            
            String msisdn = null;
            
            String chargingInformation = null;
            
            //Retriving the application name from message context
            String appName = messageContext.getProperty("api.ut.application.name").toString();
          
            //Retriving the username from message context
            String userName = messageContext.getProperty("api.ut.userId").toString();
            
            org.json.JSONObject jsonObj = null;
            
            try {
                jsonObj = XML.toJSONObject(messageContext.getEnvelope().getBody().toString());
            } catch (org.json.JSONException ex) {
                Logger.getLogger(PaymentHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          
            try {
                org.json.JSONObject jsonBody = jsonObj.getJSONObject("soapenv:Body");
                
                try {
                    
                    //Retriving the msisdn from json payload
                    msisdn = jsonBody.getJSONObject("amountTransaction").getString("endUserId");
                   // msisdn = jsonBody.getJSONObject("amountTransaction").set
                    
                    JSONObject jsonObjAmount = new JSONObject(jsonBody.getJSONObject("amountTransaction").getString("paymentAmount"));
                    
                    //Retriving the Charging Amount
                    chargingInformation = jsonObjAmount.getJSONObject("chargingInformation").getString("amount");
 
                    
                } catch (JSONException ex) {
                    Logger.getLogger(PaymentHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            } catch (org.json.JSONException ex) {
                Logger.getLogger(PaymentHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            //Saving the amount to Database
            DatabaseUtils.writeAmount(userName,appName,chargingInformation,msisdn);
            
            } catch (SQLException ex) {
                Logger.getLogger(PaymentHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(PaymentHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        return true;
    }
      
    /* (non-Javadoc)
     * @see org.apache.synapse.rest.Handler#handleResponse(org.apache.synapse.MessageContext)
     */
    public boolean handleResponse(MessageContext messageContext) {
        return true;
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
