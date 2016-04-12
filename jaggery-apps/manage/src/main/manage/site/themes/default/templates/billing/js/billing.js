var selectedYear = null ;
var selectedMonth = null ;
var selectedSubscriber = null ;
//var myData = new Array();


$(document).ready(function () {


    $("#generate").click(function () {
        var year = $("#year");
        var month = $("#month");
        var subscriber = $("#subscriber");
        selectedYear = year.val();
        selectedMonth = month.val();
        selectedSubscriber = subscriber.val();

        deleteTableRows("billingBody");
        populateUsageData(selectedYear + "-" + selectedMonth, selectedSubscriber);

        //Moved below call inside populateUsageData()
		// var firstDay = $.datepicker.formatDate('yy-mm-dd', new Date(selectedYear, selectedMonth - 1, 1));
		// var lastDay = $.datepicker.formatDate('yy-mm-dd', new Date(selectedYear, selectedMonth, 0));
		//showResponseTimes(selectedSubscriber,"__ALL__",firstDay,lastDay);

    });
});


var downloadReports = function(){
	var year = $("#year");
    var month = $("#month");
    var subscriber = $("#subscriber");
	selectedYear = year.val();
    selectedMonth = month.val();
    selectedSubscriber = subscriber.val();

	document.getElementById("download_period").value = selectedYear+"-"+selectedMonth ;
	document.getElementById("selected_subscriber").value = selectedSubscriber ;
    document.getElementById("logDownloadForm").submit();

}

