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

package com.wso2telco.dep.mediator.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DLimit")
public class DailyLimit {

	private String application;
    private String consumerKey;
    private Double dayAmount;
    private Double monthAmount;
	private String operator;

    public Double getMonthAmount() {
        return monthAmount;
    }

    @XmlElement(name="MonthAmount")
    public void setMonthAmount(Double monthAmount) {this.monthAmount = monthAmount;}



	public String getApplication() {
		return application;
	}

	@XmlElement(name = "Application")
	public void setApplication(String application) {this.application = application;}

	public Double getAmount() {
		return dayAmount;
	}

	@XmlElement(name = "DayAmount")
	public void setAmount(Double amount) {this.dayAmount = amount;}

	public String getOperator() {
		return operator;
	}

	@XmlElement(name = "Operator")
	public void setOperator(String operator) {this.operator = operator;}

    @XmlElement(name = "ConsumerKey")
    public  void setConsumerKey(String consumerKey) {this.consumerKey = consumerKey; }

    public String getConsumerKey() {
        return consumerKey;
    }
}

