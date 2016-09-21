/*
 * MobileConnectExecutor.java
 * Oct 16, 2015  3:08:29 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.dep.mediator.impl;


import com.wso2telco.dep.mediator.MediatorConstants;
import com.wso2telco.dep.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.RequestExecutor;
import com.wso2telco.dep.mediator.internal.UID;
import com.wso2telco.dep.mediator.internal.Type;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.UUID;

public class PassThroughMobileRequestExecutor extends RequestExecutor {

    private Log log = LogFactory.getLog(PassThroughMobileRequestExecutor.class);

    private OriginatingCountryCalculatorIDD occi;

    public PassThroughMobileRequestExecutor() {
        occi = new OriginatingCountryCalculatorIDD();
    }

    @Override
    public boolean execute(MessageContext context) throws AxisFault, Exception {
        String requestid = UID.getUniqueID(Type.MC_REQ.getCode(), context, getApplicationid());
        String mobileOperator = readMNO(context); // with +
        context.setProperty(MediatorConstants.USER_MSISDN, null); // without +
        OperatorEndpoint endpoint = occi.getAPIEndpointsByMNO(mobileOperator, (String) context.getProperty("API_NAME"),
                getSubResourcePath(), false, getValidoperators());
        //Setting generated transactionId in the SessionId request parameter
        String transactionId = UUID.randomUUID().toString();
        if (endpoint.getEndpointref().getAddress().contains("?")) {
            endpoint.getEndpointref().setAddress(endpoint.getEndpointref().getAddress() + "&" + MediatorConstants.TRANSACTION_ID + "=" + transactionId);
        } else {
            endpoint.getEndpointref().setAddress(endpoint.getEndpointref().getAddress() + "?" + MediatorConstants.TRANSACTION_ID + "=" + transactionId);
        }
        String sendingAdd = endpoint.getEndpointref().getAddress();
        log.info("sending endpoint found: " + sendingAdd);

        String responseString = null;

        if (getHttpMethod().equalsIgnoreCase("POST")) {
            sendingAdd = sendingAdd.replace("/"+mobileOperator, "");
            endpoint.getEndpointref().setAddress(sendingAdd);
            makeRequest(endpoint, sendingAdd, getJsonBody().toString(), false, context,true);
        } else if (getHttpMethod().equalsIgnoreCase("GET")) {
            makeGetRequest(endpoint, sendingAdd, null, false, context,true);
        } else if (getHttpMethod().equalsIgnoreCase("DELETE")) {
            responseString = makeDeleteRequest(endpoint, sendingAdd, null, false, context,true);
        }


        removeHeaders(context);

        return true;
    }

    @Override
    public boolean validateRequest(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext
            context) throws Exception {
        return true;
    }

    private String readMNO(MessageContext context) throws Exception {
        String mnoLocation = (String) context.getProperty(MediatorConstants.MNO_LOCATION_PROPERTY);
        //String mnoRegex = (String) context.getProperty(MediatorConstants.MNO_REGEX_PROPERTY);
        String mnoPosition = (String) context.getProperty(MediatorConstants.MNO_POSITION_PROPERTY);
        String mnoResource = null;

        if (mnoLocation == null || mnoPosition == null) {
            log.error("Missing required properties for MobileConnectExecutor. Synapse properties default.MNO.location " +
                    "and/or default.MNO.position is missing");
            throw new Exception("Missing required properties for DefaultExecutor");
        }

        if (mnoLocation.equalsIgnoreCase("URL")) {
            mnoResource = URLDecoder.decode(getSubResourcePath().replace("+","%2B"), "UTF-8");
        } else if (mnoLocation.equalsIgnoreCase("Body")) {
            mnoResource = getJsonBody().toString();
        }

        //Pattern pattern = Pattern.compile(mnoRegex, Pattern.DOTALL);
        //Matcher matcher = pattern.matcher(mnoResource);

        String result = null;
        //while (matcher.find()) {
        //    result = matcher.group();
        //    result = result.replace("tel:", "");
        //}
        result = mnoResource.split("/")[Integer.parseInt(mnoPosition)];
        if (log.isDebugEnabled()) {
            log.debug("MSISDN Location - " + mnoLocation);
            log.debug("MSISDN Regex - " + mnoPosition);
            log.debug("MSISDN Resource Text - " + mnoResource);
            log.debug("MSISDN Matched Result - " + result);
        }
        return result;
    }
}
