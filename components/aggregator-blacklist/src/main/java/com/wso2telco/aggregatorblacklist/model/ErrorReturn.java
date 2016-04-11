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
package com.wso2telco.aggregatorblacklist.model;


// TODO: Auto-generated Javadoc
/**
 * The Class ErrorReturn.
 */
public class ErrorReturn {

    /** The errcode. */
    private String errcode;
    
    /** The errvar. */
    private String[] errvar;

    /**
     * Instantiates a new error return.
     *
     * @param errcode the errcode
     * @param errvar the errvar
     */
    public ErrorReturn(String errcode, String[] errvar) {
        this.errcode = errcode;
        this.errvar = errvar;
    }    
    
    /**
     * Gets the errcode.
     *
     * @return the errcode
     */
    public String getErrcode() {
        return errcode;
    }

    /**
     * Sets the errcode.
     *
     * @param errcode the new errcode
     */
    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    /**
     * Gets the errvar.
     *
     * @return the errvar
     */
    public String[] getErrvar() {
        return errvar;
    }

    /**
     * Sets the errvar.
     *
     * @param errvar the new errvar
     */
    public void setErrvar(String[] errvar) {
        this.errvar = errvar;
    }
    
}
