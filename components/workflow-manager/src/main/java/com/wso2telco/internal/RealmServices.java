package com.wso2telco.internal;

 
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

/**
 * @scr.component name="org.wso2.carbon.am" immediate="true"
 * @scr.reference name="user.realmservice.default"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"
 * unbind="unsetRealmService"  
  **/
public class RealmServices {
    private static ConfigurationContextService configContextService = null;

    protected void activate(ComponentContext context) {
        BundleContext bundleContext = context.getBundleContext();       
      //  bundleContext.registerService(ApplicationManagementService.class.getName(), mgtService, null);         
    }

    protected void deactivate(ComponentContext context) {
         
    }

    protected void setRealmService(RealmService realmService) {

        Util.setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        Util.setRealmService(null);
    }

}
