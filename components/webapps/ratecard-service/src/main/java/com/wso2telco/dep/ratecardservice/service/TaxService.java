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
package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.dep.ratecardservice.dao.TaxDAO;
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TaxValidityDTO;

public class TaxService {

    TaxDAO taxDAO;

    public TaxService() {

        taxDAO = new TaxDAO();
    }

    public List<TaxDTO> getTaxes() throws BusinessException {

        List<TaxDTO> taxes = null;

        taxes = taxDAO.getTaxes();

        if (taxes != null && !taxes.isEmpty()) {

            for (int i = 0; i < taxes.size(); i++) {

                TaxDTO tax = taxes.get(i);

                List<TaxValidityDTO> taxValidityList = getTaxValidityDates(tax.getTaxId());
                TaxValidityDTO[] taxValidityArray = new TaxValidityDTO[taxValidityList.size()];
                tax.setTaxesValidityDates(taxValidityList.toArray(taxValidityArray));

            }

            return taxes;
        } else {

            return Collections.emptyList();
        }
    }

    public List<TaxValidityDTO> getTaxValidityDates(int taxId) throws BusinessException {

        List<TaxValidityDTO> taxesValidityDates = null;

        taxesValidityDates = taxDAO.getTaxValidityDates(taxId);

        if (taxesValidityDates != null && !taxesValidityDates.isEmpty()) {

            return taxesValidityDates;
        } else {

            return Collections.emptyList();
        }
    }

    public TaxDTO addTax(TaxDTO tax) throws BusinessException {

        TaxDTO newTax = null;

        newTax = taxDAO.addTax(tax);
        newTax = getTax(newTax.getTaxId());

        return newTax;
    }

    public TaxDTO getTax(int taxId) throws BusinessException {

        TaxDTO tax = null;

        tax = taxDAO.getTax(taxId);

        return tax;
    }
}
