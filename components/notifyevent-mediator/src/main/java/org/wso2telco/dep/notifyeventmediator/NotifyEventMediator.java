/*******************************************************************************
 * Copyright  (c) 2017, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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

package org.wso2telco.dep.notifyeventmediator;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.mediators.AbstractMediator;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.databridge.commons.Attribute;
import org.wso2.carbon.databridge.commons.StreamDefinition;
import org.wso2.carbon.databridge.commons.exception.MalformedStreamDefinitionException;
import org.wso2.carbon.databridge.commons.utils.DataBridgeCommonsUtils;
import org.wso2.carbon.event.sink.EventSink;
import org.wso2.carbon.event.sink.EventSinkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mediator that extracts data from current message payload/header according to the given configuration.
 * Extracted information is sent as an event.
 *
 * NOTE: Borrowed generously from WSO2 PublishEvent Mediator source code.
 */
public class NotifyEventMediator extends AbstractMediator {

    private static final String TASK_EXECUTING_TENANT_ID = "CURRENT_TASK_EXECUTING_TENANT_IDENTIFIER";

    private EventSinkService eventSinkService = null;
    private String streamName;
    private String streamVersion;
    private List<Property> metaProperties = new ArrayList<Property>();
    private List<Property> correlationProperties = new ArrayList<Property>();
    private List<Property> payloadProperties = new ArrayList<Property>();
    private List<Property> arbitraryProperties = new ArrayList<Property>();
    private String eventSinkName;

    private FaultEventHandler faultEventHandler;
    private boolean isEnabled = true;

    private long lastMediatorDisabledTime = 0L;
    private Object lastKnownEventSinkHash = null;

    @Override
    public boolean isContentAware() {
        return true;
    }

    /**
     * This is called when a new message is received for mediation.
     * Extracts data from message to construct an event based on the mediator configuration
     * Sends the constructed event to the event sink specified in mediator configuration
     *
     * @param messageContext Message context of the message to be mediated
     * @return Always returns true. (instructs to proceed with next mediator)
     */
    @Override
    public boolean mediate(MessageContext messageContext) {

        // Show data-publishing disabled message every 10 seconds
        if (!isEnabled) {
            synchronized (this) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastMediatorDisabledTime > 10000L) {
                    log.warn("NotifyEvent Mediator data publishing is disabled");
                    lastMediatorDisabledTime = currentTime;
                }
            }

