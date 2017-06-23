/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2telco.dep.nashornmediator;

import java.io.*;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Wrapped Synapse {@link MessageContext} to expose functionality in a javascript friendly manner
 */
public class NashornMessageContext {
    private static final Log logger = LogFactory.getLog(NashornMessageContext.class.getName());

    private static final String JSON_OBJECT = "JSON_OBJECT";

    private final MessageContext messageContext;

    private final ScriptEngine scriptEngine;
    private final Object emptyNashornObject;

    public NashornMessageContext(MessageContext messageContext, ScriptEngine scriptEngine, Object emptyJsonObject) {
        this.messageContext = messageContext;
        this.scriptEngine = scriptEngine;
        this.emptyNashornObject = emptyJsonObject;
    }

    /**
     * Set the JSON payload to the message context
     *
     * @param jsonPayload JSON payload passed as a Nashorn JSON object
     * @throws ScriptException Throws exception when internal error happens when setting the payload to axis2 message
     *                         context
     */
    public void setPayloadJSON(Object jsonPayload) throws ScriptException {
        try {
            ScriptObjectMirror json = (ScriptObjectMirror) scriptEngine.eval("JSON");
            String jsonString = (String)json.callMember("stringify", jsonPayload);
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes());
            org.apache.axis2.context.MessageContext axis2mc =
                    ((Axis2MessageContext) messageContext).getAxis2MessageContext();
            JsonUtil.getNewJsonPayload(axis2mc, stream, true, true);
            setJsonObject(messageContext, jsonPayload);
        } catch (AxisFault axisFault) {
            throw new ScriptException(axisFault);
        }
    }

    /**
     * Get the JSON payload
     *
     * @return JSON payload as a Nashorn specific JSON object
     */
    public Object getPayloadJSON() {
        if (messageContext == null) {
            return emptyNashornObject;
        }
        Object jsonObject = messageContext.getProperty(JSON_OBJECT);
        if (jsonObject == null) {
            return emptyNashornObject;
        }
        return jsonObject;
    }

    /**
     * Saves the JavaScript Object to the message context.
     * @param messageContext
     * @param jsonObject
     * @return
     */
    public boolean setJsonObject(MessageContext messageContext, Object jsonObject) {
        if (jsonObject == null) {
            logger.error("Setting null JSON object.");
        }
        messageContext.setProperty(JSON_OBJECT, jsonObject);
        return true;
    }

    /**
     * Get a property set in {@link MessageContext}
     *
     * @param key property key
     * @return value for the provided property key
     */
    public Object getProperty(String key) {
        return messageContext.getProperty(key);
    }

    /**
     * Set a property in the {@link MessageContext}
     *
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, Object value) {
        messageContext.setProperty(key, value);
    }

}
