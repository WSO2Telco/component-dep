
$(document).ready(function(){
	$("#payment-add-button").click(function (){
		performTask();
	});
});

function performTask(){
	
	var endUserId = $("#endUsertp").val();
	var description = $("#description").val();
	var referenceCode = $("#referenceCode").val();
	var transactionOperationStatus = $("#transactionOperationStatus").val();

	var amount = $("#amount").val();
	var callbackData = $("#callbackData").val();
	var channel = $("#channel").val();
	var clientCorrelator = $("#clientCorrelator").val();
	var code = $("#code").val();
	var currency = $("#currency").val();
	var mandateId = $("#mandateId").val();
	var notificationFormat = $("#notificationFormat").val();
	var notifyURL = $("#notifyURL").val();
	var onBehalfOf = $("#onBehalfOf").val();
	var originalServerRef = $("#originalServerRef").val();
	var productId = $("#productId").val();
	var purchaseCategoryCode = $("#purchaseCategoryCode").val();
	var serviceID = $("#serviceID").val();
	var taxAmount = $("#taxAmount").val();
	
	var action = "getRefundResponse";
	
	jagg.post("/site/blocks/payment-refund/ajax/refund.jag", {
		action : action,
		
		endUserId: endUserId,
		description: description,
		referenceCode: referenceCode,
		transactionOperationStatus: transactionOperationStatus,
		
		amount: amount,
		callbackData: callbackData,
		channel: channel,
		clientCorrelator: clientCorrelator,
		code: code,
		currency: currency,
		mandateId: mandateId,
		notificationFormat: notificationFormat,
		notifyURL: notifyURL,
		onBehalfOf: onBehalfOf,
		originalServerRef:originalServerRef,
		productId: productId,
		purchaseCategoryCode: purchaseCategoryCode,
		serviceID: serviceID,
		taxAmount: taxAmount
		
	}, function(result) {
		
		if (!result.error) {
			if (result.requestData != "null") {
				//location.reload();
				$('#request').val(result.requestData);
				$('#response').val(result.data);
			} else {
				
			}
			//setTablePagination(0);
		} else {
			alert("Error");
		}
	}, "json");
	
}