package com.wso2telco.internal;

import org.wso2.carbon.user.core.service.RealmService;

public class Util {
    private static RealmService realmService;

    public static RealmService getRealmService() {
        return realmService;
    }
    
    public static synchronized void setRealmService(RealmService realmSer) {

        realmService=realmSer;

 }

}
