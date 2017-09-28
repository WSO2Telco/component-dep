package com.wso2telco.workflow.usermanage;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.explorer.ui.login.DefaultLoginHandler;
import org.activiti.explorer.identity.LoggedInUserImpl;

import java.util.List;

public class WorkflowLoginHandler extends DefaultLoginHandler {

    private transient IdentityService identityService;

    @Override
    public LoggedInUserImpl authenticate(String userName, String password) {
        LoggedInUserImpl loggedInUser = null;

        try {
            if (identityService.checkPassword(userName, password)) {
                User user = identityService.createUserQuery().userId(userName).singleResult();
                // Fetch and cache user data
                loggedInUser = new LoggedInUserImpl(user, password);
                List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
                for (Group group : groups) {
                    if ("manage-app-admin".equals(group.getType())) {
                        loggedInUser.setAdmin(true);
                        loggedInUser.addSecurityRoleGroup(group);

                        // give Manage privilege to admin user
                        GroupEntity manageGroup = new GroupEntity("admin");
                        manageGroup.setName("Admin");
                        manageGroup.setType("security-role");

                        loggedInUser.addSecurityRoleGroup(manageGroup);
                        loggedInUser.addGroup(manageGroup);

                    } else if (group.getType().endsWith("-admin-role")) {
                        loggedInUser.setUser(true);
                        loggedInUser.addSecurityRoleGroup(group);
                    } else {
                        loggedInUser.addGroup(group);
                    }
                }
            }
        } catch (Exception e) {
            // Do nothing, returning null should be enough
        }

        return loggedInUser;
    }

    @Override
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

}
