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
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.AbstractMediator;

public class NashornMediator extends AbstractMediator {

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

    public NashornMediator() {
        try {
            scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
            emptyJsonObject = scriptEngine.eval("({})");
        } catch (ScriptException e) {
            throw new SynapseException("Error occurred while initialising mediator", e);
        }
    }

    public boolean mediate(MessageContext messageContext) {

        try {
            Bindings bindings = scriptEngine.createBindings();
            bindings.put(MC_VAR_NAME, new NashornMessageContext(messageContext, scriptEngine, emptyJsonObject));
            compiledScript.eval(bindings);
            return true;
        } catch (ScriptException e) {
            throw new SynapseException("Error occurred while evaluating script", e);
        }
    }

    public void setScript(String sourceScript) {
        try {
            compiledScript = ((Compilable) scriptEngine).compile(sourceScript);
        } catch (ScriptException e) {
            throw new SynapseException("Error occurred while compiling the script", e);
        }
    }
}
