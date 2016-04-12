
$(document).ready(function(){
	$("#payment-add-button").click(function (){
		performTask();
	});
});

function performTask(){
	
	var endUserId = $("#endUsertp").val();
	var referenceCode = $("#referenceCode").val();
	var description = $("#description").val();
	var currency = $("#currency").val();
	var amount = $("#amount").val();
	var transactionId = $("#transactionId").val();
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
	
	var action = "getChargeReservationResponse";
	
	jagg.post("/site/blocks/payment-charge-reservation/ajax/charge-reservation.jag", {
		action : action,
		
		endUserId: endUserId,
		referenceCode: referenceCode,
		description: description,
		currency: currency,
		amount: amount,
		transactionId:transactionId,
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