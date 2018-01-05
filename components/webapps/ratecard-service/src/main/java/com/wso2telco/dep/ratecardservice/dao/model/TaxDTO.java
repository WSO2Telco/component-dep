/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p/>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.dao.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaxDTO {

    private Integer taxId;
    private String taxCode;
    private String taxName;
    private List<TaxValidityDTO> taxesValidityDates = Collections.emptyList();
    private String createdBy;

    public void addValidity(TaxValidityDTO validityDTO){
        if(taxesValidityDates.isEmpty()){
            taxesValidityDates = new ArrayList<>();
        }
        taxesValidityDates.add(validityDTO);
    }
    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public TaxValidityDTO[] getTaxesValidityDates() {
        return taxesValidityDates.toArray( new TaxValidityDTO[taxesValidityDates.size()] );
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


}
