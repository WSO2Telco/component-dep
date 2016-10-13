package com.wso2telco.workflow.userstore;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.service.RealmService;

public class RemoteUserStoreManagerImpl implements RemoteUserStoreManager {
	
	private static Log log = LogFactory.getLog(RemoteUserStoreManagerImpl.class);

	@Override
	public String[] getUserListOfRole(String role) {
        String[] userList = null;
		try {
           log.info("Getting users for the role : " + role);
            RealmService realmService = ServiceReferenceHolder.getInstance().getRealmService();
            UserRealm realm = realmService.getBootstrapRealm();
            UserStoreManager manager = realm.getUserStoreManager();
            userList = manager.getUserListOfRole(role);
        }catch(Exception ex){
            ex.printStackTrace();
        }
		return userList;
	}

	@Override
	public String getUserClaimValue(String userName, String claim) {
        String claimValue = "";
        log.info("Getting claim value for the userName : " + userName + " and claim : " + claim);
         try {
            RealmService realmService = ServiceReferenceHolder.getInstance().getRealmService();
            UserRealm realm = realmService.getBootstrapRealm();
            UserStoreManager manager = realm.getUserStoreManager();
            claimValue = manager.getUserClaimValue(userName,claim,null);
            log.info("Claim value : " + claimValue);
            } catch (Exception e) {
		 	log.error("Error while getting claim value for the userName : " + userName + " and claim : " + claim);
			log.error(e.getMessage());
	    	}
		
		return claimValue;
	}
	


}
