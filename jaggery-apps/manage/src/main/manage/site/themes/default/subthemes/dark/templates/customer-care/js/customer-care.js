//Number of Rows that has to display on CC table
var recordCountPerPage = 10;

$(document).ready(function() {
	setDateFields();
	$('#customer_care_tbl_div').on( 'click', '.readLoader', function () {
		loadHumanData($(this).closest('td'));
	});
});

function loadHumanData(closestTd){
	var rowJson = closestTd.text();
	if(isJsonString(rowJson) == true){//alert(JSON.parse(rowJson));
		var node = JsonHuman.format(JSON.parse(rowJson));
		
		closestTd.html(node);
    } else {
        alert("not a JSON");
    }
}

function setDateFields() {
	// Create old date
	var date = new Date();
	var now = new Date();
	now.setDate(now.getDate() - 7);
	var oldDay = now.getDate();
	var oldMonth = now.getMonth() + 1;
	var oldYear = now.getFullYear();
	if (("0" + oldMonth).length == 2)
		oldMonth = "0" + oldMonth;
	if (("0" + oldDay).length == 2)
		oldDay = "0" + oldDay;
	var oldDate = oldYear + "-" + oldMonth + "-" + oldDay;

	// Create today date
	var day = date.getDate();
	var month = date.getMonth() + 1;
	var year = date.getFullYear();
	if (("0" + month).length == 2)
		month = "0" + month;
	if (("0" + day).length == 2)
		day = "0" + day;
	var today = year + "-" + month + "-" + day;

	// Create date controllers
	$('#from_date').datepicker({
		dateFormat : "yy-mm-dd",
		onClose : function(selectedDate) {
			$("#to_date").datepicker("option", "minDate", selectedDate);
		}
	});

	$("#to_date").datepicker({
		dateFormat : "yy-mm-dd",
		onClose : function(selectedDate) {
			$("#from_date").datepicker("option", "maxDate", selectedDate);
		}
	});

	// Set created dates to date controllers
	$("#from_date").attr("value", oldDate);
	$("#to_date").attr("value", today);
}

function getSelectedSubscriber() {
	var subscriber = $("#subscriberSelect").val();
	// alert(subscriber);
	if (subscriber.trim() != "__ALL__") {
		var action = "getAppsBySubscriber";
		jagg.post("/site/blocks/customer-care/ajax/customer-care.jag", {
			action : action,
			subscriber : subscriber
		}, function(result) {
			if (!result.error) {
				if (result.data != null) {
					// alert(result.data);
					$('#appSelect').prop("disabled", false);
					var apps = result.data;
					fillAppsCombo(apps);
				} else {
					jagg.showLogin();
				}
			} else {
				jagg.showLogin();
			}
		}, "json");
	} else {
		clearCombo();
		var option = '<option value="__All__">All</option>';
		$('#appSelect').append(option);
		$('#appSelect').prop("disabled", true);
	}
}

function fillAppsCombo(apps) {
	// alert(apps.length);
	if (apps.length != 0) {
		clearCombo();
		var option = '<option value="__ALL__">All</option>';
		var i;
		for (i = 0; i < apps.length; i++) {
			var app = apps[i];
			option += '<option value="' + app.id + '">' + app.name
					+ '</option>';
		}
		$('#appSelect').append(option);
	} else {
		clearCombo();
		/*
		 * var option = '<option value="0">All</option>';
		 * $('#app').append(option);
		 */
	}
};

function clearCombo() {
	$('#appSelect').find('option').remove().end();
}

