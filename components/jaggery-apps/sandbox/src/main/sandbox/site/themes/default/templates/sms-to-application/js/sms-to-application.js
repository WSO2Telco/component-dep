$(document).ready(function() {
	setSenderAddressesToCombo();
	setDestinationAddressesToCombo();
	setTablePagination(0);
});

function sendSMSToApplication() {
	var senderAddress = $("#sender-address").val();
	var destinationAddress = $("#destination-address").val();
	var message = $("#message").val();
	
	saveSendSMSToApplication(senderAddress, destinationAddress, message);
};

function saveSendSMSToApplication(senderAddress, destinationAddress, message) {
	var action = "saveSendSMSToApplication";
	jagg.post("/site/blocks/sms-to-application/ajax/sms-to-application.jag", {
		action : action,
		senderAddress : senderAddress,
		destinationAddress : destinationAddress,
		message : message
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				location.reload();
			} else {
				$('#errorMessage').show();
				$('#errorMessage').delay(4000).hide('fast');
			}
			setTablePagination(0);
		} else {
			$('#errorMessage').show();
			$('#errorMessage').delay(4000).hide('fast');
		}
	}, "json");
};

function setSenderAddressesToCombo() {
	var action = "getSenderAddresses";
	jagg.post("/site/blocks/sms-to-application/ajax/sms-to-application.jag", {
		action : action
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				var nubmers = result.data.replace(/\[/, "").replace(/\]/, "")
						.split(",");
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
};

function setDestinationAddressesToCombo() {
	var action = "getDestinationAddresses";
	jagg.post("/site/blocks/sms-to-application/ajax/sms-to-application.jag", {
		action : action
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				var nubmers = result.data.replace(/\[/, "").replace(/\]/, "")
						.split(",");
				fillDestinationAddressesCombo(nubmers);
			} else {
				jagg.showLogin();
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
};

function fillDestinationAddressesCombo(numbers) {
	var option = '';
	var i;
	for (i = 0; i < numbers.length-1; i++) {
		option += '<option value="' + numbers[i].replace(/\"/, "").replace(/\"/, "") + '">' + numbers[i].replace(/\"/, "").replace(/\"/, "")
				+ '</option>';
	}
	$('#destination-address').append(option);
};

function setTablePagination(pageNumber) {
	paginator(pageNumber);
}

function paginator(pageNumber) {
	var rows = $("#sms_send_request_table tbody tr").length;
	var rowsPerPage = 10;
	if (rows > rowsPerPage) {
		var numberOfPages = Math.ceil(rows / rowsPerPage);
		var currentPageStart = pageNumber * rowsPerPage;
		var currentPageEnd = (pageNumber * rowsPerPage) + rowsPerPage;
		for (var i = 0; i < rows; i++) {
			if ((currentPageStart <= i) & (i < currentPageEnd)) {
				$("#sms_send_request_table tbody tr").eq(i).show();
				// alert(i);
			} else {
				$("#sms_send_request_table tbody tr").eq(i).hide();
			}
		}
		// alert("PAGENUMBER: "+pageNumber+"\nRows: "+rows+"\nRowsPP:
		// "+rowsPerPage+"\nPAGES: "+numberOfPages+"\nSTART:
		// "+currentPageStart+"\nEND: "+currentPageEnd);
		loadPaginatorView(numberOfPages, pageNumber);
	} else {
		$(".pagination").html('');
	}
}

function loadPaginatorView(numberOfPages, currentPage) {
	$(".pagination").html('<ul></ul>');
	var previousAppender = '<li><a href="javascript:paginator(0)"><<</a></li>';
	if (currentPage == 0) {
		previousAppender = '<li class="disabled"><a><<</a></li>';
	}
	$(".pagination ul").append(previousAppender);
	for (var i = 0; i < numberOfPages; i++) {
		var currentRow;
		var rowSticker = i + 1;
		if (i == currentPage) {
			currentRow = '<li class="active"><a>' + rowSticker + '</a></li>';
		} else {
			currentRow = '<li><a href="javascript:paginator(' + i + ')">'
					+ rowSticker + '</a></li>';
		}
		$(".pagination ul").append(currentRow);
	}
	var lastPage = numberOfPages - 1;// alert(lastPage);
	var postAppender = '<li><a href="javascript:paginator(' + lastPage
			+ ')">>></a></li>';
	if (currentPage == lastPage) {
		postAppender = '<li class="disabled"><a>>></a></li>';
	}
	$(".pagination ul").append(postAppender);
}

