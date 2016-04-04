/*
 * GsmaValidater.java
 * Aug 1, 2014  11:27:18 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.oneapivalidation.impl;

import com.wso2telco.oneapivalidation.AxiataException;
import com.wso2telco.oneapivalidation.IServiceValidate;
import com.wso2telco.oneapivalidation.ReadPropertyFile;
import com.wso2telco.oneapivalidation.ResourceURLUtil;

/**
 * <TO-DO>
 * <code>GsmaValidater</code>
 *
 * @version $Id: GsmaValidater.java,v 1.00.000
 */
public class GsmaValidater implements IServiceValidate {

    public void validate(String json) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validateUrl(String pathInfo) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validate(String[] params) throws AxiataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
