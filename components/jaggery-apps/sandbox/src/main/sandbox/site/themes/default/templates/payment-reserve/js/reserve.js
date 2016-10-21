
$(document).ready(function(){
	$("#payment-add-button").click(function (){
		performTask();
	});
});

function performTask(){
	
	var clientCorrelator = $("#clientCorrelator").val();
	var endUserId = $("#endUsertp").val();
	var referenceCode = $("#referenceCode").val();
	var description = $("#description").val();
	var currency = $("#currency").val();
	var amount = $("#amount").val();
	var transactionOperationStatus = $("#transactionOperationStatus").val();
	
	var callbackData = $("#callbackData").val();
	var channel = $("#channel").val();
	var mandateId = $("#mandateId").val();
	var notificationFormat = $("#notificationFormat").val();
	var notifyURL = $("#notifyURL").val();
	var onBehalfOf = $("#onBehalfOf").val();
	var productId = $("#productId").val();
	var purchaseCategoryCode = $("#purchaseCategoryCode").val();
	var referenceSequence = $("#referenceSequence").val();
	var serviceID = $("#serviceID").val();
	var taxAmount = $("#taxAmount").val();
	var amountReserved = $("#amountReserved").val();
	var totalAmountCharged = $("#totalAmountCharged").val();
	
	var action = "getReserveResponse";
	
	jagg.post("/site/blocks/payment-reserve/ajax/reserve.jag", {
		action : action,
		
		clientCorrelator:clientCorrelator,
		endUserId: endUserId,
		referenceCode: referenceCode,
		description: description,
		currency: currency,
		amount: amount,
		transactionOperationStatus: transactionOperationStatus,
		
		callbackData: callbackData,
		channel: channel,
		mandateId: mandateId,
		notificationFormat: notificationFormat,
		notifyURL: notifyURL,
		onBehalfOf: onBehalfOf,
		productId: productId,
		purchaseCategoryCode: purchaseCategoryCode,
		referenceSequence: referenceSequence,
		serviceID: serviceID,
		taxAmount: taxAmount,
		totalAmountCharged: totalAmountCharged,
		amountReserved: amountReserved
		
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