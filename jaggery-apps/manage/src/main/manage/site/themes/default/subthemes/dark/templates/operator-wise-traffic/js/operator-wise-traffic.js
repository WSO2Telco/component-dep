$(document).ready(function() {
	setDateFields();
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

function clearCombo() {
	$('#app').find('option').remove().end();
}

function getOperatorWiseAPITraffic(){
	$('#operator-wise-api-traffic-pie-chart').hide()
	$('#loader').show();
	var fromDate = $( "#from_date" ).val();
	var toDate = $( "#to_date" ).val();
	var subscriber = $("#subscriber").val();
	var application = $("#app").val();
	var api = $("#api").val();
	
	var action = "getOperatorWiseAPITraffic";
	jagg.post("/site/blocks/operator-wise-traffic/ajax/operator-wise-traffic.jag", {
		action : action,
		fromDate : fromDate,
		toDate : toDate,
		subscriber : subscriber,
		application : application,
		api : api
	}, function(result) {
		$('#loader').hide();
		$('#operator-wise-api-traffic-pie-chart').show()
		if (!result.error) {
			if (result.pieChart != null) {
				var operator_wise_api_traffic_for_pieChart = result.pieChart;
				//alert(operator_wise_api_traffic_for_pieChart[0]);
				if(operator_wise_api_traffic_for_pieChart.length > 0){
					$('#operator-wise-api-traffic-pie-chart').empty();
					generateOperatorWiseAPITrafficPicChart(operator_wise_api_traffic_for_pieChart);
				}else{
					$('#operator-wise-api-traffic-pie-chart').empty();
					$("#operator-wise-api-traffic-pie-chart").text("There is no API Usage");
				}
			} else {
				jagg.showLogin();
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
}

function generateOperatorWiseAPITrafficPicChart(dataMap) {
	
	var apiUsageGraphNode = "operator-wise-api-traffic-pie-chart";
	var colors = ['#AF0202', '#EC7A00', '#FCD200', '#81C714', '#FF00FF' , '#00FFFF', '#C0C0C0','#41B2FA', '#63C1FA', '#83CDFA'];
	var chartColorScheme1 = ["#3da0ea","#bacf0b","#e7912a","#4ec9ce","#f377ab","#ff7373","5fd2b5","#ec7337"];
	
	
	require([
	// Require the basic chart class
	"dojox/charting/Chart",

	// Require the theme of our choosing
	"dojox/charting/themes/Claro",

	// Charting plugins:

	// We want to plot a Pie chart
	"dojox/charting/plot2d/Pie",

	// Retrieve the Legend, Tooltip, and MoveSlice classes
	// "dojox/charting/action2d/Tooltip",
	"dojox/charting/action2d/MoveSlice",

	// We want to use Markers
	"dojox/charting/plot2d/Markers",

	// We'll use default x/y axes
	"dojox/charting/axis2d/Default",

	"dojo/domReady!" 
	
	], function(Chart, theme, Pie, MoveSlice) {

		// Destroy existing graph
		dojo.empty(apiUsageGraphNode);

		// Create the chart within it's "holding" node
		var apiUsageChart = new Chart(apiUsageGraphNode);

		// Set the theme
		apiUsageChart.setTheme(theme);

		// Add the only/default plot
		apiUsageChart.addPlot("default", {
			type : Pie,
			markers : true,
			radius : 130,
			labelWiring : "ccc",
			labelStyle : "columns"
		});

		// Add axes
		apiUsageChart.addAxis("x");
		apiUsageChart.addAxis("y", {
			min : 5000,
			max : 30000,
			vertical : true,
			fixLower : "major",
			fixUpper : "major"
		});

		// Define the data
		//var chartData = [1120, 2450, 5540, 1478, 3650];
		var chartData;
		var color = -1;
		require([ "dojo/_base/array" ], function(array) {
			chartData = array.map(dataMap, function(d) {
				color++;
				return {
					y : d[1],
					text : d[0] + " (" + d[1] + ")",
					tooltip : "<b>" + d[0] + "</b><br /><i>" + d[1]	+ " call(s)</i>",
					fill : chartColorScheme1[color % chartColorScheme1.length]
				};

			});
		});
		apiUsageChart.addSeries("API Usage", chartData);

		// Create the slice mover
		var mag = new MoveSlice(apiUsageChart, "default");

		// Render the chart!
		apiUsageChart.render();
	});
}
