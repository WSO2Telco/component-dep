package com.wso2telco.dep.noauth.handler;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis2.Constants;
import org.apache.http.HttpHeaders;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.apimgt.gateway.handlers.security.Authenticator;
//import org.wso2.carbon.apimgt.gateway.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.gateway.handlers.security.oauth.OAuthAuthenticator;

public class APINoAuthenticationHandler_bak extends AbstractHandler implements ManagedLifecycle{
	
	private volatile Authenticator authenticator;
	
	private SynapseEnvironment synapseEnvironment;
	
	 private String authorizationHeader;
	 
	 private boolean removeOAuthHeadersFromOutMessage = true;

	@Override
	public boolean handleRequest(MessageContext messageContext) {
		System.out.println("---------------------------------------Super handleRequest------------------------------------");	
		System.out.println("messageContext : " + messageContext);
		/*String messageId = (String) messageContext.getProperty("MESSAGE_ID");
        String applicationId = (String) messageContext.getProperty("api.ut.application.id");
        String apiName = (String) messageContext.getProperty("api.ut.api");
        String apiVersion = (String) messageContext.getProperty("SYNAPSE_REST_API_VERSION");
        String apiPublisher = (String) messageContext.getProperty("api.ut.apiPublisher");
        
        System.out.println("messageId " + messageId);
        System.out.println("applicationId " + applicationId);
        System.out.println("apiName " + apiName);
        System.out.println("apiVersion " + apiVersion);
        System.out.println("apiPublisher " + apiPublisher);*/
        
        try {
            if (authenticate(messageContext)) {
                return true;
            }
        } catch (APISecurityException e) {
            e.printStackTrace();
        }
        
        
		return false;
	}
	
	@Override
	public boolean handleResponse(MessageContext messageContext) {
		System.out.println("---------------------------------------Super handleResponse------------------------------------");
        return true; 
    }
	
	protected boolean isAuthenticate(MessageContext messageContext) throws APISecurityException {
		Map headers = getTransportHeaders(messageContext);
        System.out.println("headers after mod " + headers);
        return authenticator.authenticate(messageContext);
    }
 
    public boolean authenticate(MessageContext messageContext) throws APISecurityException {
        Map headers = getTransportHeaders(messageContext);
        System.out.println("headers " + headers);
        String authHeader = getAuthorizationHeader(headers);
//        if (authHeader.startsWith("userName")) {
//            return true;
//        }
        
        ///////////////////////////////////////////////////////////////////
        
        Axis2MessageContext axis2smc = (Axis2MessageContext) messageContext;
        org.apache.axis2.context.MessageContext axis2MessageCtx =
                axis2smc.getAxis2MessageContext();
   

        if (headers != null && headers instanceof Map) {
            Map headersMap = (Map) headers;
            headersMap.put("Authorization", "Bearer f78b71cd-d43c-319e-ab1c-17e381740f8b");
            axis2MessageCtx.setProperty(
                    org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS,
                    headersMap);
        }
        
            
       /////////////////////////////////////////////////////////////////////////////////////////////////
        isAuthenticate(messageContext);
        setAPIParametersToMessageContext(messageContext);
        return true;
    }
 
    private String getAuthorizationHeader(Map headers) {
    	
    	System.out.println("Authorization:" + headers.get("Authorization"));
    	System.out.println("Host:" + headers.get("Host"));
    	System.out.println("accept:" + headers.get("accept"));
    	System.out.println("Connection:" + headers.get("Connection"));
    	System.out.println("Referer:" + headers.get("Referer"));
    	
        return (String) headers.get("Authorization");
    }
 
    private Map getTransportHeaders(MessageContext messageContext) {
        return (Map) ((Axis2MessageContext) messageContext).getAxis2MessageContext().
                getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
    }
    
