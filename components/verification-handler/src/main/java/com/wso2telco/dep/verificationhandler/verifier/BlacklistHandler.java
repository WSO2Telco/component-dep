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


import com.google.gson.Gson;
import com.wso2telco.dep.verificationhandler.model.smsmessaging.SMSMessagingRequestWrap;
import com.wso2telco.dep.verificationhandler.model.ussd.InboundUSSDMessageRequestWrap;
import com.wso2telco.dep.verificationhandler.model.ussd.OutboundUSSDRequestWrap;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.wso2.carbon.apimgt.gateway.handlers.Utils;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;

import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Auto-generated Javadoc
//This is the Mediator class for Blacklist Numbers Module

/**
 * The Class BlacklistHandler.
 */


public class BlacklistHandler extends AbstractHandler implements ManagedLifecycle {

	/**
	 * The Constant log.
	 */
	private static final Log log = LogFactory.getLog(BlacklistHandler.class);

	/**
	 * The is initialized.
	 */
	private static boolean isInitialized = false;

	/**
	 * The blacklist numbers.
	 */
	private List<String> blacklistNumbers;

	/**
	 * The subscription list.
	 */
	private List<String> subscriptionList;

	//Initialize BlackList Numbers
	//Read Blacklist Numbers from database and store in memory


	/**
	 * Intialize.
	 *
	 * @throws SQLException the SQL exception
	 */
	//Initialization happens only once
	public void intialize() throws SQLException {


	}

