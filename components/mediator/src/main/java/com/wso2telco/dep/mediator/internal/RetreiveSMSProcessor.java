/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.mediator.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dep.mediator.entity.nb.InboundSMSMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.synapse.MessageContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

// TODO: Auto-generated Javadoc
/**
 * The Class RetreiveSMSProcessor.
 */
public class RetreiveSMSProcessor implements Processor {

    /**
     * Instantiates a new retreive sms processor.
     */
    public RetreiveSMSProcessor() {
    }

    /** The gson. */
    Gson gson = new GsonBuilder().serializeNulls().create();

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.internal.Processor#getAppID(org.apache.synapse.MessageContext)
     */
    @Override
    public String getAppID(MessageContext context) {
        String ResourceUrlPrefix = (String) context.getProperty("REST_URL_PREFIX");
        String requestPath = (String) context.getProperty("REST_FULL_REQUEST_PATH");
        String resourceUrl = ResourceUrlPrefix + requestPath;

        String regex = "(?<=registrations/)(.*)(?=/messages)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(resourceUrl);

        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.internal.Processor#getResultList(java.lang.String)
     */
    //@Override
    public JSONArray getResultList(String response) {
        JSONArray array = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            array = jsonObject.getJSONObject("inboundSMSMessageList").getJSONArray("inboundSMSMessage");

        } catch (JSONException ex) {
            Logger.getLogger(RetreiveSMSProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return array;

    }

/* (non-Javadoc)
 * @see com.wso2telco.mediator.internal.Processor#setBatchSize(java.lang.String, java.lang.String, int)
 */
//@Override
    public APICall setBatchSize(String uri, String body, int batchSize) {
        JSONObject jobj = null;
        String url = "";
        try {
            String regex = "(?i)(?<=maxBatchSize)=([^&#]*)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(uri);

            if (m.find()) {
                url = m.replaceAll("=" + batchSize);
            } else {
                url = uri + ((uri.indexOf("?") == -1 ? "?" : "&") + "maxBatchSize=23");
            }
            jobj = XML.toJSONObject(body);

        } catch (JSONException ex) {
            //Logger.getLogger(RetreiveSMSProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new APICall(url, jobj);
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.internal.Processor#generateResponse(org.apache.synapse.MessageContext, org.json.JSONArray, java.lang.String, java.util.ArrayList)
     */
    @Override
    public JSONObject generateResponse(MessageContext context, JSONArray results, String resourceURL, ArrayList<String> responses) {
        String resultsList = "";
        JSONObject retobj = null;

        try {
            String appID = getAppID(context);
            Object[] params = {appID};
            resourceURL = MessageFormat.format(resourceURL, params);

            for (int i = 0; i < results.length(); i++) {
                resultsList = resultsList + results.get(i).toString() + ",";
            }

            resultsList = resultsList.substring(0, (resultsList.lastIndexOf(",") == -1 ? 0 : resultsList.lastIndexOf(",")));

            int pendingMsgs = 0;
            if (responses != null) {
                for (String res : responses) {
                    JSONObject resObj = new JSONObject(res);
                    int no = resObj.getJSONObject("inboundSMSMessageList").getInt("totalNumberOfPendingMessages");
                    pendingMsgs = pendingMsgs + no;
                }
            }

            String start = "{\"inboundSMSMessageList\": {"
                    + "\"inboundSMSMessage\": [";
            String end = "],"
                    + "\"numberOfMessagesInThisBatch\": \"" + results.length() + "\","
                    + "\"resourceURL\": \"" + resourceURL + "\","
                    + "\"totalNumberOfPendingMessages\": \"" + pendingMsgs + "\"}}";

            String response = start + resultsList + end;

            retobj = new JSONObject(response);
        } catch (JSONException ex) {
            Logger.getLogger(RetreiveSMSProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retobj;
    }

    /* (non-Javadoc)
     * @see com.wso2telco.mediator.internal.Processor#generateResponse(org.apache.synapse.MessageContext, java.util.List, java.lang.String, java.util.ArrayList)
     */
    @Override
    public JSONObject generateResponse(MessageContext context, List<InboundSMSMessage> inboundSMSMessageList, String resourceURL, ArrayList<String> responses) {
        String resultsList = "";
        JSONObject retobj = null;

        try {
             

            for (int i = 0; i < inboundSMSMessageList.size(); i++) {
                 
                resultsList = resultsList + gson.toJson(inboundSMSMessageList.get(i)) + ",";
            }

            resultsList = resultsList.substring(0, (resultsList.lastIndexOf(",") == -1 ? 0 : resultsList.lastIndexOf(",")));

            int pendingMsgs = 0;
            if (responses != null) {
                for (String res : responses) {
                    JSONObject resObj = new JSONObject(res);
                    int no = resObj.getJSONObject("inboundSMSMessageList").getInt("totalNumberOfPendingMessages");
                    pendingMsgs = pendingMsgs + no;
                }
            }

            String start = "{\"inboundSMSMessageList\": {"
                    + "\"inboundSMSMessage\": [";
            String end = "],"
                    + "\"numberOfMessagesInThisBatch\": \"" + inboundSMSMessageList.size() + "\","
                    + "\"resourceURL\": \"" + resourceURL + "\","
                    + "\"totalNumberOfPendingMessages\": \"" + pendingMsgs + "\"}}";

            String response = start + resultsList + end;

            retobj = new JSONObject(response);
        } catch (JSONException ex) {
            Logger.getLogger(RetreiveSMSProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retobj;
    }
}
