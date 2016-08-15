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
package com.wso2telco.dep.mediator.mediationrule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.msisdnvalidator.*;
import com.wso2telco.dbutils.fileutils.FileReader;
import com.wso2telco.dep.mediator.OperatorEndpoint;
import com.wso2telco.dep.mediator.entity.OparatorEndPointSearchDTO;
import com.wso2telco.dep.mediator.impl.smsmessaging.SendSMSHandler;
import com.wso2telco.dep.mediator.util.ErrorHolder;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorApplicationDTO;
import com.wso2telco.dep.operatorservice.model.OperatorEndPointDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;
import com.wso2telco.mnc.resolver.MNCQueryClient;
import com.wso2telco.oneapivalidation.exceptions.CustomException;

import org.apache.synapse.core.axis2.Axis2MessageContext;

// TODO: Auto-generated Javadoc
/**
 * The Class OriginatingCountryCalculatorIDD.
 */
public class OriginatingCountryCalculatorIDD extends OriginatingCountryCalculator {

	private Log log = LogFactory.getLog(OriginatingCountryCalculatorIDD.class);
	/** The error message. */
	private String ERROR_MESSAGE = "Error.No API endpoint matched your request";

	/** The operatorEndpoints. */
	List<OperatorEndPointDTO> operatorEndpoints;

	/** The mnc queryclient. */
	MNCQueryClient mncQueryclient = null;
	
	private MSISDNUtil phoneUtil ;
	    
	private  static Set<String> countryLookUpOnHeader = new HashSet<String>();
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wso2telco.mediator.mediationrule.OriginatingCountryCalculator#
	 * initialize()
	 */
	public void initialize() throws Exception {

		operatorEndpoints = new OparatorService().getOperatorEndpoints();
		mncQueryclient = new MNCQueryClient();
		phoneUtil = new MSISDNUtil();
	}

