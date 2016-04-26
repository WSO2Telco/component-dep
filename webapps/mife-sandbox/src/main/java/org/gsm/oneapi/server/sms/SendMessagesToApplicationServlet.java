package org.gsm.oneapi.server.sms;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.UserControlFunctions;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.SendSMSToApplication;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.SubscribeSMSRequest;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.functions.HTTPPoster;

import org.apache.log4j.Logger;
import org.dialog.mife.responsebean.sms.InboundSMSMessage;
import org.dialog.mife.responsebean.sms.InboundSMSMessageNotification;
import org.dialog.mife.responsebean.sms.MessageNotification;
import org.gsm.oneapi.server.OneAPIServlet;
import static org.gsm.oneapi.server.OneAPIServlet.OK;
import static org.gsm.oneapi.server.OneAPIServlet.checkRequestParameters;
import static org.gsm.oneapi.server.OneAPIServlet.getRequestParts;
import static org.gsm.oneapi.server.OneAPIServlet.validateRequest;
import org.gsm.oneapi.server.ValidationRule;

/**
 *
 * @author User
 */
public class SendMessagesToApplicationServlet extends OneAPIServlet {

    static Logger logger = Logger.getLogger(SendMessagesToApplicationServlet.class);
    public SmsImpl smsimpl;

    public void init() throws ServletException {
        logger.debug("SendMessagesToApplicationServlet initialised");
    }
    private final String[] validationRules = {"smsmessaging", "*", "outbound", "requests"};

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //dumpRequestDetails(request, logger);

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
        
        String[] requestParts = getRequestParts(request);
        if (validateRequest(request, response, requestParts, validationRules)) {

            String senderAddress = null;
            String destinationAddress = null;
            String message = null;

            if (request.getParameter("senderAddress") != null) {
                senderAddress = request.getParameter("senderAddress");
                logger.debug("Sender Address: " + request.getParameter("senderAddress"));
            }
            if (request.getParameter("destinationAddress") != null) {
                destinationAddress = request.getParameter("destinationAddress");
                logger.debug("Destination Address: " + request.getParameter("destinationAddress"));
            }
            if (request.getParameter("message") != null) {
                message = request.getParameter("message");
                logger.debug("Message: " + request.getParameter("message"));
            }

            ValidationRule[] rules = {
                //new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
                //new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "destinationAddress", destinationAddress),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_MESSAGE, "message", message),};

            if (checkRequestParameters(response, rules)) {
                if (user != null) {
                    SendSMSToApplication smsmsg = new SendSMSToApplication();
                    smsmsg.setSenderAddress(senderAddress);
                    smsmsg.setDestinationAddress(destinationAddress);
                    smsmsg.setMessage(message);
                    smsmsg.setUser(user);
                    smsmsg.setDate(new Date());

                    smsimpl.saveSendSMSToApplication(smsmsg);
                    handleNotification(smsmsg);

                    response.setStatus(OK);
                }
            }
        }
    }

    public boolean handleNotification(SendSMSToApplication msg) {


        String port = msg.getDestinationAddress();
        String smscRef = "" + msg.getSmsId();
        String message = "";

        if (msg.getMessage() == null) {
            return true;
        }

        // trim message
        message = msg.getMessage().trim();
        int pos = message.indexOf(" ");
        String keyword = "";

        if (pos == -1) {
            keyword = message;
        } else {
            keyword = message.substring(0, pos);
        }

        SubscribeSMSRequest sub = smsimpl.getAppByKeywordAndapp(msg.getDestinationAddress(), keyword);
        if (sub == null) {
            System.out.println("Suscription not found " + msg.getSmsId());
            return true;
        }

        InboundSMSMessage msginbound = new InboundSMSMessage();
        msginbound.setDateTime((msg.getDate() == null) ? "" : msg.getDate().toString());
        msginbound.setDestinationAddress(msg.getDestinationAddress());
        msginbound.setMessage(msg.getMessage());
        //msginbound.setMessageId(msg.getMessageId());
        msginbound.setMessageId("" + msg.getSmsId());
        msginbound.setSenderAddress(msg.getSenderAddress());

        MessageNotification notify = new MessageNotification();
        InboundSMSMessageNotification inbnotify = new InboundSMSMessageNotification();
        inbnotify.setInboundSMSMessage(msginbound);
        inbnotify.setCallbackData(sub.getClientCorrelator());
        notify.setInboundSMSMessageNotification(inbnotify);

        String httppostout = HTTPPoster.excutePost(sub.getNotifyURL(), new Gson().toJson(notify), true);

        return true;
    }
}
