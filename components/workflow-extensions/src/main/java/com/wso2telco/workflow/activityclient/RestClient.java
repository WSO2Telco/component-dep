package com.wso2telco.workflow.activityclient;


import com.wso2telco.hub.workflow.extensions.exceptions.WorkflowExtensionException;
import com.wso2telco.workflow.model.TaskList;

import feign.Headers;
import feign.RequestLine;


public interface RestClient {
	@RequestLine("POST  query/tasks")
	@Headers("Content-Type: application/json")
	TaskList getTasks(ProcessSearchRequest request) throws WorkflowExtensionException ;

}
