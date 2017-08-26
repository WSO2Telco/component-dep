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
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.um.ws.api.stub.ClaimDTO;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
import org.wso2.carbon.user.core.claim.Claim;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkflowUserIdentityManager extends UserEntityManager {

    private static Log log = LogFactory.getLog(WorkflowUserIdentityManager.class);

    private RemoteUserStoreManagerServiceStub remoteUserStoreManagerServiceStub;

    //list of Claim URIs
    private static final String ID_CLAIM_URI = "urn:scim:schemas:core:1.0:id";
    private static final String FIRST_NAME_CLAIM_URI = "http://axschema.org/namePerson/first";
    private static final String LAST_NAME_CLAIM_URI = "http://wso2.org/claims/lastname";
    private static final String FULL_NAME_CLAIM_URI = "http://wso2.org/claims/fullname";
    private static final String EMAIL_CLAIM_URI = "http://wso2.org/claims/emailaddress";
    private static final String ROLE_CLAIM_URI = "http://wso2.org/claims/role";

    public WorkflowUserIdentityManager() {
        try {
            remoteUserStoreManagerServiceStub = new RemoteUserStoreManagerServiceStub(null, "https://127.0.0.1:9443/services/RemoteUserStoreManagerService");

            HttpTransportProperties.Authenticator basicAuth = new HttpTransportProperties.Authenticator();
            basicAuth.setUsername("admin");
            basicAuth.setPassword("admin");
            basicAuth.setPreemptiveAuthentication(true);

            remoteUserStoreManagerServiceStub._getServiceClient().getOptions().setProperty(HTTPConstants.AUTHENTICATE, basicAuth);

        } catch (AxisFault axisFault) {
            String errorMsg = "Error while initiating RemoteUserStoreManagerServiceStub";
            log.error(errorMsg, axisFault);
        }
    }

    @Override
    public User createNewUser(String userId) {
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void insertUser(User user) {
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public UserEntity findUserById(String userId) {
        try {
            int id = remoteUserStoreManagerServiceStub.getUserId(userId);

            if (id > -1) {
                UserEntity userEntity = new UserEntity(userId);

                // set userId as first-name by default
                userEntity.setFirstName(userId);
                // set empty value for last-name by default
                userEntity.setLastName("");

                ClaimDTO[] userClaims = remoteUserStoreManagerServiceStub.getUserClaimValues(userId, null);

                for (ClaimDTO claimDTO: userClaims) {
                    if (FIRST_NAME_CLAIM_URI.equals(claimDTO.getClaimUri())) {
                        userEntity.setFirstName(claimDTO.getValue());
                    } else if (LAST_NAME_CLAIM_URI.equals(claimDTO.getClaimUri())) {
                        userEntity.setLastName(claimDTO.getValue());
                    } else if (EMAIL_CLAIM_URI.equals(claimDTO.getClaimUri())) {
                        userEntity.setEmail(claimDTO.getValue());
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
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl userQuery, Page page) {
        List<User> userList = new ArrayList<>();

        if (null != userQuery.getId()) {
            userList.add(findUserById(userQuery.getId()));
        } else {
            try {
                String[] userIdList = remoteUserStoreManagerServiceStub.listUsers("*", -1);
                for (int i = 0; i < userIdList.length; i++) {
                    userList.add(findUserById(userIdList[i]));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (RemoteUserStoreManagerServiceUserStoreExceptionException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }

    private List<User> generateFinalUserList(List<String[]> resultUserList) {
        List<String> mergedList = new ArrayList<>();
        //first result list is considered as merged list first
        for (String user : resultUserList.get(0)) {
            mergedList.add(user);
        }
        for (int i = 1; i < resultUserList.size(); i++) {
            List<String> newList = new ArrayList<>();
            for (String user : resultUserList.get(i)) {
                if (mergedList.contains(user)) {
                    newList.add(user);
                }
            }
            //make new list the merged list
            mergedList = newList;
        }

        List<User> result = new ArrayList<>();
        //prepare User list
        for (String userName : mergedList) {
            result.add(new UserEntity(userName));
        }
        return result;
    }

    private List<User> pageUserList(Page page, String[] users) {
        List<User> userList = new ArrayList<>();
        int resultLength = users.length;
        int max;
        if (page != null) {
            if (page.getFirstResult() > resultLength) {
                //no more result left, sending empty list
                return new ArrayList<>();
            }

            if (page.getMaxResults() > resultLength) {
                max = resultLength;
            } else {
                max = page.getMaxResults();
            }
            for (int i = page.getFirstResult(); i < max; i++) {
                UserEntity userEntity = new UserEntity(users[i]);
                userEntity.setPassword("admin");
                userList.add(userEntity);
            }
        } else {
            for (int i = 0; i < 1; i++) { // TODO: need to change with "resultLength"
                UserEntity userEntity = new UserEntity(users[i]);
                userEntity.setPassword("admin");
                userList.add(userEntity);
            }
        }

        return userList;
    }

    private List<Claim> transformQueryToClaim(UserQueryImpl userQuery) {
        List<Claim> claimList = new ArrayList<Claim>();

        if (userQuery.getEmail() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(EMAIL_CLAIM_URI);
            claim.setValue(userQuery.getEmail());
            claimList.add(claim);
        }

        if (userQuery.getEmailLike() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(EMAIL_CLAIM_URI);
            claim.setValue("*" + userQuery.getEmailLike() + "*");
            claimList.add(claim);
        }

        if (userQuery.getFirstName() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(FIRST_NAME_CLAIM_URI);
            claim.setValue(userQuery.getFirstName());
            claimList.add(claim);
        }

        if (userQuery.getFirstNameLike() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(FIRST_NAME_CLAIM_URI);
            claim.setValue("*" + userQuery.getFirstNameLike() + "*");
            claimList.add(claim);
        }

        if (userQuery.getFullNameLike() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(FULL_NAME_CLAIM_URI);
            claim.setValue("*" + userQuery.getFullNameLike() + "*");
            claimList.add(claim);
        }

        if (userQuery.getGroupId() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(ROLE_CLAIM_URI);
            claim.setValue(userQuery.getGroupId());
            claimList.add(claim);
        }

        if (userQuery.getId() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(ID_CLAIM_URI);
            claim.setValue(userQuery.getId());
            claimList.add(claim);
        }

        if (userQuery.getLastName() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(LAST_NAME_CLAIM_URI);
            claim.setValue(userQuery.getLastName());
            claimList.add(claim);
        }

        if (userQuery.getLastNameLike() != null) {
            Claim claim = new Claim();
            claim.setClaimUri(LAST_NAME_CLAIM_URI);
            claim.setValue("*" + userQuery.getLastNameLike() + "*");
            claimList.add(claim);
        }

        return claimList;
    }


    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl userQuery) {
        return findUserByQueryCriteria(userQuery, null).size();
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<Group> groups = new ArrayList<Group>();
        try {
            String[] roles = remoteUserStoreManagerServiceStub.getRoleListOfUser(userId);
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
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        boolean authStatus = false;

        try {
            authStatus = remoteUserStoreManagerServiceStub.authenticate(userId, password);
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
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult,
                                             int maxResults) {
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        String msg = "Invoked UserIdentityManager method is not implemented in WorkflowUserIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }
}
