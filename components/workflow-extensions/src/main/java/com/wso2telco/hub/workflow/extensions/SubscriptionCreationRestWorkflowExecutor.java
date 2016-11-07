/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.hub.workflow.extensions;

import com.wso2telco.hub.workflow.extensions.beans.CreateProcessInstanceRequest;
import com.wso2telco.hub.workflow.extensions.beans.CreateProcessInstanceResponse;
import com.wso2telco.hub.workflow.extensions.beans.ProcessInstanceData;
import com.wso2telco.hub.workflow.extensions.beans.Variable;
import com.wso2telco.hub.workflow.extensions.exceptions.WorkflowErrorDecoder;
import com.wso2telco.hub.workflow.extensions.exceptions.WorkflowExtensionException;
import com.wso2telco.hub.workflow.extensions.impl.OperatorImpl;
import com.wso2telco.hub.workflow.extensions.impl.WorkflowAPIConsumerImpl;
import com.wso2telco.hub.workflow.extensions.interfaces.OperatorApi;
import com.wso2telco.hub.workflow.extensions.interfaces.WorkflowAPIConsumer;
import com.wso2telco.hub.workflow.extensions.rest.client.BusinessProcessApi;
import com.wso2telco.hub.workflow.extensions.util.DeploymentTypes;
import com.wso2telco.hub.workflow.extensions.util.WorkflowProperties;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIProvider;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.model.*;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.SubscriptionWorkflowDTO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.impl.workflow.GeneralWorkflowResponse;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowConstants;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowStatus;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.*;

public class SubscriptionCreationRestWorkflowExecutor extends WorkflowExecutor {

    private static final Log log = LogFactory.getLog(SubscriptionCreationRestWorkflowExecutor.class);

    private static final String TENANT_ID = "-1234";
    private static final String SUBSCRIPTION_CREATION_APPROVAL_PROCESS_NAME = "subscription_approval_process";
    private static final String API_NAME = "apiName";
    private static final String API_ID = "apiId";
    private static final String API_VERSION = "apiVersion";
    private static final String API_CONTEXT = "apiContext";
    private static final String API_PROVIDER = "apiProvider";
    private static final String SUBSCRIBER = "subscriber";
    private static final String APPLICATION_NAME = "applicationName";
    private static final String TIER_NAME = "tierName";
    private static final String APPLICATION_ID = "applicationId";
    private static final String APPLICATION_DESCRIPTION = "applicationDescription";
    private static final String API_TIERS = "apiTiers";
    private static final String WORKFLOW_REF_ID = "workflowRefId";
    private static final String CALL_BACK_URL = "callBackUrl";
    private static final String OPERATORS = "operators";
    private static final String DEPLOYMENT_TYPE = "deployment_type";
    private static final String OPERATORS_SYSTEM_PARAM = "OPERATORS";
    private static final String DEPLOYMENT_TYPE_SYSTEM_PARAM = "DEPLOYMENT_TYPE";
    private static final String ADMIN_PASSWORD = "adminPassword";
    private static final String SERVICE_HOST = "service.host";
    private static final String SERVICE_URL = "serviceURL";
    private static final String PUBLISHED_STATE = "PUBLISHED";

