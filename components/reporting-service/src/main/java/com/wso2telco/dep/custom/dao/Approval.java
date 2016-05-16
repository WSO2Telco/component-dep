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
package com.wso2telco.dep.custom.dao;


// TODO: Auto-generated Javadoc
/**
 * The Class Approval.
 */
public class Approval {

    /** The application_id. */
    String application_id;
    
    /** The type. */
    String type;
    
    /** The name. */
    String name;
    
    /** The operatorid. */
    int operatorid;
    
    /** The isactive. */
    String isactive;
    
    /** The tier_id. */
    String tier_id;
    
    /** The api_name. */
    String api_name;
    
    /** The api_version. */
    String api_version;
    
    /** The created. */
    String created;
    
    /** The last_updated. */
    String last_updated;

    /**
     * Instantiates a new approval.
     *
     * @param application_id the application_id
     * @param type the type
     * @param name the name
     * @param operatorid the operatorid
     * @param isactive the isactive
     * @param tier_id the tier_id
     * @param api_name the api_name
     * @param api_version the api_version
     * @param created the created
     * @param lastupdated the lastupdated
     */
    public Approval(String application_id, String type, String name, int operatorid, String isactive, String tier_id,
            String api_name, String api_version, String created,String lastupdated) {
        this.application_id = application_id;
        this.type = type;
        this.name = name;
        this.operatorid = operatorid;
        this.isactive = isactive;
        this.tier_id = tier_id;
        this.api_name = api_name;
        this.api_version = api_version;
        this.created = created;
        this.last_updated = lastupdated;
    }
    
    /**
     * Gets the application_id.
     *
     * @return the application_id
     */
    public String getApplication_id() {
        return application_id;
    }

    /**
     * Sets the application_id.
     *
     * @param application_id the new application_id
     */
    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the operatorid.
     *
     * @return the operatorid
     */
    public int getOperatorid() {
        return operatorid;
    }

    /**
     * Sets the operatorid.
     *
     * @param operatorid the new operatorid
     */
    public void setOperatorid(int operatorid) {
        this.operatorid = operatorid;
    }

    /**
     * Gets the isactive.
     *
     * @return the isactive
     */
    public String getIsactive() {
        return isactive;
    }

    /**
     * Sets the isactive.
     *
     * @param isactive the new isactive
     */
    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    /**
     * Gets the tier_id.
     *
     * @return the tier_id
     */
    public String getTier_id() {
        return tier_id;
    }

    /**
     * Sets the tier_id.
     *
     * @param tier_id the new tier_id
     */
    public void setTier_id(String tier_id) {
        this.tier_id = tier_id;
    }

    /**
     * Gets the api_name.
     *
     * @return the api_name
     */
    public String getApi_name() {
        return api_name;
    }

    /**
     * Sets the api_name.
     *
     * @param api_name the new api_name
     */
    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    /**
     * Gets the api_version.
     *
     * @return the api_version
     */
    public String getApi_version() {
        return api_version;
    }

    /**
     * Sets the api_version.
     *
     * @param api_version the new api_version
     */
    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    /**
     * Gets the created.
     *
     * @return the created
     */
    public String getCreated() {
        return (created == null)? "" : created;
    }

    /**
     * Sets the created.
     *
     * @param created the new created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * Gets the last_updated.
     *
     * @return the last_updated
     */
    public String getLast_updated() {
        return (last_updated == null)? "" : last_updated;
    }

    /**
     * Sets the last_updated.
     *
     * @param last_updated the new last_updated
     */
    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }  
}
