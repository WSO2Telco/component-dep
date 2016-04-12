$(document).ready(function() {
	setTablePagination(0);
	getLocationSessionData();
});

function getLocationSessionData(){
	var action = "getSessionData";
	jagg.post("/site/blocks/mobile-id-task/ajax/mobile-id-api.jag", {
		action : action
	}, function(result) {
			if (result.table != "null") {
				$( "#mobileidapi_table_content" ).empty();
				createMobileIdApiTable(result.table);
				setTablePagination(0);
			}
	}, "json");
};

function sendMobileIdApiRequest() {
	var authorization= $("#authorization").val();
	authorization = authorization.replace(/\s/g, '');
	sendMobileIdApiRequestWithToken(authorization);
};

function saveMobileIdApiRequest(authorization) {
	var action = "saveMobileIdApiRequest";
	
	var sub = $("#sub").val();
	var email = $("#email").val();
	var name = $("#name").val();
	var family_name = $("#family_name").val();
	var preferred_username = $("#preferred_username").val();
	var given_name = $("#given_name").val();
	
	jagg.post("/site/blocks/mobile-id-task/ajax/mobile-id-api.jag", {
		action : action,
		
		sub : sub,
		email : email,
		name : name,
		family_name : family_name,
		preferred_username : preferred_username,
		given_name : given_name
		
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				$('#lbsAddMessage').show();
				$('#lbsAddMessage').delay(4000).hide('fast');
				window.location.reload();
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


function sendMobileIdApiRequestWithToken(authorization) {
	var action = "sendMobileIdApiRequestWithToken";
	
	jagg.post("/site/blocks/mobile-id-task/ajax/mobile-id-api.jag", {
		action : action,
		authorization : authorization,
		
	}, function(result) {
		if (!result.error) {
			if (result.data != "null") {
				$('#json-response').val(result.data);
				$( "#mobileidapi_table_content" ).empty();
				//createMobileIdApiTable(result.table);
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


function createMobileIdApiTable(table){
	var mobileIdApiTable = $('<table></table>').attr({ id: "loc_request_table"}).attr({class: "table table-bordered table-striped"});
	var hRow = $('<tr></tr>').appendTo(mobileIdApiTable);
	$('<th></th>').text("Transaction Id").appendTo(hRow);
	$('<th></th>').text("authorization").appendTo(hRow); 
	//$('<th></th>').text("Requested Accuracy").appendTo(hRow); 
		
	for(var i = 0; i < table.length; i++){
		
		var bRow = $('<tr></tr>').appendTo(mobileIdApiTable);
		$('<td></td>').text(i+1).appendTo(bRow);
		//$('<td></td>').text(table[i].authorization).appendTo(bRow); 
		//$('<td></td>').text(table[i].requestedAccuracy).appendTo(bRow); 
	}
	mobileIdApiTable.appendTo("#mobileidapi_table_content");
}

function performSettingsAction() {
	var action = "performSettingsAction";
	var alt = $("#altitude").val();
	var lat = $("#latitude").val();
	var longi = $("#longitude").val();
	var state = $("#retState").val();

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
			} else {
				$("#loc_request_table tbody tr").eq(i).hide();
			}
		}
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
			currentRow = '<li><a href="javascript:paginator(' + i + ')">' + rowSticker + '</a></li>';
		}
		$(".pagination ul").append(currentRow);
	}
	var lastPage = numberOfPages - 1;
	var postAppender = '<li><a href="javascript:paginator(' + lastPage+ ')">>></a></li>';
	if (currentPage == lastPage) {
		postAppender = '<li class="disabled"><a>>></a></li>';
	}
	$(".pagination ul").append(postAppender);
}







































/*$(document).ready(function(){
	$("#mobile-api-button").click(function (){
        var bearerToken = $("#bearerToken").val();
        var btn = $(this);
        jagg.post("/site/blocks/mobile-id-task/ajax/mobile-id-api.jag", { 
        	action:"sendMobileIdApiReq",
        	bearerToken:bearerToken
        },
        function (response) {   	
        	if (!response.error) {
                $('#response').val(response.tasks);
                $('#request').val(response.req);
                $( "#payment_table_content" ).empty();                    
            } else {
                jagg.showLogin();
            }
        }, "json");

    }).removeAttr("disabled","disabled");
	
});*/