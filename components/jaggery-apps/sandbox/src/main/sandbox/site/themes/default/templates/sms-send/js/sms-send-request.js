$(document).ready(function() {
	setSenderAddressesToCombo();
	setTablePagination(0);
});

function smsSendRequest() {
	var senderAddress = $("#sender-address").val();
	var message = $("#message").val();
	var address0 = $("#address-0").val();
	var address1 = $("#address-1").val();
	var address2 = $("#address-2").val();
	var address3 = $("#address-3").val();
	var address4 = $("#address-4").val();
	var clientCorrelator = $("#client-correlator").val();
	var senderName = $("#sender-name").val();
	var notifyURL = $("#notify-url").val();
	var callbackData = $("#callback-data").val();

	setSMSSendResponse(senderAddress, address0, address1, address2, address3,
			address4, message, clientCorrelator, senderName, notifyURL,
			callbackData);
}

function setSMSSendResponse(senderAddress, address0, address1, address2,
		address3, address4, message, clientCorrelator, senderName, notifyURL,
		callbackData) {
	var action = "getSMSSendResponse";
	//alert(senderAddress);
	jagg.post("/site/blocks/sms-send/ajax/sms-send.jag", {
		action : action,
		senderAddress : senderAddress,
		address0 : address0,
		address1 : address1,
		address2 : address2,
		address3 : address3,
		address4 : address4,
		message : message,
		clientCorrelator : clientCorrelator,
		senderName : senderName,
		notifyURL : notifyURL,
		callbackData : callbackData
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				location.reload();
				$('#json-response').val(result.data);
			} else {

			}
			setTablePagination(0);
		} else {
			alert("Error");
		}
	}, "json");
}

function setSenderAddressesToCombo() {
	var action = "getSenderAddresses";
	jagg.post("/site/blocks/sms-send/ajax/sms-send.jag", {
		action : action
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				var nubmers = result.data.replace(/\[/, "").replace(/\]/, "")
						.split(",");
				fillCombo(nubmers);
			} else {

			}
		} else {
			alert("Error");
		}
	}, "json");
}

function fillCombo(numbers) {
	var option = '';
	var i;
	for (i = 0; i < numbers.length-1; i++) {
		option += '<option value="' + numbers[i].replace(/\"/, "").replace(/\"/, "") + '">' + numbers[i].replace(/\"/, "").replace(/\"/, "")
				+ '</option>';
	}
	$('#sender-address').append(option);
}

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
