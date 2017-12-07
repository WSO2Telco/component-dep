package com.wso2telco.dep.oneapivalidation.util;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import static com.wso2telco.dep.oneapivalidation.util.Validation.getDigitsGroup;
import static com.wso2telco.dep.oneapivalidation.util.Validation.getPrefixGroup;
import static com.wso2telco.dep.oneapivalidation.util.Validation.getValidationRegex;

public class RegexMediator extends AbstractMediator {

    @Override
    public boolean mediate(MessageContext mc) {

        String validationRegex = getValidationRegex();
        int prefixGroup = getPrefixGroup();
        int digitsGroup = getDigitsGroup();

        try {

            mc.setProperty("validationRegex", validationRegex);
            mc.setProperty("validationPrefixGroup",prefixGroup);
            mc.setProperty("validationDigitsGroup",digitsGroup);

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

        synContext.setProperty("messageId", messageId);
        synContext.setProperty("errorText", errorText);
        synContext.setProperty("errorVariable", errorVariable);
        synContext.setProperty("httpStatusCode", httpStatusCode);
        synContext.setProperty("exceptionType", exceptionType);
    }

}
