package com.wso2telco.workflow.usermanage;


import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.claim.Claim;
import org.wso2.carbon.user.core.config.RealmConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkflowUserIdentityManager extends UserEntityManager {

    private static Log log = LogFactory.getLog(WorkflowUserIdentityManager.class);
    private static String methodNotImplementedError = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";

    private UserStoreManager userStoreManager;

    //list of Claim URIs
    private static final String FIRST_NAME_CLAIM_URI = "http://axschema.org/namePerson/first";
    private static final String LAST_NAME_CLAIM_URI = "http://wso2.org/claims/lastname";
    private static final String EMAIL_CLAIM_URI = "http://wso2.org/claims/emailaddress";

    public WorkflowUserIdentityManager() {
        try {
            RealmConfiguration config = new RealmConfiguration();
            userStoreManager = ServicesHolder.getInstance().getRealmService().getUserRealm(config).getUserStoreManager();
        } catch (UserStoreException e) {
            String errorMsg = "Error while initiating UserStoreManager";
            log.error(errorMsg, e);
        }
    }

    @Override
    public User createNewUser(String userId) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }

    @Override
    public void insertUser(User user) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }

    @Override
    public UserEntity findUserById(String userId) {
        try {
            int id = userStoreManager.getUserId(userId);

            if (id > -1) {
                UserEntity userEntity = new UserEntity(userId);

                // set userId as first-name by default
                userEntity.setFirstName(userId);
                // set empty value for last-name by default
                userEntity.setLastName("");

                Claim[] userClaims = userStoreManager.getUserClaimValues(userId, null);

                for (Claim claim: userClaims) {
                    if (FIRST_NAME_CLAIM_URI.equals(claim.getClaimUri())) {
                        userEntity.setFirstName(claim.getValue());
                    } else if (LAST_NAME_CLAIM_URI.equals(claim.getClaimUri())) {
                        userEntity.setLastName(claim.getValue());
                    } else if (EMAIL_CLAIM_URI.equals(claim.getClaimUri())) {
                        userEntity.setEmail(claim.getValue());
                    }
                }

                return userEntity;
            } else {
                log.error("No user exist with userId:" + userId);
                return null;
            }
        } catch (Exception e) {
            log.error("Error retrieving user info by id for: " + userId, e);
            return null;
        }
    }

    @Override
    public void deleteUser(String userId) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl userQuery, Page page) {
        List<User> userList = new ArrayList<>();

        if (null != userQuery.getId()) {
            userList.add(findUserById(userQuery.getId()));
        } else {
            try {
                String[] userIdList = userStoreManager.listUsers("*", -1);
                for (int i = 0; i < userIdList.length; i++) {
                    userList.add(findUserById(userIdList[i]));
                }
            } catch (UserStoreException e) {
                log.error("Error while retrieving list of users.");
            }
        }
        return userList;
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl userQuery) {
        return findUserByQueryCriteria(userQuery, null).size();
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<Group> groups = new ArrayList<Group>();
        try {
            String[] roles = userStoreManager.getRoleListOfUser(userId);
            for (String role : roles) {
                Group group = new GroupEntity(role);
                groups.add(group);
            }
        }catch (Exception e) {
            log.error("error retrieving user tenant info", e);
        }

        return groups;
    }

    @Override
    public UserQuery createNewUserQuery() {
        return new UserQueryImpl(getProcessEngineConfiguration().getCommandExecutor());
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        boolean authStatus = false;

        try {
            authStatus = userStoreManager.authenticate(userId, password);
        } catch (Exception e) {
           log.error(
                    "User store exception thrown while authenticating user : " + userId, e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Basic authentication request completed. " +
                    "Username : " + userId +
                    ", Authentication State : " + authStatus);
        }

        return authStatus;

    }

    @Override
    public List<User> findPotentialStarterUsers(String processDefId) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult,
                                             int maxResults) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new UnsupportedOperationException(WorkflowUserIdentityManager.methodNotImplementedError);
    }
}