var populateUsageData = function(period, subscriber) {
    jagg.post("/site/blocks/billing/ajax/billing.jag", {
        action:"getProviderAPIUsage",
        period:period,
        subscriber:subscriber
    }, function (result) {
        if (!result.error) {
            var usage = result.usage;

            var i;
            var tbody = document.getElementById("billingBody");
            var total=0.00;
			var pieData = new Array();
			var currency = "$";

			var hasAPICalls = false;
			var mapPie = {};
			var myData = new Array();
			var colors = ['#AF0202', '#EC7A00', '#FCD200', '#81C714', '#FF00FF' , '#00FFFF', '#C0C0C0','#41B2FA', '#63C1FA', '#83CDFA'];
			var chartColorScheme1 = ["#3da0ea","#bacf0b","#e7912a","#4ec9ce","#f377ab","#ff7373","5fd2b5","#ec7337"];
			var apiUsageGraphNode = "apiUsageGraph";

			$('#billingBody').innerHTML = '';

            if(usage.length==0){
                $('#showMsg').show();
                $('#usageDiv').hide();
            } else{
                $('#usageDiv').show();
                $('#showMsg').hide();

                for (var i = 0; i < usage.length; i++) {

                	var subscriber = usage[i];
					var totalPrice = 0;
					var totalTax = 0;

	               	if(subscriber.applications.length > 0){

                		for (var j = 0; j < subscriber.applications.length; j++) {

                			var application = subscriber.applications[j];

                        	if(application.subscriptions){
                        		for (var k = 0; k < application.subscriptions.length; k++) {
                        			var subscription = application.subscriptions[k];

                        			var row = document.createElement("tr");

                                    var cell1 = document.createElement("td");
                                    if(k==0 && j==0){
                                    	cell1.innerHTML = subscriber.subscriber;
                                    }
                                    row.appendChild(cell1);

                                    var cell2 = document.createElement("td");
                                    if(k==0){
										//AppSummary
										var a = document.createElement('a');
										var linkText = document.createTextNode(application.applicationname);
										a.appendChild(linkText);
										var URL = "app-summary.jag?AppName=";
										var appName = application.applicationname;
										var subscriberID = subscriber.subscriber;
										var appSummary = URL + appName + "&" + "subscriber=" + subscriberID + "&year=" + selectedYear + "&month=" + selectedMonth;

										a.href = appSummary;
                                    	//cell2.innerHTML = application.applicationname;
										cell2.appendChild(a);
                                    }
                                    row.appendChild(cell2);


                                    var cell3 = document.createElement("td");
                                    cell3.innerHTML = subscription.subscriptionapi;
                                    row.appendChild(cell3);

                                    var cell4 = document.createElement("td");
                                    cell4.innerHTML = subscription.subscriptionapiversion;
                                    row.appendChild(cell4);

                                    var cell5 = document.createElement("td");
                                    cell5.innerHTML = subscription.charginplan;
                                    row.appendChild(cell5);

                                    var cell6 = document.createElement("td");
                                    cell6.innerHTML = subscription.count;
                                    row.appendChild(cell6);


                                    var cell7 = document.createElement("td");
                                    cell7.innerHTML = currency.concat((subscription.price).toFixed(2));
                                    row.appendChild(cell7);

                                    //add tax
                                    var cell8 = document.createElement("td");
                                    cell8.innerHTML = currency.concat((subscription.tax).toFixed(2));
                                    row.appendChild(cell8);

                                    var cell9 = document.createElement("td");
                                    row.appendChild(cell9);

                                    totalPrice = totalPrice + subscription.price;
                                    totalTax = totalTax + subscription.tax;

									if(subscription.subscriptionapi in mapPie){
										mapPie[subscription.subscriptionapi] = mapPie[subscription.subscriptionapi] + subscription.count;
									}
									else if(subscription.count != 0) {
										mapPie[subscription.subscriptionapi] = subscription.count;
										hasAPICalls = true;
									}

									//Total


                                    tbody.appendChild(row);
                        		}

                        	}

                		}
                		var rowTotal = document.createElement("tr");

						var cell1 = document.createElement("td");
						rowTotal.appendChild(cell1);

						var cell2 = document.createElement("td");
						rowTotal.appendChild(cell2);

						var cell3 = document.createElement("td");
						rowTotal.appendChild(cell3);

						var cell4 = document.createElement("td");
						rowTotal.appendChild(cell4);

						var cell5 = document.createElement("td");
						rowTotal.appendChild(cell5);

					   	var cellw = document.createElement("td");
						cellw.innerHTML = "Total Amount".bold();
						rowTotal.appendChild(cellw);

						var cell6 = document.createElement("td");
						cell6.innerHTML = currency.concat(totalPrice.toFixed(2)).bold();
						rowTotal.appendChild(cell6);

						var cell7 = document.createElement("td");
						cell7.innerHTML = currency.concat(totalTax.toFixed(2)).bold();
						rowTotal.appendChild(cell7);

                        var cell8 = document.createElement("td");
                        cell8.innerHTML = currency.concat((totalPrice + totalTax).toFixed(2)).bold();
                        rowTotal.appendChild(cell8);

						tbody.appendChild(rowTotal);
						
						totalPrice = 0;
						totalTax = 0;

                	}
                }
                
                
            }

        }else {
            jagg.message({content:result.message,type:"error"});


        }
    }, "json");
};

function get_random_color() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.round(Math.random() * 15)];
    }
    return color;
}

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


var showPieChart = function(){
jagg.post("/site/blocks/billing/ajax/billing.jag", {
        action:"getProviderAPIUsage",
        period:period
    }, function (result) {
        if (!result.error) {
			var responseTimesAPI = result.responseTimes;

		}else {
			jagg.message({content:result.message,type:"error"});
		}

}, "json");
};

var deleteTableRows = function(tbodyId) {
    $("#"+tbodyId+"").empty();


}

var getAllSubscribers = function(){
	jagg.post("/site/blocks/billing/ajax/billing.jag", {
		action:"getAllSubscribers",
	}, function (result) {
		if (!result.error) {
			var data= result.data;
			data.sort(function (a, b) {
				return a.toLowerCase().localeCompare(b.toLowerCase());
			});

			var dropdown = document.getElementById("subscriber");
			for (var i = 0; i < data.length; i++) {
				var userId = data[i];
				var item = new Option(userId, userId);
				dropdown.appendChild(item);
			}


		}else {
			jagg.message({content:result.message,type:"error"});
		}

	}, "json");
};
