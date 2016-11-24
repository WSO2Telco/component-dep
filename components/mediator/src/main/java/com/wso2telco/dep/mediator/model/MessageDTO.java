package com.wso2telco.dep.mediator.model;

import com.wso2telco.dep.mediator.internal.messageenum.ClientReference;

/**
 * Created by sasikala on 11/24/16.
 */
public class MessageDTO {

    private int msgId;
    private  String mdtrequestId;
    private  String clienString;
    private String message;
    private ClientReference refcode;
    private String refval;
    private long reportedTime;

    public String getMdtrequestId() {
        return mdtrequestId;
    }

    public void setMdtrequestId(String mdtrequestId) {
        this.mdtrequestId = mdtrequestId;
    }

    public String getClienString() {
        return clienString;
    }

    public void setClienString(String clienString) {
        this.clienString = clienString;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClientReference getRefcode() {
        return refcode;
    }

    public void setRefcode(ClientReference refcode) {
        this.refcode = refcode;
    }

    public String getRefval() {
        return refval;
    }

    public void setRefval(String refval) {
        this.refval = refval;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public long getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(long reportedTime) {
        this.reportedTime = reportedTime;
    }
}
