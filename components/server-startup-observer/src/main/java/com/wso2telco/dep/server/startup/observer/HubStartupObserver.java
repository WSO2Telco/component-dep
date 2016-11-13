/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.server.startup.observer;

import com.wso2telco.dep.server.startup.observer.internal.ServiceReferenceHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.ServerStartupObserver;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.api.Permission;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

/**
 * HubStartupObserver class - should be used to do any operation that is done at server startup
 */
public class HubStartupObserver implements ServerStartupObserver {

    private static final String APIMGT_REGISTRY_LOCATION = "/" + "apimgt";
    private static final String API_APPLICATION_DATA_LOCATION = APIMGT_REGISTRY_LOCATION + "/" + "applicationdata";
    private static final String WORKFLOW_MEDIA_TYPE = "workflow-config";
    private static final String WORKFLOW_EXTENSIONS_FILE = "workflow-extensions.xml";
    private static final String WORKFLOW_EXECUTOR_LOCATION = API_APPLICATION_DATA_LOCATION + "/" + WORKFLOW_EXTENSIONS_FILE;
    private static final String MANAGE_APP_ADMIN_ROLE = "manage-app-admin";
    private static final String PRODUCT_PROFILE = "profile";
    private static final String PRODUCT_PROFILE_API_STORE = "api-store";
    private static final String PRODUCT_PROFILE_DEFAULT = "default";
    private static final Log log = LogFactory.getLog(HubStartupObserver.class);

    @Override
    public void completingServerStartup() {

        if (log.isDebugEnabled()) {
            log.debug("Completing Server Startup");
        }

        updateWorkflowConfigsInRegistry();
        createDefaultRolesAndAssignToSuperAdmin();
    }

    /**
     *  Update workflow extension configurations in the registry. Will always give preference to
     *  a custom workflow-extensions.xml file available at <CARBON_HOME>/repository/resources folder.
     *  Whatever is existing in the registry will be overwritten by the content of the
     *  workflow-extensions.xml file in the file system, if such a file is available.
     */
    private void updateWorkflowConfigsInRegistry() {

        // if the server profile neither api-store or empty (default), should not update the
        // registry with workflow-extensions.xml configuration
        String productProfile = System.getProperty(PRODUCT_PROFILE);
        if (!PRODUCT_PROFILE_DEFAULT.equals(productProfile.trim()) && !PRODUCT_PROFILE_API_STORE.equals(productProfile.trim())) {
            // return
            if (log.isDebugEnabled()) {
                log.debug("Detected product profile: [" + productProfile + "], skipping the workflow configuration update in registry");
            }
            return;
        }

        RegistryService registryService = ServiceReferenceHolder.getInstance().getRegistryService();

        UserRegistry govRegistry = null;
        // get governance registry
        try {
            govRegistry = registryService.getGovernanceSystemRegistry();
        } catch (RegistryException e) {
            handleError("Error in getting Governance Registry", e);
        }

        byte[] data;

        String resourcesDirectory = CarbonUtils.getCarbonHome() + File.separator + "repository" + File.separator + "resources";
        // check if a workflow-extensions.xml is available in the <APIM_HOME>/repository/resources
        File workflowExtensionsFile = new File(resourcesDirectory + File.separator + WORKFLOW_EXTENSIONS_FILE);
        if (workflowExtensionsFile.exists()) {
            data = readCustomWorkflowConfigFile(resourcesDirectory);
            if (data != null && data.length > 0) {
                // delete the existing workflow config file in the registry
                deleteExistingWorkflowConfig(govRegistry);
                // update
                updateWorkflowConfigs(govRegistry, data);
                log.info("Successfully updated the workflow extensions configuration at " + WORKFLOW_EXECUTOR_LOCATION);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("No custom " + WORKFLOW_EXTENSIONS_FILE + " found at carbon resources directory");
            }
        }
    }

    /**
     * Read the custom workflow configuration file from <CARBON_HOME>/repository/resources
     *
     * @param resourcesDirectory full path to <CARBON_HOME>/repository/resources
     * @return file content as bytes
     */
    private byte[] readCustomWorkflowConfigFile(String resourcesDirectory) {
        byte[] data = null;

        try {
            data = readAllBytes(get(resourcesDirectory + File.separator + WORKFLOW_EXTENSIONS_FILE));
        } catch (IOException e) {
            handleError("Error reading workflow-extensions.xml from " + resourcesDirectory, e);
        }
        return data;
    }

    /**
     * Update the workflow configurations with new settings
     *
     * @param govRegistry Governance Registry object
     * @param data new content to be updated
     */
    private void updateWorkflowConfigs(UserRegistry govRegistry, byte[] data) {
        Resource resource = null;
        try {
            resource = govRegistry.newResource();
        } catch (RegistryException e) {
            handleError("Error creating new registry resource", e);
        }

        try {
            resource.setContent(data);
        } catch (RegistryException e) {
            handleError("Error creating new registry resource", e);
        }

        resource.setMediaType(WORKFLOW_MEDIA_TYPE);

        try {
            govRegistry.put(WORKFLOW_EXECUTOR_LOCATION, resource);
        } catch (RegistryException e) {
            handleError("Error creating new registry resource", e);
        }
    }

    /**
     * Deletes the exiting workflow config resource in the registry, if it exists
     *
     * @param govRegistry Governance Registry object
     */
    private void deleteExistingWorkflowConfig(UserRegistry govRegistry) {
        try {
            // check if the workflow-extensions.xml alraedy exists in the registry, if so delete it
            if (govRegistry.resourceExists(WORKFLOW_EXECUTOR_LOCATION)) {
                log.info("Workflow extensions configuration already available in the registry, " +
                        "trying to overwrite");
                govRegistry.delete(WORKFLOW_EXECUTOR_LOCATION);
            }
        } catch (RegistryException e) {
            handleError("Error checking existence of Workflow extensions at " + WORKFLOW_EXECUTOR_LOCATION, e);
        }
    }

    /**
     * Assign the relevant roles to super tenant to use workflows with manage jaggery app
     * The role name is hard coded to 'manage-app-admin'. This role should be the same as
     * 'adminRole' of manage/src/main/manage/site/conf/site.json
     */
    private void createDefaultRolesAndAssignToSuperAdmin () {

        String role = MANAGE_APP_ADMIN_ROLE;

        try {
            RealmService realmService = ServiceReferenceHolder.getInstance().getRealmService();
            UserRealm realm = realmService.getBootstrapRealm();
            UserStoreManager manager = realm.getUserStoreManager();
            if (!manager.isExistingRole(role)) {
                if (log.isDebugEnabled()) {
                    log.debug("Creating " + MANAGE_APP_ADMIN_ROLE + " role: " + role);
                }
                Permission[] loginPermission = new Permission[]{new Permission("/permission/admin/login", "ui.execute")};
                String superTenantName = ServiceReferenceHolder.getInstance().getRealmService().getBootstrapRealmConfiguration().getAdminUserName();
                String[] userList = new String[]{superTenantName};
                manager.addRole(role, userList, loginPermission);
            }
        } catch (Exception e) {
            log.error("Error in assigning 'manage-app-admin' role to super tenant", e);
        }
    }

    @Override
    public void completedServerStartup() {

        if (log.isDebugEnabled()) {
            log.debug("Completed Server Startup");
        }
    }

    private void handleError (String errorMsg, RegistryException e) {
        log.error(errorMsg, e);
        throw new RuntimeException(errorMsg, e);
    }

    private void handleError (String errorMsg, IOException e) {
        log.error(errorMsg, e);
        throw new RuntimeException(errorMsg, e);
    }
}
