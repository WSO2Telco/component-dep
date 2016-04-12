$(document).ready(function() {
	setTablePagination(0);
	getLocationSessionData();
});

$("#altitude").keyup(function(event) {

	var inputVal = $("#altitude").val();
	var numericReg = /^[0-9]{0,10}(\.[0-9]{0,2})?$/;
	if (!numericReg.test(inputVal)) {
		event.preventDefault();
		$("#altitude").val(inputVal.substr(0, inputVal.length - 1));
		return false;
	}
});

$("#latitude").keyup(function(event) {

	var inputVal = $("#latitude").val();
	var numericReg = /^[0-9]{0,10}(\.[0-9]{0,2})?$/;
	if (!numericReg.test(inputVal)) {
		event.preventDefault();
		$("#latitude").val(inputVal.substr(0, inputVal.length - 1));
		return false;
	}
});

$("#longitude").keyup(function(event) {

	var inputVal = $("#longitude").val();
	var numericReg = /^[0-9]{0,10}(\.[0-9]{0,2})?$/;
	if (!numericReg.test(inputVal)) {
		event.preventDefault();
		$("#longitude").val(inputVal.substr(0, inputVal.length - 1));
		return false;
	}
});

function getLocationSessionData() {
	var action = "getSessionData";
	jagg.post("/site/blocks/location/ajax/location-track.jag", {
		action : action
	}, function(result) {
		if (result.table != "null") {
			$("#location_table_content").empty();
			createLocationTable(result.table);
			setTablePagination(0);
		}
	}, "json");
};

function sendlocationRequest() {
	var address = $("#address").val();
	var requestedAccuracy = $("#requestedAccuracy").val();
	// alert(requestedAccuracy);
	saveLocationRequest(address, requestedAccuracy);
};

function saveLocationRequest(address, requestedAccuracy) {
	var action = "saveLocationRequest";
	jagg.post("/site/blocks/location/ajax/location-track.jag", {
		action : action,
		address : address,
		requestedAccuracy : requestedAccuracy
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				$('#json-response').val(result.data);
				$("#location_table_content").empty();
				createLocationTable(result.table);
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

function createLocationTable(table) {
	var locTable = $('<table></table>').attr({
		id : "loc_request_table"
	}).attr({
		class : "table table-bordered table-striped"
	});
	var hRow = $('<tr></tr>').appendTo(locTable);
	$('<th></th>').text("Transaction Id").appendTo(hRow);
	$('<th></th>').text("Address").appendTo(hRow);
	$('<th></th>').text("Requested Accuracy").appendTo(hRow);

	for (var i = 0; i < table.length; i++) {
		var bRow = $('<tr></tr>').appendTo(locTable);
		$('<td></td>').text(i + 1).appendTo(bRow);
		$('<td></td>').text(table[i].address).appendTo(bRow);
		$('<td></td>').text(table[i].requestedAccuracy).appendTo(bRow);
	}
	locTable.appendTo("#location_table_content");
}

function performSettingsAction() {
	var action = "performSettingsAction";
	var alt = $("#altitude").val();
	var lat = $("#latitude").val();
	var longi = $("#longitude").val();
	var state = $("#retState").val();
	
	if(alt == null || alt == ""){
		alt = 0.0;
	}
	
	if(lat == null || lat == ""){
		lat = 0.0;
	}
	
	if(longi == null || longi == ""){
		longi = 0.0;
	}

	jagg.post("/site/blocks/location/ajax/location-track.jag", {
		action : action,
		altitude : alt,
		latitude : lat,
		longitude : longi,
		lbsStatus : state
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				$('#lbsAddMessage').show();
				$('#lbsAddMessage').delay(4000).hide('fast');
				location.reload();
			} else {
				$('#lbsErrorMessage').show();
				$('#lbsErrorMessage').delay(4000).hide('fast');
			}
			setTablePagination(0);
		} else {
			$('#lbsErrorMessage').show();
			$('#lbsErrorMessage').delay(4000).hide('fast');
		}
	}, "json");
};

function setTablePagination(pageNumber) {
	paginator(pageNumber);
}

function paginator(pageNumber) {
	var rows = $("#loc_request_table tbody tr").length;
	var rowsPerPage = 10;
	if (rows > rowsPerPage) {
		var numberOfPages = Math.ceil(rows / rowsPerPage);
		var currentPageStart = pageNumber * rowsPerPage;
		var currentPageEnd = (pageNumber * rowsPerPage) + rowsPerPage;
		for (var i = 0; i < rows; i++) {
			if ((currentPageStart <= i) & (i < currentPageEnd)) {
				$("#loc_request_table tr").eq(i).show();
				// alert(i);
			} else {
				$("#loc_request_table tbody tr").eq(i).hide();
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