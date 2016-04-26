package org.gsm.oneapi.endpoints;

import org.apache.log4j.Logger;

public class AeoponaSandbox extends ServiceEndpoints {
	static Logger logger=Logger.getLogger(AeoponaSandbox.class);
	
	public static final AeoponaSandbox INSTANCE=new AeoponaSandbox();
	
	ServiceEndpoints endpoints=null;
	
	public AeoponaSandbox () {
		endpoints=new ServiceEndpoints();
		
		endpoints.AmountCharge="/mifesandbox/AmountChargingService/OneAPI_REST_v1_0/sandbox/1/payment/{endUserId}/transactions/amount";
		endpoints.AmountRefund="/mifesandbox/AmountChargingService/OneAPI_REST_v1_0/sandbox/1/payment/{endUserId}/transactions/amount";
		endpoints.AmountReserve="/mifesandbox/ReserveAmountService/OneAPI_REST_v1_0/sandbox/1/payment/{endUserId}/transactions/amountReservation";
		endpoints.AmountReserveAdditional="/mifesandbox/ReserveAmountService/OneAPI_REST_v1_0/sandbox/1/payment/{endUserId}/transactions/amountReservation/${transactionId}";
		endpoints.AmountReservationCharge="/mifesandbox/ReserveAmountService/OneAPI_REST_v1_0/sandbox/1/payment/{endUserId}/transactions/amountReservation/${transactionId}";
		endpoints.AmountReservationRelease="/mifesandbox/ReserveAmountService/OneAPI_REST_v1_0/sandbox/1/payment/{endUserId}/transactions/amountReservation/${transactionId}";
		
		endpoints.Location="/mifesandbox/TerminalLocationService/OneAPI_REST_v1_0/sandbox/1/location/queries/location";
		
		endpoints.SendSMS="/mifesandbox/SendSmsService/OneAPI_REST_v1_0/sandbox/1/smsmessaging/outbound/{senderAddress}/requests";
		endpoints.QuerySMSDelivery="/mifesandbox/SendSmsService/OneAPI_REST_v1_0/sandbox/1/smsmessaging/outbound/{senderAddress}/requests/{requestId}/deliveryInfos";
		endpoints.SMSDeliverySubscriptions="/mifesandbox/SmsNotificationManagerService/OneAPI_REST_v1_0/sandbox/1/smsmessaging/outbound/{senderAddress}/subscriptions";
		endpoints.CancelSMSDeliverySubscription="/mifesandbox/SmsNotificationManagerService/OneAPI_REST_v1_0/sandbox/1/smsmessaging/outbound/subscriptions/{subscriptionId}";
		endpoints.RetrieveSMS="/mifesandbox/ReceiveSmsService/OneAPI_REST_v1_0/sandbox/1/smsmessaging/inbound/registrations/{registrationId}/messages/?maxBatchSize={maxBatchSize}";
		endpoints.SMSReceiptSubscriptions="/mifesandbox/SmsNotificationManagerService/OneAPI_REST_v1_0/sandbox/1/smsmessaging/inbound/subscriptions";
		endpoints.CancelSMSReceiptSubscription="/mifesandbox/SmsNotificationManagerService/OneAPI_REST_v1_0/sandbox/1/smsmessaging/inbound/subscriptions/{subscriptionId}";

	}
	
	public ServiceEndpoints getEndpoints() {
		return INSTANCE.endpoints;
	}

}
