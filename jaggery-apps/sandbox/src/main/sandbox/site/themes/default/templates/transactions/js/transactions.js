$(document).ready(function() {
	setDateFields();

	// Create table on page load
	var type = $("#transactionType").val();
	var from_date = $("#from_date").val();
	var to_date = $("#to_date").val();
	
	createTable(type, from_date, to_date);
});

function setDateFields(){
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
	
	
	//Create date controllers
	$('#from_date').datepicker({
        dateFormat: "yy-mm-dd",
        onClose: function( selectedDate ) {
           $( "#to_date" ).datepicker( "option", "minDate", selectedDate );
        }
    });

    
    $( "#to_date" ).datepicker({
        dateFormat: "yy-mm-dd",
        onClose: function( selectedDate ) {
            $( "#from_date" ).datepicker( "option", "maxDate", selectedDate );
        }
    });

	// Set created dates to date controllers
	$("#from_date").attr("value", oldDate);
	$("#to_date").attr("value", today);
}

function generateTransactionGrid() {
	var type = $("#transactionType").val();
	// alert(type);
	var from_date = $("#from_date").val();
	// alert(from_date);
	var to_date = $("#to_date").val();
	// alert(to_date);
	createTable(type, from_date, to_date);
	// alert(from_date);
}

function createTable(type, from_date, to_date) {
	jagg.post("/site/blocks/transactions/ajax/transactions.jag", {
		type : type,
		from_date : from_date,
		to_date : to_date
	}, function(result) {
		// alert(result.data);
		if (!result.error) {
			// alert("Success");
			if (result.data != "null") {
				$('#transactionGrid').html(result.data);
			} else {
				jagg.showLogin();
			}
			setTablePagination(0);
		} else {
			jagg.showLogin();
		}
	}, "json");
}

function resetValues(){
	$("#transactionType").val(1);
	setDateFields();	
	generateTransactionGrid();
}

function setTablePagination(pageNumber) {
	paginator(pageNumber);
}

function paginator(pageNumber) {
	var rows = $("#transaction_table tbody tr").length;
	var rowsPerPage = 10;
	if (rows > rowsPerPage) {
		var numberOfPages = Math.ceil(rows / rowsPerPage);
		var currentPageStart = pageNumber * rowsPerPage;
		var currentPageEnd = (pageNumber * rowsPerPage) + rowsPerPage;
		for (var i = 0; i < rows; i++) {
			if ((currentPageStart <= i) & (i < currentPageEnd)) {
				$("#transaction_table tbody tr").eq(i).show();
				// alert(i);
			} else {
				$("#transaction_table tbody tr").eq(i).hide();
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
