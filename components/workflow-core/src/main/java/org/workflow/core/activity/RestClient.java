package org.workflow.core.activity;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.TaskList;
import org.workflow.core.model.TaskVariableResponse;

public interface RestClient {

    @RequestLine("POST  query/tasks")
    @Headers("Content-Type: application/json")
    TaskList getTasks(ProcessSearchRequest request) throws WorkflowExtensionException;

    @RequestLine("GET  runtime/tasks/{taskId}/variables")
    TaskVariableResponse[] getVariables(@Param("taskId") String taskId) throws WorkflowExtensionException;

    @RequestLine("GET history/historic-task-instances?taskCreatedAfter={start}&taskCreatedBefore={end}&processDefinitionKey={process}&taskAssignee={user}")
    TaskList getHistoricTasks(@Param("start") String start, @Param("end") String end, @Param("process") String process, @Param("user") String user);
}
