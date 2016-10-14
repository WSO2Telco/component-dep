package com.wso2telco.hub.workflow.extensions;

import com.wso2telco.hub.workflow.extensions.beans.CreateProcessInstanceRequest;
import com.wso2telco.hub.workflow.extensions.beans.CreateProcessInstanceResponse;
import com.wso2telco.hub.workflow.extensions.beans.ProcessInstanceData;
import com.wso2telco.hub.workflow.extensions.beans.Variable;
import com.wso2telco.hub.workflow.extensions.exceptions.WorkflowErrorDecoder;
import com.wso2telco.hub.workflow.extensions.exceptions.WorkflowExtensionException;
import com.wso2telco.hub.workflow.extensions.impl.OperatorImpl;
import com.wso2telco.hub.workflow.extensions.interfaces.OperatorApi;
import com.wso2telco.hub.workflow.extensions.rest.client.BusinessProcessApi;
import com.wso2telco.hub.workflow.extensions.util.WorkflowProperties;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Tier;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.ApplicationWorkflowDTO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.workflow.GeneralWorkflowResponse;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowConstants;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowStatus;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.*;

public class ApplicationCreationRestWorkflowExecutor extends WorkflowExecutor {

<<<<<<< HEAD
    private static final Log log = LogFactory.getLog(ApplicationCreationRestWorkflowExecutor.class);

    private static final String TENANT_ID = "-1234";
    private static final String APPLICATION_CREATION_APPROVAL_PROCESS_NAME = "application_creation_approval_process";
    private static final String APPLICATION_NAME = "applicationName";
    private static final String APPLICATION_ID = "applicationId";
    private static final String WORKFLOW_REF_ID = "workflowRefId";
    private static final String CALL_BACK_URL = "callBackUrl";
    private static final String OPERATORS = "operators";
    private static final String DEPLOYMENT_TYPE = "deployment_type";
    private static final String OPERATORS_SYSTEM_PARAM = "OPERATORS";
    private static final String DEPLOYMENT_TYPE_SYSTEM_PARAM = "DEPLOYMENT_TYPE";
    private static final String TIER = "tier";
    private static final String DESCRIPTION = "description";
    private static final String TENANT_DOMAIN = "tenantDomain";
    private static final String USER_NAME = "userName";
    private static final String EXTERNAL_REFERENCE = "externalWorkflowReferenc";
    private static final String TIERS_STR = "tiersStr";
    private static final String ADMIN_PASSWORD = "adminPassword";
    private static final String SERVICE_HOST = "service.host";
    private static final String SERVICE_URL = "serviceURL";

