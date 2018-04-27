package org.wso2telco.dep.notifyeventmediator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.databridge.commons.Event;

import java.util.Map;

/*
*  General Event Handler is used to write all published events regardless of if it has been published to the DAS or not.
*  This handler is configurable via a property in the mediationConfig registry entry.
*/
public class GeneralEventHandler {

    static final Log GeneralEventLog = LogFactory.getLog("GENERAL_EVENT_LOGGER");

    void handleGeneralEvents(String streamId, Object[] metaDataArray, Object[] correlationDataArray, Object[] payloadDataArray, Map<String, String> arbitraryDataMap) {

        Event generalEvent = new Event(streamId, System.currentTimeMillis(), metaDataArray, correlationDataArray, payloadDataArray, arbitraryDataMap);

        GeneralEventLog.info(generalEvent.toString());
    }
}