$(document).ready(function() {
	//setDateFields();
});


function getSelectedSubscriber() {
	var subscriber = $("#subscriber").val();
	// alert(subscriber);
	if (subscriber.trim() != "__ALL__") {
		var action = "getAppsBySubscriber";
		jagg.post("/site/blocks/operator-wise-traffic/ajax/operator-wise-traffic.jag", {
			action : action,
			subscriber : subscriber
		}, function(result) {
			if (!result.error) {
				if (result.data != null) {
					// alert(result.data);
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
		var option = '<option value="0">All</option>';
		$('#app').append(option);
	}
}

function fillAppsCombo(apps) {
	// alert(apps.length);
	if (apps.length != 0) {
		clearCombo();
		var option = '<option value="0">All</option>';
		var i;
		for (i = 0; i < apps.length; i++) {
			var app = apps[i];
			option += '<option value="' + app.id + '">' + app.name
					+ '</option>';
		}
		$('#app').append(option);
	} else {
		clearCombo();
		/*
		 * var option = '<option value="0">All</option>';
		 * $('#app').append(option);
		 */
	}
};

function generateDataTable(object){
    $("#application_history_tbl_div").empty();
    $(".pagination").empty();
    
    var tableobj = '<div> </div></div><table class="table table-bordered table-striped"><thead><tr><th>Application ID</th><th>Name</th><th>Description</th><th>Status</th><th>Approved On</th></tr></thead><tbody id="dataTableBody"></tbody></table>';
    
    $("#application_history_tbl_div").append("<h4>Applications</h4>");
    $("#application_history_tbl_div").append(tableobj);
    
    $.each(object, function(key, value){
        //alert(value);
        var rowHTML = '<tr>';
        var object_in = value;
        var i=0;
        var appid=0;
        var URL = "app-history.jag?AppId=";
        $.each(object_in, function(keyN, valueN){
            //alert(keyN +","+ valueN);
            //var rowVal = [keyN , valueN];
            //dataArray.push(rowVal);
        	if (i==0) {
        		appid = valueN;
        	}
        	
        	var cell2 = document.createElement("td");
        		
        		if (i==1) {
        			//rowHTML = rowHTML + '<td>'+valueN+appid;'</td>';
        			//var appSummary = URL + appid;
        			//alert(appSummary);
                    //a.href = appSummary;
                    //cell2.innerHTML = application.applicationname;
                    //cell2.appendChild(a);
        			
        			rowHTML = rowHTML + '<td><a href="app-history.jag?AppId='+appid+'&appname='+valueN+'">'+valueN+'</a></td>';
        		} else {
        			rowHTML = rowHTML + '<td>'+valueN+'</td>';
        		}
        	
        	i++;
        });
        
        rowHTML = rowHTML + '</tr>';
        $("#dataTableBody").append(rowHTML);
        setTablePagination(0);
        
    });
    
}

function clearCombo() {
	$('#app').find('option').remove().end();
}

function setTablePagination(pageNumber) {
    paginator(pageNumber);
}


function paginator(pageNumber) {
    var rows = $("#dataTableBody tr").length;
    var rowsPerPage = 10;
    if (rows > rowsPerPage) {
        var numberOfPages = Math.ceil(rows / rowsPerPage);
        var currentPageStart = pageNumber * rowsPerPage;
        var currentPageEnd = (pageNumber * rowsPerPage) + rowsPerPage;
        for (var i = 0; i < rows; i++) {
            if ((currentPageStart <= i) & (i < currentPageEnd)) {
                $("#dataTableBody tr").eq(i).show();
                // alert(i);
            } else {
                $("#dataTableBody tr").eq(i).hide();
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

function getApprovalHistory(){
	//var fromDate = $( "#from_date" ).val();
	//var toDate = $( "#to_date" ).val();
	var subscriber = $("#subscriber").val();
	var application = $("#app").val();
	var operator = $("#operator").val();
	var api = $("#api").val();
	
	var action = "getApprovalHistory";
	jagg.post("/site/blocks/approvalaudit/ajax/approvalaudit.jag", {
		action : action,
		//fromDate : fromDate,
		//toDate : toDate,
		subscriber : subscriber,
		application : application,		
		operator : operator,
		api : api
		//api : api
	}, function(result) {
		if (!result.error) {
			if (result.applist != null) {
				var api_history = result.applist;
				//alert(operator_wise_api_traffic_for_pieChart[0]);
				if(api_history.length > 0){
					generateDataTable(api_history);
				}else{
					$('#application_history_tbl_div').empty();
					$("#application_history_tbl_div").text("There is no Application Approval History");
				}
			} else {
				jagg.showLogin();
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
}

