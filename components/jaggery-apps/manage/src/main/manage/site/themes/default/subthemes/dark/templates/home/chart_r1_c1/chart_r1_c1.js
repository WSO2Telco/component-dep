operator_db.r1_c1 = {};
operator_db.r1_c1.init = function(){




    var requestData = {};
    $("#r1_c1_wrapper span[data-field-name]").each(function(){
        requestData[$(this).attr("data-field-name")] = $(this).text().replace(/^\s+|\s+$/gm,'');
    });
    requestData.action = "getAPITrafic";

    operator_db.xhr_get({
        url: '/manage/site/blocks/home/ajax/chart_r1_c1_ajax.jag',
        id:"r1_c1",
        data: requestData,
        dataType: "json"}).done(function (data) {
            for(var i=0;i<data.length;i++){
                var $radialContainer = $('<div id="r1_c1_'+i+'" class="radial-box"><div class="radial-top"></div><div class="radial-bottom">'+data[i].api+'</div></div>').appendTo("#r1_c1");
                radialProgress($radialContainer.find('.radial-top').get(0))
                    .diameter(150)
                    .label(data[i].calls + " calls")
                    .value(data[i].pt)
                    .render();
            }



    });
};




$(document).ready(function(){
	operator_db.r1_c1.init();
	operator_db.chartBox.initDropdown('operator_r1_c1',function(){
		operator_db.r1_c1.init();
    });

	operator_db.chartBox.initDropdown('time_r1_c1',function(){
    	operator_db.r1_c1.init();
    });
});