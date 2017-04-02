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
package com.wso2telco.dep.oneapivalidation.util;

import com.wso2telco.core.dbutils.fileutils.FileReader;
import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import org.apache.log4j.Logger;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Validation.
 */
public class Validation {

    /** The logger. */
    static Logger logger = Logger.getLogger(Validation.class);
    
    /** The Constant serialVersionUID. */
    public static final long serialVersionUID = -8195763247832284073L;
    
    /** The Constant BAD_REQUEST. */
    public static final int BAD_REQUEST = 400;
    
    /** The Constant AUTHENTICATION_FAILURE. */
    public static final int AUTHENTICATION_FAILURE = 401;
    
    /** The Constant FORBIDDEN. */
    public static final int FORBIDDEN = 403;
    
    /** The Constant NOT_FOUND. */
    public static final int NOT_FOUND = 404;
    
    /** The Constant METHOD_NOT_SUPPORTED. */
    public static final int METHOD_NOT_SUPPORTED = 405;
    
    /** The Constant OK. */
    public static final int OK = 200;
    
    /** The Constant CREATED. */
    public static final int CREATED = 201;
    
    /** The Constant ACCEPTED. */
    public static final int ACCEPTED = 202;
    
    /** The Constant NONAUTHORITATIVE. */
    public static final int NONAUTHORITATIVE = 203;
    
    /** The Constant NOCONTENT. */
    public static final int NOCONTENT = 204;
    
    /** The dump request and response. */
    public static boolean dumpRequestAndResponse = false;

    public static ArrayList<String> customRegex = null;

    /** The Constant telFormats. */
    private static String[] telFormats = readCustomRegex();

    /** The Constant urlFormats. */
    private static final String[] urlFormats = {"http\\:\\/\\/.+", "https\\:\\/\\/.+"};

    /**
     * Checks if is correctly formatted number.
     *
     * @param tel the tel
     * @return true, if is correctly formatted number
     */


    static String[] readCustomRegex() {
        String[] telFormatsTemp = null;

        FileReader fileReader = new FileReader();
        String file = CarbonUtils.getCarbonConfigDirPath() + File.separator
                + FileNames.ONEAPI_VALIDATION_CONF_FILE.getFileName();

        try {
            Map<String, String> oneAPIValidationConfMap = fileReader.readPropertyFile(file);
            String customeRegexs = oneAPIValidationConfMap.get("validation.regex");
            String useCustomRegex = oneAPIValidationConfMap.get("customValidation");

            if (useCustomRegex.equals("true")) {
                if (!customeRegexs.equals("")) {
                    customRegex = new ArrayList<String>();
                    String customRegexArray[] = customeRegexs.split(",");

                    for (String reg : customRegexArray) {
                        customRegex.add(reg.trim());
                    }
                    telFormatsTemp = customRegex.toArray(new String[customRegex.size()]);
                    logger.info("Read custom validation from config file: " + Arrays.toString(telFormatsTemp));
                }
            } else {
                telFormatsTemp = new String[]{"tel\\:\\+[a-zA-Z0-9]+", "tel\\:[a-zA-Z0-9]+", "\\+[a-zA-Z0-9]+"};
            }

        } catch (Exception e) {
            logger.error("Error while reading custom custom regex. Default validation will be used.", e);
            telFormatsTemp = new String[]{"tel\\:\\+[a-zA-Z0-9]+", "tel\\:[a-zA-Z0-9]+", "\\+[a-zA-Z0-9]+"};
        }

        return telFormatsTemp;
    }

