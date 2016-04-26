/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mife.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.gsm.oneapi.server.OneAPIServlet;

/**
 *
 * @author User
 */
public class ReceivedSMSAlertServlet extends OneAPIServlet {

    static Logger logger = Logger.getLogger(ReceivedSMSAlertServlet.class);

    public void init() throws ServletException {
        logger.debug("QueryDeliveryStausServlet initialised");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

        String contNum = request.getParameter("contactnumber");
        String msg = request.getParameter("txtmessage");
        
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-DD-mm'T'hh:mm:ss");

        String resourceURL = getRequestHostnameAndContext(request)+request.getServletPath();
        //if(checkSubscription(request)){
        if (true) {
//            ServletOutputStream output = response.getOutputStream();
//            output.print(msg);
//            output.flush();


            MsgReciptNotification msgObj = new MsgReciptNotification();
            msgObj.setCallbackData("12345");

            MsgReciptNotification.InboundSMSMessage inboundMsg = new MsgReciptNotification.InboundSMSMessage();
            inboundMsg.setDateTime(df.format(now));
            inboundMsg.setSenderAddress(contNum);
            inboundMsg.setDestinationAddress("");
            inboundMsg.setMessage(msg);
            inboundMsg.setMessageId("TXT1234");

            msgObj.setInboundSMSMessage(inboundMsg);

            ObjectMapper mapper = new ObjectMapper();

            String jsonResponse = "{\"inboundSMSMessageNotification\":" + mapper.writeValueAsString(msgObj) + "}";
            //logger.debug("Response JSON: " + jsonResponse);
            //logger.debug("Sending response. ResourceURL=" + resourceURL);

            sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
        }
    }

    private boolean checkSubscription(HttpServletRequest request) {
        boolean isSubscribed = false;
        HttpSession session = request.getSession();
        if (session != null) {
            session.getAttribute("");
        }
        return isSubscribed;
    }
}
