package com.wso2telco.dep.mediator.executor;

/**
 * Class holds executor data;
 */
public class ExecutorObj {

    private String executorName;
    private String executorDescription;
    private String executorId;
    private String fullQulifiedName;

    public ExecutorObj (String executorName, String executorDescription) {
        this.executorName = executorName;
        this.executorDescription = executorDescription;
    }

    public ExecutorObj (String executorId, String executorName, String executorDescription, String fullQulifiedName) {
        this.executorId = executorId;
        this.executorName = executorName;
        this.executorDescription = executorDescription;
        this.fullQulifiedName = fullQulifiedName;
    }

    public ExecutorObj (String executorId,String executorName, String executorDescription) {
        this.executorId = executorId;
        this.executorName = executorName;
        this.executorDescription = executorDescription;
    }

    public String getExecutorDescription() {
        return executorDescription;
    }

    public void setExecutorDescription(String executorDescription) {
        this.executorDescription = executorDescription;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }


    public String getExecutorId() { return executorId; }

    public void setExecutorId(String executorId) { this.executorId = executorId; }

    //TODO: serialize or not
}
