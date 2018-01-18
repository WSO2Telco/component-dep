package org.workflow.core.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "time",
        "offset",
        "unformatted"
})
public class CreateTime {

    @JsonProperty("date")
    private String date;
    @JsonProperty("time")
    private String time;
    @JsonProperty("offset")
    private String offset;
    @JsonProperty("unformatted")
    private String unformatted;

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("offset")
    public String getOffset() {
        return offset;
    }

    @JsonProperty("offset")
    public void setOffset(String offset) {
        this.offset = offset;
    }

    @JsonProperty("unformatted")
    public String getUnformatted() {
        return unformatted;
    }

    @JsonProperty("unformatted")
    public void setUnformatted(String unformatted) {
        this.unformatted = unformatted;
    }

}