    private String serviceEndpoint;
    private String username;
    private String password;

    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_AM_APPLICATION_CREATION;
    }

    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        OperatorApi operatorApi = new OperatorImpl();

        if (log.isDebugEnabled()) {
            log.debug("Service endpoint: " + serviceEndpoint + ", username: " + username);
        }
        super.execute(workflowDTO);
        try {
            BusinessProcessApi api = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .errorDecoder(new WorkflowErrorDecoder())
                    .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                    .target(BusinessProcessApi.class, serviceEndpoint);

            ApplicationWorkflowDTO appWorkFlowDTO = (ApplicationWorkflowDTO) workflowDTO;
            Application application = appWorkFlowDTO.getApplication();
            String callBackURL = appWorkFlowDTO.getCallbackUrl();

            CreateProcessInstanceRequest
                    processInstanceRequest = new CreateProcessInstanceRequest(APPLICATION_CREATION_APPROVAL_PROCESS_NAME,
                    TENANT_ID);
            processInstanceRequest.setBusinessKey(appWorkFlowDTO.getExternalWorkflowReference());
            // TODO: how to read 'deployment_type' / how to check if hub flow or hub-as-a-gateway flow??
            // currently this is read from a java system parameter
            APIConsumer consumer = APIManagerFactory.getInstance().getAPIConsumer();
            Set<Tier> tierSet = consumer.getTiers(APIConstants.TIER_APPLICATION_TYPE, PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain(true));
            StringBuilder tiersStr = new StringBuilder();

            for (Iterator iterator = tierSet.iterator(); iterator.hasNext(); ) {
                Tier tier = (Tier) iterator.next();
                String tierName = tier.getName();
                tiersStr.append(tierName + ',');
            }
=======
	private static final Log log = LogFactory.getLog(ApplicationCreationRestWorkflowExecutor.class);

	private static final String TENANT_ID = "-1234";
	private static final String APPLICATION_CREATION_APPROVAL_PROCESS_NAME = "application_creation_approval_process";
	private static final String APPLICATION_NAME = "applicationName";
	private static final String APPLICATION_ID = "applicationId";
	private static final String WORKFLOW_REF_ID = "workflowRefId";
	private static final String CALL_BACK_URL = "callBackUrl";
	private static final String OPERATORS = "operators";
	private static final String DEPLOYMENT_TYPE = "deployment_type";
	private static final String OPERATORS_SYSTEM_PARAM = "OPERATORS";
	private static final String DEPLOYMENT_TYPE_SYSTEM_PARAM = "DEPLOYMENT_TYPE";
	private static final String TIER = "tier";
	private static final String DESCRIPTION = "description";
	private static final String TENANT_DOMAIN = "tenantDomain";
	private static final String USER_NAME = "userName";
	private static final String EXTERNAL_REFERENCE = "externalWorkflowReferenc";
	private static final String TIERS_STR = "tiersStr";
    private static final String ADMIN_PASSWORD="adminPassword";
    private static final String SERVICE_HOST="service.host";
    private static final String SERVICE_URL="serviceURL";


	private String serviceEndpoint;
	private String username;
	private String password;

	public String getWorkflowType() {
		return WorkflowConstants.WF_TYPE_AM_APPLICATION_CREATION;
	}

	public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        OperatorApi operatorApi=new OperatorImpl();

		if (log.isDebugEnabled()) {
			log.debug("Service endpoint: " + serviceEndpoint + ", username: " + username);
		}
		super.execute(workflowDTO);
		try{
			BusinessProcessApi api = Feign.builder()
			                              .encoder(new JacksonEncoder())
			                              .decoder(new JacksonDecoder())
			                              .errorDecoder(new WorkflowErrorDecoder())
			                              .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
			                              .target(BusinessProcessApi.class, serviceEndpoint);

			ApplicationWorkflowDTO appWorkFlowDTO = (ApplicationWorkflowDTO) workflowDTO;
			Application application = appWorkFlowDTO.getApplication();
			String callBackURL = appWorkFlowDTO.getCallbackUrl();

			CreateProcessInstanceRequest
					processInstanceRequest = new CreateProcessInstanceRequest(APPLICATION_CREATION_APPROVAL_PROCESS_NAME,
					                                                          TENANT_ID);
			processInstanceRequest.setBusinessKey(appWorkFlowDTO.getExternalWorkflowReference());
			// TODO: how to read 'deployment_type' / how to check if hub flow or hub-as-a-gateway flow??
			// currently this is read from a java system parameter

			APIConsumer consumer = APIManagerFactory.getInstance().getAPIConsumer();
			Set<Tier> tierSet= consumer.getTiers();


			StringBuilder tiersStr = new StringBuilder();

			for (Iterator iterator = tierSet.iterator(); iterator.hasNext(); ) {
				Tier tier = (Tier) iterator.next();
				String tierName = tier.getName();
				tiersStr.append(tierName + ',');
			}
>>>>>>> b52bad2... workflow improvement

            Properties workflowProperties = WorkflowProperties.loadWorkflowProperties();
            String serviceURLString = workflowProperties.getProperty(SERVICE_HOST);

            Variable deploymentType = new Variable(DEPLOYMENT_TYPE, getDeploymentType());
            Variable applicationName = new Variable(APPLICATION_NAME, application.getName());
            Variable workflorRefId = new Variable(WORKFLOW_REF_ID, appWorkFlowDTO.getExternalWorkflowReference());
            Variable callBackUrl = new Variable(CALL_BACK_URL, callBackURL);
            Variable applicationId = new Variable(APPLICATION_ID, String.valueOf(appWorkFlowDTO.getWorkflowReference()));
            Variable tier = new Variable(TIER, application.getTier());
            Variable description = new Variable(DESCRIPTION, application.getDescription());
            Variable tenantDomain = new Variable(TENANT_DOMAIN, appWorkFlowDTO.getTenantDomain());
            Variable userName = new Variable(USER_NAME, appWorkFlowDTO.getUserName());
            Variable externalWorkflowReference = new Variable(EXTERNAL_REFERENCE, appWorkFlowDTO.getExternalWorkflowReference());
            Variable tiers = new Variable(TIERS_STR, tiersStr.toString());
            Variable serviceURL = new Variable(SERVICE_URL, serviceURLString);
            Variable adminPassword = new Variable(ADMIN_PASSWORD, CarbonContext
                    .getThreadLocalCarbonContext()
                    .getUserRealm()
                    .getRealmConfiguration().getAdminPassword());
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            Variable operators = new Variable(OPERATORS, operatorApi.getOperators());
            if (operators == null) {
                throw new WorkflowException("No operator(s) defined!!");
            }
            if (log.isDebugEnabled()) {
                log.debug("Application name: " + applicationName + ", deployment type: " + deploymentType + ", callback url: " + callBackURL +
                        ", workflow reference id: " + workflorRefId + ", service endpoint: " + serviceEndpoint + ", tier: " + tier + ", description: " + description +
                        ", tenantDomain: " + tenantDomain + ", userName: " + userName + ",externalWorkflowReference :" + externalWorkflowReference + ",tiers :" + tiers);
            }

            List<Variable> variables = new ArrayList<Variable>();

            variables.add(deploymentType);
            variables.add(applicationName);
            variables.add(workflorRefId);
            variables.add(callBackUrl);
            variables.add(operators);
            variables.add(applicationId);
            variables.add(tier);
            variables.add(description);
            variables.add(tenantDomain);
            variables.add(userName);
            variables.add(externalWorkflowReference);
            variables.add(tiers);
=======
=======
>>>>>>> b52bad2... workflow improvement
=======
>>>>>>> b52bad2... workflow improvement
=======
>>>>>>> b52bad2... workflow improvement

			Variable operators = new Variable(OPERATORS, operatorApi.getOperators());
			if (operators == null) {
				throw new WorkflowException("No operator(s) defined!!");
			}

			if (log.isDebugEnabled()) {
				log.debug("Application name: " + applicationName + ", deployment type: " + deploymentType + ", callback url: " + callBackURL +
				          ", workflow reference id: " + workflorRefId + ", service endpoint: " + serviceEndpoint + ", tier: " + tier + ", description: " + description +
				          ", tenantDomain: " + tenantDomain + ", userName: " + userName +",externalWorkflowReference :"+externalWorkflowReference + ",tiers :"+tiers);
			}

			List<Variable> variables = new ArrayList<Variable>();

			variables.add(deploymentType);
			variables.add(applicationName);
			variables.add(workflorRefId);
			variables.add(callBackUrl);
			variables.add(operators);
			variables.add(applicationId);
			variables.add(tier);
			variables.add(description);
			variables.add(tenantDomain);
			variables.add(userName);
			variables.add(externalWorkflowReference);
			variables.add(tiers);
>>>>>>> b52bad2... workflow improvement
            variables.add(serviceURL);
            variables.add(adminPassword);
            processInstanceRequest.setVariables(variables);
            CreateProcessInstanceResponse processInstanceResponse;

            try {
                processInstanceResponse = api.createProcessInstance(processInstanceRequest);
            } catch (WorkflowExtensionException e) {
                throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
            }

            if (log.isDebugEnabled()) {
                log.debug("Process definition url: " + processInstanceResponse.getProcessDefinitionUrl());
            }
            log.info("Application Creation approval process instance task with business key " +
                    appWorkFlowDTO.getExternalWorkflowReference() + " created successfully");
        } catch (APIManagementException e) {
            log.error("Error in obtaining APIConsumer", e);
            throw new WorkflowException("Error in obtaining APIConsumer", e);
        } catch (UserStoreException e) {
            log.error("Error in obtaining APIConsumer", e);
            throw new WorkflowException("Error in obtaining APIConsumer", e);
        }
        return new GeneralWorkflowResponse();
<<<<<<< HEAD
    }

    public WorkflowResponse complete(WorkflowDTO workFlowDTO) throws WorkflowException {
        workFlowDTO.setUpdatedTime(System.currentTimeMillis());
        ApiMgtDAO dao = ApiMgtDAO.getInstance();
        try {
            if (dao.getApplicationById(Integer.parseInt(workFlowDTO.getWorkflowReference())) != null) {
                super.complete(workFlowDTO);
                log.info("Application Creation [Complete] Workflow Invoked. Workflow ID : " + workFlowDTO
                        .getExternalWorkflowReference() + "Workflow State : " + workFlowDTO.getStatus());

                String status = null;
                if (WorkflowStatus.CREATED.equals(workFlowDTO.getStatus())) {
                    status = APIConstants.ApplicationStatus.APPLICATION_CREATED;
                } else if (WorkflowStatus.REJECTED.equals(workFlowDTO.getStatus())) {
                    status = APIConstants.ApplicationStatus.APPLICATION_REJECTED;
                } else if (WorkflowStatus.APPROVED.equals(workFlowDTO.getStatus())) {
                    status = APIConstants.ApplicationStatus.APPLICATION_APPROVED;
                }

                try {
                    dao.updateApplicationStatus(Integer.parseInt(workFlowDTO.getWorkflowReference()), status);
                } catch (APIManagementException e) {
                    String msg = "Error occurred when updating the status of the Application creation " + "process";
                    log.error(msg, e);
                    throw new WorkflowException(msg, e);
                }

            } else {
                String msg = "Application does not exist";
                throw new WorkflowException(msg);
            }
        } catch (APIManagementException e) {
            String msg = "Error occurred when retrieving the Application creation with workflow ID :" + workFlowDTO
                    .getWorkflowReference();
            log.error(msg, e);
            throw new WorkflowException(msg, e);
        }

        log.info("Application Creation approval process completed. Workflow ID : " + workFlowDTO
                .getExternalWorkflowReference() + " Workflow State : " + workFlowDTO.getStatus());

        return new GeneralWorkflowResponse();
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        BusinessProcessApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new WorkflowErrorDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                .target(BusinessProcessApi.class, serviceEndpoint);

        ProcessInstanceData instanceData = null;
        try {
            instanceData = api.getProcessInstances(workflowExtRef);
        } catch (WorkflowExtensionException e) {
            throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
        }

        // should be only one process instance for this business key, hence get the 0th element
        try {
            api.deleteProcessInstance(Integer.toString(instanceData.getData().get(0).getId()));
        } catch (WorkflowExtensionException e) {
            throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
        }

        log.info("Application Creation approval process instance task with business key " +
                workflowExtRef + " deleted successfully");
    }

    private String getDeploymentType() {
        return System.getProperty(DEPLOYMENT_TYPE_SYSTEM_PARAM, "hub");
    }


    public List<WorkflowDTO> getWorkflowDetails(String s) throws WorkflowException {
        // not implemented
        return null;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
=======
	}

	public WorkflowResponse complete(WorkflowDTO workFlowDTO) throws WorkflowException {
		workFlowDTO.setUpdatedTime(System.currentTimeMillis());
		ApiMgtDAO dao = new ApiMgtDAO();
		try {
			if (dao.getApplicationById(Integer.parseInt(workFlowDTO.getWorkflowReference())) != null) {

				super.complete(workFlowDTO);
				log.info("Application Creation [Complete] Workflow Invoked. Workflow ID : " + workFlowDTO
						.getExternalWorkflowReference() + "Workflow State : " + workFlowDTO.getStatus());

				String status = null;
				if (WorkflowStatus.CREATED.equals(workFlowDTO.getStatus())) {
					status = APIConstants.ApplicationStatus.APPLICATION_CREATED;
				} else if (WorkflowStatus.REJECTED.equals(workFlowDTO.getStatus())) {
					status = APIConstants.ApplicationStatus.APPLICATION_REJECTED;
				} else if (WorkflowStatus.APPROVED.equals(workFlowDTO.getStatus())) {
					status = APIConstants.ApplicationStatus.APPLICATION_APPROVED;
				}

				try {
					dao.updateApplicationStatus(Integer.parseInt(workFlowDTO.getWorkflowReference()), status);
				} catch (APIManagementException e) {
					String msg = "Error occurred when updating the status of the Application creation " + "process";
					log.error(msg, e);
					throw new WorkflowException(msg, e);
				}

			} else {
				String msg = "Application does not exist";
				throw new WorkflowException(msg);
			}
		} catch (APIManagementException e) {
			String msg = "Error occurred when retrieving the Application creation with workflow ID :" + workFlowDTO
					.getWorkflowReference();
			log.error(msg, e);
			throw new WorkflowException(msg, e);
		}

		log.info("Application Creation approval process completed. Workflow ID : " + workFlowDTO
				.getExternalWorkflowReference() + " Workflow State : " + workFlowDTO.getStatus());

		return new GeneralWorkflowResponse();
	}

	public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
		BusinessProcessApi api = Feign.builder()
		                              .encoder(new JacksonEncoder())
		                              .decoder(new JacksonDecoder())
		                              .errorDecoder(new WorkflowErrorDecoder())
		                              .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
		                              .target(BusinessProcessApi.class, serviceEndpoint);

		ProcessInstanceData instanceData = null;
		try {
			instanceData = api.getProcessInstances(workflowExtRef);
		} catch (WorkflowExtensionException e) {
			throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
		}

		// should be only one process instance for this business key, hence get the 0th element
		try {
			api.deleteProcessInstance(Integer.toString(instanceData.getData().get(0).getId()));
		} catch (WorkflowExtensionException e) {
			throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
		}

		log.info("Application Creation approval process instance task with business key " +
		         workflowExtRef + " deleted successfully");
	}

	private String getDeploymentType () {
		return System.getProperty(DEPLOYMENT_TYPE_SYSTEM_PARAM, "hub");
	}



	public List<WorkflowDTO> getWorkflowDetails(String s) throws WorkflowException {
		// not implemented
		return null;
	}

	public String getServiceEndpoint() {
		return serviceEndpoint;
	}

	public void setServiceEndpoint(String serviceEndpoint) {
		this.serviceEndpoint = serviceEndpoint;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
>>>>>>> b52bad2... workflow improvement
}
