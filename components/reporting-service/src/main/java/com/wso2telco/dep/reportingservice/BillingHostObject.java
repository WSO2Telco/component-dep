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
package com.wso2telco.dep.reportingservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Tier;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.usage.client.dto.APIVersionUserUsageDTO;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.dep.reportingservice.dao.Approval;
import com.wso2telco.dep.reportingservice.dao.BillingDAO;
import com.wso2telco.dep.reportingservice.dao.OperatorDAO;
import com.wso2telco.dep.reportingservice.exception.ReportingServiceError;
import com.wso2telco.dep.reportingservice.northbound.NbHostObjectUtils;
import com.wso2telco.dep.reportingservice.southbound.SbHostObjectUtils;
import com.wso2telco.dep.reportingservice.util.ChargeRate;
import com.wso2telco.dep.reportingservice.util.RateKey;
import com.wso2telco.dep.reportingservice.exception.ReportingServiceError;

// TODO: Auto-generated Javadoc
/**
 * The Class BillingHostObject.
 */
public class BillingHostObject extends ScriptableObject {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(BillingHostObject.class);
    
    /** The hostobject name. */
    private String hostobjectName = "BillingHostObject";
    
    /** The api consumer. */
    private APIConsumer apiConsumer;
    
    /** The tier pricing. */
    private static Map<String, Float> tierPricing = new HashMap<String, Float>();
    
    /** The tier maximum count. */
    private static Map<String, Integer> tierMaximumCount = new HashMap<String, Integer>();
    
    /** The tier unit time. */
    private static Map<String, Integer> tierUnitTime = new HashMap<String, Integer>();
   
    
    /**
     * Gets the tier pricing.
     *
     * @return the tier pricing
     */
    public static Map<String, Float> getTierPricing() {
        return tierPricing;
    }

    /**
     * Gets the tier maximum count.
     *
     * @return the tier maximum count
     */
    public static Map<String, Integer> getTierMaximumCount() {
        return tierMaximumCount;
    }

