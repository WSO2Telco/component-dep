/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.publisheventsdata.handler;

import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.publisheventsdata.MifeEventsConstants;
import com.wso2telco.publisheventsdata.internal.MifeEventsDataHolder;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class SpendLimitHandler.
 */
public class SpendLimitHandler {

    /** The is events enabled. */
    private final boolean isEventsEnabled;
    
    /** The dbservice. */
    private AxiataDbService dbservice;

    /**
     * Instantiates a new spend limit handler.
     */
    public SpendLimitHandler() {
        Properties properties = MifeEventsDataHolder.getMifeEventsProps();
        String eventsEnabled = properties.getProperty(MifeEventsConstants.CEP_SPEND_LIMIT_HANDLER_ENABLED_PROPERTY);
        isEventsEnabled = Boolean.parseBoolean(eventsEnabled);
        dbservice = new AxiataDbService();
    }
  

    public Double getGroupTotalDayAmount(String groupName,String operator,String msisdn) throws AxataDBUtilException {
        if (isEventsEnabled) {

			return dbservice.getGroupTotalDayAmount(groupName, operator,msisdn);
		}
		return 0.0;
	}

    public Double getGroupTotalMonthAmount(String groupName,String operator,String msisdn) throws AxataDBUtilException {

        if (isEventsEnabled) {

            return dbservice.getGroupTotalMonthAmount(groupName,operator,msisdn);
        }
        return 0.0;
    }
}
