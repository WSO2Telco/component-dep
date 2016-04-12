
$(document).ready(function(){
	$("#payment-add-button").click(function (){
		performTask();
	});
});

function performTask(){
	var endUserId = $("#endUsertp").val();
	var transactionOperationStatus = $("#transactionOperationStatus").val();
	var referenceSequence = $("#referenceSequence").val();
	var transactionId = $("#transactionId").val();
	
	var action = "getReleaseReservationResponse";
	
	jagg.post("/site/blocks/payment-release-res/ajax/release-reserved.jag", {
		action : action,
		
		endUserId : endUserId,
		transactionOperationStatus : transactionOperationStatus,
		referenceSequence : referenceSequence,
		transactionId:transactionId
		
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