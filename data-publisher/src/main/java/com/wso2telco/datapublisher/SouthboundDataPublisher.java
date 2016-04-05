package com.wso2telco.datapublisher;


import com.wso2telco.datapublisher.dto.NorthboundResponsePublisherDTO;
import com.wso2telco.datapublisher.dto.SouthboundRequestPublisherDTO;
import com.wso2telco.datapublisher.dto.SouthboundResponsePublisherDTO;
import com.wso2telco.datapublisher.internal.APIMgtConfigReader;
import com.wso2telco.datapublisher.internal.DataPublisherAlreadyExistsException;
import com.wso2telco.datapublisher.internal.SouthboundDataComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.agent.thrift.lb.DataPublisherHolder;
import org.wso2.carbon.databridge.agent.thrift.lb.LoadBalancingDataPublisher;
import org.wso2.carbon.databridge.agent.thrift.lb.ReceiverGroup;
import org.wso2.carbon.databridge.agent.thrift.util.DataPublisherUtil;
import org.wso2.carbon.databridge.commons.exception.AuthenticationException;
import org.wso2.carbon.databridge.commons.exception.TransportException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class SouthboundDataPublisher {

    private static Log log = LogFactory.getLog(SouthboundDataPublisher.class);

    private LoadBalancingDataPublisher dataPublisher;

    public void init() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Initializing SouthboundDataPublisher");
            }

            this.dataPublisher = getDataPublisher();

            //If Request Stream Definition does not exist.
            if (!dataPublisher.isStreamDefinitionAdded(SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_NAME,
                    SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_VERSION)) {

                //Get Request Stream Definition
                String requestStreamDefinition = SouthboundRequestPublisherDTO.getStreamDefinition();

                //Add Request Stream Definition.
                dataPublisher.addStreamDefinition(requestStreamDefinition,
                        SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_NAME,
                        SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_VERSION);
            }

            //If Response Stream Definition does not exist.
            if(!dataPublisher.isStreamDefinitionAdded(SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME,
                     SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION)){

                //Get Response Stream Definition.
                String responseStreamDefinition = SouthboundResponsePublisherDTO.getStreamDefinition();

                //Add Response Stream Definition.
                dataPublisher.addStreamDefinition(responseStreamDefinition,
                        SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME,
                        SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION);

            }

        } catch (Exception e) {
            log.error("Error initializing SouthboundDataPublisher", e);
        }
    }

    public void publishEvent(SouthboundRequestPublisherDTO southboundRequestPublisherDTO) {
        try {
            //Publish Request Data
            dataPublisher.publish(SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_NAME,
                    SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_VERSION,
                    System.currentTimeMillis(), new Object[]{"external"}, null,
                    (Object[]) southboundRequestPublisherDTO.createPayload());
        } catch (AgentException e) {
            log.error("Error while publishing Request event", e);
        }
    }

    public void publishEvent(SouthboundResponsePublisherDTO southboundResponsePublisherDTO) {
        try {
            //Publish Response Data
            dataPublisher.publish(SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME,
                    SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION,
                    System.currentTimeMillis(), new Object[]{"external"}, null,
                    (Object[]) southboundResponsePublisherDTO.createPayload());

        } catch (AgentException e) {
            log.error("Error while publishing Response event", e);
        }
    }
    
    public void publishEvent(NorthboundResponsePublisherDTO northboundResponsePublisherDTO) {
        try {
            //Publish Response Data
            dataPublisher.publish(SouthboundPublisherConstants.NORTHBOUND_RESPONSE_STREAM_NAME,
                    SouthboundPublisherConstants.NORTHBOUND_REQUEST_STREAM_VERSION,
                    System.currentTimeMillis(), new Object[]{"external"}, null,
                    (Object[]) northboundResponsePublisherDTO.createPayload());

        } catch (AgentException e) {
            log.error("Error while NB publishing Response event", e);
        }
    }

    private static LoadBalancingDataPublisher getDataPublisher() throws AgentException, MalformedURLException,
            AuthenticationException, TransportException {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();

        //Get LoadBalancingDataPublisher which has been registered for the tenant.
        LoadBalancingDataPublisher loadBalancingDataPublisher = SouthboundDataComponent.getDataPublisher(tenantDomain);

        //If a LoadBalancingDataPublisher had not been registered for the tenant.
        if (loadBalancingDataPublisher == null) {
            APIMgtConfigReader apimgtConfigReader = SouthboundDataComponent.getApiMgtConfigReaderService();

            List<String> receiverGroups = DataPublisherUtil.getReceiverGroups(apimgtConfigReader
                    .getBamServerURL());

            String serverUser = apimgtConfigReader.getBamServerUser();
            String serverPassword = apimgtConfigReader.getBamServerPassword();
            List<ReceiverGroup> allReceiverGroups = new ArrayList<ReceiverGroup>();

            for (String receiverGroupString : receiverGroups) {
                String[] serverURLs = receiverGroupString.split(",");
                List<DataPublisherHolder> dataPublisherHolders = new ArrayList<DataPublisherHolder>();

                for (int i = 0; i < serverURLs.length; i++) {
                    String serverURL = serverURLs[i];
                    DataPublisherHolder dataPublisherHolder =
                            new DataPublisherHolder(null, serverURL, serverUser, serverPassword);
                    dataPublisherHolders.add(dataPublisherHolder);
                }

                ReceiverGroup receiverGroup = new ReceiverGroup((ArrayList) dataPublisherHolders);
                allReceiverGroups.add(receiverGroup);
            }

            //Create new LoadBalancingDataPublisher for the tenant.
            loadBalancingDataPublisher = new LoadBalancingDataPublisher((ArrayList) allReceiverGroups);
            try {
                //Add created LoadBalancingDataPublisher.
                SouthboundDataComponent.addDataPublisher(tenantDomain, loadBalancingDataPublisher);
            } catch (DataPublisherAlreadyExistsException e) {
                log.warn("Attempting to register a data publisher for the tenant " + tenantDomain +
                        " when one already exists. Returning existing data publisher");
                return SouthboundDataComponent.getDataPublisher(tenantDomain);
            }
        }

        return loadBalancingDataPublisher;
    }
}
