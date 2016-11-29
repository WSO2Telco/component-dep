/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.mediator.internal;

 
import javax.xml.bind.JAXBException;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

import com.wso2telco.dep.mediator.unmarshaler.GroupEventUnmarshaller;


/**
 * @scr.component name="org.wso2.carbon.am" immediate="true"
 * @scr.reference name="user.realmservice.default"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"
 * unbind="unsetRealmService"
 **/
public class RealmServices {
    
    /** The config context service. */
    private static ConfigurationContextService configContextService = null;

    /**
     * Activate.
     *
     * @param context the context
     */
    protected void activate(ComponentContext context) throws JAXBException{
        BundleContext bundleContext = context.getBundleContext();       
      //  bundleContext.registerService(ApplicationManagementService.class.getName(), mgtService, null);       
        GroupEventUnmarshaller.startGroupEventUnmarshaller();
    }

    /**
     * Deactivate.
     *
     * @param context the context
     */
    protected void deactivate(ComponentContext context) {
         
    }

    /**
     * Sets the realm service.
     *
     * @param realmService the new realm service
     */
    protected void setRealmService(RealmService realmService) {

        Util.setRealmService(realmService);
    }

    /**
     * Unset realm service.
     *
     * @param realmService the realm service
     */
    protected void unsetRealmService(RealmService realmService) {
        Util.setRealmService(null);
    }

}
