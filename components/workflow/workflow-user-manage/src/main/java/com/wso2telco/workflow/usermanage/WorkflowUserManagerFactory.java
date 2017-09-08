package com.wso2telco.workflow.usermanage;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

public class WorkflowUserManagerFactory implements SessionFactory {

    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return new WorkflowUserIdentityManager();
    }
}
