package com.wso2telco.dep.server.startup.observer.user.role.updater;

import com.wso2telco.dep.server.startup.observer.internal.ServiceReferenceHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.api.Permission;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.service.RealmService;


public class UserRole {

    private static Log log = LogFactory.getLog(UserRole.class);

    public void addRoles(String role) {

        try {
            RealmService realmService = ServiceReferenceHolder.getInstance().getRealmService();
            UserRealm realm = realmService.getBootstrapRealm();
            UserStoreManager manager = realm.getUserStoreManager();
            if (!manager.isExistingRole(role)) {
                if (log.isDebugEnabled()) {
                    log.debug("Creating subscriber role: " + role);
                }
                Permission[] subscriberPermissions = new Permission[]{new Permission("/permission/admin/login", "ui.execute")};
                String superTenantName = ServiceReferenceHolder.getInstance().getRealmService().getBootstrapRealmConfiguration().getAdminUserName();
                String[] userList = new String[]{superTenantName};
                manager.addRole(role, userList, subscriberPermissions);
            }

        } catch (Exception e) {
            log.error(e);
        }
    }
}

