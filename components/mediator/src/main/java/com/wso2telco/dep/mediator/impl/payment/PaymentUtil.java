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
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

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
import com.wso2telco.dep.mediator.entity.cep.ConsumerSecretWrapperDTO;
import com.wso2telco.dep.mediator.internal.Base64Coder;
import com.wso2telco.dep.mediator.unmarshaler.GroupDTO;
import com.wso2telco.dep.mediator.unmarshaler.GroupEventUnmarshaller;
import com.wso2telco.dep.mediator.util.FileNames;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.publisheventsdata.handler.SpendLimitHandler;

/**
 *
 * @author Wso2telco
 */

public class PaymentUtil {
	private static Log log = LogFactory.getLog(PaymentUtil.class);

	public boolean checkSpendLimit(String msisdn, String operator,
			MessageContext context, Double chargeAmount)
			throws AxataDBUtilException, IOException, JAXBException, Exception {

		Double groupTotalDayAmount = 0.0;
		Double groupTotalMonthAmount = 0.0;
		String consumerKey = null;
		
		AuthenticationContext authContext = APISecurityUtils
				.getAuthenticationContext(context);
		FileReader fileReader = new FileReader();
		String file = CarbonUtils.getCarbonConfigDirPath() + File.separator + FileNames.MEDIATOR_CONF_FILE.getFileName();
		Map<String, String> mediatorConfMap = fileReader.readPropertyFile(file);
		if (authContext != null) {
			consumerKey = authContext.getConsumerKey();
		}

		GroupEventUnmarshaller groupEventUnmarshaller = GroupEventUnmarshaller
				.getInstance();
		ConsumerSecretWrapperDTO consumerSecretWrapperDTO = groupEventUnmarshaller
				.getGroupEventDetailDTO(consumerKey);
		List<GroupDTO> groupDTOList = consumerSecretWrapperDTO
				.getConsumerKeyVsGroup();

		if (groupDTOList != null) {
			for (GroupDTO groupDTO : groupDTOList) {

				Double groupdailyLimit = Double.parseDouble(groupDTO
						.getDayAmount());
				Double groupMonlthlyLimit = Double.parseDouble(groupDTO
						.getMonthAmount());
				SpendLimitHandler spendLimitHandler = new SpendLimitHandler();
				groupTotalDayAmount = spendLimitHandler
						.getGroupTotalDayAmount(groupDTO.getGroupName(),
								groupDTO.getOperator(), msisdn);
				groupTotalMonthAmount = spendLimitHandler
						.getGroupTotalMonthAmount(groupDTO.getGroupName(),
								groupDTO.getOperator(), msisdn);

				if ((groupdailyLimit > 0.0)
						&& ((groupTotalDayAmount >= groupdailyLimit)
								|| (groupTotalDayAmount + chargeAmount) > groupdailyLimit || chargeAmount > groupdailyLimit)) {
					log.debug("group daily limit exceeded");
					throw new CustomException(
							"POL1001",
							"The %1 charging limit for this user has been exceeded",
							new String[] { "daily" });

				}

				if ((groupMonlthlyLimit) > 0.0
						&& ((groupTotalMonthAmount >= groupMonlthlyLimit)
								|| (groupTotalMonthAmount + chargeAmount) > groupMonlthlyLimit || chargeAmount > groupMonlthlyLimit)) {
					log.debug("group monthly limit exceeded");
					throw new CustomException(
							"POL1001",
							"The %1 charging limit for this user has been exceeded",
							new String[] { "monthly" });

				}
			}

		}

		return true;

	}

	
	
	
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
