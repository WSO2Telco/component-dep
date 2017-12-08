package com.wso2telco.dep.verificationhandler.verifier;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatMsisdn {

    private static final Log log = LogFactory.getLog(FormatMsisdn.class);

    ValidationRegexDTO validationRegexDTO = null;

    private FormatMsisdn() throws BusinessException {
        // service call to get msisdn regex
        try {
            validationRegexDTO = ValidationRegexClient.getValidationRegex();
        } catch (BusinessException e) {
            log.error("Error in retrieving validation Regex", e);
        }
    }

    private static FormatMsisdn instance;

    public static synchronized FormatMsisdn getInstance() throws BusinessException {
        if(instance==null){
            instance = new FormatMsisdn();
        }
        return instance;
    }

    /**
     * Extract msisdn
     * @param msisdn
     * @return
     */
    public String splitMsisdn(String msisdn) throws BusinessException{

        if( msisdn == null || msisdn.trim().length() == 0) {
            log.warn("Msisdn is empty");
            throw new BusinessException(ServiceError.INVALID_INPUT_VALUE);
        }

        String msisdnRegex = validationRegexDTO.getValidationRegex();

        Pattern pattern = Pattern.compile(msisdnRegex);
        Matcher matcher = pattern.matcher(msisdn);

        if (matcher.matches()) {
            // extract msisdn without the prefix
            msisdn = matcher.group(Integer.parseInt(validationRegexDTO.getDigitsGroup()));
            return msisdn;
        }

        return msisdn;
    }

}
