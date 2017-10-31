package com.wso2telco.workflow.activityclient;


import com.wso2telco.hub.workflow.extensions.exceptions.WorkflowExtensionException;
import com.wso2telco.workflow.model.TaskList;
import com.wso2telco.workflow.model.TaskVariableResponse;

import feign.Headers;
import feign.Param;
import feign.RequestLine;


public interface RestClient {
	@RequestLine("POST  query/tasks")
	@Headers("Content-Type: application/json")
	TaskList getTasks(ProcessSearchRequest request) throws WorkflowExtensionException ;
	
	@RequestLine("GET  runtime/tasks/{taskId}/variables")
	TaskVariableResponse[] getVariables(@Param("taskId") String taskId) throws WorkflowExtensionException ;
}
