$(document).ready(function () {
	var s = document.createElement("script");

	var year = $("#year").val();
	var month = $("#month").val();
	var appName = $("#appName").val();
	var subscriber = $("#subscriber").val();
  
	var firstDay = new Date(year, month -1, 1); 
	var lastDay =  new Date(year, month, 0);
    $("#fromDatePick").val($.datepicker.formatDate('yy-mm-dd', firstDay));
    $("#toDatePick").val($.datepicker.formatDate('yy-mm-dd', lastDay));

    $('#fromDatePick').datepicker({
        dateFormat: "yy-mm-dd",
        minDate: firstDay,
        maxDate: lastDay,
        onClose: function( selectedDate ) {
           $( "#toDatePick" ).datepicker( "option", "minDate", selectedDate );
        }
    });
    $( "#toDatePick" ).datepicker({
        dateFormat: "yy-mm-dd",
        minDate: firstDay,
        maxDate: lastDay,
        onClose: function( selectedDate ) {
            $( "#fromDatePick" ).datepicker( "option", "maxDate", selectedDate );
        }
    });

    
    populateUsageData(year + "-" + month, appName, subscriber);
    // showResponseTimes(subscriber, appName, $("#fromDatePick").val(), $("#toDatePick").val());	

    $("#update").click(function(){
    	showResponseTimes(subscriber, appName, $("#fromDatePick").val(), $("#toDatePick").val());
    })
});
