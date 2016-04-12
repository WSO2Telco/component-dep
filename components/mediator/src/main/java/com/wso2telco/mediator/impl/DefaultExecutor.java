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
package com.wso2telco.mediator.impl;



import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONObject;

import com.wso2telco.mediator.MSISDNConstants;
import com.wso2telco.mediator.OperatorEndpoint;
import com.wso2telco.mediator.RequestExecutor;
import com.wso2telco.mediator.mediationrule.OriginatingCountryCalculatorIDD;
import com.wso2telco.oneapivalidation.exceptions.AxiataException;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultExecutor.
 */
public class DefaultExecutor extends RequestExecutor {

    /** The log. */
    private Log log = LogFactory.getLog(DefaultExecutor.class);

    /** The occi. */
    private OriginatingCountryCalculatorIDD occi;

    /**
     * Instantiates a new default executor.
     */
    public DefaultExecutor() {
        occi = new OriginatingCountryCalculatorIDD();
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.RequestExecutor#execute(org.apache.synapse.MessageContext)
     */
    @Override
    public boolean execute(MessageContext context) throws AxiataException, AxisFault, Exception {
        String msisdn = readMSISDN(context); // with +
        context.setProperty(MSISDNConstants.USER_MSISDN, msisdn.substring(1)); // without +
        OperatorEndpoint endpoint = occi.getAPIEndpointsByMSISDN(msisdn, (String) context.getProperty("API_NAME"),
                getSubResourcePath(), false, getValidoperators());
        String sending_add = endpoint.getEndpointref().getAddress();
        log.info("sending endpoint found: " + sending_add);

        String responseString = null;
        if (getHttpMethod().equalsIgnoreCase("POST")) {
            responseString = makeRequest(endpoint, sending_add, getJsonBody().toString(), true, context);
        } else if (getHttpMethod().equalsIgnoreCase("GET")) {
            responseString = makeGetRequest(endpoint, sending_add, null, true, context);
        } else if (getHttpMethod().equalsIgnoreCase("DELETE")) {
            responseString = makeDeleteRequest(endpoint, sending_add, null, true, context);
        }

        removeHeaders(context);

        handlePluginException(responseString);
        setResponse(context, responseString);
        ((Axis2MessageContext) context).getAxis2MessageContext().setProperty("messageType", "application/json");

        return true;
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.RequestExecutor#validateRequest(java.lang.String, java.lang.String, org.json.JSONObject, org.apache.synapse.MessageContext)
     */
    @Override
    public boolean validateRequest(String httpMethod, String requestPath, JSONObject jsonBody, MessageContext
            context) throws Exception {
        return true;
    }

    /**
     * Read msisdn.
     *
     * @param context the context
     * @return the string
     * @throws Exception the exception
     */
    private String readMSISDN(MessageContext context) throws Exception {
        String msisdnLocation = (String) context.getProperty(MSISDNConstants.MSISDN_LOCATION_PROPERTY);
        String msisdnRegex = (String) context.getProperty(MSISDNConstants.MSISDN_REGEX_PROPERTY);
        String msisdnResource = null;

        if (msisdnLocation == null || msisdnRegex == null) {
            log.error("Missing required properties for DefaultExecutor. Synapse properties default.MSISDN.location " +
                    "and/or default.MSISDN.regex is missing");
            throw new Exception("Missing required properties for DefaultExecutor");
        }

        if (msisdnLocation.equalsIgnoreCase("URL")) {
            msisdnResource = URLDecoder.decode(getSubResourcePath().replace("+","%2B"), "UTF-8");
        } else if (msisdnLocation.equalsIgnoreCase("Body")) {
            msisdnResource = getJsonBody().toString();
        }

        Pattern pattern = Pattern.compile(msisdnRegex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(msisdnResource);

        String result = null;
        while (matcher.find()) {
            result = matcher.group();
            result = result.replace("tel:", "");
        }
        if (log.isDebugEnabled()) {
            log.debug("MSISDN Location - " + msisdnLocation);
            log.debug("MSISDN Regex - " + msisdnRegex);
            log.debug("MSISDN Resource Text - " + msisdnResource);
            log.debug("MSISDN Matched Result - " + result);
        }
        return result;
    }
}
