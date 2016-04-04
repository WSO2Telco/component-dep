/*
 * Validation.java
 * Nov 22, 2013  5:09:27 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.oneapivalidation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * <TO-DO>
 * <code>Validation</code>
 *
 * @version $Id: Validation.java,v 1.00.000
 */
public class Validation {

    private final static Logger logger = Logger.getLogger(Validation.class.getName());
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
    public static final int MAXADDRLIMIT = 50;
    public static final String MEDIATYPE = "SMS";
    public static final String VALIDCURR = "LKR";
    public static final String STATUSREFUND = "Refunded";
    
    public static final int MAXDESCLIMIT = 50;
    public static final double CHARGELIMIT = 50;
    

    /**
     * Ensure the input value is either a null value or a trimmed string
     */
    public static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }

    /**
     * Convert to an integer value
     */
    public static int parseInt(String s) {
        int rv = 0;
        if (s != null && s.trim().length() > 0) {
            if (s.indexOf(",") == -1) {
                try {
                    rv = Integer.parseInt(s.trim());
                } catch (NumberFormatException nfe) {
                }
            } else {
                String[] p = s.trim().split("\\,");
                try {
                    rv = Integer.parseInt(p[0].trim());
                } catch (NumberFormatException nfe) {
                }
            }
        }
        return rv;
    }

    /**
     * Convert to a long value
     */
    public static long parseLong(String s) {
        long rv = 0;
        if (s != null && s.trim().length() > 0) {
            if (s.indexOf(",") == -1) {
                try {
                    rv = Long.parseLong(s.trim());
                } catch (NumberFormatException nfe) {
                }
            } else {
                String[] p = s.trim().split("\\,");
                try {
                    rv = Long.parseLong(p[0].trim());
                } catch (NumberFormatException nfe) {
                }
            }
        }
        return rv;
    }

    /**
     * Convert to a boolean value
     */
    public static boolean isTrue(String s) {
        boolean rv = false;
        if (s != null && (s.trim().equalsIgnoreCase("true") || s.trim().equals("1") || s.trim().equalsIgnoreCase("yes"))) {
            rv = true;
        }
        return rv;
    }

    /**
     * Alternate function name to convert to a boolean value
     */
    public static boolean parseBoolean(String s) {
        return isTrue(s);
    }
    private static Pattern nf1 = Pattern.compile("[\\-\\+]?[0-9]*\\.?[0-9]+");
    private static Pattern nf2 = Pattern.compile("[\\-\\+]?\\.?[0-9]+");

    /**
     * Convert to a double value
     */
    public static double parseDouble(String s) {
        double rv = 0;
        if (s != null && s.trim().length() > 0) {
            try {
                rv = Double.parseDouble(s.trim());
            } catch (NumberFormatException nfe) {
                boolean parsed = false;
                String trimmed = s.trim();
                Matcher m1 = nf1.matcher(trimmed);
                if (m1.find()) {
                    try {
                        String pv = m1.group();
                        rv = Double.parseDouble(pv);
                        parsed = true;
                    } catch (NumberFormatException nfe1) {
                    }
                }
                if (!parsed) {
                    Matcher m2 = nf2.matcher(trimmed);
                    if (m2.find()) {
                        try {
                            String pv = m1.group();
                            rv = Double.parseDouble(pv);
                            parsed = true;
                        } catch (NumberFormatException nfe2) {
                        }
                    }
                }
            }
        }
        return rv;
    }

    /**
     * Do basic URL encoding based on UTF-8
     */
    public static String urlEncode(String s) {
        String rv = s;
        if (s != null) {
            try {
                rv = URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
            }
        } else {
            rv = "";
        }
        return rv;
    }

    /**
     * Utility function to create a java.util.Date object from constituent date/
     * time fields (UTC clock)
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     */
    public java.util.Date makeUTCDateTime(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    private static final String[] telFormats = {
        "tel\\:\\+[0-9]+", "tel\\:[0-9]+"
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
    private static final String[] urlFormats = {
        "http\\:\\/\\/.+", "https\\:\\/\\/.+"
    };

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

    public static boolean isAddressLimit(String msgpart, String[] addresses) {
        boolean valid = true;
        if (addresses.length > MAXADDRLIMIT) {
            logger.debug("POL0003: Too many addresses specified in message part " + msgpart);
            valid = false;
        }
        return valid;
    }

    public static boolean isUnlimitedNotification(String[] addresses) {
        boolean valid = true;
        //<TO-DO> to be implemented in future
//        if (addresses.length > MAXADDRLIMIT) {
//           logger.debug("POL0003: Too many addresses specified in message part "+ msgpart);
//           valid = false;
//        }
        return valid;
    }

    public static boolean isAddressLimit(String[] noficiations) {
        boolean valid = true;
        //<TO-DO> to be implemented in future
//        if (addresses.length > MAXADDRLIMIT) {
//           logger.debug("POL0003: Too many addresses specified in message part "+ msgpart);
//           valid = false;
//        }
        return valid;
    }

    public static boolean isUnlimitedGroup(String[] addresses) {
        boolean valid = true;
        //<TO-DO> to be implemented in future
//        if (addresses.length > MAXADDRLIMIT) {
//           logger.debug("POL0003: Too many addresses specified in message part "+ msgpart);
//           valid = false;
//        }
        return valid;
    }

    public static boolean isGroupLimit(String[] noficiations) {
        boolean valid = true;
        //<TO-DO> to be implemented in future
//        if (addresses.length > MAXADDRLIMIT) {
//           logger.debug("POL0003: Too many addresses specified in message part "+ msgpart);
//           valid = false;
//        }
        return valid;
    }

    public static boolean isMediaType(String mediatype) {
        boolean valid = true;
        if (!mediatype.equalsIgnoreCase(MEDIATYPE)) {
            logger.debug("POL0011: Media type not supported " + mediatype);
            valid = false;
        }
        return valid;
    }

    public static boolean isDescriptionLimit(String msgpart, String[] descriptions) {
        boolean valid = true;
        if (descriptions.length > MAXDESCLIMIT) {
            logger.debug("POL0012: Too many description entries specified in message part " + msgpart);
            valid = false;
        }
        return valid;
    }

    public static boolean isDuplicateAddress(String msgpart, String[] addresses) {
        boolean valid = true;

        List duplicate = new ArrayList();
        Set<String> uniquUsers = new HashSet<String>();

        for (int i = 0; i < addresses.length; i++) {
            if (!uniquUsers.add(addresses[i])) {
                duplicate.add(addresses[i]);
            }
        }
        if (duplicate.size() > 0) {
            logger.debug("POL0013: Duplicated addresses " + duplicate.toString());
            valid = false;
        }
        return valid;
    }

    public static boolean isChargeLimit(String msgpart) {
        boolean valid = true;
        try {
            double payamount = Double.parseDouble(msgpart);
            if (payamount > CHARGELIMIT) {
                logger.debug("POL0254: The amount exceeds the operator limit for a single charge" + msgpart);
                valid = false;
            }
            return valid;
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    public static boolean isAddressesFormatValid(String msgpart, String[] addresses) {
        boolean valid = true;
        List invlidformat = new ArrayList();
        for (int i = 0; i < addresses.length; i++) {
            if (!isCorrectlyFormattedNumber(addresses[i])) {
                invlidformat.add(addresses[i]);
            }
        }
        if (invlidformat.size() > 0) {
            logger.debug("POL0255: Address format invalid. Expected format is " + invlidformat.toString());
            valid = false;
        }
        return valid;
    }
    
     public static boolean isCurrecyValid(String curr) {
        boolean valid = true;
        if (!curr.equalsIgnoreCase(VALIDCURR)) {
            logger.debug("POL0256: Invalid currency specified " + curr);
            valid = false;
        }
        return valid;
    }
    
    public static boolean isValidTransactionCharge(String txnOperationStatus ) {
        boolean valid = true;
        
        if (txnOperationStatus.equalsIgnoreCase(STATUSREFUND)) {
            logger.debug("POL1007: Refunds not supported ");
            valid = false;
        }
        return valid;
    }
    
    
   public static boolean checkRequestParameters(ValidationRule[] rules) throws AxiataException {
		boolean valid=true;
		
		if (rules!=null && rules.length>0) {

			// Pass 1 - check mandatory parameters
			String missingList=null;
			for (int i=0; i<rules.length; i++) {
				ValidationRule current=rules[i];
				if (ValidationRule.isMandatory(current.validationType)) {
					boolean missing=false;
					if (current.parameterValue==null) {
						missing=true;
						logger.debug("Parameter "+current.parameterName+" is missing");
					} else {
						Object parameterValue=current.parameterValue;
						if (parameterValue instanceof String) {
							if (((String)current.parameterValue).trim().length()==0) {
								missing=true;
								logger.debug("Parameter "+current.parameterName+" is missing");
							}
						} else if (parameterValue instanceof Double) {
							// This is ok for the moment
						} else if (parameterValue instanceof String[]) {
							String[] sa=(String[]) parameterValue;
							boolean empty=true;
							if (sa!=null && sa.length>0) {
								// See if there is at least one non null string present
								for (int j=0; j<sa.length && empty; j++) {
									if (sa[j]!=null && sa[j].trim().length()>0) empty=false;
								}
							} 
							if (empty) {
								missing=true;
								logger.debug("Parameter "+current.parameterName+" is missing");
							}
						} else {
							logger.warn("Not sure how to validate parameter "+current.parameterName+" type="+current.parameterValue.getClass().getName());
						}
					}
					if (missing) {
						if (missingList==null) {
							missingList=current.parameterName;
						} else {
							missingList+=","+current.parameterName;
						}
						valid=false;
					}
				}
			}
			
			if (!valid) {
                                throw new AxiataException("SVC0002", new String[] {"Missing mandatory parameter: "+missingList});
				//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Missing mandatory parameter: "+missingList);
			} else {
				logger.debug("Starting second pass");
				// Pass 2 - other validations - stop on the first error 
				for (int i=0; i<rules.length && valid; i++) {
					ValidationRule current=rules[i];
					Object parameterValue=current.parameterValue;
					switch (current.validationType) {
					case ValidationRule.VALIDATION_TYPE_MANDATORY:
						if (current.specificValue!=null && parameterValue instanceof String) {
							String pv=(String) parameterValue;
							if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                                                throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" expected "+current.specificValue+" received "+pv});
								//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" expected "+current.specificValue+" received "+pv);
								//logger.debug("Parameter "+current.parameterName+" does not match expected value "+current.specificValue);
							}
						}
						break;
						
					case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO:
					case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO:
						if (current.parameterValue instanceof Double) {
							if (((Double) current.parameterValue)<=0.0) {
								valid=false;
								logger.debug("Rejecting double value "+current.parameterName+" : "+((Double) parameterValue)+" should be > 0");
                                                                throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" value "+((Double) current.parameterValue)});
							
								//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" value "+((Double) current.parameterValue));
							}
						}
						break;
					case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO:
					case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO:
						if (current.parameterValue instanceof Double) {
							if (((Double) current.parameterValue)<0.0) {
								valid=false;
								logger.debug("Rejecting double value "+current.parameterName+" : "+((Double) parameterValue)+" should be >= 0");
                                                                throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" value "+((Double) current.parameterValue)});    
								//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" value "+((Double) current.parameterValue));
							}
						}
						break;
					case ValidationRule.VALIDATION_TYPE_MANDATORY_TEL: 
					case ValidationRule.VALIDATION_TYPE_OPTIONAL_TEL:
						// Mandatory will already have been enforced - can just do the validation
						if (parameterValue!=null) {
							if (parameterValue instanceof String) {
								if (!isCorrectlyFormattedNumber((String) parameterValue)) {
									logger.debug("Rejecting phone number "+current.parameterName+" : "+(String) parameterValue);
                                                                        valid=false;
                                                                        throw new AxiataException("SVC0004",new String[] {((String) current.parameterValue)});
									//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", ((String) current.parameterValue));
									
								}
							} else if (parameterValue instanceof String[]) {
								String[] sa=(String[]) parameterValue;
								if (sa!=null && sa.length>0) {
									// See if there is at least one non null string present
									for (int j=0; j<sa.length && valid; j++) {
										if (sa[j]!=null && sa[j].trim().length()>0) {
											if (!isCorrectlyFormattedNumber(sa[j])) {
												logger.debug("Rejecting phone number "+current.parameterName+" : "+sa[j]);
												//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", sa[j]);
												valid=false;
                                                                                                throw new AxiataException("SVC0004",new String[] {sa[j]});
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
						if (parameterValue!=null) {
							if (parameterValue instanceof String) {
								if (!isCorrectlyFormattedURL((String) parameterValue)) {
									logger.debug("Bad URL "+current.parameterName+" : "+(String) parameterValue);
									//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" expected URL "+((String) parameterValue));
									valid=false;
                                                                        throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" expected URL "+((String) parameterValue)});
								}
							}
						}
						break;
					case ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO: 
					case ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO:
						if (parameterValue!=null) {
							if (parameterValue instanceof Integer) {
								if (((Integer) parameterValue).intValue()<0) {
									logger.debug("Rejecting int value "+current.parameterName+" : "+((Integer) parameterValue)+" should be >= 0");
									//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" less than zero: "+((String) parameterValue));
                                                                        throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" less than zero: "+((String) parameterValue)});
								}
							}
						}
						break;
					case ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GT_ONE: 
					case ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GT_ONE:
						if (parameterValue!=null) {
							if (parameterValue instanceof Integer) {
								if (((Integer) parameterValue).intValue()<=1) {
									logger.debug("Rejecting int value "+current.parameterName+" : "+((Integer) parameterValue)+" should be > 1");
									//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" must be greater than 1: "+((String) parameterValue));
                                                                        throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" must be greater than 1: "+((String) parameterValue)});
								}
							}
						}
						break;
					case ValidationRule.VALIDATION_TYPE_MANDATORY_JSON: 
					case ValidationRule.VALIDATION_TYPE_OPTIONAL_JSON:
						if (parameterValue!=null) {
							if (parameterValue instanceof String) {
								if (!((String) parameterValue).equalsIgnoreCase("JSON")) {
									logger.debug("Rejecting parameter "+current.parameterName+" : "+((String) parameterValue)+" should be 'JSON'");
									//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" expected 'JSON': "+((String) parameterValue));
                                                                        throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" expected 'JSON': "+((String) parameterValue)});
								}
							}
						}
						break;
					case ValidationRule.VALIDATION_TYPE_MANDATORY_PAYMENT_CHANNEL: 
					case ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL:
						if (parameterValue!=null) {
							if (parameterValue instanceof String) {
								if (!(((String) parameterValue).equalsIgnoreCase("WAP") || ((String) parameterValue).equalsIgnoreCase("WEB") || ((String) parameterValue).equalsIgnoreCase("SMS"))) {
									logger.debug("Rejecting parameter "+current.parameterName+" : "+((String) parameterValue)+" should be 'Web', 'Wap' or 'SMS'");
									//sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter "+current.parameterName+" expected 'Wap', 'Web' or 'SMS': "+((String) parameterValue));
                                                                        throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" expected 'Wap', 'Web' or 'SMS': "+((String) parameterValue)});
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
                                    throw new AxiataException("SVC0002",new String[] {"Parameter "+current.parameterName+" expected : "+((String) parameterValue)});
                                }
                            }
                            break;

                    }
					
				}
			}
			
		}
		
		logger.debug("Parameters are valid?"+valid);
		return valid;		
	}    
    
}
