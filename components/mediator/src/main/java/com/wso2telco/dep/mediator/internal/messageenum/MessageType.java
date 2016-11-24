package com.wso2telco.dep.mediator.internal.messageenum;

/**
 * Created by sasikala on 11/24/16.
 */
public enum MessageType {

    PAYMENT_REQUEST(1,"Payment Request") ,
    PAYMENT_RESPONSE(2,"Payment Response"),
    REFUND_REQUEST(3,"Refundrequest"),
    REFUND_RESPONSE(4,"RefundResponse");

    private int messageDid ;
    private String desc;

    private MessageType(final int messageType,final  String desc){
        this.messageDid = messageType;
        this.desc =desc;
    }

    public int getMessageDid() {
        return messageDid;
    }

    public String getDesc() {
        return desc;
    }
}
