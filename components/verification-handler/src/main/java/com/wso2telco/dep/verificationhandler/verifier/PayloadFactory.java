package com.wso2telco.dep.verificationhandler.verifier;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;

public class PayloadFactory {

    private static PayloadFactory instance;

    public static synchronized PayloadFactory getInstance() {
        if (instance == null) {
            instance = new PayloadFactory();
        }
        return instance;
    }

    /**
     * Payload for Error message
     * @param statusCode
     * @param message
     * @param description
     * @return
     */
    public OMElement getErrorPayload(int statusCode, String message, String description) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(APISecurityConstants.API_SECURITY_NS,
                APISecurityConstants.API_SECURITY_NS_PREFIX);
        OMElement payload = fac.createOMElement("fault", ns);

        OMElement errorCode = fac.createOMElement("code", ns);
        errorCode.setText(String.valueOf(statusCode));
        OMElement errorMessage = fac.createOMElement("message", ns);
        errorMessage.setText(message);
        OMElement errorDetail = fac.createOMElement("description", ns);
        errorDetail.setText(description);

        payload.addChild(errorCode);
        payload.addChild(errorMessage);
        payload.addChild(errorDetail);
        return payload;
    }

}
