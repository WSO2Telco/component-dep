package com.wso2telco.hub.workflow.extensions;

import com.wso2telco.hub.workflow.extensions.beans.ProcessInstanceData;
import com.wso2telco.hub.workflow.extensions.rest.client.BusinessProcessApi;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.workflow.core.execption.WorkflowExtensionException;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.impl.workflow.SubscriptionDeletionSimpleWorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;

public class SubscriptionDeletionRestWorkflowExecutor extends SubscriptionDeletionSimpleWorkflowExecutor {

    private static final Log log = LogFactory.getLog(SubscriptionDeletionRestWorkflowExecutor.class);

    private static final String API_NAME = "apiName";
    private static final String API_VERSION = "apiVersion";
    private static final String APPLICATION_ID = "applicationId";
    private static final String APPLICATION_NAME = "applicationName";
    private static final String TIER_NAME = "tierName";
    private static final String SUBSCRIBER = "subscriber";

    private String serviceEndpoint;
    private String username;
    private String password;

    @Override
    public WorkflowResponse complete(WorkflowDTO workflowDTO) throws WorkflowException {
        final WorkflowResponse response = super.complete(workflowDTO);
        BusinessProcessApi api = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                .target(BusinessProcessApi.class, serviceEndpoint);
        final String extWorkflowRef = workflowDTO.getExternalWorkflowReference();
        try {
            ProcessInstanceData instanceData = api.getProcessInstances(extWorkflowRef);
            if (instanceData != null && instanceData.getData().isEmpty()) {
                api.deleteProcessInstance(Integer.toString(instanceData.getData().get(0).getId()));
                subDeleteWfAuditLog(workflowDTO);
            }
        } catch (WorkflowExtensionException e) {
            throw new WorkflowException("WorkflowException: " + e.getMessage(), e);
        }
        return response;
    }

    private void subDeleteWfAuditLog(WorkflowDTO subWorkFlowDTO) {
        JSONObject subWorkflow = new JSONObject();
        subWorkflow.put("workflow_id", subWorkFlowDTO.getExternalWorkflowReference());
        subWorkflow.put(APIConstants.AuditLogConstants.STATUS, subWorkFlowDTO.getStatus().toString());
        subWorkflow.put(APIConstants.AuditLogConstants.API_NAME, subWorkFlowDTO.getProperties(API_NAME));
        subWorkflow.put("api_version", subWorkFlowDTO.getProperties(API_VERSION));
        subWorkflow.put(APIConstants.AuditLogConstants.APPLICATION_ID, subWorkFlowDTO.getProperties(APPLICATION_ID));
        subWorkflow.put(APIConstants.AuditLogConstants.APPLICATION_NAME, subWorkFlowDTO.getProperties(APPLICATION_NAME));
        subWorkflow.put(APIConstants.AuditLogConstants.TIER, subWorkFlowDTO.getProperties(TIER_NAME));
        subWorkflow.put("subscriber", subWorkFlowDTO.getProperties(SUBSCRIBER));
        APIUtil.logAuditMessage(
            "SubscriptionApprovalWorkflow",
            subWorkflow.toString(),
            APIConstants.AuditLogConstants.DELETED,
            subWorkFlowDTO.getProperties(SUBSCRIBER)
        );
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
