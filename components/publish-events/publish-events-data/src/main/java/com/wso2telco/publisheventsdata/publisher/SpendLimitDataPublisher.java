/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.publisheventsdata.publisher;

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
import com.wso2telco.publisheventsdata.MifeEventsConstants;
import com.wso2telco.publisheventsdata.dto.SpendLimitDataPublisherDTO;
import com.wso2telco.publisheventsdata.internal.DataPublisherAlreadyExistsException;
import com.wso2telco.publisheventsdata.internal.MifeEventsComponent;
import com.wso2telco.publisheventsdata.internal.MifeEventsDataHolder;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class SpendLimitDataPublisher.
 */
public class SpendLimitDataPublisher {
    
    /** The log. */
    private static Log log = LogFactory.getLog(SpendLimitDataPublisher.class);

    /** The data publisher. */
    private LoadBalancingDataPublisher dataPublisher;

    /**
     * Inits the.
     */
    public void init() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Initializing SpendLimitDataPublisher");
            }

            this.dataPublisher = getDataPublisher();

            //If Stream Definition does not exist.
            if(!dataPublisher.isStreamDefinitionAdded(MifeEventsConstants.MIFE_SPEND_LIMIT_DATA_STREAM_NAME,
                     MifeEventsConstants.MIFE_SPEND_LIMIT_DATA_STREAM_VERSION)){

                String spendLimitDataStreamDefinition = SpendLimitDataPublisherDTO.getStreamDefinition();

                dataPublisher.addStreamDefinition(spendLimitDataStreamDefinition,
                        MifeEventsConstants.MIFE_SPEND_LIMIT_DATA_STREAM_NAME,
                        MifeEventsConstants.MIFE_SPEND_LIMIT_DATA_STREAM_VERSION);

            }

        } catch (Exception e) {
            log.error("Error initializing SpendLimitDataPublisher", e);
        }
    }

    /**
     * Publish event.
     *
     * @param spendLimitDataPublisherDTO the spend limit data publisher dto
     */
    public void publishEvent(SpendLimitDataPublisherDTO spendLimitDataPublisherDTO) {
        try {
            //Publish Response Data
            dataPublisher.publish(MifeEventsConstants.MIFE_SPEND_LIMIT_DATA_STREAM_NAME,
                    MifeEventsConstants.MIFE_SPEND_LIMIT_DATA_STREAM_VERSION,
                    System.currentTimeMillis(), new Object[]{"external"}, null,
                    (Object[]) spendLimitDataPublisherDTO.createPayload());

        } catch (AgentException e) {
            log.error("Error while publishing spend limit event", e);
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
        LoadBalancingDataPublisher loadBalancingDataPublisher = MifeEventsComponent.getDataPublisher(tenantDomain);

        //If a LoadBalancingDataPublisher had not been registered for the tenant.
        if (loadBalancingDataPublisher == null) {
            Properties mifeEventsProps = MifeEventsDataHolder.getMifeEventsProps();

            List<String> receiverGroups = DataPublisherUtil.getReceiverGroups(mifeEventsProps.getProperty(MifeEventsConstants.CEP_URL_PROPERTY));

            String serverUser = mifeEventsProps.getProperty(MifeEventsConstants.CEP_USERNAME_PROPERTY);
            String serverPassword = mifeEventsProps.getProperty(MifeEventsConstants.CEP_PASSWORD_PROPERTY);
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
                MifeEventsComponent.addDataPublisher(tenantDomain, loadBalancingDataPublisher);
            } catch (DataPublisherAlreadyExistsException e) {
                log.warn("Attempting to register a data publisher for the tenant " + tenantDomain +
                        " when one already exists. Returning existing data publisher");
                return MifeEventsComponent.getDataPublisher(tenantDomain);
            }
        }

        return loadBalancingDataPublisher;
    }

}
