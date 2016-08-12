var selectedYear = null ;
var selectedMonth = null ;
var selectedSubscriber = null ;
//var myData = new Array();


$(document).ready(function () {
    //loadMonthPicker();      

    $("#generate").click(function () {
    	
    	var operatorName =$("#isOperator").val();
    	
        $('#showMsg').hide();
        $('#usageDiv').hide();
         $('#loader').show();
        var year = $("#year");
        var month = $("#month");
        var subscriber = $("#subscriber");
        //var year_month = month.val();
        //var arr = year_month.split("-");
        selectedYear =  year.val(); //arr[0];
        selectedMonth = month.val(); //arr[1];
        selectedSubscriber = subscriber.val();

        deleteTableRows("billingBody");
        //populateUsageData(year_month, selectedSubscriber,operatorName);
        populateUsageData(selectedYear + "-" + selectedMonth, selectedSubscriber,operatorName);

    //Moved below call inside populateUsageData()
    // var firstDay = $.datepicker.formatDate('yy-mm-dd', new Date(selectedYear, selectedMonth - 1, 1));
    // var lastDay = $.datepicker.formatDate('yy-mm-dd', new Date(selectedYear, selectedMonth, 0));
    //showResponseTimes(selectedSubscriber,"__ALL__",firstDay,lastDay);

    });	
});

function loadMonthPicker () {
    $('.monthPicker').datepicker({
        dateFormat: "yy-mm",
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        onClose: function (dateText, inst) {
            function isDonePressed() {
                return ($('#ui-datepicker-div').html().indexOf('ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all ui-state-hover') > -1);
            }

            if (isDonePressed()) {
                 $(this).val($.datepicker.formatDate('yy-mm', new Date(inst.selectedYear, inst.selectedMonth, 1)));
            }
        },

        beforeShow: function (input, inst) {
            if ((datestr = $(this).val()).length > 0) {
                year = datestr.substring(0, 4);
                month = datestr.substring(datestr.length - 2, datestr.length);
                $(this).datepicker('option', 'defaultDate', new Date(year, month - 1, 1));
                $(this).datepicker('setDate', new Date(year, month - 1, 1));          
                $(".ui-datepicker-calendar").hide();
            }
        }
    });

    $(".monthPicker").focus(function () {
        $(".ui-datepicker-calendar").hide();
    });
    $(".monthPicker").datepicker('setDate', new Date());
    
}

	
var downloadReports = function(){
    var year = $("#year");
    var month = $("#month");
    var subscriber = $("#subscriber");
    selectedYear = year.val();
    selectedMonth = month.val();
    selectedSubscriber = subscriber.val();
    
    document.getElementById("download_period").value = selectedYear+"-"+selectedMonth; //month.val() ;
    document.getElementById("selected_subscriber").value = selectedSubscriber; //subscriber.val() ;
    document.getElementById("logDownloadForm").submit();
	
}

