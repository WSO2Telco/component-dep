package com.wso2telco.dep.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.config.Entry;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class PropertyLogHandler extends AbstractMediator {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String REGISTRY_PATH = "gov:/apimgt/";
	private static final String REQUEST_ID = "mife.prop.requestId";
	private static final String MESSAGE_TYPE = "message.type";
	private static final String PAYLOAD_LOGGING_ENABLED = "payload.logging.enabled";
	private static final String APPLICATION_ID = "api.ut.application.id";
	private static final String REQUEST = "request";
	private static final String RESPONSE = "response";

	public boolean mediate(MessageContext messageContext) {

		boolean isPayloadLoggingEnabled = false;

		org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
				.getAxis2MessageContext();

		isPayloadLoggingEnabled = extractPayloadLoggingStatus(messageContext);

		String direction = (String) axis2MessageContext.getProperty(MESSAGE_TYPE);

		if (direction != null && direction.equalsIgnoreCase(REQUEST)) {
			logRequestProperties(messageContext, axis2MessageContext, isPayloadLoggingEnabled);
		} else if (direction != null && direction.equalsIgnoreCase(RESPONSE)) {
			logResponseProperties(messageContext, axis2MessageContext, isPayloadLoggingEnabled);
		}

		return true;
	}

	private void logRequestProperties(MessageContext messageContext,
			org.apache.axis2.context.MessageContext axis2MessageContext, boolean isPayloadLoggingEnabled) {

		String requestId = (String) messageContext.getProperty(REQUEST_ID);

		if (nullOrTrimmed(requestId) == null) {
			UniqueIDGenerator.generateAndSetUniqueID("MI", messageContext,
					(String) messageContext.getProperty(APPLICATION_ID));
		}

		log.info("[" + dateFormat.format(new Date()) + "] >>>>> API Request id "
				+ messageContext.getProperty(REQUEST_ID));
		
		if (isPayloadLoggingEnabled) {
			String jsonBody = JsonUtil.jsonPayloadToString(axis2MessageContext);
			log.info("                                       >>>>> reqBody :" + jsonBody);
		}

	}

	private void logResponseProperties(MessageContext messageContext,
			org.apache.axis2.context.MessageContext axis2MessageContext, boolean isPayloadLoggingEnabled) {


		log.info("[" + dateFormat.format(new Date()) + "] <<<<< API Request id "
				+ messageContext.getProperty(REQUEST_ID));
		
		if (isPayloadLoggingEnabled) {
			String jsonBody = JsonUtil.jsonPayloadToString(axis2MessageContext);
			log.info("                                       <<<<< respBody :" + jsonBody);
		}

	}
	
	private boolean extractPayloadLoggingStatus (MessageContext messageContext) {
		boolean isPayloadLoggingEnabled = false;
		
		Entry payloadEntry = new Entry(REGISTRY_PATH + PAYLOAD_LOGGING_ENABLED);

		OMTextImpl payloadEnableRegistryValue = (OMTextImpl) messageContext.getConfiguration().getRegistry()
				.getResource(payloadEntry, null);

		if (payloadEnableRegistryValue != null) {
			String payloadLogEnabled = payloadEnableRegistryValue.getText();

			if (nullOrTrimmed(payloadLogEnabled) != null) {
				isPayloadLoggingEnabled = Boolean.valueOf(payloadLogEnabled);
			}
		}
		
		return isPayloadLoggingEnabled;
	}

	private static String nullOrTrimmed(String inputString) {
		String result = null;
		if (inputString != null && inputString.trim().length() > 0) {
			result = inputString.trim();
		}
		return result;
	}
}
