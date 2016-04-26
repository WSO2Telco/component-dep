package org.gsm.oneapi.server.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.SendSMSToApplication;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.sms.InboundSMSMessage;
import org.dialog.mife.responsebean.sms.ReceivingSMSResponse;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.responsebean.sms.InboundSMSMessageList;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;

/**
 * Servlet implementing the OneAPI function for retrieving received SMS messages
 */
public class RetrieveSMSMessagesServlet extends OneAPIServlet {

    private static final long serialVersionUID = 2849235677506318772L;
    public static final String DELIVERYIMPOSSIBLE = "DeliveryImpossible";
    public static final String DELIVEREDTONETWORK = "DeliveredToNetwork";
    public static final String DELIVEREDTOTERMINAL = "DeliveredToTerminal";
    public static final String DELIVERYUNCERTAIN = "DeliveryUncertain";
    public static final String MESSAGEWAITING = "MessageWaiting";
    static Logger logger = Logger.getLogger(RetrieveSMSMessagesServlet.class);
    
    public SmsImpl smsimpl;

    public void init() throws ServletException {
        logger.debug("RetrieveSMSMessagesServlet initialised");
    }
    //private final String[] validationRules={"1", "smsmessaging", "inbound", "registrations", "*", "messages"};
    private final String[] validationRules = {"smsmessaging", "1", "inbound", "registrations", "*", "messages"};

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        dumpRequestDetails(request, logger);
        
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
            /*
             * Decode the service parameters - in this case it is an HTTP GET request 
             */
            String registrationId = requestParts[4];
            int maxBatchSize = parseInt(request.getParameter("maxBatchSize"));

            logger.debug("registrationId = " + registrationId);
            logger.debug("maxBatchSize = " + maxBatchSize);

            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "registrationId", registrationId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "maxBatchSize", Integer.valueOf(maxBatchSize)),};

            if (checkRequestParameters(response, rules)) {

                if (!smsimpl.isSenderAddress(user.getId(), registrationId)) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", registrationId +" Not Provisioned");
                } else {
                
                List<InboundSMSMessage> recivedMsgs = new ArrayList<InboundSMSMessage>();
                List<SendSMSToApplication> listinbound = smsimpl.getMessageInbound(registrationId, user.getId());
                //save transaction
                smsimpl.saveTransaction(registrationId,null,null, null,null, null, null, maxBatchSize,"success", 2,null,null, user, null);
                
                int i=0;
                for (SendSMSToApplication smsinboud : listinbound) {

                    InboundSMSMessage sampleMsg1 = new InboundSMSMessage();
                    sampleMsg1.setDateTime(smsinboud.getDate().toString());
                    sampleMsg1.setDestinationAddress(smsinboud.getDestinationAddress());
                    sampleMsg1.setMessageId(String.valueOf(smsinboud.getSmsId()));
                    sampleMsg1.setMessage(smsinboud.getMessage());
                    sampleMsg1.setResourceURL("http://example.com/smsmessaging/1.0/inbound/registrations/"+registrationId+"/messages/msg1");
                    sampleMsg1.setSenderAddress(smsinboud.getSenderAddress());                    
                    recivedMsgs.add(sampleMsg1);
                    i++;
                    if (i >=maxBatchSize) {
                        break;
                    }
                       
                }

                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/smsmessaging/1/inbound/registrations/" + urlEncode(registrationId) + "/messages";

                ReceivingSMSResponse resObject = new ReceivingSMSResponse();
                resObject.setInboundSMSMessage(recivedMsgs);
                resObject.setNumberOfMessagesInThisBatch("" + recivedMsgs.size());
                resObject.setResourceURL(resourceURL);
                resObject.setTotalNumberOfPendingMessages("0");
                
                ObjectMapper mapper = new ObjectMapper();
                String jsonResponse = "{\"inboundSMSMessageList\":" + mapper.writeValueAsString(resObject) + "}";

                sendJSONResponse(response, jsonResponse, OK, null);
                }

            }
        }
    }
}
