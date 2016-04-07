
package com.wso2telco.custom.hostobjects.southbound;

import java.util.Locale;

public class CategoryCharge {

    private Integer operationId;
    private String category;
    private String subcategory;
        
    public CategoryCharge(int operationId,String category, String subcategory) {
        this.category = category;
        this.subcategory = subcategory;
        this.operationId = operationId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }
    
   
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

    @Override
    public int hashCode() {
        int result = category != null ? category.toLowerCase(Locale.ENGLISH).hashCode() : 0;
        result = 31 * result + (subcategory != null ? subcategory.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (operationId != null ? operationId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "categoryKey{" +
                "operator='" + category + '\'' +
                ", apiName='" + subcategory + '\'' +                
                ", operationId='" + operationId + '\'' +                
                '}';
    }
    
}
