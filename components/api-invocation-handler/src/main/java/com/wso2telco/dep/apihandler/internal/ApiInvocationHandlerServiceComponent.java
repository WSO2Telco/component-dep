/*******************************************************************************
 * Copyright (c) 2015-2019, WSO2.Telco Inc. (http://www.wso2telco.com)
 *
 * All Rights Reserved. WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.apihandler.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.rest.AbstractHandler;
import org.osgi.service.component.ComponentContext;

import com.wso2telco.dep.apihandler.ApiInvocationHandler;

public class ApiInvocationHandlerServiceComponent {

    public ApiInvocationHandlerServiceComponent() {
        //
    }

    private static final Log log = LogFactory.getLog(ApiInvocationHandlerServiceComponent.class);

    protected void activate(ComponentContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("API Invocation Handler is activated ");
        }
        ctx.getBundleContext().registerService(AbstractHandler.class.getName(), new ApiInvocationHandler(), null);
    }

    protected void deactivate(ComponentContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("API Invocation Handler is deactivated");
        }
    }
}