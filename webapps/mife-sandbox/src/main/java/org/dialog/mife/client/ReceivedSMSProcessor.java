/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mife.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gsm.oneapi.foundation.FormParameters;
import org.gsm.oneapi.foundation.JSONRequest;

/**
 *
 * @author User
 */
public class ReceivedSMSProcessor {

    private String requestingURL;
    private String number;
    private String message;
    private String receivedMsg;

    public ReceivedSMSProcessor(String url, String num, String msg) {
        this.requestingURL = url;
        this.number = num;
        this.message = msg;
    }

    public void getServerResponse(String authHeader) {


        int responseCode = 0;
        String contentType = null;

        try {

            HttpURLConnection con = (HttpURLConnection) new URL(requestingURL).openConnection();//JSONRequest.setupConnection(requestingURL, authHeader);
            con.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            String requestBody = "contactnumber="+number+"&txtmessage="+message;    //JSONRequest.formEncodeParams(formParameters);
            
            //System.out.println("params:: "+requestBody);
            out.write(requestBody);
            out.close();

            responseCode = con.getResponseCode();
            contentType = con.getContentType();
            
            //System.out.println("REs code: "+responseCode);
            
            setReceivedMsg(readResponse(con));

            //HttpServletResponse res = requ
        } catch (Exception e) {
            System.out.println("Connection with Servlet: "+e);
        }
    }

    private String readResponse(HttpURLConnection con) {
        String decodedString = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            while(in.readLine() != null){
//                decodedString = decodedString+in.readLine();
//            }
            decodedString = in.readLine();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceivedSMSProcessor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Reading servelet response: "+ex);
        }
        return decodedString;
    }

    /**
     * @return the receivedMsg
     */
    public String getReceivedMsg() {
        return receivedMsg;
    }

    /**
     * @param receivedMsg the receivedMsg to set
     */
    public void setReceivedMsg(String receivedMsg) {
        this.receivedMsg = receivedMsg;
    }
}
