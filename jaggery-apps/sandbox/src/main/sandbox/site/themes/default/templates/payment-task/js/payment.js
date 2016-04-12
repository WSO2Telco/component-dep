$(document).ready(function() {
	getPaymentSessionData();
});

$(document).ready(function(){
	
    $("#param-add-button").click(function (){
        var paystatus = $("#paystatus").val();
        var maxamt = $("#maxamt").val();       
        var maxtrn = $("#maxtrn").val();
        var btn = $(this);
        btn.attr("disabled","disabled");
        //var iteration=btn.attr("iteration");
        jagg.post("/site/blocks/payment/ajax/payment.jag", { action:"saveparam",user:"username",paystatus:paystatus,maxtrn:maxtrn,maxamt:maxamt },
            function (response) {
                if (!response.error) {
                    //btn.next().show();
                    //$('#js_completeBtn'+iteration).show();
                    //btn.hide();
                    //$('#status'+iteration).text("IN_PROGRESS");
                    $('#appAddMessage').show();
                    window.location.reload();
                    
                } else {
                    jagg.showLogin();
                }
            }, "json");

    }).removeAttr("disabled","disabled");
    
    $("#payment-add-button").click(function (){
        var paystatus = $("#paystatus").val();
        var endUserId = $("#endUsertp").val();
        var transactionOperationStatus = $("#transactionOperationStatus").val();
        var referenceCode = $("#referenceCode").val();
        var description = $("#description").val();
        var amount = $("#amount").val();
        var clientCorrelator = $("#clientCorrelator").val();
        var callbackURL = $("#callbackURL").val();
        var onBehalfOf = $("#onBehalfOf").val();
        var purchaseCategoryCode = $("#purchaseCategoryCode").val();
        var channel = $("#channel").val();
        var taxAmount = $("#taxAmount").val();
                      
        var btn = $(this);
        //btn.attr("disabled","disabled");
        //var iteration=btn.attr("iteration");
        jagg.post("/site/blocks/payment/ajax/payment.jag", { action:"addpayment",endUserId:endUserId,
            transactionOperationStatus:transactionOperationStatus,referenceCode:referenceCode,
            description:description,currency:"USD",amount:amount,clientCorrelator:clientCorrelator,
            callbackURL:callbackURL,onBehalfOf:onBehalfOf,purchaseCategoryCode:purchaseCategoryCode,
            channel:channel,taxAmount:taxAmount
        },
            function (response) {
                if (!response.error) {
                    //btn.next().show();
                    //$('#js_completeBtn'+iteration).show();
                    //btn.hide();
                    //$('#status'+iteration).text("IN_PROGRESS");                    
                
                    $('#response').val(response.tasks);
                    $('#request').val(response.req);
                    $( "#payment_table_content" ).empty();
                    createPaymentTable(response.table);
                    //window.location.reload();
                    
                } else {
                    jagg.showLogin();
                }
            }, "json");

    }).removeAttr("disabled","disabled");
    
    $("#maxtrn").keydown(function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 ) {
        }
        else if (event.keyCode == 37 || event.keyCode == 39) {
        }
        else {
            if (event.keyCode < 95) {
                if (event.keyCode < 48 || event.keyCode > 57 ) {
                    event.preventDefault();
                }
            }
            else {
                if (event.keyCode < 96 || event.keyCode > 105 ) {
                    event.preventDefault();
                }
            }
            if($("#maxtrn").val().length >= 10) {
                event.preventDefault();
            }
        }
    });
    
    $("#maxamt").keyup(function(event) {
        
    	var inputVal = $("#maxamt").val();
        var numericReg = /^[0-9]{0,10}(\.[0-9]{0,2})?$/;
        if(!numericReg.test(inputVal)) {
        	event.preventDefault();
        	$("#maxamt").val(inputVal.substr(0,inputVal.length-1));
        	return false;
        }
    });
    
   
    $("#maxamt").keydown(function(event) {
       
    	if ( event.keyCode == 46 || event.keyCode == 8 ) {
        }
        else if (event.keyCode == 37 || event.keyCode == 39) {
        } else if (event.keyCode == 190) {        	
        }
        else {
            if (event.keyCode < 95) {
                if (event.keyCode < 48 || event.keyCode > 57 ) {
                    event.preventDefault();
                }
            }
            else {
                if (event.keyCode < 96 || event.keyCode > 105 ) {
                    event.preventDefault();
                }                
            }
            if($("#maxamt").val().length >= 10) {
                event.preventDefault();
            }
            var inputVal = $("#maxamt").val();
            var numericReg = /^[0-9]{0,10}(\.[0-9]{0,2})?$/;
            if(!numericReg.test(inputVal)) {
            	event.preventDefault();
            	return false;
            }
        }
    	
    	
    	
    });
    

    $('.js_completeBtn').click(function(){
        var btn = $(this);
        var taskId=btn.attr("data");
        var iteration=btn.attr("iteration");
        var description=$('#desc'+iteration).text();
        var status=$('.js_stateDropDown').val();
        btn.attr("disabled","disabled");
        jagg.post("/site/blocks/task-manager/ajax/task.jag", { action:"completeTask",status:status,taskId:taskId,taskType:"application",description:description },
            function (json) {
                if (!json.error) {
                    btn.next().show();
                    btn.next().next().html(json.msg);
                    btn.hide();
                    window.location.reload();
                } else {
                    jagg.showLogin();
                }
            }, "json");

    }).removeAttr("disabled","disabled");

    $('.js_assignBtn').click(function(){
        var btn = $(this);
        var taskId=btn.attr("data");
        var iteration=btn.attr("iteration");
        btn.attr("disabled","disabled");
        jagg.post("/site/blocks/task-manager/ajax/task.jag", { action:"assignTask",taskId:taskId,taskType:"application" },
            function (json) {
                if (!json.error) {
                    btn.next().show();
                    $('#js_startBtn'+iteration).show();
                    btn.hide();
                    $('#status'+iteration).text("RESERVED");
                } else {
                    jagg.showLogin();
                }
            }, "json");
    }).removeAttr("disabled","disabled");

});

