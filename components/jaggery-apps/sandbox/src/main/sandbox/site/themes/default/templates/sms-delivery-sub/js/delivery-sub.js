$(document).ready(function() {
	loadDeliverySubData();
	
	//Delete data and remove row
    $('#data_class_body').on( 'click', 'a.delete_tbl_icon', function () {
    	var r = confirm("Are you sure you want to Delete selected record?");
    	if (r == true) {

    	var closestTr = $(this).closest('tr');
    	var key = closestTr.find('.row_data_key').val();
    	var request_url = "/site/blocks/sms-delivery-sub/ajax/sms-delivery-sub.jag";
    	
    	//alert("Deleting key: "+key);
    	
    	jagg.post(request_url, {'action':"UnsubscribeDelivery", 'id':key}, function (result) {
    		if(!result.error){

                //$('#tableSuccess').show('fast');
                //$('#tableSuccessSpan').html('<strong>Success!</strong><br />' + "Number deleted successfully");
                //$('#tableSuccess').delay(4000).hide('fast');
    			closestTr.remove();
    			loadDeliverySubData();
    			
    		} else {
    			//alert(result);
            	$('#tableError').show('fast');
                $('#tableErrorSpan').html('<strong>Error!</strong><br /> Error in deleting Number: '+ result.message);
                $('#tableError').delay(4000).hide('fast');
    		}
    	}, "json");
    	setTablePagination(0);
    	}
    });
});

function sendDeliverySubRequest(){
	var fileterCriteria = $("#filterCriteria").val();
	var notifyURL = $("#notifyURL").val();
	var callbackData = $("#callbackData").val();
	var clientCorrelator = $("#clientCorrelator").val();
	
	//alert("XX: "+fileterCriteria+" :: "+notifyURL+" :: "+callbackData+" :: "+clientCorrelator);
	
	var action = "getDeliverySubResponse";
	
	jagg.post("/site/blocks/sms-delivery-sub/ajax/sms-delivery-sub.jag", {
		action : action,
		filterCriteria : fileterCriteria,
		notifyUrl : notifyURL,
		callbackData : callbackData,
		clientCorrelator : clientCorrelator
	}, function(result) {
		if (!result.error) {
			if (result.requestData != "null") {
				//location.reload();
				$('#request').val(result.requestData);
				$('#response').val(result.data);
				loadDeliverySubData();
			} else {
				
			}
			//setTablePagination(0);
		} else {
			alert("Error");
		}
	}, "json");
}

function loadDeliverySubData(){
	jagg.post("/site/blocks/sms-delivery-sub/ajax/sms-delivery-sub.jag", {
		action : "GetDeliverySubData",
	}, function(result) {
		// alert(result.data);

		if (!result.error) {
			// alert(result.message);
			loadTableRows(result.data);
		} else {
			// $('#tableError').show('fast');
			// $('#tableErrorSpan').html('<strong>Error!</strong><br />Server
			// Error occurs! Please re-try. : '+result.message);
		}
	}, "json");
}

function loadTableRows(rowData) {
	$("#delivery_sub_data_table #data_class_body").empty();
	$("#delivery_sub_data_table #data_class_body").append(rowData);
	setTablePagination(0);
}


//Paginations
function setTablePagination(pageNumber){
	paginator(pageNumber);
}

function paginator(pageNumber){
	var rows = $("#delivery_sub_data_table #data_class_body tr").length;
	var rowsPerPage = 10;
	if(rows > rowsPerPage){
		var numberOfPages = Math.ceil(rows/rowsPerPage);
		var currentPageStart = pageNumber*rowsPerPage;
		var currentPageEnd = (pageNumber*rowsPerPage)+rowsPerPage;
		for (var i = 0; i < rows; i++) {
	    	if((currentPageStart <= i) & (i < currentPageEnd)){
	    		$("#delivery_sub_data_table tbody tr").eq(i).show();
	    		//alert(i);
	    	} else {
	    		$("#delivery_sub_data_table tbody tr").eq(i).hide();
	    	}
		}
		//alert("PAGENUMBER: "+pageNumber+"\nRows: "+rows+"\nRowsPP: "+rowsPerPage+"\nPAGES: "+numberOfPages+"\nSTART: "+currentPageStart+"\nEND: "+currentPageEnd);
		loadPaginatorView(numberOfPages, pageNumber);
	} else {
		$(".pagination").html('');
	}
}

function loadPaginatorView(numberOfPages, currentPage){
	$(".pagination").html('<ul></ul>');
	var previousAppender = '<li><a href="javascript:paginator(0)"><<</a></li>';
	if(currentPage ==0){
		previousAppender = '<li class="disabled"><a><<</a></li>';
	}
	$(".pagination ul").append(previousAppender);
	for (var i = 0; i < numberOfPages; i++) {
		var currentRow;
		var rowSticker = i+1;
		if(i==currentPage){
			currentRow = '<li class="active"><a>'+rowSticker+'</a></li>';
		} else {
			currentRow = '<li><a href="javascript:paginator('+i+')">'+rowSticker+'</a></li>';
		}
		$(".pagination ul").append(currentRow);
	}
	var lastPage = numberOfPages-1;//alert(lastPage);
	var postAppender = '<li><a href="javascript:paginator('+lastPage+')">>></a></li>';
	if(currentPage == lastPage){
		postAppender = '<li class="disabled"><a>>></a></li>';
	}
	$(".pagination ul").append(postAppender);
}