var populateUsageData = function(period, subscriber,operatorName) {
    jagg.post("/site/blocks/billing/ajax/billing.jag", {
        action:"getProviderAPIUsage",
        period:period,
        subscriber:subscriber,
        operatorName:operatorName
    }, function (result) {
            $('#loader').hide();
            $('#usageDiv').show();
        if (!result.error) {
            var usage = result.usage;

            var i;
            var tbody = document.getElementById("billingBody");
            var total=0.00;
            var pieData = new Array();
            var currency = "";
			
            var hasAPICalls = false;
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
						
                for (var i = 0; i < usage.length; i++) {
                	
                    var subscriber = usage[i];
					
                    if(subscriber.applications.length > 0){
                        var totalPrice = 0;
                        var totalTax = 0;
                        var tatalsubCredit = 0;
                        var currencyTotals = {};

                        for (var j = 0; j < subscriber.applications.length; j++) {

                            var application = subscriber.applications[j];
                			
                            if(application.subscriptions){
                                for (var k = 0; k < application.subscriptions.length; k++) {
                                    var subscription = application.subscriptions[k];
                        			
                                    if(subscription.operators){
                                        for (var l = 0; l < subscription.operators.length; l++) {
                                            var operatorSub = subscription.operators[l];
                                            //var operationid = operatorSub.operationid;
                                            currency = operatorSub.currency;

                                            var row = document.createElement("tr");
                                			
                                            var cell1 = document.createElement("td");
                                            if(k==0 && j==0 && l==0){
                                                cell1.innerHTML = subscriber.subscriber;
                                            }
                                            row.appendChild(cell1);
                                            
                                            var cell2 = document.createElement("td");
                                            if(k==0 && l==0){
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
                                            var cell4 = document.createElement("td");
                                            var cell5 = document.createElement("td");
                                            
                                            if(l==0){
                                                cell3.innerHTML = subscription.subscriptionapi;
                                                cell4.innerHTML = subscription.subscriptionapiversion;
                                                
                                                
                                                cell5.innerHTML = operatorSub.operator;
                                                
                                            }
                                            row.appendChild(cell3);
                                            row.appendChild(cell4);
                                            row.appendChild(cell5);

                                            
                                            /*var cell5 = document.createElement("td");
                                            cell5.innerHTML = operatorSub.operator;
                                            row.appendChild(cell5);*/
                                            
                                            var cell50 = document.createElement("td");
                                            cell50.innerHTML = operatorSub.operationid;
                                            row.appendChild(cell50);
                                            
                                            /*var cell51 = document.createElement("td");
                                            cell51.innerHTML = operatorSub.count;
                                            row.appendChild(cell51);*/
                                            
                                            var cell6 = document.createElement("td");
                                            cell6.innerHTML = operatorSub.ratename;
                                            row.appendChild(cell6);

                                            var cell7 = document.createElement("td");
                                            cell7.innerHTML = operatorSub.count;
                                            row.appendChild(cell7);
        									
                                            var cell52 = document.createElement("td");
                                            cell52.innerHTML = operatorSub.opcom;
                                            row.appendChild(cell52);
                                            
                                            var cell8 = document.createElement("td");
                                            cell8.innerHTML = currency+" "+(operatorSub.price).toFixed(2);
                                            cell8.setAttribute('class','tdright');
                                            row.appendChild(cell8);
                                                                                
                                            //add tax
                                            var cell9 = document.createElement("td");
                                            cell9.innerHTML = currency+" "+(operatorSub.tax).toFixed(2);
                                            cell9.setAttribute('class','tdright');
                                            row.appendChild(cell9);
                                            
                                            var cell10 = document.createElement("td");
                                            cell10.innerHTML = currency+" "+(operatorSub.credit).toFixed(2);
                                            cell10.setAttribute('class','tdright');
                                            row.appendChild(cell10);
                                            
                                            var cell11 = document.createElement("td");
                                            row.appendChild(cell11);
                                            
                                            // totalPrice = totalPrice + operatorSub.price;
                                            // totalTax = totalTax + operatorSub.tax;
                                            // tatalsubCredit = tatalsubCredit + operatorSub.credit;
                                            if(currency in currencyTotals) {
                                                var currencyTotal = currencyTotals[currency];
                                                currencyTotal['price'] += operatorSub.price;
                                                currencyTotal['tax'] += operatorSub.tax;
                                                currencyTotal['credit'] += operatorSub.credit;
                                            }
                                            else {
                                                currencyTotals[currency] = 
                                                    {'price':operatorSub.price,'tax':operatorSub.tax, 'credit':operatorSub.credit};
                                            }

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
                        	
                            }
                			
                        }
                        var rowTotal = document.createElement("tr");
						
                        var cell1 = document.createElement("td");
                        rowTotal.appendChild(cell1);
						
                        var cell2 = document.createElement("td");
                        rowTotal.appendChild(cell2);
						
                        var cell3 = document.createElement("td");
                        rowTotal.appendChild(cell3);
						
                        /*var cell4 = document.createElement("td");
                        rowTotal.appendChild(cell4);*/
						
                        var cell5 = document.createElement("td");
                        rowTotal.appendChild(cell5);

                        var cell6 = document.createElement("td");
                        rowTotal.appendChild(cell6);

                        var cell60 = document.createElement("td");
                        rowTotal.appendChild(cell60);
                        
                        var cell61 = document.createElement("td");
                        rowTotal.appendChild(cell61);
                        
                        var cell62 = document.createElement("td");
                        rowTotal.appendChild(cell62);
                        
                        var cell7 = document.createElement("td");
                        cell7.innerHTML = "Total Amount".bold();
                        rowTotal.appendChild(cell7);

                        var allPrices = "";
                        var allTaxes = "";
                        var allCredits = "";
                        var grandTotals = "";
                        for (var currKey in currencyTotals) {
                            if (currencyTotals.hasOwnProperty(currKey)) {
                                var currValue = currencyTotals[currKey];
                                allPrices = allPrices+currKey+" "+currValue['price'].toFixed(2)+'<br>';
                                allTaxes = allTaxes+currKey+" "+currValue['tax'].toFixed(2)+'<br>';
                                allCredits = allCredits+currKey+" "+currValue['credit'].toFixed(2)+'<br>';
                                var gTotal = currValue['price']+currValue['tax'];
                                grandTotals = grandTotals+currKey+" "+gTotal.toFixed(2)+'<br>';
                            }
                        }
						
                        var cell8 = document.createElement("td");
                        cell8.innerHTML = allPrices.bold();
                        cell8.setAttribute('class','tdright');
                        rowTotal.appendChild(cell8);
						
                        var cell9 = document.createElement("td");
                        cell9.innerHTML = allTaxes.bold();
                        cell9.setAttribute('class','tdright');
                        rowTotal.appendChild(cell9);
                                                
                        var cell10 = document.createElement("td");
                        cell10.innerHTML = allCredits.bold();
                        cell10.setAttribute('class','tdright');
                        rowTotal.appendChild(cell10);                                                

                        var cell11 = document.createElement("td");
                        cell11.innerHTML = grandTotals.bold();
                        cell11.setAttribute('class','tdright');
                        rowTotal.appendChild(cell11);

                        tbody.appendChild(rowTotal);
                        
                        allPrices = 0.00;
                        allTaxes = 0.00;
                        allCredits = 0.00;
                        grandTotals=0.00;

                    }
                }
                
                /**
                if(hasAPICalls) {

                    //Draw Response Time Graph
                    var firstDay = $.datepicker.formatDate('yy-mm-dd', new Date(selectedYear, selectedMonth - 1, 1));
                    var lastDay = $.datepicker.formatDate('yy-mm-dd', new Date(selectedYear, selectedMonth, 0));
                    showResponseTimes(selectedSubscriber,"__ALL__",firstDay,lastDay);

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
                            apiUsageChart.addAxis("y", {
                                min: 5000, 
                                max: 30000, 
                                vertical: true, 
                                fixLower: "major", 
                                fixUpper: "major"
                            });

                            // Define the data
                            var chartData;
                            var color = -1;
                            require(["dojo/_base/array"], function(array){
                                chartData= array.map(myData, function(d){
                                    color++;
                                    return {
                                        y: d[1], 
                                        text: d[0]+" ("+d[1]+")", 
                                        tooltip: "<b>"+d[0]+"</b><br /><i>"+d[1]+" call(s)</i>",
                                        fill:chartColorScheme1[color%chartColorScheme1.length]
                                        };

                                });
                            });
                            apiUsageChart.addSeries("API Usage",chartData);

                            // Create the slice mover
                            var mag = new MoveSlice(apiUsageChart,"default");

                            // Render the chart!
                            apiUsageChart.render();
                        });    				
    				
                } else {
                    $("#"+apiUsageGraphNode).text('There is no API Usage for the period "' + $("#year").val() + ' ' + $("#month option:selected").text() + '".');

                    $("#responseTimesGraph").text('There is no API Usage for the period "' + $("#year").val() + ' ' + $("#month option:selected").text() + '".'); 
                    $("#responseTimesLegend").text(''); 
                }
                **/
            } 

        }else {
            jagg.message({
                content:result.message,
                type:"error"
            });


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
                    serviceTimeChart.addAxis("x", {
                        title:"ms", 
                        titleOrientation:"away", 
                        dropLabels:false, 
                        minorTicks:false, 
                        minorLabels:false, 
                        labelFunc: function(text, value, precision){
                            var suffix = (text != timeGroups.length) ? "-"+timeGroups[text] : " or more";
                            return timeGroups[text-1] + suffix;
                        }
                    });
                    serviceTimeChart.addAxis("y", {
                        title:"count",
                        vertical: true
                    });
                    // serviceTimeChart.addSeries("Series 1", [1, 2, 2, 3, 4, 5, 5, 7]);
                    serviceTimeChart.render();

                    // Create the legend
                    var legend = dijit.registry.byId("responseTimesLegend");
                    if (legend) {
                        legend.destroyRecursive(true);
                    } 
                    var legend = new dojox.charting.widget.Legend({
                        chart: serviceTimeChart, 
                        horizontal:false
                    },"responseTimesLegend");
                });
        // } else {
        //              	$("#"+resTimeGraphNode).text('There is no API Usage for the period "' + $("#year").val() + ' ' + $("#month option:selected").text() + '".'); 
        //              	$("#responseTimesLegend").text('');  
        // }
							
        }else {
            jagg.message({
                content:result.message,
                type:"error"
            });
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
            jagg.message({
                content:result.message,
                type:"error"
            });
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
            jagg.message({
                content:result.message,
                type:"error"
            });
        }

    }, "json");
};
