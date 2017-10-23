/*******************************************************************************
 * Copyright  (c) 2017, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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

package org.wso2telco.dep.notifyeventmediator;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.databridge.commons.Event;

import java.util.Map;

public class FaultEventHandler {

    static final Log failedEventLog = LogFactory.getLog("NOTIFY_EVENT_LOGGER");

    void handleFaultEvents(String streamId, Object[] metaDataArray, Object[] correlationDataArray, Object[] payloadDataArray, Map<String, String> arbitraryDataMap) {

        Event faultEvent = new Event(streamId, System.currentTimeMillis(), metaDataArray, correlationDataArray, payloadDataArray, arbitraryDataMap);

        failedEventLog.info(faultEvent.toString());
    }
}
