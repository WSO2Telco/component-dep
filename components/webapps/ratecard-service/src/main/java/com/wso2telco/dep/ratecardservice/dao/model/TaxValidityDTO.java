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

public class TaxValidityDTO {

    private Integer idtaxValidityId;
    private String taxValidityactdate;
    private String taxValiditydisdate;
    private String taxValidityval;

    public Integer getIdtaxValidityId() {
        return idtaxValidityId;
    }

    public void setIdtaxValidityId(Integer idtaxValidityId) {
        this.idtaxValidityId = idtaxValidityId;
    }

    public void setTaxValidityactdate(String taxValidityactdate) {
        this.taxValidityactdate = taxValidityactdate;
    }

    public String getTaxValidityactdate() {
        return taxValidityactdate;
    }

    public void setTaxValiditydisdate(String taxValiditydisdate) {
        this.taxValiditydisdate = taxValiditydisdate;
    }

    public String getTaxValiditydisdate() {
        return taxValiditydisdate;
    }

    public void setTaxValidityval(String taxValidityval) {
        this.taxValidityval = taxValidityval;
    }

    public String getTaxValidityval() {
        return taxValidityval;
    }

}
