package com.wso2telco.workflow.usermanage;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;

public class WorkflowGroupManagerFactory implements SessionFactory {
    @Override
    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return new WorkflowGroupIdentityManager();
    }
}
