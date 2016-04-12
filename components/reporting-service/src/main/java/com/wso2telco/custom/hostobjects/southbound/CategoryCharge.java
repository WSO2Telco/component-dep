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
package com.wso2telco.custom.hostobjects.southbound;

import java.util.Locale;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryCharge.
 */
public class CategoryCharge {

    /** The operation id. */
    private Integer operationId;
    
    /** The category. */
    private String category;
    
    /** The subcategory. */
    private String subcategory;
        
    /**
     * Instantiates a new category charge.
     *
     * @param operationId the operation id
     * @param category the category
     * @param subcategory the subcategory
     */
    public CategoryCharge(int operationId,String category, String subcategory) {
        this.category = category;
        this.subcategory = subcategory;
        this.operationId = operationId;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the subcategory.
     *
     * @return the subcategory
     */
    public String getSubcategory() {
        return subcategory;
    }

    /**
     * Sets the subcategory.
     *
     * @param subcategory the new subcategory
     */
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    /**
     * Gets the operation id.
     *
     * @return the operation id
     */
    public Integer getOperationId() {
        return operationId;
    }

    /**
     * Sets the operation id.
     *
     * @param operationId the new operation id
     */
    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }
    
   
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryCharge categoryKey = (CategoryCharge) o;

        if (category != null ? !category.equalsIgnoreCase(categoryKey.category) : categoryKey.category != null) return false;
        if (subcategory != null ? !subcategory.equalsIgnoreCase(categoryKey.subcategory) : categoryKey.subcategory != null) return false;
        if (operationId != null ? operationId != categoryKey.operationId : categoryKey.operationId != null) return false;        
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = category != null ? category.toLowerCase(Locale.ENGLISH).hashCode() : 0;
        result = 31 * result + (subcategory != null ? subcategory.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (operationId != null ? operationId.hashCode() : 0);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "categoryKey{" +
                "operator='" + category + '\'' +
                ", apiName='" + subcategory + '\'' +                
                ", operationId='" + operationId + '\'' +                
                '}';
    }
    
}