    /**
     * Gets the tier unit time.
     *
     * @return the tier unit time
     */
    public static Map<String, Integer> getTierUnitTime() {
        return tierUnitTime;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /** The username. */
    private String username;

    /* (non-Javadoc)
     * @see org.mozilla.javascript.ScriptableObject#getClassName()
     */
    @Override
    public String getClassName() {
        return hostobjectName;
    }

    /**
     * Instantiates a new billing host object.
     *
     * @param username the username
     * @throws BusinessException 
     * @throws APIManagementException the API management exception
     */
    public BillingHostObject(String username) throws BusinessException{
        log.info("::: Initialized HostObject for : " + username);
        try {
			if (username != null) {
			    this.username = username;
			    apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
			} else {
			    apiConsumer = APIManagerFactory.getInstance().getAPIConsumer();
			}
		} catch (Exception e) {
			log.error("BillingHostObject error",e);
			throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * Instantiates a new billing host object.
     */
    public BillingHostObject() {
        log.info("::: Initialized HostObject ");
    }

    /**
     * Gets the api consumer.
     *
     * @return the api consumer
     */
    public APIConsumer getApiConsumer() {
        return apiConsumer;
    }

    /**
     * Gets the API consumer.
     *
     * @param thisObj the this obj
     * @return the API consumer
     */
    private static APIConsumer getAPIConsumer(Scriptable thisObj) {
        return ((BillingHostObject) thisObj).getApiConsumer();
    }

    /**
     * Js function_get report file content.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the string
     * @throws Exception 
     */
    public static String jsFunction_getReportFileContent(Context cx, Scriptable thisObj,
            Object[] args, Function funObj)
            throws BusinessException {
        if (args == null || args.length == 0) {  
        	log.error("jsFunction_getReportFileContent null or empty arguments");
            throw new BusinessException(ReportingServiceError.INPUT_ERROR);
        }

        String subscriberName = (String) args[0];
        String period = (String) args[1];
        boolean isNorthbound = (Boolean) args[2];
      
        try {
			generateReport(subscriberName, period, true, isNorthbound, "__ALL__");
		} catch (Exception e) {
			log.error("jsFunction_getReportFileContent",e);
			throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
		}

        String fileContent = (isNorthbound) ? NbHostObjectUtils.getReport(subscriberName, period) : SbHostObjectUtils.getReport(subscriberName, period);
        return fileContent;
    }

    /**
     * Js function_get custom care data report.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     * @throws BusinessException 
     */
    public static NativeArray jsFunction_getCustomCareDataReport(Context cx, Scriptable thisObj,
            Object[] args, Function funObj)
            throws BusinessException {
        if (args == null || args.length == 0) {
            log.error("jsFunction_getCustomCareDataReport Invalid number of parameters");
        	throw new BusinessException(ReportingServiceError.INPUT_ERROR);
        }

        String fromDate = (String) args[0];
        String toDate = (String) args[1];
        String msisdn = (String) args[2];
        String subscriberName = (String) args[3];
        String operator = (String) args[4];
        String app = (String) args[5];
        String api = (String) args[6];
        String limitStart = (String) args[7];
        String limitEnd = (String) args[8];
        String timeOffset = (String) args[9];

        NativeArray dataArray = new NativeArray(0);

        dataArray = getCustomCareDataReport(fromDate, toDate, msisdn, subscriberName, operator, app, api, limitStart, limitEnd, timeOffset);

        return dataArray;
    }

    /**
     * Js function_get custom care data records count.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the string
     * @throws APIManagementException the API management exception
     */
    public static String jsFunction_getCustomCareDataRecordsCount(Context cx, Scriptable thisObj,
            Object[] args, Function funObj)
            throws BusinessException {
        if (args == null || args.length == 0) {
            log.error("jsFunction_getCustomCareDataRecordsCount Invalid number of parameters");
        	throw new BusinessException(ReportingServiceError.INPUT_ERROR);
        }

        String fromDate = (String) args[0];
        String toDate = (String) args[1];
        String msisdn = (String) args[2];
        String subscriberName = (String) args[3];
        String operator = (String) args[4];
        String app = (String) args[5];
        String api = (String) args[6];

        String dataString;		
		dataString = getCustomCareDataRecordCount(fromDate, toDate, msisdn, subscriberName, operator, app, api);	

        return dataString;
    }

    /**
     * Js function_get custom api traffic report file content.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the string
     * @throws APIManagementException the API management exception
     */
    public static String jsFunction_getCustomApiTrafficReportFileContent(Context cx, Scriptable thisObj,
            Object[] args, Function funObj)
            throws BusinessException {
        if (args == null || args.length == 0) {
            log.error("jsFunction_getCustomApiTrafficReportFileContent Invalid number of parameters");
        	throw new BusinessException(ReportingServiceError.INPUT_ERROR);
        }

        String fromDate = (String) args[0];
        String toDate = (String) args[1];
        String subscriberName = (String) args[2];
        String operator = (String) args[3];
        String api = (String) args[4];
        String isError = (String) args[5];
        int applicationId = Integer.parseInt(args[6].toString());
        String timeOffset = (String) args[7];
        String responseType = (String) args[8];
        boolean isNorthbound = (Boolean) args[9];

        generateCustomApiTrafficReport(fromDate, toDate, subscriberName, operator, api, (isError.equalsIgnoreCase("true") ? true : false), applicationId, timeOffset, responseType, isNorthbound);

        String fileContent = SbHostObjectUtils.getCustomReport(fromDate, toDate, subscriberName, operator, api);
        return fileContent;
    }

    /**
     * Js function_get api usagefor subscriber.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getAPIUsageforSubscriber(Context cx, Scriptable thisObj,
            Object[] args, Function funObj)
            throws BusinessException {
        List<APIVersionUserUsageDTO> list = null;
        if (args == null || args.length == 0) {
            log.error("jsFunction_getAPIUsageforSubscriber Invalid number of parameters");
        	throw new BusinessException(ReportingServiceError.INPUT_ERROR);
        }
        NativeArray ret = null;
        try {
            NativeArray myn = new NativeArray(0);
            if (!SbHostObjectUtils.checkDataPublishingEnabled()) {
                return myn;
            }
            String subscriberName = (String) args[0];
            String period = (String) args[1];
            boolean isNorthbound = (Boolean) args[2];
            String operatorName = (String) args[3];

            ret = generateReport(subscriberName, period, false, isNorthbound, operatorName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ret = new NativeArray(0);
            NativeObject row = new NativeObject();
            row.put("error", row, true);
            row.put("message", row, e.getMessage());
            ret.put(ret.size(), ret, row);
        }

        return ret;

    }

    /**
     * Js function_get cost per api.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws Exception 
     */
    public static NativeArray jsFunction_getCostPerAPI(Context cx, Scriptable thisObj,
            Object[] args, Function funObj)
            throws BusinessException {
        List<APIVersionUserUsageDTO> list = null;
        if (args == null || args.length == 0) {
           log.error("jsFunction_getCostPerAPI Invalid number of parameters");
        	throw new BusinessException(ReportingServiceError.INPUT_ERROR);
        }
        NativeArray myn = new NativeArray(0);
        if (!SbHostObjectUtils.checkDataPublishingEnabled()) {
            return myn;
        }
        String subscriberName = (String) args[0];
        String period = (String) args[1];

        String opcode = (String) args[2];
        String application = (String) args[3];
        if ((application == null) || (application.equalsIgnoreCase("0"))) {
            application = "__All__";
        }

        boolean isNorthbound = (Boolean) args[4];


        NativeArray ret = generateReport(subscriberName, period, false, isNorthbound, opcode);
        NativeArray arr = (NativeArray) ret;

        Map<String, Double> apiPriceMap = new HashMap<String, Double>();
        apiPriceMap.clear();

        NativeArray apiPriceResponse = new NativeArray(0);

        for (Object o : arr.getIds()) {
            int i = (Integer) o;
            NativeObject subs = (NativeObject) arr.get(i);
            String subscriber = subs.get("subscriber").toString();
            log.info(subscriber);
            NativeArray applicatons = (NativeArray) subs.get("applications");

            for (Object o2 : applicatons.getIds()) {
                int j = (Integer) o2;
                NativeObject app = (NativeObject) applicatons.get(j);
                String appname = app.get("applicationname").toString();

                if (application.equalsIgnoreCase("__All__")) {
                    NativeArray subscriptions = (NativeArray) app.get("subscriptions");
                    for (Object o3 : subscriptions.getIds()) {
                        int k = (Integer) o3;
                        NativeObject apis = (NativeObject) subscriptions.get(k);
                        String api = apis.get("subscriptionapi").toString();
                        Double price = 0.0;
                        NativeArray operators = (NativeArray) apis.get("operators");
                        for (Object o4 : operators.getIds()) {
                            int l = (Integer) o4;
                            NativeObject opis = (NativeObject) operators.get(l);
                            String operator = opis.get("operator").toString();
                            if ((opcode.equalsIgnoreCase("__All__")) || (operator.equalsIgnoreCase(opcode))) {
                                price = price + Double.valueOf(opis.get("price").toString());
                            }
                        }

                        if (apiPriceMap.containsKey(api)) {
                            apiPriceMap.put(api, (apiPriceMap.get(api) + Double.valueOf(price)));
                        } else {
                            apiPriceMap.put(api, Double.valueOf(price));
                        }
                    }
                } else {
                    try {
                        String applicationName = SbHostObjectUtils.getApplicationNameById(application);
                        if (appname.equalsIgnoreCase(applicationName)) {
                            NativeArray subscriptions = (NativeArray) app.get("subscriptions");
                            for (Object o3 : subscriptions.getIds()) {
                                int k = (Integer) o3;
                                NativeObject apis = (NativeObject) subscriptions.get(k);
                                String api = apis.get("subscriptionapi").toString();
                                Double price = 0.0;
                                NativeArray operators = (NativeArray) apis.get("operators");
                                for (Object o4 : operators.getIds()) {
                                    int l = (Integer) o4;
                                    NativeObject opis = (NativeObject) operators.get(l);
                                    String operator = opis.get("operator").toString();
                                    if ((opcode.equalsIgnoreCase("__All__")) || (operator.equalsIgnoreCase(opcode))) {
                                        price = price + Double.valueOf(opis.get("price").toString());
                                    }
                                }

                                if (apiPriceMap.containsKey(api)) {
                                    apiPriceMap.put(api, (apiPriceMap.get(api) + Double.valueOf(price)));
                                } else {
                                    apiPriceMap.put(api, Double.valueOf(price));
                                }
                            }
                        }
                    } catch (Exception e) {
                        
                        log.error("jsFunction_getCostPerAPI",e);
                        throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
                    }
                }

            }
        }

        short i = 0;
        for (Map.Entry pairs : apiPriceMap.entrySet()) {
            NativeObject row = new NativeObject();
            String apiName = pairs.getKey().toString();
            row.put(apiName, row, pairs.getValue().toString());
            apiPriceResponse.put(i, apiPriceResponse, row);
            i++;
        }
       
        return apiPriceResponse;
    }

    /**
     * Generate report.
     *
     * @param subscriberName the subscriber name
     * @param period the period
     * @param persistReport the persist report
     * @param isNorthbound the is northbound
     * @param operatorName the operator name
     * @return the native array
     * @throws Exception 
     */
    private static NativeArray generateReport(String subscriberName, String period, boolean persistReport, boolean isNorthbound, String operatorName) throws BusinessException {

        //createTierPricingMap();
        Map<RateKey, ChargeRate> rateCard;
		try {
			rateCard = (isNorthbound) ? NbHostObjectUtils.getRateCard() : SbHostObjectUtils.getRateCard();
		} catch (Exception e) {
			log.error("generateReport",e);
			throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
		}

        NativeArray ret = null;
        try {
            ret = (isNorthbound) ? NbHostObjectUtils.generateReportofSubscriber(persistReport, subscriberName, period, rateCard) : SbHostObjectUtils.generateReportofSubscriber(persistReport, subscriberName, period, rateCard, operatorName);
        } catch (Exception e) {            
        	log.error("generateReport",e);
        	throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return ret;
    }

    /**
     * Generate custom api traffic report.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param subscriberName the subscriber name
     * @param operator the operator
     * @param api the api
     * @param timeOffset the time offset
     * @param resType the res type
     * @param isNorthbound the is northbound
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    private static NativeArray generateCustomApiTrafficReport(String fromDate, String toDate, String subscriberName, String operator, String api,boolean isError, int applicationId, String timeOffset, String resType, boolean isNorthbound) throws BusinessException {

        NativeArray ret = null;
        try {
            if (isNorthbound) {
                try {
					ret = NbHostObjectUtils.generateCustomTrafficReport(true, fromDate, toDate, subscriberName, operator, api, isError, applicationId, timeOffset, resType);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
            } else {
                ret = SbHostObjectUtils.generateCustomTrafficReport(true, fromDate, toDate, subscriberName, operator, api, isError, applicationId, timeOffset, resType);
            }
        } catch (Exception e) {
            log.error("generateCustomApiTrafficReport",e);        	
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return ret;
    }

    /**
     * Gets the custom care data report.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param msisdn the msisdn
     * @param subscriberName the subscriber name
     * @param operator the operator
     * @param app the app
     * @param api the api
     * @param stLimit the st limit
     * @param endLimit the end limit
     * @param timeOffset the time offset
     * @return the custom care data report
     * @throws APIManagementException the API management exception
     */
    private static NativeArray getCustomCareDataReport(String fromDate, String toDate, String msisdn, String subscriberName, String operator, String app, String api, String stLimit, String endLimit, String timeOffset) throws BusinessException {

        NativeArray ret = null;
        try {
            ret = SbHostObjectUtils.generateCustomrCareDataReport(true, fromDate, toDate, msisdn, subscriberName, operator, app, api, stLimit, endLimit, timeOffset);
        } catch (Exception e) {
            log.error("getCustomCareDataReport Error occurred while retrieving data.", e);
        	throw new BusinessException(ReportingServiceError.INPUT_ERROR);
        }
        return ret;
    }

    /**
     * Gets the custom care data record count.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @param msisdn the msisdn
     * @param subscriberName the subscriber name
     * @param operator the operator
     * @param app the app
     * @param api the api
     * @return the custom care data record count
     * @throws APIManagementException the API management exception
     * @throws BusinessException 
     */
    private static String getCustomCareDataRecordCount(String fromDate, String toDate, String msisdn, String subscriberName, String operator, String app, String api) throws BusinessException {

        String ret = null;
        try {
            ret = SbHostObjectUtils.generateCustomrCareDataRecordCount(true, fromDate, toDate, msisdn, subscriberName, operator, app, api);
        } catch (Exception e) {
        	log.error("Error occurred while generating report",e);
        	throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return ret;
    }

    /**
     * Generate financial report.
     *
     * @param subscriberName the subscriber name
     * @param period the period
     * @param opcode the opcode
     * @param application the application
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    private static NativeArray generateFinancialReport(String subscriberName, String period,
            String opcode, String application) throws BusinessException {

        NativeArray ret = null;
        try {
            ret = SbHostObjectUtils.generateCostperApisummary(true, subscriberName, period, opcode, application);
        } catch (Exception e) {
            log.error("Error occurred while generating report.", e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return ret;
    }

    /**
     * Js function_get response time data.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getResponseTimeData(Context cx, Scriptable thisObj,
            Object[] args, Function funObj)
            throws BusinessException {
        String subscriberName = (String) args[0];
        NativeArray nativeArray = null;
        log.debug("Starting getResponseTimeData funtion with " + subscriberName);
        try {
            Map<String, String> responseTimes = SbHostObjectUtils.getResponseTimesForSubscriber(subscriberName);
            short i = 0;
            log.debug(subscriberName + ", responseTimes " + responseTimes);
            if (responseTimes != null) {
                nativeArray = new NativeArray(0);
            }
            for (Map.Entry<String, String> timeEntry : responseTimes.entrySet()) {
                NativeObject row = new NativeObject();
                log.debug(subscriberName + ", timeEntry key " + timeEntry.getKey());
                log.debug(subscriberName + ", timeEntry value" + timeEntry.getValue());
                row.put("apiName", row, timeEntry.getKey().toString());
                row.put("responseTime", row, timeEntry.getValue().toString());
                nativeArray.put(i, nativeArray, row);
                i++;
            }

        } catch (Exception e) {
            log.error("Error occured getResponseTimeData ",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR); 
        }
        log.info("End of getResponseTimeData");
        return nativeArray;
    }

    /**
     * Js function_get all response times.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getAllResponseTimes(Context cx, Scriptable thisObj, Object[] args, Function funObj)
            throws BusinessException {
        String operatorName = (String) args[0];
        String subscriberName = (String) args[1];
        String appId = (String) args[2];
        String fromDate = (String) args[3];
        String toDate = (String) args[4];

        String appName = "";
        if (appId.equals("0") || appId.equalsIgnoreCase("__All__")) {
            appId = "__ALL__";
            appName = "__ALL__";
        } else {
            try {
                Application application = new ApiMgtDAO().getApplicationById(Integer.parseInt(appId));
                appName = application.getName();//HostObjectUtils.getApplicationNameById(appId);
            } catch (Exception e) {
            	log.error("jsFunction_getAllResponseTimes",e);
            	throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
            }
        }

        NativeArray apis = new NativeArray(0);
        log.debug("Starting getAllResponseTimes function for user- " + subscriberName + " app- " + appName);
        try {
            Map<String, List<APIResponseDTO>> responseMap = SbHostObjectUtils.getAllResponseTimes(operatorName, subscriberName,
                    appName, appId, fromDate, toDate);
            short i = 0;
            log.debug(subscriberName + ", responseMap " + responseMap);

            for (Map.Entry<String, List<APIResponseDTO>> timeEntry : responseMap.entrySet()) {

                NativeObject api = new NativeObject();
                api.put("apiName", api, timeEntry.getKey());

                NativeArray responseTimes = new NativeArray(0);
                for (APIResponseDTO dto : timeEntry.getValue()) {
                    NativeObject responseData = new NativeObject();
                    responseData.put("serviceTime", responseData, dto.getServiceTime());
                    responseData.put("responseCount", responseData, dto.getResponseCount());
                    responseData.put("date", responseData, dto.getDate().toString());
                    responseTimes.put(responseTimes.size(), responseTimes, responseData);
                }
                api.put("responseData", api, responseTimes);
                apis.put(i, apis, api);
                i++;
            }

        } catch (Exception e) {
            log.error("Error occured getAllResponseTimes",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }

        return apis;
    }

    /**
     * Js function_get all subscribers.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getAllSubscribers(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        try {
            List<String> subscribers = SbHostObjectUtils.getAllSubscribers();

            if (subscribers != null) {
                int i = 0;
                for (String subscriber : subscribers) {
                    nativeArray.put(i, nativeArray, subscriber);
                    i++;
                }
            }

        } catch (Exception e) {
            log.error("Error occurred getAllSubscribers",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get all operators.
     *
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getAllOperators() throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        try {
            List<String> operators = SbHostObjectUtils.getAllOperators();

            if (operators != null) {
                int i = 0;
                for (String op : operators) {
                    nativeArray.put(i, nativeArray, op);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getAllOperators",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get total api traffic for pie chart.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     * @throws BusinessException 
     */
    public static NativeArray jsFunction_getTotalAPITrafficForPieChart(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String fromDate = args[0].toString();
        String toDate = args[1].toString();
        String subscriber = args[2].toString();
        int applicationId = Integer.parseInt(args[3].toString());
        String operator = args[4].toString();

        try {
            List<String[]> api_requests = SbHostObjectUtils.getTotalAPITrafficForPieChart(fromDate, toDate, subscriber, operator, applicationId);

            if (api_requests != null) {
                int i = 0;
                for (String[] api_request : api_requests) {
                    nativeArray.put(i, nativeArray, api_request);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getTotalTrafficForPieChart",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get operator app list.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getOperatorAppList(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String operator = args[0].toString();

        try {
            List<Integer> opertatorAppIds = OperatorDAO.getApplicationsByOperator(operator);
            int i = 0;
            if (opertatorAppIds != null) {
                for (Integer temp : opertatorAppIds) {
                    String appName = SbHostObjectUtils.getApplicationNameById(temp.toString());
                    if (appName != null) {
                        NativeObject appData = new NativeObject();
                        appData.put("id", appData, temp.toString());
                        appData.put("name", appData, appName);
                        nativeArray.put(i, nativeArray, appData);
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getOperatorAppList",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }

        return nativeArray;
    }

    /**
     * Js function_get total api traffic for histogram.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getTotalAPITrafficForHistogram(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String fromDate = args[0].toString();
        String toDate = args[1].toString();
        String subscriber = args[2].toString();
        int applicationId = Integer.parseInt(args[3].toString());
        String operator = args[4].toString();
        String api = args[5].toString();

        try {
            List<String[]> api_requests = SbHostObjectUtils.getTotalAPITrafficForHistogram(fromDate, toDate, subscriber, operator, applicationId, api);
            List<String[]> apis = SbHostObjectUtils.getAllAPIs(fromDate, toDate, subscriber, operator, applicationId, api);
            NativeArray apiHits = null;
            NativeArray apiHitDates = null;

            if (api_requests != null && apis != null) {
                for (int i = 0; i < apis.size(); i++) {
                    apiHits = new NativeArray(0);
                    apiHitDates = new NativeArray(0);
                    int x = 0;
                    for (int j = 0; j < api_requests.size(); j++) {
                        if (apis.get(i)[0].toString().equals(api_requests.get(j)[0].toString())) {
                            apiHits.put(x, apiHits, api_requests.get(j)[2].toString());
                            apiHitDates.put(x, apiHitDates, api_requests.get(j)[1].toString());
                            x++;
                        }
                    }
                    NativeObject reqData = new NativeObject();
                    reqData.put("apiName", reqData, apis.get(i)[0].toString());
                    reqData.put("apiHits", reqData, apiHits);
                    reqData.put("apiHitDates", reqData, apiHitDates);
                    nativeArray.put(i, nativeArray, reqData);
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getTotalTrafficForHistogram");
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get operator wise api traffic for pie chart.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getOperatorWiseAPITrafficForPieChart(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String fromDate = args[0].toString();
        String toDate = args[1].toString();
        String subscriber = args[2].toString();
        int applicationId = Integer.parseInt(args[3].toString());
        String api = args[4].toString();

        try {
            List<String[]> api_requests = SbHostObjectUtils.getOperatorWiseAPITrafficForPieChart(fromDate, toDate, subscriber, api, applicationId);

            if (api_requests != null) {
                int i = 0;
                for (String[] api_request : api_requests) {
                    nativeArray.put(i, nativeArray, api_request);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getTotalTrafficForHistogram",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get subscribers by operator.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     * @throws BusinessException 
     */
    public static NativeArray jsFunction_getSubscribersByOperator(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String operatorName = args[0].toString();

        try {
            List<String> subscribers = SbHostObjectUtils.getSubscribersByOperator(operatorName);

            if (subscribers != null) {
                int i = 0;
                for (String subscriber : subscribers) {
                    nativeArray.put(i, nativeArray, subscriber);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getSubscribersByOperator",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get applications by subscriber.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     * @throws BusinessException 
     */
    public static NativeArray jsFunction_getApplicationsBySubscriber(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String subscriberName = args[0].toString();

        try {
            List<String[]> applications = SbHostObjectUtils.getApplicationsBySubscriber(subscriberName);

            if (applications != null) {
                int i = 0;
                for (String[] application : applications) {
                    nativeArray.put(i, nativeArray, application);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getApplicationsBySubscriber",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get operators by subscriber.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getOperatorsBySubscriber(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String subscriberName = args[0].toString();

        try {
            List<String> operators = SbHostObjectUtils.getOperatorsBySubscriber(subscriberName);

            if (operators != null) {
                int i = 0;
                for (String operator : operators) {
                    nativeArray.put(i, nativeArray, operator);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getOperatorsBySubscriber",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get ap is by subscriber.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getAPIsBySubscriber(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String subscriberName = args[0].toString();

        try {
            List<String> apis = SbHostObjectUtils.getAPIsBySubscriber(subscriberName);

            if (apis != null) {
                int i = 0;
                for (String api : apis) {
                    nativeArray.put(i, nativeArray, api);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getAPIsBySubscriber",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get all operation types.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getAllOperationTypes(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);
        List<String[]> opTypes;
        try {
            opTypes = SbHostObjectUtils.getOperationTypes();

            if (opTypes != null) {
                int i = 0;
                for (String[] operation : opTypes) {
                    nativeArray.put(i, nativeArray, operation);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getAllOperationTypes",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get approval history.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getApprovalHistory(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String fromDate = null;
        String toDate = null;
        String subscriber = args[2].toString();
        int applicationId = Integer.parseInt(args[3].toString());
        String operator = (String) args[4];
        String api = null;

        try {
            List<String[]> api_requests = SbHostObjectUtils.getApprovalHistory(fromDate, toDate, subscriber, api, applicationId, operator);

            if (api_requests != null) {
                int i = 0;
                for (String[] api_request : api_requests) {
                    nativeArray.put(i, nativeArray, api_request);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getTotalTrafficForHistogram",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get approval history app.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native object
     * @throws APIManagementException the API management exception
     */
    public static NativeObject jsFunction_getApprovalHistoryApp(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        int applicationId = Integer.parseInt(args[0].toString());
        String operator = args[1].toString();
        //String api = args[4].toString();

        NativeObject Application = new NativeObject();
        NativeArray opcoapps = new NativeArray(0);
        NativeArray Subscriptions = new NativeArray(0);
        NativeArray opcosubs = new NativeArray(0);

        try {

            List<Approval> api_requests = SbHostObjectUtils.getApprovalHistoryApp(applicationId, operator);

            if (api_requests != null) {
                int j = 0;
                int k = 0;
                int ai = 0;
                for (Approval obj : api_requests) {
                    //set application status

                    String retstat = obj.getIsactive();
                    if ((obj.getIsactive().equalsIgnoreCase("CREATED")) || (obj.getIsactive().equalsIgnoreCase("ON_HOLD"))) {
                        retstat = "PENDING APPROVE";
                    } else if (obj.getIsactive().equalsIgnoreCase("UNBLOCKED")) {
                        retstat = "APPROVED";
                    }

                    if (obj.getType().equalsIgnoreCase("1")) {
                        NativeObject app = new NativeObject();
                        app.put("appid", app, obj.getApplication_id());
                        app.put("type", app, obj.getType());
                        app.put("name", app, obj.getName());
                        app.put("operatorid", app, obj.getOperatorid());
                        app.put("status", app, retstat);
                        Application.put("application", Application, app);
                    } else if (obj.getType().equalsIgnoreCase("2")) {
                        NativeObject appopco = new NativeObject();
                        appopco.put("type", appopco, obj.getType());
                        appopco.put("name", appopco, obj.getName());
                        appopco.put("operatorid", appopco, obj.getOperatorid());
                        appopco.put("status", appopco, retstat);
                        opcoapps.put(ai, opcoapps, appopco);
                        ai++;
                    } else if (obj.getType().equalsIgnoreCase("3")) {
                        NativeObject sub = new NativeObject();
                        sub.put("appid", sub, obj.getApplication_id());
                        sub.put("type", sub, obj.getType());
                        sub.put("name", sub, obj.getName());
                        sub.put("operatorid", sub, obj.getOperatorid());
                        sub.put("status", sub, retstat);
                        sub.put("tier", sub, obj.getTier_id());
                        sub.put("api", sub, obj.getApi_name());
                        sub.put("apiversion", sub, obj.getApi_version());
                        Subscriptions.put(j, Subscriptions, sub);
                        j++;
                    } else if (obj.getType().equalsIgnoreCase("4")) {
                        NativeObject subop = new NativeObject();
                        subop.put("appid", subop, obj.getApplication_id());
                        subop.put("type", subop, obj.getType());
                        subop.put("name", subop, obj.getName());
                        subop.put("operatorid", subop, obj.getOperatorid());
                        subop.put("status", subop, retstat);
                        subop.put("tier", subop, obj.getTier_id());
                        subop.put("api", subop, obj.getApi_name());
                        subop.put("apiversion", subop, obj.getApi_version());
                        subop.put("created", subop, obj.getCreated());
                        subop.put("lastupdated", subop, obj.getLast_updated());

                        opcosubs.put(k, opcosubs, subop);
                        k++;
                    }
                }
                Application.put("opcoapps", Application, opcoapps);
                Application.put("Subscriptions", Application, Subscriptions);
                Application.put("opcosubs", Application, opcosubs);

            }
        } catch (Exception e) {
            log.error("Error occurred getTotalTrafficForHistogram",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return Application;
    }

    /**
     * Js function_get error response codes for pie chart.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getErrorResponseCodesForPieChart(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String fromDate = args[0].toString();
        String toDate = args[1].toString();
        String subscriber = args[2].toString();
        int applicationId = Integer.parseInt(args[3].toString());
        String operator = args[4].toString();
        String api = args[5].toString();

        try {
            List<String[]> api_requests = SbHostObjectUtils.getErrorResponseCodesForPieChart(fromDate, toDate, subscriber, operator, applicationId, api);

            if (api_requests != null) {
                int i = 0;
                for (String[] api_request : api_requests) {
                    nativeArray.put(i, nativeArray, api_request);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getErrorResponseCodesForPieChart");
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get error response codes for histogram.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getErrorResponseCodesForHistogram(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String fromDate = args[0].toString();
        String toDate = args[1].toString();
        String subscriber = args[2].toString();
        int applicationId = Integer.parseInt(args[3].toString());
        String operator = args[4].toString();
        String api = args[5].toString();

        try {
            List<String[]> api_response_codes = SbHostObjectUtils.getErrorResponseCodesForHistogram(fromDate, toDate, subscriber, operator, applicationId, api);

            List<String[]> resCodes = SbHostObjectUtils.getAllErrorResponseCodes(fromDate, toDate, subscriber, operator, applicationId, api);
            NativeArray apiHits = null;
            NativeArray apiHitDates = null;

            if (api_response_codes != null && resCodes != null) {
                for (int i = 0; i < resCodes.size(); i++) {
                    apiHits = new NativeArray(0);
                    apiHitDates = new NativeArray(0);
                    int x = 0;
                    for (int j = 0; j < api_response_codes.size(); j++) {
                        if (resCodes.get(i)[0].equals(api_response_codes.get(j)[0])) {
                            apiHitDates.put(x, apiHitDates, api_response_codes.get(j)[1]);
                            apiHits.put(x, apiHits, api_response_codes.get(j)[2]);
                            x++;
                        }
                    }
                    NativeObject reqData = new NativeObject();
                    reqData.put("errorCode", reqData, resCodes.get(i)[0]);
                    reqData.put("apiHits", reqData, apiHits);
                    reqData.put("apiHitDates", reqData, apiHitDates);
                    nativeArray.put(i, nativeArray, reqData);
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getErrorResponseCodesForHistogram",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);            
        }
        return nativeArray;
    }

    /**
     * Gets the max count.
     *
     * @param tier the tier
     * @return the max count
     * @throws XMLStreamException the XML stream exception
     */
    private static int getMaxCount(Tier tier) throws XMLStreamException {
        OMElement policy = AXIOMUtil.stringToOM(new String(tier.getPolicyContent()));
        OMElement maxCount = policy.getFirstChildWithName(APIConstants.POLICY_ELEMENT)
                .getFirstChildWithName(APIConstants.THROTTLE_CONTROL_ELEMENT)
                .getFirstChildWithName(APIConstants.POLICY_ELEMENT)
                .getFirstChildWithName(APIConstants.THROTTLE_MAXIMUM_COUNT_ELEMENT);
        if (maxCount != null) {
            return Integer.parseInt(maxCount.getText());
        }

        return -1;
    }

    /**
     * Gets the time unit.
     *
     * @param tier the tier
     * @return the time unit
     * @throws XMLStreamException the XML stream exception
     */
    private static int getTimeUnit(Tier tier) throws XMLStreamException {
        OMElement policy = AXIOMUtil.stringToOM(new String(tier.getPolicyContent()));
        OMElement timeUnit = policy.getFirstChildWithName(APIConstants.POLICY_ELEMENT)
                .getFirstChildWithName(APIConstants.THROTTLE_CONTROL_ELEMENT)
                .getFirstChildWithName(APIConstants.POLICY_ELEMENT)
                .getFirstChildWithName(APIConstants.THROTTLE_UNIT_TIME_ELEMENT);
        if (timeUnit != null) {
            return Integer.parseInt(timeUnit.getText());
        }
        return -1;
    }

    /**
     * Creates the tier pricing map.
     *
     * @throws APIManagementException the API management exception
     */
    private static void createTierPricingMap() throws APIManagementException {
        Map<String, Tier> tierMap = APIUtil.getTiers();
        for (Map.Entry<String, Tier> entry : tierMap.entrySet()) {
            Map<String, Object> attributes = entry.getValue().getTierAttributes();
            if (attributes != null && attributes.containsKey("Rate")) {
                tierPricing.put(entry.getKey(), Float.parseFloat(attributes.get("Rate").toString()));
            } else {
                tierPricing.put(entry.getKey(), 0f);
            }
            try {
                int maxCount = getMaxCount(entry.getValue());
                tierMaximumCount.put(entry.getKey(), maxCount);
            } catch (XMLStreamException e) {
            }

            try {
                int unitTime = getTimeUnit(entry.getValue());
                tierUnitTime.put(entry.getKey(), unitTime);
            } catch (XMLStreamException e) {
            }
        }
    }

    /**
     * Prints the tier pricing.
     *
     * @throws APIManagementException the API management exception
     */
    private static void printTierPricing() throws APIManagementException {
        createTierPricingMap();
        System.out.println("Print Tier Pricings");
        for (Map.Entry<String, Float> pricing : tierPricing.entrySet()) {
            System.out.println("Price for Tier : " + pricing.getKey() + " = " + pricing.getValue());
        }
    }

    /**
     * Handle exception.
     *
     * @param msg the msg
     * @throws APIManagementException the API management exception
     */
    private static void handleException(String msg) throws APIManagementException {
        log.error(msg);
        throw new APIManagementException(msg);
    }

    /**
     * Handle exception.
     *
     * @param msg the msg
     * @param t the t
     * @throws APIManagementException the API management exception
     */
    private static void handleException(String msg, Throwable t) throws APIManagementException {
        log.error(msg, t);
        throw new APIManagementException(msg, t);
    }

    /**
     * Js function_get s pfor blacklist.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws Exception 
     */
    @SuppressWarnings("null")
    public static NativeArray jsFunction_getSPforBlacklist(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws BusinessException {
        if (args == null || args.length == 0) {
            log.error("Invalid number of parameters.");
        }
        String operator = String.valueOf(args[2]);
        Boolean isadmin = Boolean.valueOf(String.valueOf(args[1]));
        
        BillingDAO billingDAO = new BillingDAO();
        
        List<SPObject> spList;
		
        try {
			spList = billingDAO.generateSPList();
		} catch (Exception e) {
			throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
		}
		
        List<SPObject> spListoperator = OperatorDAO.getSPList(operator);
        NativeArray nativeArray = new NativeArray(0);
        NativeObject nativeObject;
        if (spList != null) {
            int i = 0;
            for (SPObject spObject : spList) {
                NativeObject row = new NativeObject();
                row.put("appId", row, spObject.getAppId().toString());
                row.put("spName", row, spObject.getSpName());

                if (!isadmin) {
                    for (SPObject SPObjectoperator : spListoperator) {
                        if (SPObjectoperator.getAppId() == spObject.getAppId()) {
                            nativeArray.put(i, nativeArray, row);
                            break;
                        }
                    }
                } else {
                    nativeArray.put(i, nativeArray, row);
                }
                i++;
            }
        }
        
        return nativeArray;
    }

    /**
     * Js function_get appfor blacklist.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native object
     * @throws Exception 
     */
    public static NativeObject jsFunction_getAppforBlacklist(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws BusinessException {
        if (args == null || args.length == 0) {
            log.error("Invalid number of parameters.");
        }
        BillingDAO billingDAO = new BillingDAO();
        String appId = args[0].toString();
        SPObject spObject;
		try {
			spObject = billingDAO.generateSPObject(appId);
		} catch (Exception e) {
			throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
		}
        NativeObject row = new NativeObject();
        if (spObject != null) {

            row.put("appId", row, spObject.getAppId().toString());
            row.put("spName", row, spObject.getSpName());
            row.put("userName", row, spObject.getUserName());
            row.put("token", row, spObject.getToken());
            row.put("secret", row, spObject.getSecret());
            row.put("key", row, spObject.getKey());
        }
        return row;
    }

    /**
     * Js function_get dashboard api traffic for pie chart.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getDashboardAPITrafficForPieChart(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String timeRange = args[0].toString();
        String operator = args[1].toString();
        String subscriber = args[2].toString();

        if (operator.equalsIgnoreCase("All")) {
            operator = HostObjectConstants.ALL_OPERATORS;
        } else {
            operator = operator.toUpperCase();
        }
        if (subscriber.equalsIgnoreCase("All")) {
            subscriber = HostObjectConstants.ALL_SUBSCRIBERS;
        }

        Calendar now = Calendar.getInstance();
        String toDate = getCurrentTime(now);
        String fromDate = subtractTimeRange(now, timeRange);


        int applicationId = HostObjectConstants.ALL_APPLICATIONS;

        try {
            List<String[]> api_requests = SbHostObjectUtils.getTotalAPITrafficForPieChart(fromDate, toDate, subscriber, operator, applicationId);

            if (api_requests != null) {
                //get the total requests first to calculate the percentage
                double totalRequests = 0;
                for (String[] api_request : api_requests) {
                    totalRequests = totalRequests + Integer.parseInt(api_request[1]);
                }
                int i = 0;
                for (String[] api_request : api_requests) {
                    String[] chartData = new String[3];
                    chartData[0] = api_request[0];
                    chartData[1] = api_request[1];
                    double percentage = Math.round((Integer.parseInt(api_request[1]) * 100) / totalRequests);
                    chartData[2] = String.valueOf((int) percentage);
                    nativeArray.put(i, nativeArray, chartData);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred getDashboardAPITrafficForPieChart",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

     
    /**
     * Subtract time range.
     *
     * @param date the date
     * @param timeRange the time range
     * @return the string
     */
    private static String subtractTimeRange(Calendar date, String timeRange) {
        String fromDate = null;
        if (timeRange.equals(HostObjectConstants.DATE_LAST_DAY)) {
            date.add(Calendar.DATE, -1);
        } else if (timeRange.equals(HostObjectConstants.DATE_LAST_WEEK)) {
            date.add(Calendar.DATE, -7);
        } else if (timeRange.equals(HostObjectConstants.DATE_LAST_MONTH)) {
            date.add(Calendar.MONTH, -1);
        } else if (timeRange.equals(HostObjectConstants.DATE_LAST_YEAR)) {
            date.add(Calendar.YEAR, -1);
        }

        fromDate = date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DATE);
        return fromDate;
    }

     
    /**
     * Gets the current time.
     *
     * @param date the date
     * @return the current time
     */
    private static String getCurrentTime(Calendar date) {
        return date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DATE);
    }

     
    /**
     * Gets the time in milli.
     *
     * @param date the date
     * @return the time in milli
     */
    private static String getTimeInMilli(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "";
        Date parsedDate;
        try {
            parsedDate = format.parse(date);
            dateString = String.valueOf(parsedDate.getTime());
        } catch (ParseException e) {
            log.error("error in parsing the date");
        }

        return dateString;
    }

    /**
     * Js function_get dashboard api traffic for line chart.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getDashboardAPITrafficForLineChart(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
        NativeArray nativeArray = new NativeArray(0);

        String timeRange = args[0].toString();
        String operator = args[1].toString();
        String subscriber = args[2].toString();

        if (operator.equalsIgnoreCase("All")) {
            operator = HostObjectConstants.ALL_OPERATORS;
        } else {
            operator = operator.toUpperCase();
        }
        if (subscriber.equalsIgnoreCase("All")) {
            subscriber = HostObjectConstants.ALL_SUBSCRIBERS;
        }

        Calendar now = Calendar.getInstance();
        String toDate = getCurrentTime(now);
        String fromDate = subtractTimeRange(now, timeRange);


        int applicationId = HostObjectConstants.ALL_APPLICATIONS;
        String api = HostObjectConstants.ALL_APIS;

        try {
            List<String[]> api_requests = SbHostObjectUtils.getTotalAPITrafficForLineChart(fromDate, toDate, subscriber, operator, applicationId, api);
            NativeArray apiHits = null;
            NativeArray apiHitDates = null;

            apiHits = new NativeArray(0);
            apiHitDates = new NativeArray(0);
            int x = 0;

            for (int j = 0; j < api_requests.size(); j++) {
                apiHits.put(x, apiHits, api_requests.get(j)[1].toString());
                String hitDateInMilli = getTimeInMilli(api_requests.get(j)[0].toString());
                apiHitDates.put(x, apiHitDates, hitDateInMilli);
                x++;
            }

            NativeObject reqData = new NativeObject();
            reqData.put("apiHits", reqData, apiHits);
            reqData.put("apiHitDates", reqData, apiHitDates);
            reqData.put("startDate", reqData, getTimeInMilli(fromDate));
            nativeArray.put(0, nativeArray, reqData);
        } catch (Exception e) {
            log.error("Error occurred getTotalAPITrafficForLineChart",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeArray;
    }

    /**
     * Js function_get dashboard api response time for line chart.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native object
     * @throws APIManagementException the API management exception
     */
    public static NativeObject jsFunction_getDashboardAPIResponseTimeForLineChart(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws BusinessException {
    	
        NativeObject nativeObject = new NativeObject();

        String timeRange = args[0].toString();
        String operator = args[1].toString();
        String subscriber = args[2].toString();

        if (operator.equalsIgnoreCase("All")) {
            operator = HostObjectConstants.ALL_OPERATORS;
        } else {
            operator = operator.toUpperCase();
        }
        if (subscriber.equalsIgnoreCase("All")) {
            subscriber = HostObjectConstants.ALL_SUBSCRIBERS;
        }

        Calendar now = Calendar.getInstance();
        String toDate = getCurrentTime(now);
        String fromDate = subtractTimeRange(now, timeRange);


        try {
            List<APIResponseDTO> apiResponses = SbHostObjectUtils.getTotalAPIResponseTimeForLineChart(fromDate, toDate, subscriber, operator, timeRange);

            NativeArray apiServiceTimes = new NativeArray(0);
            NativeArray apiResponseCount = new NativeArray(0);
            NativeArray apiAvgServiceTime = new NativeArray(0);
            NativeArray apiHitDates = new NativeArray(0);
            int x = 0;


            int serviceTime = 0;
            int respCount = 0;
            int avgServiceTime = 0;

            for (int j = 0; j < apiResponses.size(); j++) {
                APIResponseDTO temp = apiResponses.get(j);

                serviceTime = temp.getServiceTime();
                respCount = temp.getResponseCount();
                avgServiceTime = serviceTime / respCount;

                String hitDateInMilli = getTimeInMilli(temp.getDate().toString());

                apiServiceTimes.put(x, apiServiceTimes, Integer.toString(serviceTime));
                apiResponseCount.put(x, apiResponseCount, Integer.toString(respCount));
                apiAvgServiceTime.put(x, apiAvgServiceTime, Integer.toString(avgServiceTime));
                apiHitDates.put(x, apiHitDates, hitDateInMilli);

                x++;
            }


            NativeObject respData = new NativeObject();
            respData.put("apiServiceTimes", respData, apiServiceTimes);
            respData.put("apiResponseCount", respData, apiResponseCount);
            respData.put("apiAvgServiceTime", respData, apiAvgServiceTime);
            respData.put("apiHitDates", respData, apiHitDates);

            nativeObject.put("responseTimes", nativeObject, respData);
            nativeObject.put("startDate", nativeObject, getTimeInMilli(fromDate));

        } catch (Exception e) {
            log.error("Error occurred getDashboardAPIResponseTimeForLineChart");
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }
        return nativeObject;
    }

    /**
     * Js function_get dashboard response times by api.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
    public static NativeArray jsFunction_getDashboardResponseTimesByAPI(Context cx, Scriptable thisObj, Object[] args, Function funObj)
            throws BusinessException {

        String timeRange = args[0].toString();
        String operator = args[1].toString();
        String subscriber = args[2].toString();

        if (operator.equalsIgnoreCase("All")) {
            operator = HostObjectConstants.ALL_OPERATORS;
        } else {
            operator = operator.toUpperCase();
        }
        if (subscriber.equalsIgnoreCase("All")) {
            subscriber = HostObjectConstants.ALL_SUBSCRIBERS;
        }

        Calendar now = Calendar.getInstance();
        String toDate = getCurrentTime(now);
        String fromDate = subtractTimeRange(now, timeRange);

        NativeArray apis = new NativeArray(0);
        try {
            Map<String, List<APIResponseDTO>> responseMap = SbHostObjectUtils.getAllResponseTimesByDate(operator, subscriber, fromDate, toDate);
            short i = 0;
            int serviceTime = 0;
            int respCount = 0;
            int avgServiceTime = 0;

            for (Map.Entry<String, List<APIResponseDTO>> timeEntry : responseMap.entrySet()) {

                NativeObject api = new NativeObject();
                api.put("apiName", api, timeEntry.getKey());

                NativeArray responseTimes = new NativeArray(0);
                for (APIResponseDTO dto : timeEntry.getValue()) {
                    NativeObject responseData = new NativeObject();


                    serviceTime = dto.getServiceTime();
                    respCount = dto.getResponseCount();
                    avgServiceTime = serviceTime / respCount;

                    String hitDateInMilli = getTimeInMilli(dto.getDate().toString());
                    responseData.put("apiServiceTimes", responseData, serviceTime);
                    responseData.put("apiResponseCount", responseData, respCount);
                    responseData.put("apiAvgServiceTime", responseData, avgServiceTime);
                    responseData.put("apiHitDates", responseData, hitDateInMilli);

                    responseTimes.put(responseTimes.size(), responseTimes, responseData);
                }
                api.put("responseData", api, responseTimes);
                apis.put(i, apis, api);
                i++;
            }

        } catch (Exception e) {
            log.error("Error occured getAllResponseTimes ",e);
            throw new BusinessException(ReportingServiceError.INTERNAL_SERVER_ERROR);
        }

        return apis;
    }

    /**
     * Js function_get dashboard time consumers by api.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native object
     * @throws APIManagementException the API management exception
     */
    public static NativeObject jsFunction_getDashboardTimeConsumersByAPI(Context cx, Scriptable thisObj, Object[] args, Function funObj)
            throws APIManagementException {

        String timeRange = args[0].toString();
        String operator = args[1].toString();
        String subscriber = args[2].toString();

        if (operator.equalsIgnoreCase("All")) {
            operator = HostObjectConstants.ALL_OPERATORS;
        } else {
            operator = operator.toUpperCase();
        }
        if (subscriber.equalsIgnoreCase("All")) {
            subscriber = HostObjectConstants.ALL_SUBSCRIBERS;
        }

        Calendar now = Calendar.getInstance();
        String toDate = getCurrentTime(now);
        String fromDate = subtractTimeRange(now, timeRange);

        NativeObject apiConsumpData = new NativeObject();
        NativeArray slowestApis = new NativeArray(0);
        NativeArray chartData = new NativeArray(0);

        try {

            Map<String, String[]> responseMap = SbHostObjectUtils.getTimeConsumptionForAllAPIs(operator, subscriber, fromDate, toDate);
            short i = 0;

            for (Map.Entry<String, String[]> timeEntry : responseMap.entrySet()) {
                NativeObject slowestApiInfo = new NativeObject();
                NativeObject chartDataForApi = new NativeObject();



                String[] data = timeEntry.getValue();
                slowestApiInfo.put("apiName", slowestApiInfo, timeEntry.getKey());
                slowestApiInfo.put("highestAvgConsumption", slowestApiInfo, data[1]);

                chartDataForApi.put("apiName", chartDataForApi, timeEntry.getKey());
                chartDataForApi.put("totalAvgConsumption", chartDataForApi, data[2]);


                slowestApis.put(i, slowestApis, slowestApiInfo);
                chartData.put(i, chartData, chartDataForApi);
                i++;
            }

            apiConsumpData.put("slowestApis", apiConsumpData, slowestApis);
            apiConsumpData.put("chartData", apiConsumpData, chartData);


        } catch (Exception e) {
            log.error("Error occured getAllResponseTimes ");
            log.error(e.getStackTrace());
            handleException("Error occurred while populating Response Time graph.", e);
        }

        return apiConsumpData;
    }
}