    private String serviceEndpoint;
    private String username;
    private String password;

    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_AM_SUBSCRIPTION_CREATION;
    }

    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        OperatorApi operatorApi = new OperatorImpl();

        try {
            if (log.isDebugEnabled()) {
                log.debug("Service endpoint: " + serviceEndpoint + ", username: " + username);
            }
            super.execute(workflowDTO);

            BusinessProcessApi httpClient = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                    .errorDecoder(new WorkflowErrorDecoder()).requestInterceptor(
                            new BasicAuthRequestInterceptor(username, password))
                    .target(BusinessProcessApi.class, serviceEndpoint);

            SubscriptionWorkflowDTO subscriptionWorkFlowDTO = (SubscriptionWorkflowDTO) workflowDTO;

            String callBackURL = subscriptionWorkFlowDTO.getCallbackUrl();
            int applicationId = APIUtil.getApplicationId(subscriptionWorkFlowDTO.getApplicationName(),
                    subscriptionWorkFlowDTO.getSubscriber());
            String applicationIdStr = String.valueOf(applicationId);

            // Obtain application details.
            APIConsumer consumer = APIManagerFactory.getInstance().getAPIConsumer(username);

            Application subscribedApp = consumer.getApplicationById(applicationId);

            String providerName = subscriptionWorkFlowDTO.getApiProvider().toLowerCase();
            String apiName = subscriptionWorkFlowDTO.getApiName();
            String version = subscriptionWorkFlowDTO.getApiVersion();
            APIIdentifier apiIdentifier = new APIIdentifier(providerName, apiName, version);
            API api = consumer.getAPI(apiIdentifier);
            //Why apiId is required,WorkflowAPIConsumer hasn't closed the db connections
            WorkflowAPIConsumer workFlowAPIConsumer = new WorkflowAPIConsumerImpl();
            String apiID = String.valueOf(workFlowAPIConsumer.getAPIID(apiIdentifier));
            Set<Tier> tierSet = api.getAvailableTiers();

            StringBuilder tiersStr = new StringBuilder();

            for (Iterator iterator = tierSet.iterator(); iterator.hasNext(); ) {
                Tier tier = (Tier) iterator.next();
                String tierName = tier.getName();
                tiersStr.append(tierName + ',');
            }

            // get API Publisher
            String publisherName;

            APIProvider apiProvider = null;
            try {
                apiProvider = APIManagerFactory.getInstance().getAPIProvider(providerName);
            } catch (APIManagementException e) {
                throw new WorkflowException("Unable to get API Provider for API provider " + providerName, e);
            }

            List<LifeCycleEvent> lifeCycleEvents = null;
            try {
                lifeCycleEvents = apiProvider.getLifeCycleEvents(new APIIdentifier(providerName, apiName, version));
            } catch (APIManagementException e) {
                throw new WorkflowException("Unable to get Lifecycle events for API: " +
                        apiName + ", version: " + version, e);
            }

            // get the final life cycle change
            int lastLCEventNumber = lifeCycleEvents.size() - 1;
            // check if last state is published
            if (!PUBLISHED_STATE.equalsIgnoreCase(lifeCycleEvents.get(lastLCEventNumber).getNewStatus())) {
                throw new WorkflowException("API: " + apiName + ", version: " + version + " is not in published state");
            }
            publisherName = lifeCycleEvents.get(lastLCEventNumber).getUserId();

            CreateProcessInstanceRequest processInstanceRequest =
                    new CreateProcessInstanceRequest(SUBSCRIPTION_CREATION_APPROVAL_PROCESS_NAME, TENANT_ID);
            processInstanceRequest.setBusinessKey(subscriptionWorkFlowDTO.getExternalWorkflowReference());

            Properties workflowProperties = WorkflowProperties.loadWorkflowProperties();
            String serviceURLString = workflowProperties.getProperty(SERVICE_HOST);


            Variable deploymentType = new Variable(DEPLOYMENT_TYPE, getDeploymentType());
            Variable subscribedApiName = new Variable(API_NAME, apiName);
            Variable subscribedApiId = new Variable(API_ID, apiID);
            Variable subscribedApiVersion = new Variable(API_VERSION, version);
            Variable subscribedApiContext = new Variable(API_CONTEXT, api.getContext());
            Variable apiPublisher = new Variable(API_PROVIDER, publisherName);
            Variable subscriber = new Variable(SUBSCRIBER, subscriptionWorkFlowDTO.getSubscriber());
            Variable appId = new Variable(APPLICATION_ID, applicationIdStr);
            Variable tierName = new Variable(TIER_NAME, subscriptionWorkFlowDTO.getTierName());
            Variable apiTierList = new Variable(API_TIERS, tiersStr.toString());
            Variable applicationName = new Variable(APPLICATION_NAME, subscriptionWorkFlowDTO.getApplicationName());
            Variable applicationDescription = new Variable(APPLICATION_DESCRIPTION, subscribedApp.getDescription());
            Variable workflowRefId =
                    new Variable(WORKFLOW_REF_ID, subscriptionWorkFlowDTO.getExternalWorkflowReference());
            Variable callBackUrl = new Variable(CALL_BACK_URL, callBackURL);
            Variable serviceURL = new Variable(SERVICE_URL, serviceURLString);
            Variable adminPassword = new Variable(ADMIN_PASSWORD, CarbonContext
                    .getThreadLocalCarbonContext()
                    .getUserRealm()
                    .getRealmConfiguration().getAdminPassword());

            Variable operators = new Variable(OPERATORS, operatorApi.getOperators());
            if (operators == null) {
                throw new WorkflowException("No operator(s) defined!!");
            }

            if (log.isDebugEnabled()) {
                log.debug("Application name: " + applicationName + ", deployment type: " + deploymentType +
                        ", callback url: " + callBackURL +
                        ", workflow reference id: " + workflowRefId + ", service endpoint: " + serviceEndpoint);
            }

            List<Variable> variables = new ArrayList<Variable>();

            variables.add(subscribedApiName);
            variables.add(subscribedApiId);
            variables.add(subscribedApiVersion);
            variables.add(subscribedApiContext);
            variables.add(apiPublisher);
            variables.add(subscriber);
            variables.add(appId);
            variables.add(tierName);
            variables.add(apiTierList);
            variables.add(applicationDescription);
            variables.add(deploymentType);
            variables.add(applicationName);
            variables.add(workflowRefId);
            variables.add(callBackUrl);
            variables.add(operators);
            variables.add(serviceURL);
            variables.add(adminPassword);

            processInstanceRequest.setVariables(variables);

            CreateProcessInstanceResponse processInstanceResponse;
            try {
                processInstanceResponse = httpClient.createProcessInstance(processInstanceRequest);
            } catch (WorkflowExtensionException e) {
                throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
            }

            if (log.isDebugEnabled()) {
                log.debug("Process definition url: " + processInstanceResponse.getProcessDefinitionUrl());
            }

            log.info("Subscription Creation approval process instance task with Id " +
                    processInstanceResponse.getActivityId() + " created successfully");
        } catch (APIManagementException e) {
            throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
        } catch (UserStoreException e) {
            log.error("Error in obtaining APIConsumer", e);
            throw new WorkflowException("Error in obtaining APIConsumer", e);
        }

        return new GeneralWorkflowResponse();
    }

    public WorkflowResponse complete(WorkflowDTO workFlowDTO) throws WorkflowException {
        workFlowDTO.setUpdatedTime(System.currentTimeMillis());
        super.complete(workFlowDTO);
        log.info("Subscription Creation [Complete] Workflow Invoked. Workflow ID : " +
                workFlowDTO.getExternalWorkflowReference() + "Workflow State : " + workFlowDTO.getStatus());

        if (WorkflowStatus.APPROVED.equals(workFlowDTO.getStatus()) ||
                WorkflowStatus.REJECTED.equals(workFlowDTO.getStatus())) {
            String status = null;

            if ("APPROVED".equals(workFlowDTO.getStatus().toString())) {
                status = APIConstants.SubscriptionStatus.UNBLOCKED;

            } else if ("REJECTED".equals(workFlowDTO.getStatus().toString())) {
                status = APIConstants.SubscriptionStatus.REJECTED;
            }

            if (status != null || status.length() > 0) {
                ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
                try {
                    apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workFlowDTO.getWorkflowReference()), status);
                } catch (APIManagementException e) {
                    log.error("Could not complete subscription creation workflow", e);
                    throw new WorkflowException("Could not complete subscription creation workflow", e);
                }

            } else {
                log.error("Could not complete subscription creation workflow. Approval status is invalid.");
            }
        }
        return null;
    }

    public void cleanUpPendingTask(String workflowExtRef) throws WorkflowException {
        BusinessProcessApi api = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
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
        return System.getProperty(DEPLOYMENT_TYPE_SYSTEM_PARAM, DeploymentTypes.HUB.getDeploymentType());
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
}

