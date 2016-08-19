/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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

package com.wso2telco.dep.mediator.impl.payment;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.utils.CarbonUtils;

import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.MSISDNConstants;
import com.wso2telco.dep.mediator.entity.DailyLimit;
import com.wso2telco.dep.mediator.entity.DailyLimitList;
import com.wso2telco.dep.mediator.internal.Base64Coder;
import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.publisheventsdata.handler.SpendLimitHandler;

/**
 *
 * @author Wso2telco
 */

public class PaymentUtil {
	private static Log log = LogFactory.getLog(PaymentUtil.class);

	public static String storeSubscription(MessageContext context)
			throws AxisFault {
		String subscription = null;

		org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
				.getAxis2MessageContext();
		Object headers = axis2MessageContext
				.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
		if (headers != null && headers instanceof Map) {
			try {
				Map headersMap = (Map) headers;
				String jwtparam = (String) headersMap.get("x-jwt-assertion");
				String[] jwttoken = jwtparam.split("\\.");
				String jwtbody = Base64Coder.decodeString(jwttoken[1]);
				JSONObject jwtobj = new JSONObject(jwtbody);
				subscription = jwtobj
						.getString("http://wso2.org/claims/subscriber");

			} catch (JSONException ex) {
				throw new AxisFault("Error retriving application id");
			}
		}

		return subscription;
	}

	
    public boolean checkSpendLimit(String msisdn, String operator, MessageContext context, Double chargeAmount) throws AxataDBUtilException,IOException,JAXBException {
        
    	AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext(context);
		String consumerKey = "";
		FileReader fileReader = new FileReader();
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();
		
		if (authContext != null) {
			consumerKey = authContext.getConsumerKey();
		}

		SpendLimitHandler spendLimitHandler = new SpendLimitHandler();
				
		Double day_Msisdn_Limit = 0.0;
		Double month_Msisdn_Limit =0.0;
		Double totalDayCalculatedAmount = 0.0;
		Double totalMonthCalculateAmount = 0.0;
		
		day_Msisdn_Limit = Double.parseDouble(mediatorConfMap.get("msisdn_day_amount"));
		month_Msisdn_Limit = Double.parseDouble(mediatorConfMap.get("msisdn_month_amount"));
		
		String configPath = CarbonUtils.getCarbonConfigDirPath() + File.separator + "mifeEventDayLimit.xml";
        File file = new File(configPath);
				
		JAXBContext jaxbContext = JAXBContext.newInstance(DailyLimitList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		DailyLimitList dailyLimits = (DailyLimitList) jaxbUnmarshaller.unmarshal(file);
		
		for (Iterator iterator = dailyLimits.getDailyLimits().iterator(); iterator.hasNext();) {
		    DailyLimit dailyLimit = (DailyLimit) iterator.next();
		
		    String dayConsumerKey = String.valueOf(dailyLimit.getConsumerKey());
		    String dayOperator = dailyLimit.getOperator();
		    System.out.println(dayOperator);
		
		    if ( dayConsumerKey.equalsIgnoreCase(consumerKey) && dayOperator.equalsIgnoreCase(operator) &&dailyLimit.getAmount() > 0.0 ) {
		
		
		        totalDayCalculatedAmount = spendLimitHandler.getDayTotalCalculatedAmount(consumerKey,msisdn);
		        totalMonthCalculateAmount = spendLimitHandler.getMonthTotalCalculatedAmount(consumerKey,msisdn);
		
		        if ((totalDayCalculatedAmount >= dailyLimit.getAmount()) || ((chargeAmount + totalDayCalculatedAmount)> dailyLimit.getAmount())
				                || (dailyLimit.getAmount() <= chargeAmount)) {
		
		            throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[]{"daily"});
		
		        } else if (  (totalMonthCalculateAmount >= dailyLimit.getMonthAmount()) || ((totalMonthCalculateAmount + chargeAmount) > dailyLimit.getMonthAmount())
		                || (dailyLimit.getMonthAmount() < chargeAmount) ) {
		
		            throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[]{"monthly"});
		
		                }
		
		            }	
		}
 /*
    Double day_Application_Limit = 0.0;
    Double day_Oprator_Limit = 0.0;
    Double month_Application_Limit =0.0;
    Double month_Oprator_Limit = 0.0;
    Double calculated_day_Msisdn_Amount = 0.0;
   Double calculated_day_Application_Amount =0.0;
    Double calculated_day_Operator_Amount = 0.0;
    Double calculated_month_msisdn = 0.0;
    Double calculated_month_application=0.0;
    Double calculated_month_operator =0.0;
    day_Application_Limit = Double.parseDouble(Util.propMap.get("application_day_amount"));
    day_Oprator_Limit = Double.parseDouble( Util.propMap.get("operator_day_amount"));
    month_Application_Limit = Double.parseDouble(Util.propMap.get("application_month_amount"));
    month_Oprator_Limit = Double.parseDouble( Util.propMap.get("operator_month_amount"));

      for (DayLimit limit : lstMcc) {

            String pConsumerKey = String.valueOf(limit.getConsumerKey());
            if ( pConsumerKey.equalsIgnoreCase(consumerKey) && Double.parseDouble(limit.getAmount) > 0.0 ) {

                totalDayCalculatedAmount = spendLimitHandler.getDayTotalCalculatedAmount(consumerKey,msisdn);
                totalMonthCalculateAmount = spendLimitHandler.getMonthTotalCalculatedAmount(consumerKey,msisdn);

                if ((totalDayCalculatedAmount >= day_Msisdn_Limit) || ((chargeAmount + totalDayCalculatedAmount)> day_Msisdn_Limit)
                        || (day_Msisdn_Limit <= totalDayCalculatedAmount)) {

                    throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[]{"daily"});

                } else if (  (totalMonthCalculateAmount >= month_Msisdn_Limit) || ((totalMonthCalculateAmount + chargeAmount) > month_Msisdn_Limit)
                        || (month_Msisdn_Limit < chargeAmount) ) {

                    throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[]{"monthly"});

                }

                break;
            }
       } */



/*
                if (spendLimitHandler.isMSISDNSpendLimitExceeded(msisdn)> 0.0) {
                    calculated_day_Msisdn_Amount= spendLimitHandler.isMSISDNSpendLimitExceeded(msisdn);
                }
                if (spendLimitHandler.isApplicationSpendLimitExceeded(consumerKey) > 0.0) {
                    calculated_day_Application_Amount = spendLimitHandler.isApplicationSpendLimitExceeded(consumerKey) ;
                }
                if(spendLimitHandler.isOperatorSpendLimitExceeded(operator) >0.0) {
                    calculated_day_Operator_Amount = spendLimitHandler.isOperatorSpendLimitExceeded(operator);
                }
                if(spendLimitHandler.isMonthMSISDNSpendLimitExceeded(msisdn) >0.0) {
                    calculated_month_msisdn= spendLimitHandler.isMonthMSISDNSpendLimitExceeded(msisdn);
                }
                if(spendLimitHandler.isMonthOperatorSpendLimitExceeded(operator) >0.0) {
                    calculated_month_application = spendLimitHandler.isMonthOperatorSpendLimitExceeded(operator) ;
                }
                if(spendLimitHandler.isApplicationSpendLimitExceeded(consumerKey) >0.0) {
                    calculated_month_operator =spendLimitHandler.isApplicationSpendLimitExceeded(consumerKey);
                }



            if ((calculated_day_Msisdn_Amount >= day_Msisdn_Limit) || ((chargeAmount + calculated_day_Msisdn_Amount)> day_Msisdn_Limit)
                    || (day_Msisdn_Limit <= (chargeAmount))) {

               throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[]{"daily"});

            } else if (  (calculated_month_msisdn >= month_Msisdn_Limit) || ((calculated_month_msisdn + chargeAmount) > month_Msisdn_Limit)
                   || (month_Msisdn_Limit < chargeAmount) ) {

                throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[]{"monthly"});

            }


           else if ((calculated_day_Application_Amount >= day_Application_Limit) ||
                    ((calculated_day_Application_Amount + chargeAmount)> day_Application_Limit)
                    ||(day_Application_Limit <= chargeAmount)) {
		                throw new CustomException("POL1001", "The %1 charging limit for this application has been exceeded", new String[]{"daily"});

            } else if ((calculated_day_Operator_Amount >= day_Oprator_Limit)||(calculated_day_Operator_Amount+ chargeAmount > day_Oprator_Limit)
                    || (day_Oprator_Limit <= chargeAmount)) {

                throw new CustomException("POL1001", "The %1 charging limit for this operator has been exceeded", new String[]{"daily"});

            } else if (  (calculated_month_msisdn >= month_Msisdn_Limit) || ((calculated_month_msisdn + chargeAmount) > month_Msisdn_Limit)
                    || (month_Msisdn_Limit < chargeAmount) ) {

                throw new CustomException("POL1001", "The %1 charging limit for this user has been exceeded", new String[]{"monthly"});

            } else if ((calculated_month_application >= month_Oprator_Limit) ||((calculated_month_application+chargeAmount) > month_Oprator_Limit)
                    || (month_Msisdn_Limit < chargeAmount)) {

                throw new CustomException("POL1001", "The %1 charging limit for this operator has been exceeded", new String[]{"monthly"});

            } else if ((calculated_month_operator >= month_Application_Limit) || ((calculated_month_operator + chargeAmount )> month_Application_Limit)
                    || (month_Application_Limit < chargeAmount)) {
		                throw new CustomException("POL1001", "The %1 charging limit for this application has been exceeded", new String[]{"monthly"});
            }*/

