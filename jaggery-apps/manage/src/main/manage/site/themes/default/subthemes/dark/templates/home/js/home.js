if( !('axiata_db' in window) ) window['axiata_db'] = {}; //see the object axiata_db exists and create it if not.
/*******************************
 *
 * Handling a single chartBox
 *
 *****************************/
axiata_db.chartBox = {};
axiata_db.chartBox.initDropdown = function(opid,updateCallBack){
    var $dropdown_r1_c1 = $('#'+opid);

    var dropdown_r1_c1_value = amplify.store(opid);
    if(dropdown_r1_c1_value!=undefined){
        $('.selected-label',$dropdown_r1_c1).html(dropdown_r1_c1_value);
    }
    $dropdown_r1_c1.on("click","li a",function(event){
        event.preventDefault();
        var selected = $(this).text().replace(/^\s+|\s+$/gm,'');
        var previousValue = $('.selected-label',$dropdown_r1_c1).text().replace(/^\s+|\s+$/gm,'');
        if(previousValue != selected){
            $('.selected-label',$dropdown_r1_c1).html(selected);
            amplify.store(opid,selected);
            if(jQuery.isFunction(updateCallBack)){
                updateCallBack();
            }
        }
    });
};
axiata_db.xhr_get = function(params) {

    return $.ajax({
        url: params.url,
        type: 'get',
        dataType: params.dataType,
        data:params.data,
        success:params.success,
        beforeSend: function(){
            $("#"+params.id).html('<i class="fa fa-spinner fa-spin spinner-big"></i>');
        }
    })
    .always(function() {
            $("#"+params.id + " i.fa").remove();
    })
    .fail(function() {
        alert('error');
    });

};

