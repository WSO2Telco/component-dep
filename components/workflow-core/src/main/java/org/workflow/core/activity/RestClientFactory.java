package org.workflow.core.activity;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.WorkflowErrorDecoder;
import org.workflow.core.restclient.RateRestClient;
import org.workflow.core.util.WorkFlowHealper;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.user.api.UserStoreException;

import com.wso2telco.core.dbutils.exception.BusinessException;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class RestClientFactory {
	private  Log    log = LogFactory.getLog(RestClientFactory.class);
	private  String username;
	private   String password;
	private Map<String,ActivityRestClient> restClientMap ;
	private static RestClientFactory instance;
	final private String defaultHost="http://localhost";
	final private String defaultPort;

	/**
	 * private constructor limits the unwonted object creation.Admin username & password
	 * are initialize at the object creation
	 * @throws UserStoreException
	 */
	private RestClientFactory() throws BusinessException{
		try {
			username=  CarbonContext
			         .getThreadLocalCarbonContext()
			         .getUserRealm()
			         .getRealmConfiguration().getAdminUserName();

			password= CarbonContext
			         .getThreadLocalCarbonContext()
			         .getUserRealm()
			         .getRealmConfiguration().getAdminPassword();
			
			restClientMap = new HashMap<String,ActivityRestClient> ();
			defaultPort = System.getProperty("carbon.http.port");
			
		} catch (UserStoreException e) {
			log.error("error at static initializer ActivityClientFactory ",e);
			throw new BusinessException(e);
		}
	}
	public static RestClientFactory getInstance() throws BusinessException {
		if(instance==null) {
			instance =new RestClientFactory();
		}
		return instance;
	}
	
	
	/**
	 * return a feign http client with for activity  workflow
	 * @return
	 */

	public RateRestClient getRateClient() {

		RateRestClient appClient = Feign.builder().encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder())
				.errorDecoder(new WorkflowErrorDecoder())
				.requestInterceptor(new BasicAuthRequestInterceptor(username, password))
//				.logger(new Logger.JavaLogger().appendToFile("/install/wso2telcohub-2.2.1-SNAPSHOT/repository/logs/wso2carbon.log"))
//                .logLevel(feign.Logger.Level.FULL)
				.target(RateRestClient.class,defaultHost+":"+defaultPort + "/ratecard-service/ratecardservice/");
		

		
		return appClient;
	
	}
	
	
	
	/**
	 * return a feign http client with for activity  workflow
	 * @return
	 */

	public ActivityRestClient getClient(final String processDefinitionKey) {
		if(restClientMap.containsKey(processDefinitionKey.trim() )) {
			return restClientMap.get( processDefinitionKey.trim());
		}else {
			ActivityRestClient appClient = Feign.builder().encoder(new JacksonEncoder())
					.decoder(new JacksonDecoder())
					.errorDecoder(new WorkflowErrorDecoder())
					.requestInterceptor(new BasicAuthRequestInterceptor(username, password))
//					.logger(new Logger.JavaLogger().appendToFile("/install/wso2telcohub-2.2.1-SNAPSHOT/repository/logs/wso2carbon.log"))
//                    .logLevel(feign.Logger.Level.FULL)
					.requestInterceptor(new ProcessTypeInterCeptor(processDefinitionKey))
					.target(ActivityRestClient.class, WorkFlowHealper.getInstance().getWorkflowServiceEndPoint());
			

			restClientMap.put( processDefinitionKey.trim(), appClient);
			
			return appClient;
		}
	}
		/**
		 * feign intercepter for injecting process type
		 *
		 */
	class ProcessTypeInterCeptor implements RequestInterceptor {
		private String processDefinitionKey;

		ProcessTypeInterCeptor(String processDefinitionKey) {
			this.processDefinitionKey = processDefinitionKey;
		}

		@Override
		public void apply(RequestTemplate template) {
			template.query("processDefinitionKey",this.processDefinitionKey );

		}

	}
}
