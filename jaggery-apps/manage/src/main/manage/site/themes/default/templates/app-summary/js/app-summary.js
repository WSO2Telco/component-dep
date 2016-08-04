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

var populateUsageData = function(period, appName, subscriber) {
    jagg.post("/site/blocks/app-summary/ajax/app-summary.jag", {
        action:"getProviderAPIUsage",
        period:period,
        subscriber:subscriber
    }, function (result) {
        if (!result.error) {
            var usage = result.usage;
			
			var mapPie = {};
			var myData = new Array();
			var colors = ['#AF0202', '#EC7A00', '#FCD200', '#81C714', '#FF00FF' , '#00FFFF', '#C0C0C0','#41B2FA', '#63C1FA', '#83CDFA'];
			var chartColorScheme1 = ["#3da0ea","#bacf0b","#e7912a","#4ec9ce","#f377ab","#ff7373","5fd2b5","#ec7337"];
			var apiUsageGraphNode = "apiUsageGraph";

            if(usage.length==0){
                $('#showMsg').show();
                $('#usageDiv').hide();
            } else{
                $('#usageDiv').show();
                $('#showMsg').hide();  

                var subscriber = usage[0];
                var hasAPICalls = false;
            	
            	if(subscriber.applications) {
            		for (var j = 0; j < subscriber.applications.length; j++) {
            			var application = subscriber.applications[j];
            			
            			if(appName != null && appName == application.applicationname) {
            				if(application.subscriptions){
                				for (var k = 0; k < application.subscriptions.length; k++) {
                					var subscription = application.subscriptions[k];
                					
                					if(subscription.subscriptionapi in mapPie){
    									mapPie[subscription.subscriptionapi] = mapPie[subscription.subscriptionapi] + subscription.count;
    								}
    								else if(subscription.count != 0) {
    									mapPie[subscription.subscriptionapi] = subscription.count;
    									hasAPICalls = true;
    								}
                				}
                			}
            				break;
            			}
            		}
            	}
            	if(hasAPICalls) {

                    //Draw Response Time Graph
                    showResponseTimes($("#subscriber").val(), appName, $("#fromDatePick").val(), $("#toDatePick").val());

            		var count = 0;
    				
    				for (var i in mapPie) {
    					myData.push([i, mapPie[i]]);
    					count++;
    				}
    				
    				require([
                        // Require the basic chart class
                        "dojox/charting/Chart",

                        // Require the theme of our choosing
                        "dojox/charting/themes/Claro",

                        // Charting plugins:

                        //  We want to plot a Pie chart
                        "dojox/charting/plot2d/Pie",

                        // Retrieve the Legend, Tooltip, and MoveSlice classes
                        // "dojox/charting/action2d/Tooltip",
                        "dojox/charting/action2d/MoveSlice",

                        //  We want to use Markers
                        "dojox/charting/plot2d/Markers",

                        //  We'll use default x/y axes
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
                            type: Pie,
                            markers: true,
                            radius:130,
                            labelWiring: "ccc",
							labelStyle:  "columns"
                        });

                        // Add axes
                        apiUsageChart.addAxis("x");
                        apiUsageChart.addAxis("y", { min: 5000, max: 30000, vertical: true, fixLower: "major", fixUpper: "major" });

                        // Define the data
                        var chartData; var color = -1;
                        require(["dojo/_base/array"], function(array){
                            chartData= array.map(myData, function(d){
                                color++;
                                return {y: d[1], text: d[0]+" ("+d[1]+")", tooltip: "<b>"+d[0]+"</b><br /><i>"+d[1]+" call(s)</i>",fill:chartColorScheme1[color%chartColorScheme1.length]};

                            });
                        });
                        apiUsageChart.addSeries("API Usage",chartData);

                        // Create the slice mover
                        var mag = new MoveSlice(apiUsageChart,"default");

                        // Render the chart!
                        apiUsageChart.render();
                    });    			
    				
            	} else {
            		$("#"+apiUsageGraphNode).text('There is no API Usage for the application "' + appName + '".');

                    $("#responseTimesGraph").text('There is no API Usage for the application "' + appName + '".'); 
                    $("#responseTimesLegend").text('');  
            	}
            } 
        }else {
            jagg.message({content:result.message,type:"error"});
        }
    }, "json");
};

var showResponseTimes = function(subscriber, application, fromDate, toDate){
jagg.post("/site/blocks/billing/ajax/billing.jag", {
        action:"getAllResponseTimes",
        subscriber:subscriber,
        application:application,
        fromDate:fromDate,
        toDate:toDate
    }, function (result) {
        if (!result.error) {
			var data= result.data;
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

				var chartLineData = new Array(timeGroups.length);
				for (var m = 0; m < timeGroups.length; m++) {
						chartLineData[m] = 0;
				}
				for (var j=0; j<resData.length; j++){
					var time = resData[j].serviceTime;
					var count = resData[j].responseCount;
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
			// } else {
   //              	$("#"+resTimeGraphNode).text('There is no API Usage for the period "' + $("#year").val() + ' ' + $("#month option:selected").text() + '".'); 
   //              	$("#responseTimesLegend").text('');  
			// }
		}else {
			jagg.message({content:result.message,type:"error"});
		}
	
}, "json");
};