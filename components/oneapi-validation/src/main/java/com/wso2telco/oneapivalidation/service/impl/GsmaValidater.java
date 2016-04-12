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
package com.wso2telco.oneapivalidation.service.impl;

import com.wso2telco.oneapivalidation.exceptions.CustomException;
import com.wso2telco.oneapivalidation.service.IServiceValidate;
import com.wso2telco.oneapivalidation.service.impl.location.ValidateLocation;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateChargeReservation;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateListTransactions;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidatePaymentCharge;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateRefund;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateReleaseReservation;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateReserveAdditional;
import com.wso2telco.oneapivalidation.service.impl.payment.ValidateReserveAmount;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateCancelDeliverySubs;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateCancelSubscription;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateDeliveryStatus;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateDeliverySubscription;
import com.wso2telco.oneapivalidation.service.impl.sms.ValidateSendSms;
import com.wso2telco.oneapivalidation.service.impl.sms.nb.ValidateNBRetrieveSms;
import com.wso2telco.oneapivalidation.service.impl.sms.nb.ValidateNBSubscription;
import com.wso2telco.oneapivalidation.service.impl.sms.sb.ValidateSBRetrieveSms;
import com.wso2telco.oneapivalidation.service.impl.ussd.ValidateUssdCancelSubscription;
import com.wso2telco.oneapivalidation.service.impl.ussd.ValidateUssdSend;
import com.wso2telco.oneapivalidation.service.impl.ussd.ValidateUssdSubscription;
import com.wso2telco.oneapivalidation.util.ReadPropertyFile;
import com.wso2telco.oneapivalidation.util.ResourceURLUtil;
 
// TODO: Auto-generated Javadoc
/**
 * The Class GsmaValidater.
 */
public class GsmaValidater implements IServiceValidate {

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validateUrl(java.lang.String)
     */
    public void validateUrl(String pathInfo) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Validate.
     *
     * @param reqUrl the req url
     * @param reqPayload the req payload
     * @param httpmethod the httpmethod
     * @param params the params
     * @throws Exception the exception
     */
    public void validate(String reqUrl, String reqPayload, String httpmethod, String[] params) throws Exception {

        String apitype = new ResourceURLUtil().getAPIType(reqUrl, reqPayload);
        ReadPropertyFile property = new ReadPropertyFile();
        if (apitype.equalsIgnoreCase("send_sms")) {
            if (property.getApiTypeAvailability("sms.send_sms")) {
                ValidateSendSms validator = new ValidateSendSms();
                validator.validateUrl(reqUrl);
                validator.validate(reqPayload);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("query_sms_delivery")) {
            if (property.getApiTypeAvailability("sms.query_sms_delivery")) {
                ValidateDeliveryStatus val = new ValidateDeliveryStatus();
                val.validateUrl(reqUrl);
                val.validate(params);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("start_delivery_subscription")) {
            if (property.getApiTypeAvailability("sms.start_delivery_subscription")) {
                ValidateDeliverySubscription val = new ValidateDeliverySubscription();
                val.validateUrl(reqUrl);
                val.validate(reqPayload);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("stop_delivery_subscription")) {
            if (property.getApiTypeAvailability("sms.stop_delivery_subscription")) {
                ValidateCancelDeliverySubs val = new ValidateCancelDeliverySubs();
                val.validateUrl(reqUrl);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("retrive_sms_subscriptions")) {
            if (httpmethod.equalsIgnoreCase("DELETE")) {
                if (property.getApiTypeAvailability("sms.cancel_retrive_sms_subscriptions")) {
                    ValidateCancelSubscription validator = new ValidateCancelSubscription();
                    validator.validateUrl(reqUrl);
                    validator.validate(params);
                } else {
                    throw new Exception("API Type Not Supported");
                }
            } else {
                if (property.getApiTypeAvailability("sms.retrive_sms_subscriptions")) {
                    ValidateNBSubscription validator = new ValidateNBSubscription();
                    validator.validateUrl(reqUrl);
                    validator.validate(reqPayload);
                } else {
                    throw new Exception("API Type Not Supported");
                }
            }
        } else if (apitype.equalsIgnoreCase("retrive_sms")) {
            if (httpmethod.equalsIgnoreCase("GET")) {
                ValidateSBRetrieveSms validator = new ValidateSBRetrieveSms();
                validator.validateUrl(reqUrl);
                validator.validate(params);
            } else {
                ValidateNBRetrieveSms validator = new ValidateNBRetrieveSms();
                validator.validateUrl(reqUrl);
                validator.validate(params);
            }
        } else if (apitype.equalsIgnoreCase("charge")) {
            if (property.getApiTypeAvailability("payment.charge")) {
                ValidatePaymentCharge validator = new ValidatePaymentCharge();
                validator.validateUrl(reqUrl);
                validator.validate(reqPayload);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("refund")) {
            if (property.getApiTypeAvailability("payment.refund")) {
                ValidateRefund val = new ValidateRefund();
                val.validate(reqPayload);
                val.validateUrl(reqUrl);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("reserve_amount")) {
            if (property.getApiTypeAvailability("payment.reserve_amount")) {
                ValidateReserveAmount val = new ValidateReserveAmount();
                val.validate(reqPayload);
                val.validateUrl(reqUrl);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("reserve_additional_amount")) {
            if (property.getApiTypeAvailability("payment.reserve_additional_amount")) {
                ValidateReserveAdditional val = new ValidateReserveAdditional();
                val.validate(reqPayload);
                //val.validate(params);
                val.validateUrl(reqUrl);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("charge_against_reservation")) {
            if (property.getApiTypeAvailability("payment.charge_against_reservation")) {
                ValidateChargeReservation val = new ValidateChargeReservation();
                val.validate(reqPayload);
                val.validateUrl(reqUrl);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("release_reservation")) {
            if (property.getApiTypeAvailability("payment.release_reservation")) {
                ValidateReleaseReservation val = new ValidateReleaseReservation();
                val.validate(reqPayload);
                val.validateUrl(reqUrl);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("list_transactions")) {
            if (property.getApiTypeAvailability("payment.list_transactions")) {
                ValidateListTransactions val = new ValidateListTransactions();
                val.validateUrl(reqUrl);
                val.validate(params);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("location")) {
            if (property.getApiTypeAvailability("lbs.location")) {
                ValidateLocation validator = new ValidateLocation();
                validator.validateUrl(reqUrl);
                validator.validate(params);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("ussd_send")) {
            if (property.getApiTypeAvailability("ussd.ussd_send")) {
                ValidateUssdSend v = new ValidateUssdSend();
                v.validateUrl(reqUrl);
                v.validate(reqPayload);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("ussd_subscription")) {
            if (property.getApiTypeAvailability("ussd.ussd_subscription")) {
                ValidateUssdSubscription v = new ValidateUssdSubscription();
                v.validateUrl(reqUrl);
                v.validate(reqPayload);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else if (apitype.equalsIgnoreCase("stop_ussd_subscription")) {
            if (property.getApiTypeAvailability("ussd.stop_ussd_subscription")) {
                ValidateUssdCancelSubscription v = new ValidateUssdCancelSubscription();
                v.validateUrl(reqUrl);
            } else {
                throw new Exception("API Type Not Supported");
            }
        } else {
            throw new Exception("API Type Not found");
        }
    }
}
