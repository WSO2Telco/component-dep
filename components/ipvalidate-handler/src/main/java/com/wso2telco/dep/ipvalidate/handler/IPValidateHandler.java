package com.wso2telco.dep.ipvalidate.handler;

import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.transport.passthru.PassThroughConstants;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.APIAuthenticationHandler;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;

import com.wso2telco.dep.ipvalidate.handler.validation.ClientValidator;
import com.wso2telco.dep.ipvalidate.handler.validation.configuration.IPValidationProperties;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.RequestData;
import com.wso2telco.dep.ipvalidate.handler.validation.impl.ClientValidatorImpl;
import com.wso2telco.dep.ipvalidate.handler.validation.service.ValidationCacheService;

public class IPValidateHandler extends APIAuthenticationHandler {

	private static final Log log = LogFactory.getLog(IPValidateHandler.class);
	private ClientValidator clientvalidator = null;	
	
	public boolean handleRequest(MessageContext messageContext) {
		log.debug("Request received : " + messageContext);
		
		try {

			Map headers = getTransportHeaders(messageContext);
			log.debug("headers : " + headers);
			RequestData requestData = new RequestData();

			requestData.setClientkey(getClientKey(messageContext));
			requestData.setHostip(getHostIP(headers));

			if (IPValidationProperties.isCustomValidationEnabled()) {
				clientvalidator = new ClientValidatorImpl();
				if (clientvalidator.validateRequest(requestData)) {
					ValidationCacheService validationCacheService = new ValidationCacheService();
					String clientToken = validationCacheService.getTokenfromCache(requestData.getClientkey());
					if (clientToken != null) {
						setTokentoContext(messageContext, clientToken);
					} else {
						throw new APISecurityException(IPValidationProperties.getValidationFalidErrCode(),
								IPValidationProperties.getValidationFalidErrMsg());
					}
				} else {
					throw new APISecurityException(IPValidationProperties.getInvalidHostErrCode(),
							IPValidationProperties.getInvalidHostErrMsg());
				}
			}
			return super.handleRequest(messageContext);
		} catch (APISecurityException e) {
			log.error("Error : " + e);
			handleIPValidateFailure(messageContext, e);
			return false;
		}
	}

	private Map getTransportHeaders(MessageContext messageContext) {
		return (Map) ((Axis2MessageContext) messageContext).getAxis2MessageContext()
				.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
	}

	private String getHostIP(Map headers) {
		String hostIp = null;

		if (headers.get(IPValidationProperties.getIpHeaderName()) != null) {
			hostIp = String.valueOf(headers.get(IPValidationProperties.getIpHeaderName())).split(",")[0];
		} else if (headers.get(IPValidationProperties.getHostHeaderName()) != null) {
			hostIp = String.valueOf(IPValidationProperties.getHostHeaderName()).split(":")[0];
		}

		return hostIp;
	}
	
	private String getClientKey(MessageContext messageContext) throws APISecurityException
	{
		String clientKey = null;
		
		try
		{
			String urlPath = messageContext.getTo().toString().split("[?]")[1];
			clientKey = urlPath.split("=")[1];
		}
		catch(Exception e)
		{
			log.error("Error occurred while geting client key " + e);
			throw new APISecurityException(IPValidationProperties.getValidationFalidErrCode(),
					IPValidationProperties.getValidationFalidErrMsg());
		}
		return clientKey;
	}
	
	private void handleIPValidateFailure(MessageContext messageContext, APISecurityException e) {
        messageContext.setProperty(SynapseConstants.ERROR_CODE, e.getErrorCode());
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE,
                APISecurityConstants.getAuthenticationFailureMessage(e.getErrorCode()));
        messageContext.setProperty(SynapseConstants.ERROR_EXCEPTION, e);

        Mediator sequence = messageContext.getSequence(APISecurityConstants.API_AUTH_FAILURE_HANDLER);
        // Invoke the custom error handler specified by the user
        if (sequence != null && !sequence.mediate(messageContext)) {
            // If needed user should be able to prevent the rest of the fault handling
            // logic from getting executed
            return;
        }
        // By default we send a 401 response back
        org.apache.axis2.context.MessageContext axis2MC = ((Axis2MessageContext) messageContext).
                getAxis2MessageContext();
        // This property need to be set to avoid sending the content in pass-through pipe (request message)
        // as the response.
        axis2MC.setProperty(PassThroughConstants.MESSAGE_BUILDER_INVOKED, Boolean.TRUE);
        try {
            RelayUtils.consumeAndDiscardMessage(axis2MC);
        } catch (AxisFault axisFault) {
            //In case of an error it is logged and the process is continued because we're setting a fault message in the payload.
            log.error("Error occurred while consuming and discarding the message", axisFault);
        }
        axis2MC.setProperty(Constants.Configuration.MESSAGE_TYPE, "application/soap+xml");
        int status;
        if (e.getErrorCode() == APISecurityConstants.API_AUTH_GENERAL_ERROR) {
            status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        } else if (e.getErrorCode() == APISecurityConstants.API_AUTH_INCORRECT_API_RESOURCE ||
                e.getErrorCode() == APISecurityConstants.API_AUTH_FORBIDDEN ||
                e.getErrorCode() == APISecurityConstants.INVALID_SCOPE) {
            status = HttpStatus.SC_FORBIDDEN;
        } else {
            status = HttpStatus.SC_UNAUTHORIZED;
            Map<String, String> headers =
                    (Map) axis2MC.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
            if (headers != null) {
                headers.put(HttpHeaders.WWW_AUTHENTICATE, getAuthenticator().getChallengeString() +
                        ", error=\"invalid token\"" +
                        ", error_description=\"The access token expired\"");
                axis2MC.setProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS, headers);
            }
        }

        if (messageContext.isDoingPOX() || messageContext.isDoingGET()) {
            setFaultPayload(messageContext, e);
        } else {
            setSOAPFault(messageContext, e);
        }
        sendFault(messageContext, status);
    }


	private void setTokentoContext(MessageContext messageContext, String token) {
		Map headers = getTransportHeaders(messageContext);
		Axis2MessageContext axis2smc = (Axis2MessageContext) messageContext;
		org.apache.axis2.context.MessageContext axis2MessageCtx = axis2smc.getAxis2MessageContext();

		if (headers != null && headers instanceof Map) {
			Map headersMap = (Map) headers;
			headersMap.put("Authorization", "Bearer " + token);
			axis2MessageCtx.setProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS, headersMap);
		}
	}
}
