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

package com.wso2telco.dep.mediator.executor;

/**
 * Class holds handler data.
 */
public class HandlerObj {

    private String handlerName;
    private String handlerFullQulifiedName;
    private String handlerDescription;

    public HandlerObj (String handelerName, String handlerFullQulifiedName, String handlerDescription) {
        this.handlerName = handelerName;
        this.handlerDescription = handlerDescription;
        this.handlerFullQulifiedName = handlerFullQulifiedName;
    }

    public HandlerObj (String handlerName, String handlerFullQulifiedName) {
        this.handlerName = handlerName;
        this.handlerFullQulifiedName = handlerFullQulifiedName;
    }

    public String getHandlerFullQulifiedName() {
        return handlerFullQulifiedName;
    }

    public void setHandlerFullQulifiedName(String handlerFullQulifiedName) {
        this.handlerFullQulifiedName = handlerFullQulifiedName;
    }

    public String getHandlerDescription() {
        return handlerDescription;
    }

    public void setHandlerDescription(String handlerDescription) {
        this.handlerDescription = handlerDescription;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }
}
