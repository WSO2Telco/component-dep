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
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.impl.workflow.ApplicationDeletionSimpleWorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;

public class ApplicationDeletionRestWorkflowExecutor extends ApplicationDeletionSimpleWorkflowExecutor {

    private static final Log log = LogFactory.getLog(ApplicationDeletionRestWorkflowExecutor.class);

    private static final String APPLICATION_NAME = "applicationName";
    private static final String APPLICATION_TIER = "applicationTier";
    private static final String USER_NAME = "userName";

    private String serviceEndpoint;
    private String username;
    private String password;

    @Override
    public WorkflowResponse complete(WorkflowDTO workflowDTO) throws WorkflowException {
        WorkflowResponse response = super.complete(workflowDTO);
        BusinessProcessApi api = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                .target(BusinessProcessApi.class, serviceEndpoint);
        final String extWorkflowRef = workflowDTO.getExternalWorkflowReference();
        try {
            ProcessInstanceData instanceData = api.getProcessInstances(extWorkflowRef);
            if (instanceData != null && instanceData.getData().isEmpty()) {
                api.deleteProcessInstance(Integer.toString(instanceData.getData().get(0).getId()));
                appDeleteWfAuditLog(workflowDTO);
            }
        } catch (Exception e) {
            throw new WorkflowException("Error occurred while deleting the workflow: " + e.getMessage(), e);
        }
        return response;
    }

    private void appDeleteWfAuditLog(WorkflowDTO appWorkFlowDTO) {
        JSONObject appWorkflow = new JSONObject();
        appWorkflow.put("workflow_id", appWorkFlowDTO.getExternalWorkflowReference());
        appWorkflow.put(APIConstants.AuditLogConstants.STATUS, appWorkFlowDTO.getStatus().toString());
        appWorkflow.put(APIConstants.AuditLogConstants.APPLICATION_ID, appWorkFlowDTO.getWorkflowReference());
        appWorkflow.put(APIConstants.AuditLogConstants.APPLICATION_NAME, appWorkFlowDTO.getProperties(APPLICATION_NAME));
        appWorkflow.put(APIConstants.AuditLogConstants.TIER, appWorkFlowDTO.getProperties(APPLICATION_TIER));
        appWorkflow.put("subscriber", appWorkFlowDTO.getProperties(USER_NAME));

        APIUtil.logAuditMessage(
            "ApplicationApprovalWorkflow",
            appWorkflow.toString(),
            APIConstants.AuditLogConstants.DELETED,
            appWorkFlowDTO.getProperties(USER_NAME)
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
