$(document).ready(function() {
	setSenderAddressesToCombo();
});

function sendDeliveryStateRequest(){
	var senderAddress = $("#sender-address").val().replace(/\s/g, '');;
	var requestId = $("#requestId").val().replace(/\s/g, '');;
	
	//alert(senderAddress+" :: "+requestId);
	
	var action = "getDeliveryStateResponse";
	
	jagg.post("/site/blocks/sms-delivery-notification/ajax/sms-delivery-notification.jag", {
		action : action,
		senderAddress : senderAddress,
		requestId : requestId
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				//location.reload();
				$('#json-response').val(result.data);
			} else {
				
			}
			//setTablePagination(0);
		} else {
			alert("Error");
		}
	}, "json");
}


function setSenderAddressesToCombo() {
	var action = "getSenderAddresses";
	jagg.post("/site/blocks/sms-delivery-notification/ajax/sms-delivery-notification.jag", {
		action : action
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				var nubmers = result.data.replace(/\[/, "").replace(/\]/, "").split(",");
				//alert(result.data);
				fillSenderAddressesCombo(nubmers);
			} else {
				jagg.showLogin();
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
};

function fillSenderAddressesCombo(numbers) {
	var option = '';
	var i;
	for (i = 0; i < numbers.length-1; i++) {
		option += '<option value="' + numbers[i].replace(/\"/, "").replace(/\"/, "") + '">' + numbers[i].replace(/\"/, "").replace(/\"/, "")
				+ '</option>';
	}
	$('#sender-address').append(option);
}