function getPaymentSessionData(){
	var action = "getSessionData";
	jagg.post("/site/blocks/payment/ajax/payment.jag", {
		action : action
	}, function(result) {
			if (result.table != "null") {
				$( "#payment_table_content" ).empty();
				createPaymentTable(result.table);
				setTablePagination(0);
			}
	}, "json");
};

function createPaymentTable(table){
	var payTable = $('<table></table>').attr({ id: "applicationTable"}).attr({class: "table table-bordered table-striped"});
	var hRow = $('<tr></tr>').appendTo(payTable);
	$('<th></th>').text("Transaction Id").appendTo(hRow);
	$('<th></th>').text("Client Correlator").appendTo(hRow); 
	$('<th></th>').text("End User Id").appendTo(hRow); 
	$('<th></th>').text("Payment Amount").appendTo(hRow);
	$('<th></th>').text("Reference Code").appendTo(hRow);
	$('<th></th>').text("Transaction Operation Status").appendTo(hRow);
	
	for(var i = 0; i < table.length; i++){
		var bRow = $('<tr></tr>').appendTo(payTable);
		$('<td></td>').text(i+1).appendTo(bRow);
		$('<td></td>').text(table[i].clientCorrelator).appendTo(bRow); 
		$('<td></td>').text(table[i].endUserId).appendTo(bRow); 
		$('<td></td>').text(table[i].amount).appendTo(bRow);
		$('<td></td>').text(table[i].referenceCode).appendTo(bRow);
		$('<td></td>').text(table[i].transactionOperationStatus).appendTo(bRow);
	}
	payTable.appendTo("#payment_table_content");
}

function setTablePagination(pageNumber) {
	paginator(pageNumber);
}

function paginator(pageNumber) {
	var rows = $("#applicationTable tbody tr").length;
	var rowsPerPage = 10;
	if (rows > rowsPerPage) {
		var numberOfPages = Math.ceil(rows / rowsPerPage);
		var currentPageStart = pageNumber * rowsPerPage;
		var currentPageEnd = (pageNumber * rowsPerPage) + rowsPerPage;
		for (var i = 0; i < rows; i++) {
			if ((currentPageStart <= i) & (i < currentPageEnd)) {
				$("#applicationTable tr").eq(i).show();
				// alert(i);
			} else {
				$("#applicationTable tbody tr").eq(i).hide();
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
