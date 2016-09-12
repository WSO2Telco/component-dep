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
package com.wso2telco.dep.datapublisher;


import com.wso2telco.dep.datapublisher.dto.NorthboundResponsePublisherDTO;
import com.wso2telco.dep.datapublisher.dto.SouthboundRequestPublisherDTO;
import com.wso2telco.dep.datapublisher.dto.SouthboundResponsePublisherDTO;
import com.wso2telco.dep.datapublisher.internal.APIMgtConfigReader;
import com.wso2telco.dep.datapublisher.internal.DataPublisherAlreadyExistsException;
import com.wso2telco.dep.datapublisher.internal.SouthboundDataComponent;

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


// TODO: Auto-generated Javadoc
/**
 * The Class SouthboundDataPublisher.
 */
public class SouthboundDataPublisher {

    /** The log. */
    private static Log log = LogFactory.getLog(SouthboundDataPublisher.class);

    /** The data publisher. */
    private LoadBalancingDataPublisher dataPublisher;

    /**
     * Inits the.
     */
    public void init() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Initializing SouthboundDataPublisher");
            }

            this.dataPublisher = getDataPublisher();

            //If Request Stream Definition does not exist.
            if (!dataPublisher.isStreamDefinitionAdded(DataPublisherConstants.SOUTHBOUND_REQUEST_STREAM_NAME,
                    DataPublisherConstants.SOUTHBOUND_REQUEST_STREAM_VERSION)) {

                //Get Request Stream Definition
                String requestStreamDefinition = SouthboundRequestPublisherDTO.getStreamDefinition();

                //Add Request Stream Definition.
                dataPublisher.addStreamDefinition(requestStreamDefinition,
                        DataPublisherConstants.SOUTHBOUND_REQUEST_STREAM_NAME,
                        DataPublisherConstants.SOUTHBOUND_REQUEST_STREAM_VERSION);
            }

            //If Response Stream Definition does not exist.
            if(!dataPublisher.isStreamDefinitionAdded(DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME,
                     DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION)){

                //Get Response Stream Definition.
                String responseStreamDefinition = SouthboundResponsePublisherDTO.getStreamDefinition();

                //Add Response Stream Definition.
                dataPublisher.addStreamDefinition(responseStreamDefinition,
                        DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME,
                        DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION);

            }

        } catch (Exception e) {
            log.error("Error initializing SouthboundDataPublisher", e);
        }
    }

    /**
     * Publish event.
     *
     * @param southboundRequestPublisherDTO the southbound request publisher dto
     */
    public void publishEvent(SouthboundRequestPublisherDTO southboundRequestPublisherDTO) {
        try {
            //Publish Request Data
            dataPublisher.publish(DataPublisherConstants.SOUTHBOUND_REQUEST_STREAM_NAME,
                    DataPublisherConstants.SOUTHBOUND_REQUEST_STREAM_VERSION,
                    System.currentTimeMillis(), new Object[]{"external"}, null,
                    (Object[]) southboundRequestPublisherDTO.createPayload());
        } catch (AgentException e) {
            log.error("Error while publishing Request event", e);
        }
    }

    /**
     * Publish event.
     *
     * @param southboundResponsePublisherDTO the southbound response publisher dto
     */
    public void publishEvent(SouthboundResponsePublisherDTO southboundResponsePublisherDTO) {
        try {
            //Publish Response Data
            dataPublisher.publish(DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME,
                    DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION,
                    System.currentTimeMillis(), new Object[]{"external"}, null,
                    (Object[]) southboundResponsePublisherDTO.createPayload());

        } catch (AgentException e) {
            log.error("Error while publishing Response event", e);
        }
    }
    
    /**
     * Publish event.
     *
     * @param northboundResponsePublisherDTO the northbound response publisher dto
     */
    public void publishEvent(NorthboundResponsePublisherDTO northboundResponsePublisherDTO) {
        try {
            //Publish Response Data
            dataPublisher.publish(DataPublisherConstants.NORTHBOUND_RESPONSE_STREAM_NAME,
                    DataPublisherConstants.NORTHBOUND_REQUEST_STREAM_VERSION,
                    System.currentTimeMillis(), new Object[]{"external"}, null,
                    (Object[]) northboundResponsePublisherDTO.createPayload());

        } catch (AgentException e) {
            log.error("Error while NB publishing Response event", e);
        }
    }

    /**
     * Gets the data publisher.
     *
     * @return the data publisher
     * @throws AgentException the agent exception
     * @throws MalformedURLException the malformed url exception
     * @throws AuthenticationException the authentication exception
     * @throws TransportException the transport exception
     */
    private static LoadBalancingDataPublisher getDataPublisher() throws AgentException, MalformedURLException,
            AuthenticationException, TransportException {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();

        //Get LoadBalancingDataPublisher which has been registered for the tenant.
        LoadBalancingDataPublisher loadBalancingDataPublisher = SouthboundDataComponent.getDataPublisher(tenantDomain);

        //If a LoadBalancingDataPublisher had not been registered for the tenant.
        if (loadBalancingDataPublisher == null) {
            APIMgtConfigReader apimgtConfigReader = SouthboundDataComponent.getApiMgtConfigReaderService();

            List<String> receiverGroups = DataPublisherUtil.getReceiverGroups(apimgtConfigReader
                    .getDasServerURL());

            String serverUser = apimgtConfigReader.getDasServerUser();
            String serverPassword = apimgtConfigReader.getDasServerPassword();
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
