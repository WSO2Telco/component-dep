operator_db.r2_c2 = {};
operator_db.r2_c2.init = function(){




    var requestData = {};
    $("#r2_c2_wrapper span[data-field-name]").each(function(){
        requestData[$(this).attr("data-field-name")] = $(this).text().replace(/^\s+|\s+$/gm,'');
    });
    requestData.action = "getAPITrafic";

//    $('#r2_c2 .single-pie-container').get(0)
    operator_db.xhr_get({
        url: '/manage/site/blocks/home/ajax/chart_r2_c2_ajax.jag',
        id:"r2_c2",
        data: requestData,
        dataType: "json"}).done(function (data) {
        var $pieContainer = $('<div class="single-pie-container"></div>').appendTo('#r2_c2');
        var $barContainer = $('<div class="single-bar-container"></div>').appendTo('#r2_c2');


                require([
                        "dojox/charting/Chart",
                        "dojox/charting/plot2d/Pie",
                        "dojox/charting/action2d/Tooltip",
                        "dojox/charting/themes/Julie",
                        "dojo/ready",
                        "dojo/dom-construct",
                        "dojox/charting/plot2d/StackedColumns",
                        "dojo/fx/easing",
                        "dojox/charting/action2d/MoveSlice"
                    ],
                    function(Chart, Pie, Tooltip, Julie, ready,domConstruct,StackedColumns,easing,MoveSlice){

                        var pieChart = null;
                        var legend = null;
                        var barChart = null;
                        domConstruct.empty($('#r2_c2 .single-pie-container').get(0));

                        ready(function(){
                            var pieContainer = $('#r2_c2 .single-pie-container').get(0);
                            var barContainer = $('#r2_c2 .single-bar-container').get(0);
                            domConstruct.empty(pieContainer);

                            pieChart = new Chart(pieContainer);
                            pieChart.setTheme(Julie).addPlot("default", {
                                type: "Pie",
                                font: "normal normal 10px Tahoma",
                                fontColor: "#444",
                                labelWiring: "#444",
                                radius: 100,
                                labelStyle: "columns",
                                htmlLabels: true,
                                startAngle: -1,
                                animate: { duration: 1000, easing: easing.linear}
                            }).addSeries("Series A", data.pie);

                            var anim_c = new Tooltip(pieChart, "default");
                            var mag = new MoveSlice(pieChart,"default");
                            pieChart.render();


                            /*
                            new Chart(barContainer)
                                .addPlot("default", { type: StackedColumns, tension: "X" ,animate: { duration: 1000, easing: easing.linear}, gap:5, minBarSize: 3, maxBarSize: 20 ,
                                    markers: true,
                                    labels: true,
                                    labelStyle:"inside",
                                    //maxBarSize: 35,
                                    tension: "2"})
                                .setTheme(Julie)
                                .addAxis("x",{
                                    labels: [{value: 1, text: "smsmessage"}, {value: 2, text: "payment"},
                                        {value: 3, text: "mmsmessage"}]

                                })
                                .addAxis("y",{vertical:true})
                                .addSeries("Series A", [100, 70, 105 ])
                                .addSeries("Series C", [90, 80, 95 ])
                                .render();
                                */

                        });
                    });
    });
};




$(document).ready(function(){
	operator_db.r2_c2.init();
	operator_db.chartBox.initDropdown('operator_r2_c2',function(){
		operator_db.r2_c2.init();
    });

	operator_db.chartBox.initDropdown('time_r2_c2',function(){
		operator_db.r2_c2.init();
    });
});