		return true;
		
	}

	public static boolean isAggregator(MessageContext context) throws AxisFault {
		boolean aggregator = false;

		try {
			org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
					.getAxis2MessageContext();
			Object headers = axis2MessageContext
					.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
			if (headers != null && headers instanceof Map) {
				Map headersMap = (Map) headers;
				String jwtparam = (String) headersMap.get("x-jwt-assertion");
				String[] jwttoken = jwtparam.split("\\.");
				String jwtbody = Base64Coder.decodeString(jwttoken[1]);
				JSONObject jwtobj = new JSONObject(jwtbody);
				String claimaggr = jwtobj
						.getString("http://wso2.org/claims/role");
				if (claimaggr != null) {
					String[] allowedRoles = claimaggr.split(",");
					for (int i = 0; i < allowedRoles.length; i++) {
						if (allowedRoles[i]
								.contains(MSISDNConstants.AGGRIGATOR_ROLE)) {
							aggregator = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("Error retrive aggregator");
		}

		return aggregator;
	}

	public static void validatePaymentCategory(JSONObject chargingdmeta,
			List<String> lstCategories) throws JSONException {
		boolean isvalid = false;
		String chargeCategory = "";
		if ((!chargingdmeta.isNull("purchaseCategoryCode"))
				&& (!chargingdmeta.getString("purchaseCategoryCode").isEmpty())) {

			chargeCategory = chargingdmeta.getString("purchaseCategoryCode");
			for (String d : lstCategories) {
				if (d.equalsIgnoreCase(chargeCategory)) {
					isvalid = true;
					break;
				}
			}
		} else {
			isvalid = true;
		}

		if (!isvalid) {
			throw new CustomException("POL0001",
					"A policy error occurred. Error code is %1",
					new String[] { "Invalid " + "purchaseCategoryCode : "
							+ chargeCategory });
		}
	}

	public static String str_piece(String str, char separator, int index) {
		String str_result = "";
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == separator) {
				count++;
				if (count == index) {
					break;
				}
			} else {
				if (count == index - 1) {
					str_result += str.charAt(i);
				}
			}
		}
		return str_result;
	}

}
