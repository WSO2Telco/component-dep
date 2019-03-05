package com.wso2telco.dep.apimlogging;

import org.wso2.carbon.registry.core.jdbc.handlers.filters.MediaTypeMatcher;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;

public class APILogFilter extends MediaTypeMatcher {

    @Override
    public boolean handleApplyTag(RequestContext requestContext) throws RegistryException {
        return true;
    }

    @Override
    public boolean handleRemoveTag(RequestContext requestContext) throws RegistryException {
        return true;
    }
}