    protected void setAPIParametersToMessageContext(MessageContext messageContext) {
    	
    	System.out.println("------------------setAPIParametersToMessageContext---------2-------------");
    	
    	try
    	{

        AuthenticationContext authContext = getAuthenticationContext(messageContext);
        org.apache.axis2.context.MessageContext axis2MsgContext =
                ((Axis2MessageContext) messageContext).getAxis2MessageContext();
           
        System.out.println("authContext" + authContext);

        String consumerKey = "";
        String username = "";
        String applicationName = "";
        String applicationId = "";
        if (authContext != null) {
        	System.out.println("authContext" + authContext);
        	System.out.println("getApiKey" + authContext.getApiKey());
        	System.out.println("getApiTier" + authContext.getApiTier());
        	System.out.println("getApplicationId" + authContext.getApplicationId());
        	System.out.println("getApplicationName" + authContext.getApplicationName());
        	System.out.println("getApplicationTier" + authContext.getApplicationTier());
        	System.out.println("getCallerToken" + authContext.getCallerToken());
        	System.out.println("getConsumerKey" + authContext.getConsumerKey());
        	System.out.println("getKeyType" + authContext.getKeyType());
        	System.out.println("getSpikeArrestLimit" + authContext.getSpikeArrestLimit());
        	System.out.println("getSpikeArrestUnit" + authContext.getSpikeArrestUnit());
        	
        	System.out.println("getSubscriber" + authContext.getSubscriber());        	
        	System.out.println("getSubscriberTenantDomain" + authContext.getSubscriberTenantDomain());
        	System.out.println("getTier" + authContext.getTier());
        	System.out.println("getUsername" + authContext.getUsername());
        	System.out.println("getThrottlingDataList" + authContext.getThrottlingDataList());
        	
            consumerKey = authContext.getConsumerKey();
            username = authContext.getUsername();
            applicationName = authContext.getApplicationName();
            applicationId = authContext.getApplicationId();
        }

        String context = (String) messageContext.getProperty(RESTConstants.REST_API_CONTEXT);
        String apiVersion = (String) messageContext.getProperty(RESTConstants.SYNAPSE_REST_API);

        String apiPublisher = (String) messageContext.getProperty(APIMgtGatewayConstants.API_PUBLISHER);
        //if publisher is null,extract the publisher from the api_version
        if (apiPublisher == null) {
            int ind = apiVersion.indexOf("--");
            apiPublisher = apiVersion.substring(0, ind);
            if (apiPublisher.contains(APIConstants.EMAIL_DOMAIN_SEPARATOR_REPLACEMENT)) {
                apiPublisher = apiPublisher
                        .replace(APIConstants.EMAIL_DOMAIN_SEPARATOR_REPLACEMENT, APIConstants.EMAIL_DOMAIN_SEPARATOR);
            }
        }
        int index = apiVersion.indexOf("--");

        if (index != -1) {
            apiVersion = apiVersion.substring(index + 2);
        }

        String api = apiVersion.split(":")[0];
        String version = (String) messageContext.getProperty(RESTConstants.SYNAPSE_REST_API_VERSION);
        String resource = extractResource(messageContext);
        String method = (String) (axis2MsgContext.getProperty(
                Constants.Configuration.HTTP_METHOD));
        String hostName = APIUtil.getHostAddress();

        messageContext.setProperty(APIMgtGatewayConstants.CONSUMER_KEY, consumerKey);
        messageContext.setProperty(APIMgtGatewayConstants.USER_ID, username);
        messageContext.setProperty(APIMgtGatewayConstants.CONTEXT, context);
        messageContext.setProperty(APIMgtGatewayConstants.API_VERSION, apiVersion);
        messageContext.setProperty(APIMgtGatewayConstants.API, api);
        messageContext.setProperty(APIMgtGatewayConstants.VERSION, version);
        messageContext.setProperty(APIMgtGatewayConstants.RESOURCE, resource);
        messageContext.setProperty(APIMgtGatewayConstants.HTTP_METHOD, method);
        messageContext.setProperty(APIMgtGatewayConstants.HOST_NAME, hostName);
        messageContext.setProperty(APIMgtGatewayConstants.API_PUBLISHER, apiPublisher);
        messageContext.setProperty(APIMgtGatewayConstants.APPLICATION_NAME, applicationName);
        messageContext.setProperty(APIMgtGatewayConstants.APPLICATION_ID, applicationId);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    
    }

    protected AuthenticationContext getAuthenticationContext(MessageContext messageContext) {
        return APISecurityUtils.getAuthenticationContext(messageContext);
    }

    private String extractResource(MessageContext mc) {
        String resource = "/";
        Pattern pattern = Pattern.compile(APIMgtGatewayConstants.RESOURCE_PATTERN);
        Matcher matcher = pattern.matcher((String) mc.getProperty(RESTConstants.REST_FULL_REQUEST_PATH));
        if (matcher.find()) {
            resource = matcher.group(1);
        }
        return resource;
    }

	@Override
	public void init(SynapseEnvironment synapseEnvironment) {
		this.synapseEnvironment = synapseEnvironment;
		System.out.println("---------------------------------------Super init------------------------------------");
//        if (log.isDebugEnabled()) {
//            log.debug("Initializing API authentication handler instance");
//        }
        //if (getApiManagerConfigurationService() != null) {
            initializeAuthenticator();
        //}
		
	}
	
	public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    public void setAuthorizationHeader(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }
    
    public boolean getRemoveOAuthHeadersFromOutMessage() {
        return removeOAuthHeadersFromOutMessage;
    }

    public void setRemoveOAuthHeadersFromOutMessage(boolean removeOAuthHeadersFromOutMessage) {
        this.removeOAuthHeadersFromOutMessage = removeOAuthHeadersFromOutMessage;
    }

	@Override
	public void destroy() {
		System.out.println("---------------------------------------Super destroy------------------------------------");
		if (authenticator != null) {
            authenticator.destroy();
        } else {
        	System.out.println("EEEEEEEEEE Unable to destroy uninitialized authentication handler instance");
            //log.warn("Unable to destroy uninitialized authentication handler instance");
        }
		
	}
	
//	protected APIManagerConfigurationService getApiManagerConfigurationService() {
//        return ServiceReferenceHolder.getInstance().getApiManagerConfigurationService();
//    }
	
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "LEST_LOST_EXCEPTION_STACK_TRACE", justification = "The exception needs to thrown for fault sequence invocation")
    protected void initializeAuthenticator() {
        getAuthenticator().init(synapseEnvironment);
    }
	
	protected Authenticator getAuthenticator() {
        if (authenticator == null) {
            if (authorizationHeader == null) {
                try {
                    authorizationHeader = APIUtil.getOAuthConfigurationFromAPIMConfig(APIConstants.AUTHORIZATION_HEADER);
                    if (authorizationHeader == null) {
                        authorizationHeader = HttpHeaders.AUTHORIZATION;
                    }
                } catch (APIManagementException e) {
                	e.printStackTrace();
                    //log.error("Error while reading authorization header from APIM configurations", e);
                }
            }

            authenticator = new OAuthAuthenticator(authorizationHeader, removeOAuthHeadersFromOutMessage);
        }

        return authenticator;
    }

}
