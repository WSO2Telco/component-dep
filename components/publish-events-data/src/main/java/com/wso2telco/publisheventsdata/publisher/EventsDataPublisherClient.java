package com.wso2telco.publisheventsdata.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import com.wso2telco.publisheventsdata.MifeEventsConstants;
import com.wso2telco.publisheventsdata.dto.SpendLimitDataPublisherDTO;
import com.wso2telco.publisheventsdata.internal.MifeEventsDataHolder;

import java.util.Properties;

public class EventsDataPublisherClient {

    private static final Log log = LogFactory.getLog(EventsDataPublisherClient.class);

    private boolean enabled;
    private volatile SpendLimitDataPublisher publisher;

    public EventsDataPublisherClient() {
        Properties properties = MifeEventsDataHolder.getMifeEventsProps();
        String eventsEnabled = properties.getProperty(MifeEventsConstants.CEP_SPEND_LIMIT_HANDLER_ENABLED_PROPERTY);
        enabled = Boolean.parseBoolean(eventsEnabled);
    }

    public void publishEvent(MessageContext mc) {

        if (!enabled) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (publisher == null) {
            synchronized (this) {
                if (publisher == null) {
                    log.debug("Instantiating Events Data Publisher");
                    publisher = new SpendLimitDataPublisher();
                    publisher.init();
                }
            }
        }
        AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(mc);
        String consumerKey = "";
        String username = "";
        String applicationName = "";
        String applicationId = "";
        if (authContext != null) {
            consumerKey = authContext.getConsumerKey();
            username = authContext.getUsername();
            applicationName = authContext.getApplicationName();
            applicationId = authContext.getApplicationId();
        }
        String context = (String) mc.getProperty(RESTConstants.REST_API_CONTEXT);
        String api_version = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API);

        int index = api_version.indexOf("--");
        if (index != -1) {
            api_version = api_version.substring(index + 2);
        }

        String api = api_version.split(":")[0];
        index = api.indexOf("--");
        if (index != -1) {
            api = api.substring(index + 2);
        }
        String version = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API_VERSION);
        String tenantDomain = MultitenantUtils.getTenantDomain(username);

        SpendLimitDataPublisherDTO dataPublisherDTO = new SpendLimitDataPublisherDTO();
        dataPublisherDTO.setConsumerKey(consumerKey);
        dataPublisherDTO.setContext(context);
        dataPublisherDTO.setApi(api);
        dataPublisherDTO.setVersion(version);
        dataPublisherDTO.setResponseTime(currentTime);
        dataPublisherDTO.setUsername(username);
        dataPublisherDTO.setTenantDomain(tenantDomain);
        dataPublisherDTO.setApplicationName(applicationName);
        dataPublisherDTO.setApplicationId(applicationId);

        dataPublisherDTO.setOperatorId((String) mc.getProperty(MifeEventsConstants.OPERATOR_ID));
        dataPublisherDTO.setResponseCode((String) mc.getProperty(MifeEventsConstants.RESPONSE_CODE));
        dataPublisherDTO.setMsisdn((String) mc.getProperty(MifeEventsConstants.MSISDN));
        String chargeAmountStr = (String) mc.getProperty(MifeEventsConstants.CHARGE_AMOUNT);
        dataPublisherDTO.setChargeAmount(Double.parseDouble(chargeAmountStr));

        publisher.publishEvent(dataPublisherDTO);
    }

}
