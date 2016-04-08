package com.wso2telco.publisheventsdata;


public class MifeEventsConstants {

    public static final String MIFE_SPEND_LIMIT_DATA_STREAM_NAME = "mife.events.spend.limit.data";
    public static final String MIFE_SPEND_LIMIT_DATA_STREAM_VERSION = "1.0.0";

    public static final String MIFE_EVENTS_PROPERTIES_FILE = "mife-events.properties";
    public static final String CEP_SPEND_LIMIT_HANDLER_ENABLED_PROPERTY = "mife.events.spend.limit.handler.enabled";
    public static final String CEP_URL_PROPERTY = "mife.events.cep.url";
    public static final String CEP_USERNAME_PROPERTY = "mife.events.cep.username";
    public static final String CEP_PASSWORD_PROPERTY = "mife.events.cep.password";

    public static final String MSISDN_HAZELCAST_MAP_NAME = "msisdnSpendLimitMap";
    public static final String APPLICATION_HAZELCAST_MAP_NAME = "appSpendLimitMap";
    public static final String OPERATOR_HAZELCAST_MAP_NAME = "operatorSpendLimitMap";

    public static final String OPERATOR_ID = "mife.prop.operatorId";
    public static final String MSISDN = "mife.prop.msisdn";
    public static final String CHARGE_AMOUNT = "mife.prop.chargeAmount";
    public static final String RESPONSE_CODE = "mife.prop.responseCode";
}