	static{
		
		FileReader fileReader = new FileReader();
		Map<String, String> mediatorConfMap = fileReader.readMediatorConfFile();
		String countries = mediatorConfMap.get("search.oparatorOnHeader");
		if (countries != null) {
			// Split the comma separated country codes
			String[] countryArray = countries.split(",");
			for (String string : countryArray) {
				countryLookUpOnHeader.add(string.trim());
			}
		}
	}
	

	
	/**
	 * Gets the API endpoints by msisdn.
	 *
	 * @param userMSISDN
	 *            the user msisdn
	 * @param apikey
	 *            the apikey
	 * @param requestPathURL
	 *            the request path url
	 * @param isredirect
	 *            the isredirect
	 * @param operators
	 *            the operators
	 * @return the API endpoints by msisdn
	 * @throws Exception
	 *             the exception
	 */
	  public OperatorEndpoint getAPIEndpointsByMSISDN(String userMSISDN, String apikey, String requestPathURL, boolean isredirect, List<OperatorApplicationDTO> operators) throws Exception {
		String operator;
		String userName = userMSISDN.substring(1);

		// Initialize End points
		initialize();

		String mcc = null;
		// mcc not known in mediator
		operator = mncQueryclient.QueryNetwork(mcc, userMSISDN);

		if (operator == null) {

			throw new CustomException("SVC0001", "", new String[] { "No valid operator found for given MSISDN" });
		}

		// is operator provisioned
		OperatorApplicationDTO valid = null;
		for (OperatorApplicationDTO d : operators) {

			if (d.getOperatorname() != null && d.getOperatorname().contains(operator.toUpperCase())) {
				valid = d;
				break;
			}
		}

		if (valid == null) {

			throw new CustomException("SVC0001", "", new String[] { "Requested service is not provisioned" });
		}

		OperatorEndPointDTO validOperatorendpoint = getValidEndpoints(apikey, operator);
		if (validOperatorendpoint == null) {

			throw new CustomException("SVC0001", "", new String[] { "Requested service is not provisioned" });
		}

		String extremeEndpoint = validOperatorendpoint.getEndpoint();
		if (!isredirect) {

			extremeEndpoint = validOperatorendpoint.getEndpoint() + requestPathURL;
		}

		EndpointReference eprMSISDN = new EndpointReference(extremeEndpoint);

		return new OperatorEndpoint(eprMSISDN, operator.toUpperCase());
	}
	
	
	  /**
	       * this will return the end point base on serchDTO
	       * @param searchDTO
	       * @return
	       * @throws Exception
	       */
	public OperatorEndpoint getOperatorEndpoint(
			final OparatorEndPointSearchDTO searchDTO) throws Exception {

		if (searchDTO == null) {
			throw new CustomException(
					ErrorHolder.INVALID_SERVICE_INVORK.getCode(), "",
					new String[] { ErrorHolder.INVALID_SERVICE_INVORK
							.getDescription() });
		}

		String operator = null;

		// Initialize End points
		initialize();

		/**
		 * MSISDN provided at JSon body convert into Phone number object.
		 */
		MSISDN numberProto = phoneUtil.parse(searchDTO.getMSISDN());

		/**
		 * obtain the country code form the phone number object
		 */
		int countryCode = numberProto.getCountryCode();

		/**
		 * if the country code within the header look up context , the operator
		 * taken from the header object
		 */
		if (countryLookUpOnHeader.contains(String.valueOf(countryCode))) {
			
			log.debug(" The request countryCode in the lookup set ");

			MessageContext axis2MessageContext = ((Axis2MessageContext) searchDTO
					.getContext()).getAxis2MessageContext();
			Object headers = axis2MessageContext
					.getProperty(MessageContext.TRANSPORT_HEADERS);
			if (headers != null && headers instanceof Map) {
				Map headersMap = (Map) headers;
				/*if (headersMap != null) {
					log.info("printing header");
					for (Object iterator : headersMap.entrySet()) {
						Map.Entry entry = (Map.Entry) iterator;
						log.info("Key :" + entry.getKey() + " value : "
								+ entry.getValue());
					}
				}*/
				operator = (String) headersMap.get("oparator");
				log.debug(" Operator pick from the Header : " + operator);
			}else{
			 	log.debug(" The request doesnot obtain from the header");
			}
			

		}
		/**
		 * build the MSISDN
		 */
		StringBuffer msisdn = new StringBuffer();
		msisdn.append("+").append(numberProto.getCountryCode())
				.append(numberProto.getNationalNumber());

		/**
		 * if the operator still not selected the operator selection logic goes
		 * as previous .ie select from MCC_NUMBER_RANGE
		 */
		if (operator == null) {

			String mcc = null;

			/*
			 * if(countryCode>=0){ mcc = String.valueOf("+"+countryCode); }
			 */
			// mcc not known in mediator
			log.debug(" Unable to obtain Operator   from the Header ,Oprator look for mcc_range_table ,operator:"+operator+ " mcc :"+mcc+ "msisdn: " +msisdn.toString());
			operator = mncQueryclient.QueryNetwork(mcc, msisdn.toString());
		}

		if (operator == null) {
			throw new CustomException(ErrorHolder.NO_VALID_OPARATOR.getCode(),
					"",
					new String[] { ErrorHolder.NO_VALID_OPARATOR
							.getDescription() });
		}

		// is operator provisioned
		OperatorApplicationDTO valid = null;
		for (OperatorApplicationDTO d : searchDTO.getOperators()) {
			if (d.getOperatorname() != null
					&& d.getOperatorname().contains(operator.toUpperCase())) {
				valid = d;
				break;
			}
		}

		if (valid == null) {
			throw new CustomException(
					ErrorHolder.OPARATOR_NOT_PROVISIONED.getCode(), "",
					new String[] { ErrorHolder.OPARATOR_NOT_PROVISIONED
							.getDescription() });
		}

		OperatorEndPointDTO validOperatorendpoint = getValidEndpoints(searchDTO.getApiName()!=null?searchDTO.getApiName(): searchDTO.getApiType().getCode(), operator);

		if (validOperatorendpoint == null) {
			throw new CustomException(
					ErrorHolder.OPARATOR_ENDPOINT_NOT_DEFIEND.getCode(), "",
					new String[] { ErrorHolder.OPARATOR_ENDPOINT_NOT_DEFIEND
							.getDescription() });
		}

		String extremeEndpoint = validOperatorendpoint.getEndpoint();
		if (!searchDTO.isIsredirect()) {
			extremeEndpoint = validOperatorendpoint.getEndpoint()
					+ searchDTO.getRequestPathURL();
		}
		EndpointReference eprMSISDN = new EndpointReference(extremeEndpoint);

		return new OperatorEndpoint(eprMSISDN, operator.toUpperCase());

	}
	

