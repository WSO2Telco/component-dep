package com.wso2telco.dep.verificationhandler.internal;

import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.concurrent.atomic.AtomicBoolean;

public class VerificationHandlerReferenceHolder {

    private static final VerificationHandlerReferenceHolder instance = new VerificationHandlerReferenceHolder();

    private RegistryService registryService;
    private RealmService realmService;

    private VerificationHandlerReferenceHolder() {
        registryService = null;

    }

    public static VerificationHandlerReferenceHolder getInstance() {
        return instance;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public RealmService getRealmService() {
        return realmService;
    }

    public void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }


}
