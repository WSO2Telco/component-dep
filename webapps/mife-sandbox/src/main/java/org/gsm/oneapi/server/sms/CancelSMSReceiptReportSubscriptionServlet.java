package org.gsm.oneapi.server.sms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
//import static org.dialog.mifesandbox.sms.SMSMethods.cancelSMSSubscription;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;

/**
 * Servlet implementing the OneAPI function for cancelling SMS message receipt
 * subscriptions
 */
public class CancelSMSReceiptReportSubscriptionServlet extends OneAPIServlet {

    private static final long serialVersionUID = 4877311182916937332L;
    static Logger logger = Logger.getLogger(CancelSMSReceiptReportSubscriptionServlet.class);
    public SmsImpl smsimpl;

    public void init() throws ServletException {
        logger.debug("CancelSMSReceiptReportSubscriptionServlet initialised");
    }
    private final String[] validationRules = {"smsmessaging", "1", "inbound", "subscriptions", "*"};

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

        String[] requestParts = getRequestParts(request);

        smsimpl = new SmsImpl();

        //Get user profile 
        //Implementation part of local DB :START
        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = smsimpl.getUser(sandboxusr);
        Smsparam smsparam = smsimpl.querySmsParam(sandboxusr);

        if (validateRequest(request, response, requestParts, validationRules)) {

            /*
             * Decode the service parameters - in this case it is an HTTP GET request 
             */
            String subscriptionId = requestParts[4];


            logger.debug("subscriptionId = " + subscriptionId);
            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "subscriptionId", subscriptionId),};

            if (checkRequestParameters(response, rules)) {

                //int user_profile_id = Integer.parseInt(getProfileIdFromRequest(request));
                //cancelSMSSubscription(user_profile_id);
                smsimpl.saveTransaction(subscriptionId,null,null, null,null, null, null, 0,"success",4,null,null, user, null); 
                if (!smsimpl.deleteSubscription(user.getId(),subscriptionId)) {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL0001", "A service error occurred. Error code is %1", subscriptionId +" SMS Receipt Subscription Not Found");
                } else {
                    response.setStatus(NOCONTENT);
                }
            }
        }
    }
}
