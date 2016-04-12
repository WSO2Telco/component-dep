/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.custom.hostobjects;

import java.math.BigDecimal;
import java.sql.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Tax.
 */
public class Tax {
    
    /** The type. */
    private String type;
    
    /** The effective_from. */
    private Date effective_from;
    
    /** The effective_to. */
    private Date effective_to;

     
    /** The value. */
    private BigDecimal value;

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the effective_from.
     *
     * @return the effective_from
     */
    public Date getEffective_from() {
        return effective_from;
    }

    /**
     * Sets the effective_from.
     *
     * @param effective_from the new effective_from
     */
    public void setEffective_from(Date effective_from) {
        this.effective_from = effective_from;
    }

    /**
     * Gets the effective_to.
     *
     * @return the effective_to
     */
    public Date getEffective_to() {
        return effective_to;
    }

    /**
     * Sets the effective_to.
     *
     * @param effective_to the new effective_to
     */
    public void setEffective_to(Date effective_to) {
        this.effective_to = effective_to;
    }

     
    /**
     * Gets the value.
     *
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
