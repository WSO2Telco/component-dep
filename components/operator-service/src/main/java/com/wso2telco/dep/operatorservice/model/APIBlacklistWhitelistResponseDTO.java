/**
 * Copyright (c) 2019, APIGATE Inc. (https://www.apigate.com/) All Rights Reserved.
 *
 * APIGATE Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.operatorservice.model;

/**
 * DTO class for blacklist whitelist service response
 */
public class APIBlacklistWhitelistResponseDTO {
    private boolean validationError;
    private int processed;
    private int failed;

    public APIBlacklistWhitelistResponseDTO() {
        this.processed = 0;
        this.failed = 0;
        validationError = false;
    }

    public boolean isValidationError() {
        return validationError;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
        validationError = failed > 0;
    }
}
