$(document).ready(function() {
	setSenderAddressesToCombo();
	setTablePagination(0);
});

function smsRetrieveRequest() {
	var registrationId = $("#registration-id").val();
	var maxBatchSize = $("#max-batch-size").val();
	//alert(registrationId + "  " + maxBatchSize);
	setSMSRetrieveResponse(registrationId, maxBatchSize);
}

function setSMSRetrieveResponse(registrationId, maxBatchSize) {
	var action = "getSMSRetrieveResponse";
	//alert(senderAddress);
	jagg.post("/site/blocks/sms-retrieve/ajax/sms-retrieve.jag", {
		action : action,
		registrationId : registrationId,
		maxBatchSize : maxBatchSize,
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				$('#json-response').val(result.data);
				$('#footer_separator').show();
				$('#retrieve_messages_table_content').empty();
				createRetrieveMessagesTable(result.data);
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
}

function setSenderAddressesToCombo() {
	var action = "getRegistrationIds";
	jagg.post("/site/blocks/sms-retrieve/ajax/sms-retrieve.jag", {
		action : action
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				var nubmers = result.data.replace(/\[/, "").replace(/\]/, "")
						.split(",");
				fillCombo(nubmers);
			} else {
				jagg.showLogin();
			}
		} else {
			jagg.showLogin();
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
	$('#registration-id').append(option);
}

function createRetrieveMessagesTable(result){
	var responseJsonBody = JSON.parse(result);
	var retMsgArray = new Array();
	retMsgArray = responseJsonBody.inboundSMSMessageList.inboundSMSMessage;
	var retMsgTable = $('<table></table>').attr({ id: "retrieve_messages_table"}).attr({class: "table table-bordered table-striped"});
	var hRow = $('<tr></tr>').appendTo(retMsgTable);
	$('<th></th>').text("Transaction Id").appendTo(hRow);
	$('<th></th>').text("Destination Address").appendTo(hRow); 
	$('<th></th>').text("Sender Address").appendTo(hRow); 
	$('<th></th>').text("Message Id").appendTo(hRow); 
	$('<th></th>').text("Resource URL").appendTo(hRow); 
	$('<th></th>').text("Message").appendTo(hRow);
	$('<th></th>').text("Date Time").appendTo(hRow);
	
	for(var i = 0; i < retMsgArray.length; i++){
		var bRow = $('<tr></tr>').appendTo(retMsgTable);
		$('<td></td>').text(i+1).appendTo(bRow);
		$('<td></td>').text(retMsgArray[i].destinationAddress).appendTo(bRow); 
		$('<td></td>').text(retMsgArray[i].senderAddress).appendTo(bRow); 
		$('<td></td>').text(retMsgArray[i].messageId).appendTo(bRow); 
		$('<td></td>').text(retMsgArray[i].resourceURL).appendTo(bRow); 
		$('<td></td>').text(retMsgArray[i].message).appendTo(bRow);
		$('<td></td>').text(retMsgArray[i].dateTime).appendTo(bRow);
	}
	retMsgTable.appendTo("#retrieve_messages_table_content");
}

function setTablePagination(pageNumber) {
	paginator(pageNumber);
}

function paginator(pageNumber) {
	var rows = $("#retrieve_messages_table tbody tr").length;
	var rowsPerPage = 10;
	if (rows > rowsPerPage) {
		var numberOfPages = Math.ceil(rows / rowsPerPage);
		var currentPageStart = pageNumber * rowsPerPage;
		var currentPageEnd = (pageNumber * rowsPerPage) + rowsPerPage;
		for (var i = 0; i < rows; i++) {
			if ((currentPageStart <= i) & (i < currentPageEnd)) {
				$("#retrieve_messages_table tr").eq(i).show();
				// alert(i);
			} else {
				$("#retrieve_messages_table tbody tr").eq(i).hide();
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
