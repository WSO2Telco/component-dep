package com.wso2telco.workflow.usermanage;


import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.user.core.service.RealmService;

public class ServicesHolder {

    private static ServicesHolder instance;
    private RealmService realmService;

    public ServicesHolder() {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        this.realmService = (RealmService) carbonContext.getOSGiService(RealmService.class, null);
    }

    public static ServicesHolder getInstance() {
        if (instance == null) {
            instance = new ServicesHolder();
        }

        return instance;
    }

    public RealmService getRealmService() {
        return realmService;
    }
}
