
$(document).ready(function(){
	$("#ussd-submit-add-button").click(function (){
		performTask();
	});
	getRequestSessionData();
});

function performTask(){

	var endUserId = $("#address").val();
	var shortCode = $("#shortCode").val();
	var outboundUSSDMessage = $("#outboundUSSDMessage").val();
	var clientCorrelator = $("#clientCorrelator").val();
	var ussdAction = $("#ussdAction").val();
	var callbackData = $("#callbackData").val();
	var notifyURL = $("#notifyURL").val();
	var keyword = $("#keyword").val();
	
	var action = "sendMTUSSD";
	
	jagg.post("/site/blocks/ussd-send/ajax/send-ussd.jag", {
		action : action,
		
		endUserId: endUserId,
		shortCode: shortCode,
		outboundUSSDMessage: outboundUSSDMessage,
		clientCorrelator: clientCorrelator,
		ussdAction: ussdAction,
		callbackData: callbackData,
		notifyURL: notifyURL,
		keyword: keyword
		
	}, function(result) {
		
		if (!result.error) {
			if (result.requestData != "null") {
				//location.reload();
				$('#request').val(result.requestData);
				$('#response').val(result.data);
				//loadRequestRow(endUserId, shortCode, outboundUSSDMessage, clientCorrelator, ussdAction);
				$( "#ussd_table_cont" ).empty();
				createUssdTable(result.table);	
			} else {
				
			}
			//setTablePagination(0);
		} else {
			alert("Error");
		}
	}, "json");
}

function loadRequestRow(senderaddress, shortcode, message, clientcorrelator, action){
	row = $("<tr></tr>");
	col1 = $('<td>'+senderaddress+'</td>');
	col2 = $('<td>'+shortcode+'</td>');
	col3 = $('<td>'+message+'</td>');
	col4 = $('<td>'+clientcorrelator+'</td>');
	col5 = $('<td>'+action+'</td>');
	row.append(col1, col2, col3, col4, col5).prependTo("#ussd_request_table");
}

function createUssdTable(tableData){
	if(tableData.length > 0){
		$("#ussd_request_data_container").css('display', "inline");
	}
	
	var ussdTable = $('<table></table>').attr({ id: "ussd_request_table"}).attr({class: "table table-bordered table-striped"});
	var hRow = $('<tr></tr>').appendTo(ussdTable);
	$('<th></th>').text("Sender Address").appendTo(hRow);
	$('<th></th>').text("Shortcode").appendTo(hRow); 
	$('<th></th>').text("Message").appendTo(hRow); 
	$('<th></th>').text("Client Correlator").appendTo(hRow);
	$('<th></th>').text("UssdAction").appendTo(hRow);
	
	for(var i = 0; i < tableData.length; i++){
		var bRow = $('<tr></tr>').appendTo(ussdTable);
		$('<td></td>').text(tableData[i].senderAddress).appendTo(bRow); 
		$('<td></td>').text(tableData[i].shortCode).appendTo(bRow); 
		$('<td></td>').text(tableData[i].message).appendTo(bRow);
		$('<td></td>').text(tableData[i].clientCorrelator).appendTo(bRow);
		$('<td></td>').text(tableData[i].ussdAction).appendTo(bRow);
	}
	ussdTable.appendTo("#ussd_table_cont");
}

function getRequestSessionData(){
	var action = "getSessionData";
	jagg.post("/site/blocks/ussd-send/ajax/send-ussd.jag", {
		action : action
	}, function(result) {
			if (result.table != "null") {
				$( "#ussd_table_cont" ).empty();
				createUssdTable(result.table);
				//setTablePagination(0);
			}
	}, "json");
}