	/**
	 * Gets the API endpoints by app.
	 *
	 * @param apiKey
	 *            the api key
	 * @param requestPathURL
	 *            the request path url
	 * @param validoperator
	 *            the validoperator
	 * @return the API endpoints by app
	 * @throws Exception
	 *             the exception
	 */
	public List<OperatorEndpoint> getAPIEndpointsByApp(String apiKey, String requestPathURL,
			List<OperatorApplicationDTO> validoperator) throws Exception {

		List<OperatorEndpoint> endpoints = new ArrayList<OperatorEndpoint>();

		initialize();

		List<OperatorEndPointDTO> validendpoints = getValidEndpoints(apiKey, validoperator);
		String extremeEndpoint;

		for (OperatorEndPointDTO oe : validendpoints) {

			extremeEndpoint = oe.getEndpoint() + requestPathURL;
			endpoints.add(new OperatorEndpoint(new EndpointReference(extremeEndpoint), oe.getOperatorcode()));
		}

		return endpoints;
	}

	/**
	 * Str_piece.
	 *
	 * @param str
	 *            the str
	 * @param separator
	 *            the separator
	 * @param index
	 *            the index
	 * @return the string
	 */
	private String str_piece(String str, char separator, int index) {

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

	/**
	 * Gets the application property.
	 *
	 * @param operatorcode
	 *            the operatorcode
	 * @param api
	 *            the api
	 * @return the application property
	 */
	private String getApplicationProperty(String operatorcode, String api) {

		String endpoint = null;
		for (OperatorEndPointDTO d : operatorEndpoints) {

			if ((d.getApi().contains(api)) && (d.getOperatorcode().contains(operatorcode))) {

				endpoint = d.getEndpoint();
				break;
			}
		}

		return endpoint;
	}

	/**
	 * Gets the valid endpoints.
	 *
	 * @param api
	 *            the api
	 * @param validoperator
	 *            the validoperator
	 * @return the valid endpoints
	 */
	private List<OperatorEndPointDTO> getValidEndpoints(String api, List<OperatorApplicationDTO> validoperator) {

		String endpoint = null;
		List<String> validlist = new ArrayList();
		List<OperatorEndPointDTO> validoperendpoints = new ArrayList();

		for (OperatorApplicationDTO op : validoperator) {

			validlist.add(op.getOperatorname());
		}

		for (OperatorEndPointDTO d : operatorEndpoints) {

			if ((d.getApi().contains(api)) && (validlist.contains(d.getOperatorcode()))) {

				validoperendpoints.add(d);
			}
		}

		return validoperendpoints;
	}

	/**
	 * Gets the valid endpoints.
	 *
	 * @param api
	 *            the api
	 * @param validoperator
	 *            the validoperator
	 * @return the valid endpoints
	 */
	private OperatorEndPointDTO getValidEndpoints(String api, String validoperator) {

		String endpoint = null;
		OperatorEndPointDTO validoperendpoint = null;

		for (OperatorEndPointDTO d : operatorEndpoints) {

			if ((d.getApi().equalsIgnoreCase(api)) && (validoperator.equalsIgnoreCase(d.getOperatorcode()) ) ) {

				validoperendpoint = d;
				 break;
			}
		}

		return validoperendpoint;
	}
	
	public OperatorEndpoint getAPIEndpointsByMNO(String mobileOpco,
			String apikey, String requestPathURL, boolean isredirect,
			List<OperatorApplicationDTO> operators) throws Exception {

		String operator = mobileOpco;

		// Initialize End points
		initialize();

		if (operator == null) {
			throw new CustomException("SVC0001", "",
					new String[] { "No valid operator found" });
		}

		// is operator provisioned
		OperatorApplicationDTO valid = null;
		for (OperatorApplicationDTO d : operators) {
			if (d.getOperatorname() != null
					&& d.getOperatorname().contains(operator.toUpperCase())) {
				valid = d;
				break;
			}
		}

		if (valid == null) {
			throw new CustomException("SVC0001", "",
					new String[] { "Requested service is not provisioned" });
		}

		OperatorEndPointDTO validOperatorendpoint = getValidEndpoints(apikey,
				operator);
		if (validOperatorendpoint == null) {
			throw new CustomException("SVC0001", "",
					new String[] { "Requested service is not provisioned" });
		}

		String extremeEndpoint = validOperatorendpoint.getEndpoint();
		if (!isredirect) {
			extremeEndpoint = validOperatorendpoint.getEndpoint()
					+ requestPathURL;
		}
		EndpointReference eprMSISDN = new EndpointReference(extremeEndpoint);

		return new OperatorEndpoint(eprMSISDN, operator.toUpperCase());

	}
		 
}