	/* (non-Javadoc)
	 * @see org.apache.synapse.rest.Handler#handleRequest(org.apache.synapse.MessageContext)
	 */
	//Entry point for the blacklist Module
	public boolean handleRequest(MessageContext messageContext) {

		String resourceUrl = (String) messageContext.getProperty("REST_FULL_REQUEST_PATH");

		String acr = null;


		String apiName = APIUtil.getAPI(messageContext);

		if (apiName == null) {
			return true;
		}

		String apiVersion = (String) messageContext.getProperty("api.ut.version");//

		ArrayList<String> msisdns = null;

		if (apiName.equalsIgnoreCase(APINameConstant.PAYMENT)) {
			acr = str_piece(resourceUrl, '/', 4);

		} else if (apiName.equalsIgnoreCase(APINameConstant.LOCATION)) {

			//Retriveing MSISDN from the incoming request
			acr = str_piece(str_piece(resourceUrl, '=', 2), '&', 1);
			log.info("API : LOCATION ; MSISDN : " + acr);

		} else if (apiName.equalsIgnoreCase(APINameConstant.MESSAGING)) {

			String urlMSISDN = null;
			String rgx = ".*/outbound/(.+?)/requests";
			Pattern pattern = Pattern.compile(rgx);
			Matcher matcher = pattern.matcher(resourceUrl);
			if (matcher.find()) {
				urlMSISDN = (matcher.group(1));
			} else {
				log.info("Blacklist : [MESSAGING] NOT A SMS Request : " + resourceUrl);
				return true;
			}


			Gson gson = new Gson();

			try {
				Mediator sequence = messageContext.getSequence("_build_");
				sequence.mediate(messageContext);
				String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext)
						.getAxis2MessageContext());

				SMSMessagingRequestWrap sms = gson.fromJson(jsonPayloadToString, SMSMessagingRequestWrap.class);
				acr = sms.getOutboundSMSMessageRequest().getAddress().get(0);
				log.info("API : SMS ; MSISDN : " + acr);
				if (!ACRModule.getMSISDNFromACR(acr).equals(ACRModule.getMSISDNFromACR(urlMSISDN))) {
					log.warn("URL MSISDN not match Body " + urlMSISDN + ":" + acr);
				}

				if (sms.getOutboundSMSMessageRequest().getAddress().size() > 1)
					msisdns = sms.getOutboundSMSMessageRequest().getAddress();

			} catch (Exception e) {
				Logger.getLogger(BlacklistHandler.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if (apiName.equalsIgnoreCase(APINameConstant.USSD)) {
			log.info(" USSD API REQUEST ");
			if (resourceUrl.contains("outbound") && !resourceUrl.contains("subscriptions")) {
				Gson gson = new Gson();
				try {
					Mediator sequence = messageContext.getSequence("_build_");
					sequence.mediate(messageContext);
					String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext)
							.getAxis2MessageContext());
					//log.info(" jsonPayloadToString : " + jsonPayloadToString);
					OutboundUSSDRequestWrap ussd = gson.fromJson(jsonPayloadToString, OutboundUSSDRequestWrap.class);
					acr = ussd.getOutboundUSSDMessageRequest().getAddress();
					log.info("API : USSD ; MSISDN : " + acr);

				} catch (Exception e) {
					Logger.getLogger(BlacklistHandler.class.getName()).log(Level.SEVERE, null, e);
				}
			} else if (resourceUrl.contains("inbound") && !resourceUrl.contains("subscriptions")) {
				Gson gson = new Gson();
				try {
					Mediator sequence = messageContext.getSequence("_build_");
					sequence.mediate(messageContext);
					String jsonPayloadToString = JsonUtil.jsonPayloadToString(((Axis2MessageContext) messageContext)
							.getAxis2MessageContext());
					//log.info(" jsonPayloadToString : " + jsonPayloadToString);
					InboundUSSDMessageRequestWrap ussd = gson.fromJson(jsonPayloadToString,
							InboundUSSDMessageRequestWrap.class);
					acr = ussd.getInboundUSSDMessageRequest().getAddress();
					log.info("API : USSD ; MSISDN : " + acr);

				} catch (Exception e) {
					Logger.getLogger(BlacklistHandler.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		} else {
			//nop
			log.info("Undefined API URL obtained:" + resourceUrl);
		}

		String userMSISDN = ACRModule.getMSISDNFromACR(acr);
		log.info("Get MSISDN for Blacklist Handle: " + userMSISDN);
		String appID = messageContext.getProperty("api.ut.application.id").toString();

		String apiID = null;
		//String subscriptionID = null;

		try {
			String apiContext = (String) messageContext.getProperty("REST_API_CONTEXT");
			apiID = DatabaseUtils.getAPIId(apiContext, apiVersion);
		} catch (NamingException ex) {
			Logger.getLogger(BlacklistHandler.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(BlacklistHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		//If blacklisted number error response is sent in the response
		try {
			if (isBlackListNumber(userMSISDN, apiID)) {
				log.info(userMSISDN + " is BlackListed number for " + apiName + " API");
				hadleBlakListResponse(messageContext);
			} else if (msisdns != null) {
				try {
					log.info("Multiple MSISDN");
					for (String cur : msisdns) {
						cur = ACRModule.getMSISDNFromACR(cur);
						if (isBlackListNumber(cur, apiID)) {
							log.info("BlackListed number : " + cur);
							hadleBlakListResponse(messageContext);
							break;
						}
					}
				} catch (Exception e) {
					log.error("Multiple MSISDN ERROR " + e.getMessage());
				}

			} else {
				log.info("MSISDN/MSISDNs not blacklisted ");
			}
		} catch (SQLException e) {
			log.error("SQL Exception occured during performing blacklist operation", e);
		} catch (NamingException e) {
			log.error("SQL Exception occured during performing blacklist operation", e);
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
		messageContext.setProperty(SynapseConstants.ERROR_CODE, "POL0001:");
		messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, "Internal Server Error. Blacklisted Number");
		int status = 400;
		OMElement faultPayload = getFaultPayload();

		org.apache.axis2.context.MessageContext axis2MC = ((Axis2MessageContext) messageContext)
				.getAxis2MessageContext();
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
		//publishResponseData(messageContext, faultPayload);
		Utils.sendFault(messageContext, status);
	}


	/**
	 * Checks if is black list number.
	 *
	 * @param msisdn the msisdn
	 * @param apiId  the api name
	 * @return true, if is black list number
	 * @throws SQLException    the SQL exception
	 * @throws NamingException the naming exception
	 */
	private boolean isBlackListNumber(String msisdn, String apiId) throws SQLException, NamingException {
		if (blacklistNumbers != null) {
			blacklistNumbers.clear();
		}

		blacklistNumbers = DatabaseUtils.ReadBlacklistNumbers(apiId);

		return blacklistNumbers.contains(msisdn);
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

	/**
	 * Str_piece.
	 *
	 * @param str       the str
	 * @param separator the separator
	 * @param index     the index
	 * @return the string
	 */
	private static String str_piece(String str, char separator, int index) {
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

	public boolean handleResponse(MessageContext messageContext) {

		return true;
	}

}
