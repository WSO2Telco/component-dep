$(document).ready(function() {
	//loadMonthPicker();
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

function getSelectedSubscriber() {
	var subscriber = $("#subscriberSelect").val();
	// alert(subscriber);
	if (subscriber.trim() != "__ALL__") {
		var action = "getAppsBySubscriber";
		jagg.post("/site/blocks/finance-nb/ajax/finance.jag", {
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
		var option = '<option value="__All__">All</option>';
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

function generateApiTrafficChart(period, subscriber, operator, app) {
	var action = "getApiRevenueData";
	jagg.post("/site/blocks/finance-nb/ajax/finance.jag", {
		action : action,
		period : period,
		subscriber : subscriber,
		operator : operator,
		application : app
	}, function(result) {
		$('#loader').hide();
		$('#apiUsageGraph').show();
		if (!result.error) {
			if(result.usage != null){
				//Test data
				//var testArray = [["aaa", 1450],["bbbb", 7458],["cccc",5450],["ddd",950]];
				var dataArray = [];
				var object = result.usage;
				$.each(object, function(key, value){
					//alert(value);
					var object_in = value;
					$.each(object_in, function(keyN, valueN){
						//alert(keyN +","+ valueN);
						var rowVal = [keyN , valueN];
						dataArray.push(rowVal);
					});
				});
				//alert(dataArray);
				createAPITrafficChart(dataArray);
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
}

function generateFinanceReport() {
	$('#apiUsageGraph').hide();
	$('#loader').show();
	// alert("generateFinanceReport");
	var year = $("#yearSelect").val();
	var month = $("#monthSelect").val();
	var subscriber = $("#subscriberSelect").val();
	var operator = $("#operatorSelect").val();
	var app = $("#appSelect").val();

	//var period = month;
	var period = year + "-" + month;

	//alert(year + " :: " + month + " :: " + subscriber + " :: " + operator + " :: " + app + " :: " + period);
	
	//var testArray = [["aaa", 1450],["bbbb", 7458],["cccc",5450],["ddd",950]];
	//generateAPITrafficChart($.makeArray(testArray));
	
	generateApiTrafficChart(period, subscriber, operator, app);
	
	// generateFinanceReportTable(period, subscriber);
}

function createAPITrafficChart(dataMap) {
	
	var apiUsageGraphNode = "apiUsageGraph";
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
				if (d[0] == 'payment' ) {
					d[0] = "<br>"+d[0];
				}
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
