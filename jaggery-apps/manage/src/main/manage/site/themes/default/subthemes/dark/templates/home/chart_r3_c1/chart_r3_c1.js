axiata_db.r3_c1 = {};
axiata_db.r3_c1.init = function(){




    var requestData = {};
    $("#r3_c1_wrapper span[data-field-name]").each(function(){
        requestData[$(this).attr("data-field-name")] = $(this).text().replace(/^\s+|\s+$/gm,'');
    });
    requestData.action = "getAPITrafic";

//    $('#r3_c1 .single-pie-container').get(0)
    axiata_db.xhr_get({
        url: '/manage/site/blocks/home/ajax/chart_r3_c1_ajax.jag',
        id:"r3_c1",
        data: requestData,
        dataType: "json"}).done(function (data) {
        var $pieContainer = $('<div class="single-box-container"></div>').appendTo('#r3_c1');
        var $barContainer = $('<div class="single-bar-container"></div>').appendTo('#r3_c1');
        require([
                "dojox/charting/Chart",
                "dojox/charting/plot2d/Pie",
                "dojox/charting/action2d/Tooltip",
                "dojox/charting/themes/Julie",
                "dojo/ready",
                "dojo/dom-construct",
                "dojox/charting/plot2d/StackedBars",
                "dojo/fx/easing",
                "dojox/charting/action2d/MoveSlice"
            ],
            function(Chart, Pie, Tooltip, Julie, ready,domConstruct,StackedBars,easing,MoveSlice){

                var pieChart = null;
                var legend = null;
                var barChart = null;

                ready(function(){
                    var $boxContainer = $('#r3_c1 .single-box-container');
                    var barContainer = $('#r3_c1 .single-bar-container').get(0);
                    domConstruct.empty($boxContainer.get(0));
                    domConstruct.empty(barContainer);

                    $boxContainer.append(
                        '<h4>Slowest APIs</h4>'
                        );

                    $boxContainer.append(data.slowestApiHtml);
 
                    var xLabelFunc = function(text, value, precision){
                        return text + "ms";
                    };

                    new Chart(barContainer)
                        .addPlot("default", { type: StackedBars, tension: "X" ,animate: { duration: 1000, easing: easing.linear}, gap:5, minBarSize: 3, maxBarSize: 20 ,
                            markers: true,
                            labels: true,
                            labelStyle:"inside",
                            //maxBarSize: 35,
                            tension: "2"})
                        .setTheme(Julie)
                        .addAxis("x",{min:0, labelFunc:xLabelFunc})
                        .addAxis("y",{                 
                            min:0,
                            vertical:true,
                            labels: data.yAxisLables

                        })
                        .addSeries("Series A", data.yAxisData)
                        .render();

                });
            });
    });
};




$(document).ready(function(){
    axiata_db.r3_c1.init();
    axiata_db.chartBox.initDropdown('operator_r3_c1',function(){
        axiata_db.r3_c1.init();
    });

    axiata_db.chartBox.initDropdown('time_r3_c1',function(){
        axiata_db.r3_c1.init();
    });
});