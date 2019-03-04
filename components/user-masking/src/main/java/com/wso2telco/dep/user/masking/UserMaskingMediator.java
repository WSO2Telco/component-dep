package com.wso2telco.dep.user.masking;

import com.wso2telco.dep.user.masking.utils.APIType;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class UserMaskingMediator extends AbstractMediator {

    @Override
    public boolean mediate(MessageContext messageContext) {
        messageContext.setProperty("ENABLE_WSO2AM_EXT_IN_PATH", "true");
        if (MaskingUtils.isUserAnonymizationEnabled() && APIType.PAYMENT.equals(MaskingUtils.getAPIType(messageContext))) {
            messageContext.setProperty("ENABLE_WSO2AM_EXT_IN_PATH", "false");
        }
        return true;
    }
}