    public static boolean isCorrectlyFormattedNumber(String tel) {
        boolean matched = false;

            if (tel != null) {
                for (int i = 0; i < telFormats.length && !matched; i++) {
                    if (tel.matches(telFormats[i])) {
                        matched = true;
                    }
                    logger.debug("Number=" + tel + " matches regex=" + telFormats[i] + " = " + matched);
                }
            }
        return matched;
    }

     
    /**
     * Check duplicated address.
     *
     * @param addressTemp the address temp
     * @return the string
     */
    private static String checkDuplicatedAddress(String[] addressTemp) {
        Set<String> addressValueSet = new HashSet<String>();
        for (String tempValueSet : addressTemp) {
            if (addressValueSet.contains(tempValueSet)) {
                return tempValueSet;
            } else if (!tempValueSet.equals("")) {
                addressValueSet.add(tempValueSet);
            }
        }
        return "false";
    }

     
    /**
     * Checks if is correctly formatted url.
     *
     * @param url the url
     * @return true, if is correctly formatted url
     */
    public static boolean isCorrectlyFormattedURL(String url) {
        boolean matched = false;
        if (url != null) {
            for (int i = 0; i < urlFormats.length && !matched; i++) {
                if (url.matches(urlFormats[i])) {
                    matched = true;
                }
                logger.debug("URL=" + url + " matches regex=" + urlFormats[i] + " = " + matched);
            }
        }
        return matched;
    }

     
    /**
     * Check request params.
     *
     * @param rules the rules
     * @return true, if successful
     */
    public static boolean checkRequestParams(ValidationRule[] rules) {
        boolean valid = true;

        if (rules != null && rules.length > 0) {

            // Pass 1 - check mandatory parameters
            String missingList = null;
            for (int i = 0; i < rules.length; i++) {
                ValidationRule current = rules[i];
                if (ValidationRule.isMandatory(current.validationType)) {
                    boolean missing = false;
                    if (current.parameterValue == null) {
                        missing = true;
                        logger.debug("Parameter " + current.parameterName + " is missing");
                    } else {
                        Object parameterValue = current.parameterValue;
                        if (parameterValue instanceof String) {
                            if (((String) current.parameterValue).trim().length() == 0) {
                                missing = true;
                                logger.debug("Parameter " + current.parameterName + " is missing");
                            }
                        } else if (parameterValue instanceof Double) {
                            // This is ok for the moment
                        } else if (parameterValue instanceof String[]) {
                            String[] sa = (String[]) parameterValue;
                            boolean empty = true;
                            if (sa != null && sa.length > 0) {
                                // See if there is at least one non null string present
                                for (int j = 0; j < sa.length && empty; j++) {
                                    if (sa[j] != null && sa[j].trim().length() > 0) {
                                        empty = false;
                                    }
                                }
                            }
                            if (empty) {
                                missing = true;
                                logger.debug("Parameter " + current.parameterName + " is missing");
                            }
                        } else {
                            logger.warn("Not sure how to validate parameter " + current.parameterName + " type=" + current.parameterValue.getClass().getName());
                        }
                    }
                    if (missing) {
                        if (missingList == null) {
                            missingList = current.parameterName;
                        } else {
                            missingList += "," + current.parameterName;
                        }
                        valid = false;
                    }
                }
            }

            if (!valid) {
                //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Missing mandatory parameter: " + missingList);
                throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Missing mandatory parameter: " + missingList});
            } else {
                logger.debug("Starting second pass");
                // Pass 2 - other validations - stop on the first error 
                for (int i = 0; i < rules.length && valid; i++) {
                    ValidationRule current = rules[i];
                    Object parameterValue = current.parameterValue;
                    switch (current.validationType) {
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_MESSAGE:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv});
                                }
                            } else if (parameterValue != null) {

                                String parameterValueString = parameterValue.toString();
                                int msgChaCount = parameterValueString.length();
                                 
                                 
                                if (msgChaCount > 800) {

                                    valid = false;
                                    logger.debug("Message too long. " + msgChaCount);
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0280", "Message too long. Maximum length is %1 characters", "800");
                                    throw new CustomException("SVC0280", "Message too long. Maximum length is %1 characters", new String[]{"800"});
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv});
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID:
                            // Mandatory will already have been enforced - can just do the validation
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!isCorrectlyFormattedNumber((String) parameterValue)) {
                                        //logger.debug("Rejecting phone number " + current.parameterName + " : " + (String) parameterValue);
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", ((String) current.parameterValue));
                                        valid = false;
                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + (String) parameterValue);
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0271", "endUserId format invalid. Expected format is %1", "tel:+94771211212");
                                        throw new CustomException("SVC0004", "endUserId format invalid. %1", new String[]{(String) parameterValue});
                                    }
                                } else if (parameterValue instanceof String[]) {
                                    String[] sa = (String[]) parameterValue;
                                    if (sa != null && sa.length > 0) {
                                        // See if there is at least one non null string present
                                        for (int j = 0; j < sa.length && valid; j++) {
                                            if (sa[j] != null && sa[j].trim().length() > 0) {
                                                if (!isCorrectlyFormattedNumber(sa[j])) {
                                                    //logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", sa[j]);
                                                    valid = false;
                                                    logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0271", "endUserId format invalid. Expected format is %1", "tel:+94771211212");
                                                    throw new CustomException("SVC0004", "endUserId format invalid. %1", new String[]{sa[j]});
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv});
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO:
                            if (current.parameterValue instanceof Double) {
                                if (((Double) current.parameterValue) <= 0.0) {
                                    valid = false;
                                    //logger.debug("Rejecting double value " + current.parameterName + " : " + ((Double) parameterValue) + " should be > 0");
                                     
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " value " + ((Double) current.parameterValue));
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", "");
                                    throw new CustomException("SVC0007", "Invalid charging information", new String[]{""});
                                }  
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO:
                            if (current.parameterValue != null) {
                                try {
                                   Double parameter= Double.parseDouble(current.parameterValue.toString());
                                   if(!(parameter >= 0.0)){
                                  throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName});

                                   }

                                } catch (NumberFormatException e) {
                                    logger.debug("Rejecting int value " + current.parameterName);
                                    throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName});

                            }
	                                
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_TEL:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_TEL:
                            // Mandatory will already have been enforced - can just do the validation
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!isCorrectlyFormattedNumber((String) parameterValue)) {
                                        valid = false;
                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + (String) parameterValue);
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", ((String) current.parameterValue));
                                        throw new CustomException("SVC0004", "No valid addresses provided in message part %1", new String[]{((String) current.parameterValue)});
                                    }
                                } else if (parameterValue instanceof String[]) {
                                    String[] sa = (String[]) parameterValue;
                                    if (sa != null && sa.length > 0) {
                                    	
                                        String duplicatedAddress = checkDuplicatedAddress(sa);
                                        
                                        PropertyUtils propertyUtils = new PropertyUtils();
                                        if (sa.length > propertyUtils.getSMSBatchSize()) {
                                            valid = false;
                                            logger.debug("Num of addresses in array" + sa.length);
                                            //sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0003", "Too many addresses specified in message part %1", current.parameterName);
                                            throw new CustomException("POL0003", "Too many addresses specified in message part %1", new String[]{(String) current.parameterValue});
                                        } else if (duplicatedAddress != "false") {

                                            valid = false;
                                            logger.debug("Duplicated addresses in array" + duplicatedAddress);
                                            //sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0013", "Duplicated addresses", duplicatedAddress);
                                            throw new CustomException("POL0013", "Duplicated addresses", new String[]{duplicatedAddress});
                                        } else {
                                            // See if there is at least one non null string present
                                            for (int j = 0; j < sa.length && valid; j++) {
                                                if (sa[j] != null && sa[j].trim().length() > 0) {
                                                    if (!isCorrectlyFormattedNumber(sa[j])) {
                                                        
                                                        valid = false;
                                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", sa[j]);
                                                        throw new CustomException("SVC0004", "No valid addresses provided in message part %1", new String[]{sa[j]});
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_URL:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_URL:
                            // Mandatory will already have been enforced - can just do the validation
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!isCorrectlyFormattedURL((String) parameterValue)) {
                                        valid = false;
                                        logger.debug("Bad URL " + current.parameterName + " : " + (String) parameterValue);
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected URL " + ((String) parameterValue));
                                        throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected URL " + ((String) parameterValue)});
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO:
                            if (parameterValue != null) {
                                if (parameterValue instanceof Integer) {
                                    if (((Integer) parameterValue).intValue() < 0) {
                                        logger.debug("Rejecting int value " + current.parameterName + " : " + ((Integer) parameterValue) + " should be >= 0");
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " less than zero: " + ((String) parameterValue));
                                        throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " less than zero: " + ((String) parameterValue)});
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GT_ONE:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GT_ONE:
                            if (parameterValue != null) {
                                if (parameterValue instanceof Integer) {
                                    if (((Integer) parameterValue).intValue() <= 1) {
                                        logger.debug("Rejecting int value " + current.parameterName + " : " + ((Integer) parameterValue) + " should be > 1");
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " must be greater than 1: " + ((String) parameterValue));
                                        throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " must be greater than 1: " + ((String) parameterValue)});
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_JSON:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_JSON:
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!((String) parameterValue).equalsIgnoreCase("JSON")) {
                                        logger.debug("Rejecting parameter " + current.parameterName + " : " + ((String) parameterValue) + " should be 'JSON'");
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected 'JSON': " + ((String) parameterValue));
                                        throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected 'JSON': " + ((String) parameterValue)});
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_PAYMENT_CHANNEL:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL:
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!(((String) parameterValue).equalsIgnoreCase("WAP") || ((String) parameterValue).equalsIgnoreCase("WEB") || ((String) parameterValue).equalsIgnoreCase("SMS"))) {
                                        logger.debug("Rejecting parameter " + current.parameterName + " : " + ((String) parameterValue) + " should be 'Web', 'Wap' or 'SMS'");
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected 'Wap', 'Web' or 'SMS': " + ((String) parameterValue));
                                        throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected 'Wap', 'Web' or 'SMS': " + ((String) parameterValue)});
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_NUMBER:
                            if (parameterValue != null) {
                                try {
                                    Double.parseDouble(parameterValue.toString());
                                } catch (NumberFormatException e) {
                                    logger.debug("Rejecting int value " + current.parameterName);
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName);
                                    throw new CustomException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName });
                                }
                            }
                            break;
                    }

                }
            }

        }

        logger.debug("Parameters are valid?" + valid);
        return valid;
    }
}
