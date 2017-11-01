package org.workflow.core.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.WorkflowErrorDecoder;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.WorkFlowHealper;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.user.api.UserStoreException;

import com.wso2telco.core.dbutils.exception.BusinessException;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class ActivityClientFactory {
	private  Log    log = LogFactory.getLog(ActivityClientFactory.class);
	private  String username;
	private   String password;
	private  RestClient appClient;
	private  RestClient subscriptionClient;
	
	private static ActivityClientFactory instance;
	
	/**
	 * private constructor limits the unwonted object creation.Admin username & password 
	 * are initialize at the object creation
	 * @throws UserStoreException
	 */
	private ActivityClientFactory() throws BusinessException{
		try {
			username=  CarbonContext
			         .getThreadLocalCarbonContext()
			         .getUserRealm()
			         .getRealmConfiguration().getAdminUserName();
		
			password= CarbonContext
			         .getThreadLocalCarbonContext()
			         .getUserRealm()
			         .getRealmConfiguration().getAdminPassword();
		} catch (UserStoreException e) {
			log.error("error at static initializer ActivityClientFactory ",e);
			throw new BusinessException(e);
		}
	}
	public static ActivityClientFactory getInstance() throws BusinessException {
		if(instance==null) {
			instance =new ActivityClientFactory();
		}
		return instance;
	}
	/**
	 * return a feign http client with for activity  workflow
	 * @param authHeader
	 * @return
	 */
	public RestClient getClient() {
		if(appClient==null) {
			appClient = Feign.builder().encoder(new JacksonEncoder())
					.decoder(new JacksonDecoder())
					.errorDecoder(new WorkflowErrorDecoder())
					.requestInterceptor(new BasicAuthRequestInterceptor(username, password))
					.requestInterceptor(new ProcessTypeInterCeptor(	DeploymentTypes.getByName( WorkFlowHealper.getDeploymentType()).getAppProcessType()))
					.target(RestClient.class, WorkFlowHealper.getInstance().getWorkflowServiceEndPoint());
		}
		return appClient;
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
			template.query("processDefinitionKey", this.processDefinitionKey);
			
		}

	}
}
