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
package com.wso2telco.dep.apihandler;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.wso2.carbon.apimgt.impl.APIConstants;

import javax.cache.Cache;
import javax.cache.Caching;

public class CacheUpdateMediator extends AbstractMediator {

    private static final String CONSUMER_KEY = "app_consumer_key";
    private static final String SP_TOKEN_CACHE = "spTokenCache";

    @Override
    public boolean mediate(MessageContext messageContext) {

        String consumerKey = (String) messageContext.getProperty(CONSUMER_KEY);

        if (log.isDebugEnabled())
        {
            log.debug("consumerKey: " + consumerKey);
        }

        Cache spToken =  Caching.getCacheManager(APIConstants.API_MANAGER_CACHE_MANAGER).getCache(SP_TOKEN_CACHE);

        if (spToken.containsKey(consumerKey)) {
            spToken.remove(consumerKey);
        }
            return true;
    }
}
