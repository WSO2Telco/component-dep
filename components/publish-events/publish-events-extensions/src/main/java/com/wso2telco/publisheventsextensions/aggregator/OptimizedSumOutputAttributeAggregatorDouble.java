/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package com.axiata.dialog.mife.events.extensions.aggregator;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.query.selector.attribute.handler.OutputAttributeAggregator;
import org.wso2.siddhi.query.api.definition.Attribute;

public class OptimizedSumOutputAttributeAggregatorDouble implements OutputAttributeAggregator {

    static final Logger log = Logger.getLogger(OptimizedSumOutputAttributeAggregatorDouble.class);
    private double value = 0.0;
    private static final Attribute.Type type = Attribute.Type.DOUBLE;

    public Attribute.Type getReturnType() {
        return type;
    }

    @Override
    public Object processAdd(Object obj) {

        if (obj instanceof Object[]) {
            Object[] objArray = (Object[]) obj;
            String paymentType = (String) objArray[2];
            boolean reset = (Boolean) objArray[1];

                if (reset) {
                    value = 0.0;
                } else if (paymentType.equalsIgnoreCase("Charged")) {
                    value += (Double) objArray[0];
                } else if (paymentType.equalsIgnoreCase("Refund")){
                    value -= (Double) objArray[0];
                }
        }

        log.info(value);
        return value;
    }

    @Override
    public Object processRemove(Object obj) {
        value -= (Double) obj;
        return value;
    }

    @Override
    public OutputAttributeAggregator newInstance() {
        return new OptimizedSumOutputAttributeAggregatorDouble();
    }

    @Override
    public void destroy(){

    }
}
