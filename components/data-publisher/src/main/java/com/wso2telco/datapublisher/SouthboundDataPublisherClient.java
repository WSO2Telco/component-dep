package com.wso2telco.datapublisher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.apimgt.usage.publisher.DataPublisherUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import com.wso2telco.datapublisher.dto.SouthboundResponsePublisherDTO;
import com.wso2telco.datapublisher.internal.SouthboundDataComponent;

public class SouthboundDataPublisherClient {

    private static final Log log = LogFactory.getLog(DataPublisherClient.class);

    private boolean enabled = SouthboundDataComponent.getApiMgtConfigReaderService().isEnabled();
    private volatile SouthboundDataPublisher publisher;
    private String publisherClass = "com.axiata.dialog.mife.southbound.data.publisher.SouthboundDataPublisher";

    public void publishRequest(MessageContext mc, String jsonBody) {

        if (!enabled) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (publisher == null) {
            synchronized (this) {
                if (publisher == null) {
                    log.debug("Instantiating Data Publisher");
                    publisher = new SouthboundDataPublisher();
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
        String hostName = DataPublisherUtil.getHostAddress();
       String context = (String) mc.getProperty(RESTConstants.REST_API_CONTEXT);
        String api_version = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API);
        String fullRequestPath = (String) mc.getProperty(RESTConstants.REST_FULL_REQUEST_PATH);

        int tenantDomainIndex = fullRequestPath.indexOf("/t/");
        String apiPublisher = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (tenantDomainIndex != -1) {
            String temp = fullRequestPath.substring(tenantDomainIndex + 3, fullRequestPath.length());
            apiPublisher = temp.substring(0, temp.indexOf("/"));
       }

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
        String resource = extractResource(mc);
        String method = (String) ((Axis2MessageContext) mc).getAxis2MessageContext().getProperty(
                Constants.Configuration.HTTP_METHOD);
        String tenantDomain = MultitenantUtils.getTenantDomain(username);
//        int tenantId = ServiceReferenceHolder.getInstance().getRealmService().getTenantManager().getTenantId
// (tenantDomain);

        /*NorthboundRequestPublisherDTO requestPublisherDTO = new NorthboundRequestPublisherDTO();
        requestPublisherDTO.setConsumerKey(consumerKey);
        requestPublisherDTO.setContext(context);
        requestPublisherDTO.setApi_version(api_version);
        requestPublisherDTO.setApi(api);
        requestPublisherDTO.setVersion(version);
        requestPublisherDTO.setResourcePath(resource);
        requestPublisherDTO.setMethod(method);
        requestPublisherDTO.setRequestTime(currentTime);
        requestPublisherDTO.setUsername(username);
        requestPublisherDTO.setTenantDomain(tenantDomain);
        requestPublisherDTO.setHostName(hostName);
        requestPublisherDTO.setApiPublisher(apiPublisher);
        requestPublisherDTO.setApplicationName(applicationName);
        requestPublisherDTO.setApplicationId(applicationId);

       requestPublisherDTO.setJsonBody(jsonBody);

        publisher.publishEvent(requestPublisherDTO);

*/
		mc.setProperty(APIMgtGatewayConstants.CONSUMER_KEY, consumerKey);
		mc.setProperty(APIMgtGatewayConstants.CONTEXT, context);
		mc.setProperty(APIMgtGatewayConstants.API_VERSION, api_version);
		mc.setProperty(APIMgtGatewayConstants.API, api);
		mc.setProperty(APIMgtGatewayConstants.VERSION, version);
		mc.setProperty(APIMgtGatewayConstants.RESOURCE, resource);
		mc.setProperty(APIMgtGatewayConstants.HTTP_METHOD, method);
		// using new property name for southbound request time
		mc.setProperty(DataPublisherConstants.REQUEST_TIME, currentTime);
		mc.setProperty(APIMgtGatewayConstants.USER_ID, username);
		mc.setProperty(APIMgtGatewayConstants.HOST_NAME, hostName);
		mc.setProperty(APIMgtGatewayConstants.API_PUBLISHER,
				apiPublisher);
		mc.setProperty(APIMgtGatewayConstants.APPLICATION_NAME,
				applicationName);
		mc.setProperty(APIMgtGatewayConstants.APPLICATION_ID,
				applicationId);
    }

    public void publishResponse(MessageContext mc, String jsonBody) {
        if (!enabled) {
            return;
     }

        if (publisher == null) {
            synchronized (this) {
                if (publisher == null) {
                    log.debug("Instantiating Data Publisher");
                    publisher = new SouthboundDataPublisher();
                    publisher.init();
                }
            }
        }

        Long currentTime = System.currentTimeMillis();

        Long serviceTime = currentTime - (Long) mc.getProperty(DataPublisherConstants.REQUEST_TIME);

        SouthboundResponsePublisherDTO responsePublisherDTO = new SouthboundResponsePublisherDTO();
                responsePublisherDTO.setConsumerKey((String) mc.getProperty(APIMgtGatewayConstants.CONSUMER_KEY));
                responsePublisherDTO.setUsername((String) mc.getProperty(APIMgtGatewayConstants.USER_ID));
                responsePublisherDTO.setTenantDomain(MultitenantUtils.getTenantDomain(responsePublisherDTO.getUsername()));
                responsePublisherDTO.setContext((String) mc.getProperty(APIMgtGatewayConstants.CONTEXT));
                responsePublisherDTO.setApi_version((String) mc.getProperty(APIMgtGatewayConstants.API_VERSION));
                responsePublisherDTO.setApi((String) mc.getProperty(APIMgtGatewayConstants.API));
                responsePublisherDTO.setVersion((String) mc.getProperty(APIMgtGatewayConstants.VERSION));
                responsePublisherDTO.setResourcePath((String) mc.getProperty(APIMgtGatewayConstants.RESOURCE));
                responsePublisherDTO.setMethod((String) mc.getProperty(APIMgtGatewayConstants.HTTP_METHOD));
                responsePublisherDTO.setResponseTime(currentTime);
                responsePublisherDTO.setServiceTime(serviceTime);
                responsePublisherDTO.setHostName((String) mc.getProperty(APIMgtGatewayConstants.HOST_NAME));
                responsePublisherDTO.setApiPublisher((String) mc.getProperty(APIMgtGatewayConstants.API_PUBLISHER));
                responsePublisherDTO.setApplicationName((String) mc.getProperty(APIMgtGatewayConstants
                        .APPLICATION_NAME));
                responsePublisherDTO.setApplicationId((String) mc.getProperty(APIMgtGatewayConstants.APPLICATION_ID));
                        responsePublisherDTO.setRequestId((String) mc.getProperty(DataPublisherConstants.REQUEST_ID));
                //responsePublisherDTO.setOperatorId(DataPublisherConstants.MIFE_DEFAULT_OPERATOR);
                responsePublisherDTO.setResponseCode((String) mc.getProperty(DataPublisherConstants.RESPONSE_CODE));
                responsePublisherDTO.setExceptionId((String) mc.getProperty(DataPublisherConstants.EXCEPTION_ID));
                responsePublisherDTO.setExceptionMessage((String) mc.getProperty(DataPublisherConstants.EXCEPTION_MESSAGE));
                if (mc.getProperty(DataPublisherConstants.RESPONSE) != null) {
                    responsePublisherDTO.setResponse(Integer.parseInt((String) mc.getProperty(DataPublisherConstants.RESPONSE)));
        } else {
            responsePublisherDTO.setResponse(1);
        }
        responsePublisherDTO.setJsonBody(jsonBody);

        publisher.publishEvent(responsePublisherDTO);
    }

    private String extractResource(MessageContext mc) {
        String resource = "/";
        Pattern pattern = Pattern.compile("^/.+?/.+?([/?].+)$");
        Matcher matcher = pattern.matcher((String) mc.getProperty(RESTConstants.REST_FULL_REQUEST_PATH));
        if (matcher.find()) {
            resource = matcher.group(1);
        }
        return resource;
    }
}
