axiata_db.r2_c1 = {};
axiata_db.r2_c1.init = function(){
    var requestData = {};
    $("#r2_c1_wrapper span[data-field-name]").each(function(){
        requestData[$(this).attr("data-field-name")] = $(this).text().replace(/^\s+|\s+$/gm,'');
    });
    requestData.action = "getAPITrafic";

    axiata_db.xhr_get({
        url: '/manage/site/blocks/home/ajax/chart_r2_c1_ajax.jag',
        id:"r2_c1",
        data: requestData,
        dataType: "json"}).done(function (data) {
        for(var i=0;i<data.length;i++){
            var $radialContainer = $(
                '<div id="r2_c1_'+i+'" class="radial-box radial-box-big">' +
                    '<div class="radial-top">' +
                        '<div class="radial-container"></div>' +
                        '<div class="pie-container" style="width: 200px;height: 160px;"></div>' +
                        '<div class="legend-container"></div>' +
                        '<div style="clear: both"></div>' +
                    '</div>' +
                    '<div class="radial-bottom">'+data[i].api+'</div>' +
                '</div>')
                .appendTo("#r2_c1");

            radialProgress($radialContainer.find('.radial-container').get(0))
                .diameter(150)
                .label(data[i].currency + " " + data[i].amount)
                .value(data[i].pt)
                .render();



            require(["dojox/charting/Chart", "dojox/charting/plot2d/Pie",
                    "dojox/charting/action2d/Tooltip",
                    "dojox/charting/themes/Julie",
                     "dojo/ready",
                    "dojo/dom-construct",
                    "dojox/charting/action2d/MoveSlice"],
                (function(){    //Doing this to make the i variable going out of scope inside the callback
                    var $radialContainer_tmp = $radialContainer;
                    var breakdown = data[i].breakdown;
                    return function(Chart, Pie, Tooltip, Julie, ready,domConstruct,MoveSlice){
                        var pieChart = null;

                        domConstruct.empty(breakdown);

                        ready(function(){
                            var chartContainer = $radialContainer_tmp.find('.pie-container').get(0);
                            domConstruct.empty(chartContainer);

                            pieChart = new Chart(chartContainer);
                            pieChart.setTheme(Julie).addPlot("default", {
                                type: "Pie",
                                font: "normal normal 10px Tahoma",
                                fontColor: "#444",
                                labelWiring: "#444",
                                radius: 50,
                                labelStyle: "columns",
                                htmlLabels: true,
                                startAngle: -1
                            }).addSeries("Series A", breakdown);
                            var anim_c = new Tooltip(pieChart, "default");
                            var mag = new MoveSlice(pieChart,"default");
                            pieChart.render();

                        });
                    };
                })()

            );
        }
    });
};

$(document).ready(function(){
    axiata_db.r2_c1.init();
    axiata_db.chartBox.initDropdown('operator_r2_c1',function(){
        axiata_db.r2_c1.init();
    });

    axiata_db.chartBox.initDropdown('time_r2_c1',function(){
        axiata_db.r2_c1.init();
    });
});