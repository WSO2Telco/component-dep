/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TariffDTO {

	private Integer tariffId;
	private String tariffName;
	private String tariffDescription;
	private Double tariffDefaultVal;
	private Integer tariffMaxCount;
	private Double tariffExcessRate;
	private Double tariffDefRate;
	private Double tariffSPCommission;
	private Double tariffAdsCommission;
	private Double tariffOpcoCommission;
	private Double tariffSurChargeval;
	private Double tariffSurChargeAds;
	private Double tariffSurChargeOpco;
	private String createdBy;
	
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public String getTariffName() {
		return tariffName;
	}
	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}
	public String getTariffDescription() {
		return tariffDescription;
	}
	public void setTariffDescription(String tariffDescription) {
		this.tariffDescription = tariffDescription;
	}
	public Double getTariffDefaultVal() {
		return tariffDefaultVal;
	}
	public void setTariffDefaultVal(Double tariffDefaultVal) {
		this.tariffDefaultVal = tariffDefaultVal;
	}
	public Integer getTariffMaxCount() {
		return tariffMaxCount;
	}
	public void setTariffMaxCount(Integer tariffMaxCount) {
		this.tariffMaxCount = tariffMaxCount;
	}
	public Double getTariffExcessRate() {
		return tariffExcessRate;
	}
	public void setTariffExcessRate(Double tariffExcessRate) {
		this.tariffExcessRate = tariffExcessRate;
	}
	public Double getTariffDefRate() {
		return tariffDefRate;
	}
	public void setTariffDefRate(Double tariffDefRate) {
		this.tariffDefRate = tariffDefRate;
	}
	public Double getTariffSPCommission() {
		return tariffSPCommission;
	}
	public void setTariffSPCommission(Double tariffSPCommission) {
		this.tariffSPCommission = tariffSPCommission;
	}
	public Double getTariffAdsCommission() {
		return tariffAdsCommission;
	}
	public void setTariffAdsCommission(Double tariffAdsCommission) {
		this.tariffAdsCommission = tariffAdsCommission;
	}
	public Double getTariffOpcoCommission() {
		return tariffOpcoCommission;
	}
	public void setTariffOpcoCommission(Double tariffOpcoCommission) {
		this.tariffOpcoCommission = tariffOpcoCommission;
	}
	public Double getTariffSurChargeval() {
		return tariffSurChargeval;
	}
	public void setTariffSurChargeval(Double tariffSurChargeval) {
		this.tariffSurChargeval = tariffSurChargeval;
	}
	public Double getTariffSurChargeAds() {
		return tariffSurChargeAds;
	}
	public void setTariffSurChargeAds(Double tariffSurChargeAds) {
		this.tariffSurChargeAds = tariffSurChargeAds;
	}
	public Double getTariffSurChargeOpco() {
		return tariffSurChargeOpco;
	}
	public void setTariffSurChargeOpco(Double tariffSurChargeOpco) {
		this.tariffSurChargeOpco = tariffSurChargeOpco;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
