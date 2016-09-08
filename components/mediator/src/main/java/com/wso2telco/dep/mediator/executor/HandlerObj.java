package com.wso2telco.dep.mediator.executor;

/**
 * Class holds handler data.
 */
public class HandlerObj {

    private String handlerName;
    private String handlerFullQulifiedName;
    private String handlerDescription;

    public HandlerObj (String handelerName, String handlerFullQulifiedName, String handlerDescription) {
        this.handlerName = handelerName;
        this.handlerDescription = handlerDescription;
        this.handlerFullQulifiedName = handlerFullQulifiedName;
    }

    public HandlerObj (String handlerName, String handlerFullQulifiedName) {
        this.handlerName = handlerName;
        this.handlerFullQulifiedName = handlerFullQulifiedName;
    }

    public String getHandlerFullQulifiedName() {
        return handlerFullQulifiedName;
    }

    public void setHandlerFullQulifiedName(String handlerFullQulifiedName) {
        this.handlerFullQulifiedName = handlerFullQulifiedName;
    }

    public String getHandlerDescription() {
        return handlerDescription;
    }

    public void setHandlerDescription(String handlerDescription) {
        this.handlerDescription = handlerDescription;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }
}
