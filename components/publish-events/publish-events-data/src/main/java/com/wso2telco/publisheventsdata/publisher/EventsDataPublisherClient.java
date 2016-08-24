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
import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import com.wso2telco.publisheventsdata.MifeEventsConstants;
import com.wso2telco.publisheventsdata.dto.SpendLimitDataPublisherDTO;
import com.wso2telco.publisheventsdata.internal.MifeEventsDataHolder;

import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class EventsDataPublisherClient.
 */
public class EventsDataPublisherClient {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(EventsDataPublisherClient.class);

    /** The enabled. */
    private boolean enabled;
    
    /** The publisher. */
    private volatile SpendLimitDataPublisher publisher;

    /**
     * Instantiates a new events data publisher client.
     */
    public EventsDataPublisherClient() {
        Properties properties = MifeEventsDataHolder.getMifeEventsProps();
        String eventsEnabled = properties.getProperty(MifeEventsConstants.CEP_SPEND_LIMIT_HANDLER_ENABLED_PROPERTY);
        enabled = Boolean.parseBoolean(eventsEnabled);
    }

    /**
     * Publish event.
     *
     * @param mc the mc
     */
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
        dataPublisherDTO.setGroupName((String) mc.getProperty(MifeEventsConstants.GROUP_NAME));
        dataPublisherDTO.setPaymentType((String) mc.getProperty(MifeEventsConstants.PAYMENT_TYPE));
        dataPublisherDTO.setCurrentDateTime((String)mc.getProperty(MifeEventsConstants.CURRENT_DATE_TIME));
        

        publisher.publishEvent(dataPublisherDTO);
    }

}
