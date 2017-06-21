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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.mozilla.javascript.ConsString;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Wrapper;

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
        org.apache.axis2.context.MessageContext axis2MessageContext;
        axis2MessageContext = ((Axis2MessageContext) messageContext).getAxis2MessageContext();

        byte[] json = {'{', '}'};
        if (jsonPayload instanceof String) {
            json = jsonPayload.toString().getBytes();
        } else if (jsonPayload != null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                serializeJSON_(jsonPayload, out);
                json = out.toByteArray();
            } catch (IOException e) {
                logger.error("#setPayloadJSON. Could not retrieve bytes from JSON object. Error>>> "
                        + e.getLocalizedMessage());
            }
        }
        // save this JSON object as the new payload.
        try {
            JsonUtil.getNewJsonPayload(axis2MessageContext, json, 0, json.length, true, true);
        } catch (AxisFault axisFault) {
            throw new ScriptException(axisFault);
        }
        //JsonUtil.setContentType(messageContext);
        Object jsonObject = scriptEngine.eval(JsonUtil.newJavaScriptSourceReader(axis2MessageContext));
        setJsonObject(messageContext, jsonObject);
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

    private void serializeJSON_(Object obj, OutputStream out) throws IOException {
        if (out == null) {
            logger.warn("#serializeJSON_. Did not serialize JSON object. Object: " + obj + "  Stream: " + out);
            return;
        }
        if(obj instanceof Wrapper) {
            obj = ((Wrapper) obj).unwrap();
        }

        if(obj == null){
            out.write("null".getBytes());
        }
        else if (obj instanceof NativeArray) {
            // TODO : Find a proper solution for array handling
            /*out.write('[');
            NativeArray o = (NativeArray) obj;
            Object[] ids = o.getIds();
            boolean first = true;
            for (Object id : ids) {
                Object value = o.get((Integer) id, o);
                if (!first) {
                    out.write(',');
                    out.write(' ');
                } else {
                    first = false;
                }
                serializeJSON_(value, out);
            }
            out.write(']');*/
        } else if (obj instanceof ScriptObjectMirror) {
            out.write('{');
            ScriptObjectMirror o = (ScriptObjectMirror) obj;
            Set<String> keys = o.keySet();
            boolean first = true;
            for (String key : keys) {
                Object value = o.get(key);
                if (!first) {
                    out.write(',');
                    out.write(' ');
                } else {
                    first = false;
                }
                out.write('"');
                out.write(key.getBytes());
                out.write('"');
                out.write(':');
                serializeJSON_(value, out);
            }
            out.write('}');
        } else if (obj instanceof Object[]) {
            out.write('[');
            boolean first = true;
            for (Object value : (Object[]) obj) {
                if (!first) {
                    out.write(',');
                    out.write(' ');
                } else {
                    first = false;
                }
                serializeJSON_(value, out);
            }
            out.write(']');
        } else if (obj instanceof String) {
            out.write('"');
            out.write(((String) obj).getBytes());
            out.write('"');
        } else if (obj instanceof ConsString) {
            //This class represents a string composed of two components using the "+" operator
            //in java script with rhino7 upward. ex:var str = "val1" + "val2";
            out.write('"');
            out.write((((ConsString) obj).toString()).getBytes());
            out.write('"');
        } else if (obj instanceof Integer ||
                obj instanceof Long ||
                obj instanceof Float ||
                obj instanceof Double ||
                obj instanceof Short ||
                obj instanceof BigInteger ||
                obj instanceof BigDecimal ||
                obj instanceof Boolean) {
            out.write(obj.toString().getBytes());
        } else {
            out.write('{');
            out.write('}');
        }
    }
}
