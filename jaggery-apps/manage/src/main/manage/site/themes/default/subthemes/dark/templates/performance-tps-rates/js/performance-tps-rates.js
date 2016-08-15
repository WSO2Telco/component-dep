$(document).ready(function() {
	setDateFields();
});

function setDateFields() {
	// Create old date
	var date = new Date();
	var now = new Date();
	now.setDate(now.getDate() - 7);
	var oldDay = now.getDate();
	var oldMonth = now.getMonth()  1;
	var oldYear = now.getFullYear();
	if (("0"  oldMonth).length == 2)
		oldMonth = "0"  oldMonth;
	if (("0"  oldDay).length == 2)
		oldDay = "0"  oldDay;
	var oldDate = oldYear  "-"  oldMonth  "-"  oldDay;

	// Create today date
	var day = date.getDate();
	var month = date.getMonth()  1;
	var year = date.getFullYear();
	if (("0"  month).length == 2)
		month = "0"  month;
	if (("0"  day).length == 2)
		day = "0"  day;
	var today = year  "-"  month  "-"  day;

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
	$("#from_date").attr("value", today);
	$("#to_date").attr("value", today);
}

function getSelectedSubscriber() {
	var subscriber = $("#subscriber").val();
	// alert(subscriber);
	if (subscriber.trim() != "__ALL__") {
		var action = "getAppsBySubscriber";
		jagg.post("/site/blocks/performance-tps-rates/ajax/performance-tps-rates.jag", {
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
		for (i = 0; i < apps.length; i) {
			var app = apps[i];
			option = '<option value="'  app.id  '">'  app.name
					 '</option>';
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

function getTpsSummary() {
$('#tps-summary-histogram').hide();
	$('#loader').show();
	var fromDate = $("#from_date").val();
	//var toDate = $("#to_date").val();
	var subscriber = $("#subscriber").val();
	var application = $("#app").val();
	var operator = $("#operator").val();
	var api = $("#api").val();

	var action = "getTpsSummary";
	jagg.post("/site/blocks/performance-tps-rates/ajax/performance-tps-rates.jag",
					{
						action : action,
						fromDate : fromDate,
						subscriber : subscriber,
						application : application,
						operator : operator,
						api : api
					},
					function(result) {						
						if (!result.error) {
                                                $('#tps-summary-histogram').show();
							if (result.histogram != null) {		
									
									var tps_summary_histogram = result.histogram;
									if (tps_summary_histogram.length > 0) {
										$('#tps-summary-histogram').empty();
										generateTpsHistogram(tps_summary_histogram);
										$('#loader').hide();
									} else {
										$('#tps-summary-histogram').empty();
										$('#legend').empty();
										$("#tps-summary-histogram").text(
												"There is no API Usage");
									}
							} else {
								$('#loader').hide();
								jagg.showLogin();
							}
						} else {
							$('#loader').hide();
							jagg.showLogin();
						}
					}, "json");
}

function generateTpsHistogram(tps_summary_histogram) {

	var tpsSummaryHistogram = "tps-summary-histogram";
	require(
			[
			// Require the basic chart class
			"dojox/charting/Chart",

			// Require the theme of our choosing
			"dojox/charting/themes/Dollar",

			// We want to plot Lines
			"dojox/charting/plot2d/StackedAreas",

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
				var chart = new Chart(tpsSummaryHistogram);

				// Set the theme
				chart.setTheme(theme);

				// Add the only/default plot
				chart.addPlot("default", {
					type : "StackedAreas",
					markers : false
				});

				var maxArray = [];
				for (var i = 0; i < tps_summary_histogram.length; i) {
					maxArray[i] = findMax(tps_summary_histogram[i].apiHits);
				}

				var maxValue = findMax(maxArray);

				maxValue = maxValue  1;

				// Add axes
				//var xAxisLabels = createXAxisLabels(tps_summary_histogram[0].apiHitDates.reverse());
				var xAxisLabels = createXAxisLabels(tps_summary_histogram[0].apiHitDates);
				chart.addAxis("x", {
					title:"Time",
					titleOrientation:"away", 
					labels : xAxisLabels
				});
				chart.addAxis("y", {
					min : 0,
					max : maxValue,
					title:"",
					vertical : true,
					fixLower : "major",
					fixUpper : "major"
				});

				// Add the series of data
				for (var i = 0; i < tps_summary_histogram.length; i) {
					var apiHitsArray = [];
					for (var j = 0; j < tps_summary_histogram[i].apiHits.length; j) {
						apiHitsArray[j] = parseInt(tps_summary_histogram[i].apiHits[j]);
					}
					
					var histogramLineName = tps_summary_histogram[i].errorCode;
					
					chart.addSeries("TPS", apiHitsArray);
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

function findMax(hits) {
	var max = parseInt(hits[0]);
	for (var i = 0; i < hits.length; i) {
		if (parseInt(hits[i]) > max) {
			max = parseInt(hits[i]);
		}
	}
	return max;
}

function createXAxisLabels(reqDate) {
	var reqDateArray = reqDate.reverse();
	var labelArray = [];
	for (var i = 0; i < reqDateArray.length; i) {
		labelArray.push({
			text : reqDateArray[i],
			value : i  1
		});
	}
	return labelArray;
}



