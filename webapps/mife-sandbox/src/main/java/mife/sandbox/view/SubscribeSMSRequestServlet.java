/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.SubscriberSMSCtrlFunctions;
import mife.sandbox.model.entities.SubscribeSMSRequest;
import mife.sandbox.model.functions.SubscribeSMSFunctions;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.server.OneAPIServlet;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class SubscribeSMSRequestServlet extends OneAPIServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] requestParts = getRequestParts(request);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (requestParts[0].equals("EditSubscribeRequest")) {
            String return_val;

            try {

                String json = RequestHandler.getRequestJSON(request);
                
                JSONObject jo = new JSONObject(json);

                String id = jo.getString("id").replaceAll("\\s", "");
                String callbackData = jo.get("callbackData").toString();
                String criteria = jo.get("criteria").toString();
                String notifyURL = jo.get("notifyURL").toString();

                int idX = Integer.parseInt(id);

                return_val = SubscribeSMSFunctions.editSubscribeSMSRequest(idX, callbackData, criteria, notifyURL) + "";
            } catch (Exception e) {
                return_val = e.getMessage();
            }
            try {
                out.println(return_val);
            } finally {
                out.close();
            }
        } else if (requestParts[0].equals("DeleteSubscribeRequest")) {
            String requestid = request.getParameter("subscribeid").replaceAll("\\s", "");
            String return_val = "";
            if (requestid != null) {
                try {
                    int idX = Integer.parseInt(requestid);
                    return_val = SubscribeSMSFunctions.deleteSubscribeSMSRequest(idX) + "";
                } catch (Exception e) {
                    return_val = e.getMessage();
                }
                try {
                    out.println(return_val);
                } finally {
                    out.close();
                }
            }
        } if (requestParts[0].equals("SearchSubscribeRequest")) {
            String requestid = request.getParameter("subscribeid").replaceAll("\\s", "");
            String return_val = "";
            if (requestid != null) {
                try {
                    int idX = Integer.parseInt(requestid);
                    SubscribeSMSRequest s = SubscriberSMSCtrlFunctions.getSubscribeSMSObj(idX);
                    if(s != null){
                        return_val = s.getCallbackData()+","+s.getClientCorrelator()+","+s.getCriteria()+","+s.getDestinationAddress()+","+s.getNotificationFormat()+","+s.getNotifyURL();
                    } else {
                        return_val = "No data found for response";
                    }
                } catch (Exception e) {
                    return_val = e.getMessage();
                }
                try {
                    out.println(return_val);
                } finally {
                    out.close();
                }
            }
        }
    }
}
