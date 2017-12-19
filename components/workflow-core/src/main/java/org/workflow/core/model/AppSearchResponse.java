package org.workflow.core.model;

/**
 * Created by manoj on 10/17/17.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "applicationTasks",
        "metadata"
})
public class AppSearchResponse {

    @JsonProperty("applicationTasks")
    private List<ApplicationTask> applicationTasks = null;
    @JsonProperty("metadata")
    private TaskMetadata metadata;

    @JsonProperty("applicationTasks")
    public List<ApplicationTask> getApplicationTasks() {
        return applicationTasks;
    }

    @JsonProperty("applicationTasks")
    public void setApplicationTasks(List<ApplicationTask> applicationTasks) {
        this.applicationTasks = applicationTasks;
    }

    @JsonProperty("metadata")
    public TaskMetadata getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(TaskMetadata metadata) {
        this.metadata = metadata;
    }
}



