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

package com.wso2telco.dep.mediator.internal;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum PaymentType {

    REFUND("Refunded"),CHARGED("Charged"),UNDEFINED("undefined");

    private String type;
    private PaymentType(String code){
        this.type = code;
    }

    public String getType() {
        return type;
    }

    static Map<String,PaymentType> valueMap= new HashMap<String,PaymentType>();
    static {
        Iterator<PaymentType> enumTy = EnumSet.allOf(PaymentType.class).iterator();
        while(enumTy.hasNext()){
            PaymentType paymentType = enumTy.next();
            valueMap.put(paymentType.getType(), paymentType);
        }
    }

    public static PaymentType get(final String code){
        PaymentType retType =valueMap.get(code);
        if(retType==null){
            retType =PaymentType.UNDEFINED;
        }
        return retType;
    }

}
