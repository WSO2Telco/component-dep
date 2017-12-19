package org.workflow.core.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "order",
        "size",
        "sort",
        "start",
        "total"
})
public class TaskMetadata {

    @JsonProperty("order")
    private String order;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("sort")
    private String sort;
    @JsonProperty("start")
    private Integer start;
    @JsonProperty("total")
    private Integer total;

    public TaskMetadata(TaskList taskList) {

        this.order = taskList.getOrder();
        this.size = taskList.getSize();
        this.sort = taskList.getSort();
        this.start = taskList.getStart();
        this.total = taskList.getTotal();

    }

    @JsonProperty("order")
    public String getOrder() {
        return order;
    }


    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }


    @JsonProperty("sort")
    public String getSort() {
        return sort;
    }


    @JsonProperty("start")
    public Integer getStart() {
        return start;
    }


    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

}