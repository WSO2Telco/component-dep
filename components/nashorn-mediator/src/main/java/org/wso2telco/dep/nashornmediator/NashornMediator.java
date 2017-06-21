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

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class NashornMediator extends AbstractMediator {
    private static final Log logger = LogFactory.getLog(NashornMediator.class.getName());

    /**
     * Binding name for the {@link MessageContext}
     */
    private static final String MC_VAR_NAME = "mc";

    /**
     * Reference to an empty JSON object
     */
    private final Object emptyJsonObject;

    /**
     * Reference to Nashorn Script Engine
     */
    private final ScriptEngine scriptEngine;

    /**
     * Compiled script that is reused when mediating the message. This compiled script evaluation improves performance
     */
    private CompiledScript compiledScript;

    /**
     * JSON parser used to parse JSON strings
     */
    private JsonParser jsonParser;

    /**
     * Flag to determine whether nashorn mediator is using JSON payload
     */
    private boolean isJsonPayloadAware;

    public NashornMediator() {
        try {
            scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
            emptyJsonObject = scriptEngine.eval("({})");
            jsonParser = new JsonParser();
        } catch (ScriptException e) {
            throw new SynapseException("Error occurred while initialising mediator", e);
        }
    }

    public boolean mediate(MessageContext messageContext) {

        try {
            NashornMessageContext nashornMessageContext = new NashornMessageContext(messageContext, scriptEngine, emptyJsonObject);

            if (isJsonPayloadAware) {
                processJSONPayload(messageContext, nashornMessageContext);
            }

            Bindings bindings = scriptEngine.createBindings();
            bindings.put(MC_VAR_NAME, nashornMessageContext);
            compiledScript.eval(bindings);
            return true;
        } catch (ScriptException e) {
            throw new SynapseException("Error occurred while evaluating script", e);
        }
    }

    private void processJSONPayload(MessageContext synapseContext, NashornMessageContext nashornMessageContext) throws ScriptException {
        if (!(synapseContext instanceof Axis2MessageContext)) {
            return;
        }
        org.apache.axis2.context.MessageContext messageContext = ((Axis2MessageContext) synapseContext).getAxis2MessageContext();
        String jsonString = (String) messageContext.getProperty("JSON_STRING");
        Object jsonObject = null;
        if (JsonUtil.hasAJsonPayload(messageContext)) {
            try {
                JsonElement o = jsonParser.parse(new JsonReader(JsonUtil.newJsonPayloadReader(messageContext))); // first, check if the stream is valid.
                if (o.isJsonNull()) {
                    logger.error("#processJSONPayload. JSON stream is not valid.");
                    return;
                }
                jsonObject = this.scriptEngine.eval(JsonUtil.newJavaScriptSourceReader(messageContext));
                // TODO : arrays are converted into maps. Need to avoid
            } catch (Exception e) {
                handleException("Failed to get the JSON payload from the input stream. Error>>>\n" + e.getLocalizedMessage(), synapseContext);
            }
        } else if (jsonString != null) {
            String jsonPayload = jsonParser.parse(jsonString).toString();
            jsonObject = this.scriptEngine.eval('(' + jsonPayload + ')');
        }
        if (jsonObject != null) {
            nashornMessageContext.setJsonObject(synapseContext, jsonObject);
        }
    }

    public void setScript(String sourceScript) {
        try {
            if (sourceScript.contains("getPayloadJSON")) {
                isJsonPayloadAware = true;
            }

            compiledScript = ((Compilable) scriptEngine).compile(sourceScript);
        } catch (ScriptException e) {
            throw new SynapseException("Error occurred while compiling the script", e);
        }
    }
}
