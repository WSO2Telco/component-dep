/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2telco.oneapivalidation;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class ValidationNew {

    static Logger logger = Logger.getLogger(ValidationNew.class);
    public static final long serialVersionUID = -8195763247832284073L;
    public static final int BAD_REQUEST = 400;
    public static final int AUTHENTICATION_FAILURE = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_SUPPORTED = 405;
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NONAUTHORITATIVE = 203;
    public static final int NOCONTENT = 204;
    public static boolean dumpRequestAndResponse = false;

    /*private static final String[] telFormats = {
        "tel\\:\\+[0-9]+", "tel\\:[0-9]+"
    };*/
    
    private static final String[] telFormats = {
        "tel\\:\\+[a-zA-Z0-9]+", "tel\\:[a-zA-Z0-9]+" 
    };

    private static final String[] urlFormats = {
        "http\\:\\/\\/.+", "https\\:\\/\\/.+"
    };

    /**
     * Check on valid telephone number formats. Extend the regular expression
     * rules in telFormats if needed.
     */
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

    /*
     ****** This method is used to identify duplicate values in address array
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
     * Check on valid URL formats. Extend the regular expression rules in
     * urlFormats if needed.
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
     * This function implements the specific parameter validation rules applying
     * to each OneAPI servlet (specifically request parameters).
     *
     * @param response
     * @param rules
     * @return true if all parameter validation rules have been passed
     * @see ValidationRule
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
                throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Missing mandatory parameter: " + missingList});
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
                                    throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv});
                                }
                            } else if (parameterValue != null) {

                                String parameterValueString = parameterValue.toString();
                                int msgChaCount = parameterValueString.length();
                                /*previously this value set as 150. but we change that to the 160 because of a request made by David Rempe in 2015-08-28*/
                                /*we change this again to 800 in 2015-09-01*/
                                if (msgChaCount > 800) {

                                    valid = false;
                                    logger.debug("Message too long. " + msgChaCount);
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0280", "Message too long. Maximum length is %1 characters", "150");
                                    throw new AxiataException("SVC0280", "Message too long. Maximum length is %1 characters", new String[]{"800"});
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv});
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
                                        throw new AxiataException("SVC0004", "endUserId format invalid. %1", new String[]{(String) parameterValue});
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
                                                    throw new AxiataException("SVC0004", "endUserId format invalid. %1", new String[]{sa[j]});
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
                                    throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv});
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO:
                            if (current.parameterValue instanceof Double) {
                                if (((Double) current.parameterValue) <= 0.0) {
                                    valid = false;
                                    //logger.debug("Rejecting double value " + current.parameterName + " : " + ((Double) parameterValue) + " should be > 0");
                                    /*
                                     ***** Comment this and use the new exception message because charge amount need exception type SVC0007
                                     */
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " value " + ((Double) current.parameterValue));
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", "");
                                    throw new AxiataException("SVC0007", "Invalid charging information", new String[]{""});
                                } /* else if (((Double) current.parameterValue) > 2500) {

                                    valid = false;
                                    logger.debug("Invalid charge amount " + current.parameterName + " : " + ((Double) parameterValue) + " should be <= 2500");
                                    //sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0254", "The amount exceeds the operator limit for a single charge", "");
                                    throw new AxiataException("POL0254", "The amount exceeds the operator limit for a single charge", new String[]{""});
                                } */
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO:
                            if (current.parameterValue instanceof Double) {
                                if (((Double) current.parameterValue) < 0.0) {
                                    valid = false;
                                    logger.debug("Rejecting double value " + current.parameterName + " : " + ((Double) parameterValue) + " should be >= 0");
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " value " + ((Double) current.parameterValue));
                                    throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " value " + ((Double) current.parameterValue)});
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
                                        throw new AxiataException("SVC0004", "No valid addresses provided in message part %1", new String[]{((String) current.parameterValue)});
                                    }
                                } else if (parameterValue instanceof String[]) {
                                    String[] sa = (String[]) parameterValue;
                                    if (sa != null && sa.length > 0) {
                                        String duplicatedAddress = checkDuplicatedAddress(sa);
                                        ReadPropertyFile property = new ReadPropertyFile();
                                        if (sa.length > property.getSMSBatchSize()) {
                                            valid = false;
                                            logger.debug("Num of addresses in array" + sa.length);
                                            //sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0003", "Too many addresses specified in message part %1", current.parameterName);
                                            throw new AxiataException("POL0003", "Too many addresses specified in message part %1", new String[]{(String) current.parameterValue});
                                        } else if (duplicatedAddress != "false") {

                                            valid = false;
                                            logger.debug("Duplicated addresses in array" + duplicatedAddress);
                                            //sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0013", "Duplicated addresses", duplicatedAddress);
                                            throw new AxiataException("POL0013", "Duplicated addresses", new String[]{duplicatedAddress});
                                        } else {
                                            // See if there is at least one non null string present
                                            for (int j = 0; j < sa.length && valid; j++) {
                                                if (sa[j] != null && sa[j].trim().length() > 0) {
                                                    if (!isCorrectlyFormattedNumber(sa[j])) {
                                                        
                                                        valid = false;
                                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", sa[j]);
                                                        throw new AxiataException("SVC0004", "No valid addresses provided in message part %1", new String[]{sa[j]});
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
                                        throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected URL " + ((String) parameterValue)});
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
                                        throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " less than zero: " + ((String) parameterValue)});
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
                                        throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " must be greater than 1: " + ((String) parameterValue)});
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
                                        throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected 'JSON': " + ((String) parameterValue)});
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
                                        throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName + " expected 'Wap', 'Web' or 'SMS': " + ((String) parameterValue)});
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
                                    throw new AxiataException("SVC0002", "Invalid input value for message part %1", new String[]{"Parameter " + current.parameterName });
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
