package com.wso2telco.publisheventsdata.handler;

import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.publisheventsdata.MifeEventsConstants;
import com.wso2telco.publisheventsdata.internal.MifeEventsDataHolder;
import java.util.Properties;

public class SpendLimitHandler {

    private final boolean isEventsEnabled;
    private AxiataDbService dbservice;

    public SpendLimitHandler() {
        Properties properties = MifeEventsDataHolder.getMifeEventsProps();
        String eventsEnabled = properties.getProperty(MifeEventsConstants.CEP_SPEND_LIMIT_HANDLER_ENABLED_PROPERTY);
        isEventsEnabled = Boolean.parseBoolean(eventsEnabled);
        dbservice = new AxiataDbService();
    }

    public boolean isSpendLimitExceeded(String msisdn, String consumerKey, String operatorId) throws
            AxataDBUtilException {
        return isEventsEnabled && (isMSISDNSpendLimitExceeded(msisdn) || isApplicationSpendLimitExceeded(consumerKey)
                || isOperatorSpendLimitExceeded(operatorId));
    }

    public boolean isMSISDNSpendLimitExceeded(String msisdn) throws AxataDBUtilException {
        if (isEventsEnabled) {
//            HazelcastInstance hazelcastInstance = MifeEventsDataHolder.getHazelcastInstance();
//            IMap<String, Object> cacheMap = hazelcastInstance.getMap(MifeEventsConstants.MSISDN_HAZELCAST_MAP_NAME);
//            return cacheMap.containsKey(msisdn);
            return dbservice.checkMSISDNSpendLimit(msisdn);
        }
        return false;
    }

    public boolean isApplicationSpendLimitExceeded(String consumerKey) throws AxataDBUtilException {
        if (isEventsEnabled) {
//            HazelcastInstance hazelcastInstance = MifeEventsDataHolder.getHazelcastInstance();
//            IMap<String, Object> cacheMap = hazelcastInstance.getMap(MifeEventsConstants.APPLICATION_HAZELCAST_MAP_NAME);
//            return cacheMap.containsKey(consumerKey);
            return dbservice.checkApplicationSpendLimit(consumerKey);
        }
        return false;
    }

    public boolean isOperatorSpendLimitExceeded(String operatorId) throws AxataDBUtilException {
        if (isEventsEnabled) {
//            HazelcastInstance hazelcastInstance = MifeEventsDataHolder.getHazelcastInstance();
//            IMap<String, Object> cacheMap = hazelcastInstance.getMap(MifeEventsConstants.OPERATOR_HAZELCAST_MAP_NAME);
//            return cacheMap.containsKey(operatorId);
            return dbservice.checkOperatorSpendLimit(operatorId);
        }
        return false;
    }
}
