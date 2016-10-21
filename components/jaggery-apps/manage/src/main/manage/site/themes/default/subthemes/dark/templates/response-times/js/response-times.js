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

function performResponseTimes(){
	$('#responseTimesGraph').hide();
	$('#loader').show();
	var fromDate = $('#from_date').val();
	var toDate = $('#to_date').val();
	var operator = $('#operatorSelect').val();
	var sp = $('#subscriberSelect').val();
	var app = $('#appSelect').val();
	
	//alert(fromDate+" : "+toDate+" : "+operator+" : "+sp+" : "+app);
	showResponseTimes(operator, sp, app, fromDate, toDate);
}

var showResponseTimes = function(operator, subscriber, application, fromDate, toDate){
	
	jagg.post("/site/blocks/billing/ajax/billing.jag", {
	        action:"getAllResponseTimes",
	        operator:operator,
	        subscriber:subscriber,
	        application:application,
	        fromDate:fromDate,
	        toDate:toDate
	    }, function (result) {
	    	$('#loader').hide();
	    	$('#responseTimesGraph').show();
	        if (!result.error) {
				var data= result.data;
				var isNoData = true;
				var colors = ['#AF0202', '#EC7A00', '#FCD200', '#81C714', '#FF00FF' , '#00FFFF', '#C0C0C0','#41B2FA', '#63C1FA', '#83CDFA'];
				var timeGroups = [0,10,20,30,50,100,200,300,500,1000];
				var hasChartData = false;
				var chartData = {};
				var resTimeGraphNode = "responseTimesGraph";


				for (var i = 0; i < data.length; i++) {				
					var barChartData = new Array();
					var api = data[i];

					var resData = api.responseData;
					if (resData.length==0) { 
						continue; 
					}
					if(resData.length > 0){
						isNoData = false;
					}

					var chartLineData = new Array(timeGroups.length);
					for (var m = 0; m < timeGroups.length; m++) {
							chartLineData[m] = 0;
					}
					for (var j=0; j<resData.length; j++){
						var time = resData[j].serviceTime;
						var count = resData[j].responseCount;

				    	//alert("Hi2"+time+"::"+count);
						for (var k = 0; k < timeGroups.length; k++) {
							if ((timeGroups[k] - time) > 0) {
								chartLineData[k-1] = chartLineData[k-1] + count;
								break;
							}
							if (k==timeGroups.length-1){
								chartLineData[k] = chartLineData[k] + count; 
								break;
							}
						};
					}			

					chartData[api.apiName] = chartLineData;
									
					hasChartData = true;
					// var randomColor = '#'+((Math.random() * i).toString(16) + '0000000').slice(2, 8);
					// myChartBar.setLineColor(randomColor,api.apiName);
				}

				// if (hasChartData) {
				require([
	                        // Require the basic chart class
	                        "dojox/charting/Chart",

	                        // Require the theme of our choosing
	                        "dojox/charting/themes/ApimDefault",

	                        // // Tooltip
	                        // "dojox/charting/action2d/Tooltip",
	                        // // Require the highlighter
	                        // "dojox/charting/action2d/Highlight",

	                        //  We want to plot lines
	                        "dojox/charting/plot2d/Lines",

	                        //  We want to use Markers
	                        "dojox/charting/plot2d/Markers",

	                        //  We'll use default x/y axes
	                        "dojox/charting/axis2d/Default",

	                        // //mouse zoom and pan
	                        // "dojox/charting/action2d/MouseZoomAndPan",

	                        "dojox/charting/widget/Legend",

	                        "dijit/registry", 
	    					"dojo/domReady!"

	                    ], function(Chart, theme,Lines,Legend,registry) {    
	                    		// Destroy existing graph
								dojo.empty(resTimeGraphNode);

	                    		// Create the chart within it's "holding" node
	                    		var serviceTimeChart = new Chart(resTimeGraphNode);
	                    		serviceTimeChart.setTheme(theme);

	                    		serviceTimeChart.addPlot("default", {
							    	type: Lines,
	                            	markers: true,
	                            	// animate:{duration:1000}
							    });
	                    		for (var key in chartData) {
	                    			if (chartData.hasOwnProperty(key)) {
	                    				serviceTimeChart.addSeries(key, chartData[key]);
	                    			}
								}
							    serviceTimeChart.addAxis("x", {title:"ms", titleOrientation:"away", dropLabels:false, minorTicks:false, minorLabels:false, 
							    	labelFunc: function(text, value, precision){
							    		var suffix = (text != timeGroups.length) ? "-"+timeGroups[text] : " or more";
										return timeGroups[text-1] + suffix;
									}
								});
							    serviceTimeChart.addAxis("y", {title:"count",vertical: true});
							    // serviceTimeChart.addSeries("Series 1", [1, 2, 2, 3, 4, 5, 5, 7]);
							    serviceTimeChart.render();

							    // Create the legend
							    var legend = dijit.registry.byId("responseTimesLegend");
								if (legend) {
								   legend.destroyRecursive(true);
								} 
	    						var legend = new dojox.charting.widget.Legend({ chart: serviceTimeChart, horizontal:false },"responseTimesLegend");
	                    });
				if(isNoData){
	                 	$("#"+resTimeGraphNode).text('There is no API Usage for the period'); 
	                 	$("#responseTimesLegend").text(''); 
				 }
			}else {
				jagg.message({content:result.message,type:"error"});
			}
		
	}, "json");
	};
