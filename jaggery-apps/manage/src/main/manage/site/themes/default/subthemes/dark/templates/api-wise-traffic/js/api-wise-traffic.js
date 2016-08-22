$(document).ready(function() {
	setDateFields();
});

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

function clearCombo() {
	$('#app').find('option').remove().end();
}

function getAPIWiseTraffic() {
	var fromDate = $("#from_date").val();
	var toDate = $("#to_date").val();
	var operator = $("#operator").val();
	var subscriber = $("#subscriber").val();
	var api = $("#api").val();

	var action = "getAPIWiseTraffic";
	jagg.post("/site/blocks/api-wise-traffic/ajax/api-wise-traffic.jag", {
		action : action,
		fromDate : fromDate,
		toDate : toDate,
		subscriber : subscriber,
		operator : operator,
		api : api
	}, function(result) {
		if (!result.error) {
			if (result.histogram != null) {
				var api_wise_traffic_for_histogram = result.histogram;
				// alert(api_wise_traffic_for_histogram[0]);
				if (api_wise_traffic_for_histogram.length > 0) {
					$('#api-wise-traffic-histogram').empty();
					setHistogramData(api_wise_traffic_for_histogram);
				} else {
					$('#api-wise-traffic-histogram').empty();
					$("#api-wise-traffic-histogram").text(
							"There is no API Usage");
				}
			} else {
				jagg.showLogin();
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
}

function setHistogramData(api_wise_traffic_for_histogram) {

	var apiName = [];
	var reqDate = [];
	var hits = [];

	for (var i = 0; i < api_wise_traffic_for_histogram.length; i++) {
		// alert(api_wise_traffic_for_histogram[i]);
		var temp = api_wise_traffic_for_histogram[i];
		apiName[i] = temp[0];
		// var tempSplit = temp[1].split("-");
		// reqDate[i] = parseInt(tempSplit[2]);
		reqDate[i] = temp[1];
		hits[i] = parseInt(temp[2]);
	}
	
	var labelArray = createXAxisLabels(reqDate);
	var maxValue = findMax(hits);
	generateAPIWiseTrafficHistogram(apiName, labelArray, hits, maxValue);
}

function createXAxisLabels(reqDate) {
	var reqDateArray = reqDate.reverse();
	var labelArray = [];
	for (var i = 0; i < reqDateArray.length; i++) {
		labelArray.push({
			text : reqDateArray[i],
			value : i+1
		});
	}
	return labelArray;
}

function findMax(hits){
	var max = hits[0];
	for (var i = 0; i < hits.length; i++) {
		if(hits[i] > max){
			max = hits[i];
		}
	}
	return max;
}

function generateAPIWiseTrafficHistogram(apiName, labelArray, hits, maxValue) {
	var apiUsageGraphNode = "api-wise-traffic-histogram";

	require([
	// Require the basic chart class
	"dojox/charting/Chart",

	// Require the theme of our choosing
	"dojox/charting/themes/Claro",

	// We want to plot Lines
	"dojox/charting/plot2d/Lines",

	// Load the Legend, Tooltip, and Magnify classes
	"dojox/charting/widget/Legend", "dojox/charting/action2d/Tooltip",
			"dojox/charting/action2d/Magnify",

			// We want to use Markers
			"dojox/charting/plot2d/Markers",

			// We'll use default x/y axes
			"dojox/charting/axis2d/Default",

			// Wait until the DOM is ready
			"dojo/domReady!" ], function(Chart, theme, LinesPlot, Legend,
			Tooltip, Magnify) {

		// Define the data
		var chartData = hits.reverse();

		// Define X axis labels
		var xAxisLabels = labelArray;

		// Create the chart within it's "holding" node
		var chart = new Chart(apiUsageGraphNode);

		// Set the theme
		chart.setTheme(theme);

		// Add the only/default plot
		chart.addPlot("default", {
			type : LinesPlot,
			markers : true
		});

		// Add axes
		chart.addAxis("x", {
			labels : xAxisLabels
		});
		chart.addAxis("y", {
			min : 0,
			max : maxValue + 1,
			vertical : true,
			fixLower : "major",
			fixUpper : "major"
		});

		// Add the series of data
		chart.addSeries("Monthly Sales - 2010", chartData);

		// Create the tooltip
		var tip = new Tooltip(chart, "default");

		// Create the magnifier
		var mag = new Magnify(chart, "default");

		// Render the chart!
		chart.render();

		// Create the legend
		var legend = new Legend({
			chart : chart
		}, "legend");
	});
}
var downloadTrafficReports = function(){
	var fromDate = $("#from_date");
    var toDate = $("#to_date");
    var operator = $("#operator");
    var subscriber = $("#subscriber");
    var api = $("#api");
    var isError = $("#isError");
 
	selectedFromDate = fromDate.val();
    selectedToDate = toDate.val();
    selectedOperator = operator.val();
    selectedSubscriber = subscriber.val();
    selectedApi = api.val();
    selectedisError = isError.val();
    	
	document.getElementById("selected_fromdate").value = selectedFromDate;
	document.getElementById("selected_todate").value = selectedToDate ;
	document.getElementById("selected_operator").value = selectedOperator ;
	document.getElementById("selected_subscriber").value = selectedSubscriber ;
	document.getElementById("selected_api").value = selectedApi ;
	document.getElementById("selected_iserror").value = selectedisError ;
    document.getElementById("trafficDownloadForm").submit();
    
}