            return true;
        }

		/*
		 Following will get the tenant-id if it's in the message context
		 This is useful when injecting the message via a Scheduled Task, etc. which uses threads that are not tenant aware
		 */
        Integer tenantId = null;
        if (messageContext.getProperty(TASK_EXECUTING_TENANT_ID) != null && messageContext.getProperty(TASK_EXECUTING_TENANT_ID) instanceof Integer){
            tenantId = (Integer) messageContext.getProperty(TASK_EXECUTING_TENANT_ID);
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantId);
        }

        EventSink eventSink = null;

        try {
            eventSink = loadEventSink();
        } catch (SynapseException e) {
            String errorMsg = "Cannot mediate message. Failed to load event sink '" + getEventSinkName() +
                    "'. Error: " + e.getLocalizedMessage();
            handleException(errorMsg, e, messageContext);
        }

		/*
		Anything relates to tenant specific should be completed before this
		 */
        if(tenantId != null) {
            PrivilegedCarbonContext.endTenantFlow();
        }

        SynapseLog synLog = getLog(messageContext);

        if (synLog.isTraceOrDebugEnabled()) {
            synLog.traceOrDebug("Start : " + NotifyEventMediatorFactory.getTagName() + " mediator");
            if (synLog.isTraceTraceEnabled()) {
                synLog.traceTrace("Message : " + messageContext.getEnvelope());
            }
        }

        ActivityIDSetter.setActivityIdInTransportHeader(messageContext);

        try {
            Object[] metaData = new Object[metaProperties.size()];
            for (int i = 0; i < metaProperties.size(); ++i) {
                metaData[i] = metaProperties.get(i).extractPropertyValue(messageContext);
            }

            Object[] correlationData = new Object[correlationProperties.size()];
            for (int i = 0; i < correlationProperties.size(); ++i) {
                correlationData[i] = correlationProperties.get(i).extractPropertyValue(messageContext);
            }

            Object[] payloadData = new Object[payloadProperties.size()];
            for (int i = 0; i < payloadProperties.size(); ++i) {
                payloadData[i] = payloadProperties.get(i).extractPropertyValue(messageContext);
            }

            Map<String, String> arbitraryData = new HashMap<String, String>();
            for (int i = 0; i < arbitraryProperties.size(); ++i) {
                Property arbitraryProperty = arbitraryProperties.get(i);
                arbitraryData.put(arbitraryProperty.getKey(),
                        arbitraryProperty.extractPropertyValue(messageContext).toString());
            }

            boolean success = eventSink.getDataPublisher()
                    .tryPublish(DataBridgeCommonsUtils.generateStreamId(getStreamName(), getStreamVersion()), metaData,
                            correlationData, payloadData, arbitraryData);

            // Handling the events when fails to put into the data-bridge queue
            if (!success)
                faultEventHandler.handleFaultEvents(DataBridgeCommonsUtils.generateStreamId(getStreamName(), getStreamVersion()), metaData,
                        correlationData, payloadData, arbitraryData);

        } catch (SynapseException e) {
            String errorMsg = "Error occurred while constructing the event: " + e.getLocalizedMessage();
            handleException(errorMsg, e, messageContext);
        } catch (Exception e) {
            String errorMsg = "Error occurred while sending the event: " + e.getLocalizedMessage();
            handleException(errorMsg, e, messageContext);
        }

        if (synLog.isTraceOrDebugEnabled()) {
            synLog.traceOrDebug("End : " + NotifyEventMediatorFactory.getTagName() + " mediator");
            if (synLog.isTraceTraceEnabled()) {
                synLog.traceTrace("Message : " + messageContext.getEnvelope());
            }
        }

        return true;
    }

    /**
     * Finds the event sink by eventSinkName and sets the stream definition to the data publisher of event sink
     *
     * @return Found EventSink
     */
    private EventSink loadEventSink() {
        if (eventSinkService == null) {
            Object serviceObject = PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(EventSinkService.class);
            if (serviceObject instanceof EventSinkService) {
                eventSinkService = (EventSinkService) serviceObject;
            } else {
                throw new SynapseException("Internal error occurred. Failed to obtain EventSinkService");
            }
        }

        EventSink eventSink = eventSinkService.getEventSink(getEventSinkName());
        if (eventSink == null) {
            throw new SynapseException("Event sink \"" + getEventSinkName() + "\" not found");
        }

        Object eventSinkHash = eventSink.hashCode();

        // Compare hash codes to identify a new event-sink deployment
        if (null == lastKnownEventSinkHash || !lastKnownEventSinkHash.equals(eventSinkHash)) {

            synchronized (this) {

                log.info("New event sink has identified: " + eventSink.getName());
                this.lastKnownEventSinkHash = eventSinkHash;

                // Check whether current stream definition complies with the new event-sink's stream definition
                try {
                    StreamDefinition streamDef = new StreamDefinition(getStreamName(), getStreamVersion());
                    streamDef.setCorrelationData(generateAttributeList(getCorrelationProperties()));
                    streamDef.setMetaData(generateAttributeList(getMetaProperties()));
                    streamDef.setPayloadData(generateAttributeList(getPayloadProperties()));
                } catch (MalformedStreamDefinitionException e) {
                    String errorMsg = "Failed to set stream definition. Malformed Stream Definition: " + e.getMessage();
                    throw new SynapseException(errorMsg, e);
                } catch (Exception e) {
                    String errorMsg = "Error occurred while creating the Stream Definition: " + e.getMessage();
                    throw new SynapseException(errorMsg, e);
                }
            }

        }

        return eventSink;
    }

    /**
     * Creates a list of data-bridge attributes for the given property list.
     *
     * @param propertyList List of properties for which attribute list should be created.
     * @return Created data-bridge attribute list.
     */
    private List<Attribute> generateAttributeList(List<Property> propertyList) {
        List<Attribute> attributeList = new ArrayList<Attribute>();
        for (Property property : propertyList) {
            attributeList.add(new Attribute(property.getKey(), property.getDatabridgeAttributeType()));
        }
        return attributeList;
    }

    public String getEventSinkName() {
        return eventSinkName;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getStreamVersion() {
        return streamVersion;
    }

    public List<Property> getMetaProperties() {
        return metaProperties;
    }

    public List<Property> getCorrelationProperties() {
        return correlationProperties;
    }

    public List<Property> getPayloadProperties() {
        return payloadProperties;
    }

    public List<Property> getArbitraryProperties() {
        return arbitraryProperties;
    }

    public void setEventSinkName(String eventSinkName) {
        this.eventSinkName = eventSinkName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public void setStreamVersion(String streamVersion) {
        this.streamVersion = streamVersion;
    }

    public void setMetaProperties(List<Property> metaProperties) {
        this.metaProperties = metaProperties;
    }

    public void setCorrelationProperties(List<Property> correlationProperties) {
        this.correlationProperties = correlationProperties;
    }

    public void setPayloadProperties(List<Property> payloadProperties) {
        this.payloadProperties = payloadProperties;
    }

    public void setArbitraryProperties(List<Property> arbitraryProperties) {
        this.arbitraryProperties = arbitraryProperties;
    }

    public FaultEventHandler getFaultEventHandler() {
        return faultEventHandler;
    }

    public void setFaultEventHandler(FaultEventHandler faultEventHandler) {
        this.faultEventHandler = faultEventHandler;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
