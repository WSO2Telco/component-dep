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
package com.wso2telco.oneapivalidation.exceptions;

 
// TODO: Auto-generated Javadoc
/**
 * The Class CustomException.
 */
public class CustomException extends RuntimeException {

    /** The errcode. */
    private String errcode;
    
    /** The errmsg. */
    private String errmsg;
    
    /** The errvar. */
    private String[] errvar;

     
    /**
     * Instantiates a new custom exception.
     */
    public CustomException() {
    }

     
    /**
     * Instantiates a new custom exception.
     *
     * @param errcode the errcode
     * @param errmsg the errmsg
     * @param errvar the errvar
     */
    public CustomException(String errcode, String errmsg, String[] errvar) {
        super(errcode);
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.errvar = errvar;
    }
    
    /**
     * Instantiates a new custom exception.
     *
     * @param errcode the errcode
     * @param errvar the errvar
     */
    public CustomException(String errcode, String[] errvar) {
        super(errcode);
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
     * Gets the errvar.
     *
     * @return the errvar
     */
    public String[] getErrvar() {
        return errvar;
    }

     
    /**
     * Gets the errmsg.
     *
     * @return the errmsg
     */
    public String getErrmsg() {
        return errmsg;
    }

     
    /**
     * Sets the errmsg.
     *
     * @param errmsg the new errmsg
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
