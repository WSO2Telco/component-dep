operator_db.r1_c2 = {};
operator_db.r1_c2.init = function(){
    require(["dojox/charting/Chart",
            "dojox/charting/axis2d/Default",
            "dojox/charting/plot2d/Lines",
            "dojo/ready",
            "dojox/charting/plot2d/Pie",
            "dojo/dom-construct"],
        function(Chart, Default, Lines, ready, Pie,domConstruct){
            ready(function(){
                domConstruct.empty("r1_c2");

                var weekday = new Array(7);
                weekday[0]=  "Sunday";
                weekday[1] = "Monday";
                weekday[2] = "Tuesday";
                weekday[3] = "Wednesday";
                weekday[4] = "Thursday";
                weekday[5] = "Friday";
                weekday[6] = "Saturday";


                var requestData = {};
                $("#r1_c2_wrapper span[data-field-name]").each(function(){
                    requestData[$(this).attr("data-field-name")] = $(this).text().replace(/^\s+|\s+$/gm,'');
                });
                requestData.action = "getAPITrafic";

                operator_db.xhr_get({
                    url: '/manage/site/blocks/home/ajax/chart_r1_c2_ajax.jag',
                    id:"r1_c2",
                    data: requestData,
                    dataType: "json"}).done(function (data) {
                    var labelfTime = function (o) {
                        var dt = new Date();
                        dt.setTime(o);
                        var d = (dt.getMonth() + 1) + "/" + dt.getDate() + "/" + dt.getFullYear() + "<br><center>(" + weekday[dt.getDay()] + ")</center>";
                        return d;
                    };

                    var chart1 = new Chart("r1_c2");
                    chart1.addPlot("default", {type: Lines});
                    // set up axis and specify label function for dates
                    chart1.addAxis("x", {min:data.startDate, max: data.endDate, labelFunc: labelfTime});
                    chart1.addAxis("y", { min:0, vertical: true});
                    chart1.addSeries("Series 1", data.pts);
                    chart1.render();
                });

            });
        });
};




$(document).ready(function(){
	operator_db.r1_c2.init();
	operator_db.chartBox.initDropdown('operator_r1_c2',function(){
		operator_db.r1_c2.init();
    });

	operator_db.chartBox.initDropdown('time_r1_c2',function(){
		operator_db.r1_c2.init();
    });
});