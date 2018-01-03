package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.model.TaskSearchDTO;
import org.workflow.core.util.DeploymentTypes;

/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class IntGtwSubRequestBuilder extends AbstractSubRequestBuilder {

    private static IntGtwSubRequestBuilder instance;

    IntGtwSubRequestBuilder(DeploymentTypes depType) {
        log = LogFactory.getLog(IntGtwSubRequestBuilder.class);
        super.depType = depType;
    }

    public static IntGtwSubRequestBuilder getInstace(DeploymentTypes depType) {
        if (instance == null) {
            instance = new IntGtwSubRequestBuilder(depType);
        }
        return instance;
    }
    
    @Override
    protected Callback buildApprovalRequest(ApprovalRequest approvalRequest, UserProfileDTO userProfile) throws BusinessException {
        return null;
    }

    @Override
    public Callback getHistoryData(TaskSearchDTO searchDTO, UserProfileDTO userProfile) throws BusinessException {
        return null;
    }
}
