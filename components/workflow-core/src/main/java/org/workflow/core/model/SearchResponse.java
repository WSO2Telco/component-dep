package org.workflow.core.model;

/**
 * Created by manoj on 10/17/17.
 */

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "applicationTasks",
        "metadata"
})
public class SearchResponse {

    @JsonProperty("applicationTasks")
    private List<ApplicationTask> applicationTasks = null;
    @JsonProperty("metadata")
    private TaskMetadata metadata;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}



