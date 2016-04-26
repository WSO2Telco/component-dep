/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gsm.oneapi.server.ussd;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.UssdCtrlFunctions;
import mife.sandbox.model.UssdImpl;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.functions.UssdFunctions;
import org.apache.log4j.Logger;
import org.dialog.mife.server.SessionManager;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;

/**
 *
 * @author User
 */
public class CancelUssdSubscriptionServlet extends OneAPIServlet {

    static Logger logger = Logger.getLogger(CancelUssdSubscriptionServlet.class);
    private static final long serialVersionUID = -6237772242372106922L;
    private final String[] validationRules = {"ussd", "*", "inbound", "subscriptions", "*"};
    SessionManager sessionMgr = new SessionManager();
    public UssdImpl ussdImpl;

    @Override
    public void init() throws ServletException {
        logger.debug("CancelUssdSubscriptionServlet initialised");
        sessionMgr.start();
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        dumpRequestDetails(request, logger);

        String[] requestParts = getRequestParts(request);

        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }
        user = ussdImpl.getUser(sandboxusr);

        if (validateRequest(request, response, requestParts, validationRules)) {
            String subscriptionId = requestParts[5];
            logger.debug("subscriptionId = " + subscriptionId);

            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "subscriptionId", subscriptionId),};

            if (checkRequestParameters(response, rules)) {
                if(UssdCtrlFunctions.isSubscriptionExists(subscriptionId)){
                    UssdFunctions.unsubscribeUssdSub(subscriptionId);
                    response.setStatus(NOCONTENT);
                } else {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL0001", "A service error occurred. Error code is %1", subscriptionId +" USSD Subscription Not Found");
                }
            }
        }
    }
}
