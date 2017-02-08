package com.wso2telco.dep.verificationhandler.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="com.wso2telco.dep.verificationhandler.internal.VerificationHandlerServiceComponent" immediate="true"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="user.realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 */

public class VerificationHandlerServiceComponent {

    private static final Log log = LogFactory.getLog(VerificationHandlerServiceComponent.class);

    protected void activate(ComponentContext ctxt) {

        log.info("Initializing the VerificationHandlerServiceComponent custom component...");
    }

    protected void deactivate(ComponentContext ctxt) {
        log.debug("Stopping the Synapse Handler Service Component. This is a custom handler bundle ...");
    }

    protected void setRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("Registry service initialized");
        }
        VerificationHandlerReferenceHolder.getInstance().setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {
        VerificationHandlerReferenceHolder.getInstance().setRegistryService(null);
    }

    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Relam service initialized");
        }
        VerificationHandlerReferenceHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        VerificationHandlerReferenceHolder.getInstance().setRealmService(null);
    }
}