function generateCustomerCareData(fromdate, todate, number, subscriber, operator, app, api, startLimit, endLimit) {
	var action = "getCustomerCareData";
	var d = new Date()
	var timeOffset = d.getTimezoneOffset()+"";
	console.log("timeOffset - "+timeOffset);

	jagg.post("/site/blocks/customer-care/ajax/customer-care.jag", {
		action : action,
		fromDate : fromdate,
		toDate : todate,
		msisdn : number,
		subscriber : subscriber,
		operator : operator,
		application : app,
		api : api,
		startLimit : startLimit,
		endLimit : endLimit,
		timeOffset : timeOffset
	}, function(result) {
		if (!result.error) {
			if(result.usage != null){
				if(result.usage == ""){
					$("#customer_care_tbl_div").html('');
					$("#customer_care_tbl_div").append("There is no API Usage");
				} else {
					var object = result.usage;
					generateDataTable(object);
				}
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
}

function generateCustomerCareDataCount(fromdate, todate, number, subscriber, operator, app, api) {
	var action = "getCustomerCareDataCount";
	var count = 0;
	jagg.post("/site/blocks/customer-care/ajax/customer-care.jag", {
		action : action,
		fromDate : fromdate,
		toDate : todate,
		msisdn : number,
		subscriber : subscriber,
		operator : operator,
		application : app,
		api : api
	}, function(result) {
		if (!result.error) {
			if(result.data != null){
				//result
				count = result.data;
				var paginatePages = count/recordCountPerPage;
				//$("#pagination-demo").empty();
				setTablePagination(Math.ceil(paginatePages));
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
	
	return count;
}

function generateCustomerCareReport(startCount, endCount) {
	var fromDate = $("#from_date").val();
	var toDate = $("#to_date").val();
	var msisdn = $("#msisdnText").val();
	var subscriber = $("#subscriberSelect").val();
	var operator = $("#operatorSelect").val();
	var app = $("#appSelect").val();
	var api = '__ALL__';
	var startLimit = 1;
	var endLimit = recordCountPerPage;
	
	if(startCount == 0 & endCount ==0){
		startLimit = 1;
	} else {
		startLimit = startCount;
	}
	
	generateCustomerCareDataCount(fromDate, toDate, msisdn, subscriber, operator, app, api);
	generateCustomerCareData(fromDate, toDate, msisdn, subscriber, operator, app, api, startLimit, endLimit);
}

function generateDataTable(object){
	$("#customer_care_tbl_div").empty();
	
	var tableobj = '<table class="table table-bordered"><thead><tr><th style="width: 140px;">Date</th><th>Json Body</th><th style="width: 140px;">API</th></tr></thead><tbody id="dataTableBody"></tbody></table>';
	$("#customer_care_tbl_div").append(tableobj);
	
	$.each(object, function(key, value){
		//alert(value);
		var rowHTML = '<tr>';
		var object_in = value;
		$.each(object_in, function(keyN, valueN){
			if(keyN == 'jsonBody' && isJsonString(valueN) == true && valueN != ""){
				rowHTML = rowHTML + '<td>'+valueN+'<br/><input type="button" class="readLoader" value="View Data" />'+'</td>';
			} else {
				rowHTML = rowHTML + '<td>'+valueN+'</td>';
			}
		});
		
		rowHTML = rowHTML + '</tr>';
		$("#dataTableBody").append(rowHTML);
		
	});
	
}

function loadJsonReadable(){
	var closestData = $(this).closest('tr');
}

function isJsonString(str) {
    if (/^[\],:{}\s]*$/.test(str.replace(/\\["\\\/bfnrtu]/g, '@').
        replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
        replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
        //the json is ok
        return true;
    }else{
        //the json is not ok
        return false;
    }
}


function setTablePagination(totalPages) {
	$("#pagination-demo").empty();
	if(totalPages > 0){
		$(function() {
		    $('#pagination-demo').pagination({
		        pages: totalPages,
		        itemsOnPage: 0,
		        cssStyle: 'light-theme',
		        onPageClick: function (pageNumber, event) {
		        	var fromDate = $("#from_date").val();
		        	var toDate = $("#to_date").val();
		        	var msisdn = $("#msisdnText").val();
		        	var subscriber = $("#subscriberSelect").val();
		        	var operator = $("#operatorSelect").val();
		        	var app = $("#appSelect").val();
		        	var api = '__ALL__';

		        	var maxRecord = pageNumber*recordCountPerPage;
		        	var minRecord = (maxRecord - recordCountPerPage)+1;
		        	generateCustomerCareData(fromDate, toDate, msisdn, subscriber, operator, app, api, minRecord, recordCountPerPage);
		        }
		    });
		});
	}
}
