package com.wso2telco.dep.oneapivalidation.util;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import static com.wso2telco.dep.oneapivalidation.util.Validation.getDigitsGroup;
import static com.wso2telco.dep.oneapivalidation.util.Validation.getPrefixGroup;
import static com.wso2telco.dep.oneapivalidation.util.Validation.getValidationRegex;

public class MsisdnFormatMediator extends AbstractMediator {

    static final String MESSAGE_ID = "messageId";
    static final String ERROR_TEXT = "errorText";
    static final String ERROR_VARIABLE = "errorVariable";
    static final String HTTP_STATUS_CODE = "httpStatusCode";
    static final String EXCEPTION_TYPE = "exceptionType";
    static final String VALIDATION_REGEX = "validationRegex";
    static final String VALIDATION_PREFIX_GROUP = "validationPrefixGroup";
    static final String VALIDATION_DIGITS_GROUP = "validationDigitsGroup";


    @Override
    public boolean mediate(MessageContext mc) {

        String validationRegex = getValidationRegex();
        int prefixGroup = getPrefixGroup();
        int digitsGroup = getDigitsGroup();

        try {

            mc.setProperty(VALIDATION_REGEX, validationRegex);
            mc.setProperty(VALIDATION_PREFIX_GROUP, prefixGroup);
            mc.setProperty(VALIDATION_DIGITS_GROUP, digitsGroup);

        } catch (Exception e) {
            log.error("Error Returning Validation Regex", e);
            setErrorInContext(mc,"SVC0001",e.getMessage(),"","400","SERVICE_EXCEPTION");
            mc.setProperty("INTERNAL_ERROR","true");
        }

        return true;
    }

    private void setErrorInContext(MessageContext synContext, String messageId,
                                   String errorText, String errorVariable, String httpStatusCode,
                                   String exceptionType) {

        synContext.setProperty(MESSAGE_ID, messageId);
        synContext.setProperty(ERROR_TEXT, errorText);
        synContext.setProperty(ERROR_VARIABLE, errorVariable);
        synContext.setProperty(HTTP_STATUS_CODE, httpStatusCode);
        synContext.setProperty(EXCEPTION_TYPE, exceptionType);
    }

}
