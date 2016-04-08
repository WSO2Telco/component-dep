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

public class SpendLimitDataPublisher {
    private static Log log = LogFactory.getLog(SpendLimitDataPublisher.class);

    private LoadBalancingDataPublisher dataPublisher;

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
