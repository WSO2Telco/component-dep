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

function getSelectedSubscriber() {
	var subscriber = $("#subscriber").val();
	// alert(subscriber);
	if (subscriber.trim() != "__ALL__") {
		var action = "getAppsBySubscriber";
		jagg.post("/site/blocks/total-traffic/ajax/total-traffic.jag", {
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

function getAPITraffic() {
	$('#total-api-traffic-pie-chart').hide()
	$('#loader').show();
	var fromDate = $("#from_date").val();
	var toDate = $("#to_date").val();
	var subscriber = $("#subscriber").val();
	var application = $("#app").val();
	var operator = $("#operator").val();
	var api = $("#api").val();

	var action = "getAPITraffic";
	jagg
			.post(
					"/site/blocks/total-traffic/ajax/total-traffic.jag",
					{
						action : action,
						fromDate : fromDate,
						toDate : toDate,
						subscriber : subscriber,
						application : application,
						operator : operator,
						api : api
					},
					function(result) {

						$('#loader').hide();
						if (!result.error) {
							if (result.pieChart != null
									&& result.histogram != null) {
								//alert(api);		
								if(api == "__ALL__"){
									var total_api_traffic_for_pieChart = result.pieChart;
									// alert(total_api_traffic_for_pieChart[0]);
									$('#total-api-traffic-pie-chart').show();
									if (total_api_traffic_for_pieChart.length > 0) {
										$('#total-api-traffic-pie-chart').empty();
										generateTotalAPITrafficPicChart(total_api_traffic_for_pieChart);
									} else {
										$('#total-api-traffic-pie-chart').empty();
										$("#total-api-traffic-pie-chart").text(
												"There is no API Usage");
									}									
								}else{
									$('#total-api-traffic-pie-chart').hide();
								}

								var total_api_traffic_for_histogram = result.histogram;
								// alert(total_api_traffic_for_histogram[0].apiName);
								if (total_api_traffic_for_histogram.length > 0) {
									$('#total-api-traffic-histogram').empty();
									generateTotalAPITrafficHistogram(total_api_traffic_for_histogram);
								} else {
									$('#total-api-traffic-histogram').empty();
									$('#legend').empty();
									$("#total-api-traffic-histogram").text(
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

function generateTotalAPITrafficPicChart(dataMap) {
	var totalAPITrafficPiechart = "total-api-traffic-pie-chart";
	var colors = [ '#AF0202', '#EC7A00', '#FCD200', '#81C714', '#FF00FF',
			'#00FFFF', '#C0C0C0', '#41B2FA', '#63C1FA', '#83CDFA' ];
	var chartColorScheme1 = [ "#3da0ea", "#bacf0b", "#e7912a", "#4ec9ce",
			"#f377ab", "#ff7373", "5fd2b5", "#ec7337" ];

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
		dojo.empty(totalAPITrafficPiechart);

		// Create the chart within it's "holding" node
		var apiUsageChart = new Chart(totalAPITrafficPiechart);

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
		// var chartData = [1120, 2450, 5540, 1478, 3650];
		var chartData;
		var color = -1;
		require([ "dojo/_base/array" ], function(array) {
			chartData = array.map(dataMap, function(d) {
				color++;
				return {
					y : d[1],
					text : d[0] + " (" + d[1] + ")",
					tooltip : "<b>" + d[0] + "</b><br /><i>" + d[1]
							+ " call(s)</i>",
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

function generateTotalAPITrafficHistogram(total_api_traffic_for_histogram) {

	var totalAPITrafficHistogram = "total-api-traffic-histogram";
	require(
			[
			// Require the basic chart class
			"dojox/charting/Chart",

			// Require the theme of our choosing
			"dojox/charting/themes/Claro",

			// We want to plot Lines
			"dojox/charting/plot2d/Lines",

			// Load the Legend, Tooltip, and Magnify classes
			"dojox/charting/widget/Legend", 
			"dojox/charting/action2d/Tooltip",
					"dojox/charting/action2d/Magnify",

					// We want to use Markers
					"dojox/charting/plot2d/Markers",

					// We'll use default x/y axes
					"dojox/charting/axis2d/Default",


                    "dijit/registry", 
                    
					// Wait until the DOM is ready
					"dojo/domReady!" ],
			function(Chart, theme, LinesPlot, Legend, Tooltip, Magnify) {

				// Create the chart within it's "holding" node
				var chart = new Chart(totalAPITrafficHistogram);

				// Set the theme
				chart.setTheme(theme);

				// Add the only/default plot
				chart.addPlot("default", {
					type : LinesPlot,
					markers : true
				});

				var maxArray = [];
				for (var i = 0; i < total_api_traffic_for_histogram.length; i++) {
					maxArray[i] = findMax(total_api_traffic_for_histogram[i].apiHits);
				}

				var maxValue = findMax(maxArray);

				maxValue = maxValue + 1;

				// Add axes
				var xAxisLabels = createXAxisLabels(total_api_traffic_for_histogram[0].apiHitDates);
				chart.addAxis("x", {
					title:"date",
					titleOrientation:"away",
					labels : xAxisLabels
				});
				chart.addAxis("y", {
					min : 0,
					max : maxValue,
					title:"count",
					vertical : true,
					fixLower : "major",
					fixUpper : "major"
				});

				// Add the series of data
				for (var i = 0; i < total_api_traffic_for_histogram.length; i++) {
					var apiHitsArray = [];
					for (var j = 0; j < total_api_traffic_for_histogram[i].apiHits.length; j++) {
						apiHitsArray[j] = parseInt(total_api_traffic_for_histogram[i].apiHits[j]);
					}
					chart.addSeries(total_api_traffic_for_histogram[i].apiName,
							apiHitsArray.reverse());
				}

				// Create the tooltip
				var tip = new Tooltip(chart, "default");

				// Create the magnifier
				var mag = new Magnify(chart, "default");

				// Render the chart!
				chart.render();

				// Create the legend
				//var legend = new Legend({ chart : chart	}, "legend");
				
				var legend = dijit.registry.byId("legend");
				if (legend) {
				   legend.destroyRecursive(true);
				} 
				var legend = new dojox.charting.widget.Legend({ chart: chart, horizontal:false },"legend");
				
				
			});
	$("#legend tbody tr td").css('min-width', '120px');
}

function createXAxisLabels(reqDate) {
	var reqDateArray = reqDate.reverse();
	var labelArray = [];
	for (var i = 0; i < reqDateArray.length; i++) {
		labelArray.push({
			text : reqDateArray[i],
			value : i + 1
		});
	}
	return labelArray;
}

function findMax(hits) {
	var max = parseInt(hits[0]);
	for (var i = 0; i < hits.length; i++) {
		if (parseInt(hits[i]) > max) {
			max = parseInt(hits[i]);
		}
	}
	return max;
}
