/**
 * Copyright (c) 2020-2021, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.dep.lifecycleextension;

import com.wso2telco.dep.lifecycleextension.notification.NotificationService;
import com.wso2telco.dep.lifecycleextension.notification.impl.NotificationServiceImpl;
import com.wso2telco.dep.lifecycleextension.util.Constants;
import org.wso2.carbon.apimgt.impl.executors.APIExecutor;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;


public class APICustomExecutor extends APIExecutor {

    private final NotificationService notificationService;

    public APICustomExecutor(){
        notificationService=new NotificationServiceImpl();
    }

    @Override
    public boolean execute(RequestContext context, String currentState, String targetState) {

        boolean superExecuted = super.execute(context, currentState, targetState);
        if(superExecuted && currentState.equalsIgnoreCase(Constants.STATE_CREATED) && targetState.equalsIgnoreCase(Constants.STATE_PUBLISHED) && notificationService.validateIsNotNewApiVersion(context)){
            notificationService.sendApiProviderEmail(context);
            }

            return superExecuted;
    }
}
