$(document).ready(function(){
	setDestinationAddressesToCombo();
	
    $("#param-add-button").click(function (){
        var delstatus = $("#delstatus").val();
        var notifydelay = $("#notifydelay").val();       
        var maxallowed = $("#maxallowed").val();
        var btn = $(this);
        btn.attr("disabled","disabled");
        //var iteration=btn.attr("iteration");
        jagg.post("/site/blocks/sms-task/ajax/sms.jag", { action:"saveparam",user:"username",delstatus:delstatus,notifydelay:notifydelay,maxallowed:maxallowed },
            function (response) {
                if (!response.error) {
                    //btn.next().show();
                    //$('#js_completeBtn'+iteration).show();
                    //btn.hide();
                    //$('#status'+iteration).text("IN_PROGRESS");
                    $('#appAddMessage').show();
                    window.location.reload();
                    
                } else {
                    jagg.showLogin();
                }
            }, "json");

    }).removeAttr("disabled","disabled");
    
    $("#nofify-add-button").click(function (){
        
        var senderaddr = $("#senderaddr").val();
        var notifyURL = $("#notifyURL").val();
        var callbackData = $("#callbackData").val();
        var criteria = $("#criteria").val();
        var clientCorrelator = $("#clientCorrelator").val();
                      
        var btn = $(this);
        //btn.attr("disabled","disabled");
        //var iteration=btn.attr("iteration");
        jagg.post("/site/blocks/sms-task/ajax/sms.jag", { action:"addnotify",senderaddr:senderaddr,notifyURL:notifyURL,callbackData:callbackData,criteria:criteria,clientCorrelator:clientCorrelator},
            function (response) {
                if (!response.error) {
                    //btn.next().show();
                    //$('#js_completeBtn'+iteration).show();
                    //btn.hide();
                    //$('#status'+iteration).text("IN_PROGRESS");                    
                	$('#request').val(response.message);
                    $('#response').val(response.tasks);
                    
                    window.location.reload();
                    
                } else {
                    jagg.showLogin();
                }
            }, "json");

    }).removeAttr("disabled","disabled");

    $('.js_completeBtn').click(function(){
        var btn = $(this);
        var taskId=btn.attr("data");
        var iteration=btn.attr("iteration");
        var description=$('#desc'+iteration).text();
        var status=$('.js_stateDropDown').val();
        btn.attr("disabled","disabled");
        jagg.post("/site/blocks/task-manager/ajax/task.jag", { action:"completeTask",status:status,taskId:taskId,taskType:"application",description:description },
            function (json) {
                if (!json.error) {
                    btn.next().show();
                    btn.next().next().html(json.msg);
                    btn.hide();
                    window.location.reload();
                } else {
                    jagg.showLogin();
                }
            }, "json");

    }).removeAttr("disabled","disabled");

    $('.js_assignBtn').click(function(){
        var btn = $(this);
        var taskId=btn.attr("data");
        var iteration=btn.attr("iteration");
        btn.attr("disabled","disabled");
        jagg.post("/site/blocks/task-manager/ajax/task.jag", { action:"assignTask",taskId:taskId,taskType:"application" },
            function (json) {
                if (!json.error) {
                    btn.next().show();
                    $('#js_startBtn'+iteration).show();
                    btn.hide();
                    $('#status'+iteration).text("RESERVED");
                } else {
                    jagg.showLogin();
                }
            }, "json");
    }).removeAttr("disabled","disabled");
    
    
  //Making rows editable
    $('#data_tbl_body').on( 'click', 'a.edit_tbl_icon', function () {
        $(this).closest('tr').find('td.editable_td').each(function() {
            var textval = $(this).text(); // this will be the text of each TD tag
            
            $(this).addClass("cellEditing");
            $(this).html('<input type="text" value="'+ textval + ' "/>');
            $(this).children().first().focus();
             
        });
        
        //Setting action buttons
        $(this).closest('tr').find('td:last-child .edit_tbl_icon').hide();
        $(this).closest('tr').find('td:last-child .delete_tbl_icon').hide();
        $(this).closest('tr').find('td:last-child .save_edit_icon').show();
        $(this).closest('tr').find('td:last-child .reset_edit_icon').show();
    });
    
  //Delete data and remove row
    $('#data_tbl_body').on( 'click', 'a.delete_tbl_icon', function () {
    	var closestTr = $(this).closest('tr');
    	var key = closestTr.find('.row_data_key').val();
    	var request_url = "/site/blocks/sms-task/ajax/sms.jag";
    	
    	//alert("Deleting key: "+key);
    	
    	jagg.post(request_url, {'action':"deletenotifirecord", 'id':key}, function (result) {
    		//if(!result.error){
    			closestTr.remove();
    		//} else {
    			//alert("Error! :: "+result.message);
    		//}
    	}, "json");
    	setTablePagination(0);
    });
    
    $("#notifydelay").keydown(function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 ) {
        }
        else if (event.keyCode == 37 || event.keyCode == 39) {
        }
        else {
            if (event.keyCode < 95) {
                if (event.keyCode < 48 || event.keyCode > 57 ) {
                    event.preventDefault();
                }
            }
            else {
                if (event.keyCode < 96 || event.keyCode > 105 ) {
                    event.preventDefault();
                }
            }
            if($("#notifydelay").val().length >= 10) {
                event.preventDefault();
            }
        }
    });
   
    $("#maxallowed").keydown(function(event) {
        if ( event.keyCode == 46 || event.keyCode == 8 ) {
        }
        else if (event.keyCode == 37 || event.keyCode == 39) {
        }
        else {
            if (event.keyCode < 95) {
                if (event.keyCode < 48 || event.keyCode > 57 ) {
                    event.preventDefault();
                }
            }
            else {
                if (event.keyCode < 96 || event.keyCode > 105 ) {
                    event.preventDefault();
                }
            }
            if($("#notifydelay").val().length >= 10) {
                event.preventDefault();
            }
        }
    });    
    
   //Save modified row data
    $('#data_tbl_body').on( 'click', 'a.save_edit_icon', function () {

        var closestTR = $(this).closest('tr');
        
        var key = closestTR.find('.row_data_key').val().trim();
        var callbackData = $(closestTR.find('td.editable_td').children()[0]).val();
        var criteria = $(closestTR.find('td.editable_td').children()[1]).val();
        var notifyURL = $(closestTR.find('td.editable_td').children()[2]).val();
        
        var request_url = "/site/blocks/sms-task/ajax/sms.jag";
        jagg.post(request_url, {'action':"updateNotifyData", 'id':key, 'callbackData':callbackData , 'criteria':criteria, 'notifyURL':notifyURL}, function (result) {
            //alert(result.data);
            if(!result.error){
            	//if(result.data == true){
            	closestTR.find('td').children().each(function() {
            		$(this).parent().removeClass("cellEditing");
                });
                closestTR.find('td:nth-child(2)').text(callbackData);
                closestTR.find('td:nth-child(4)').text(criteria);
                closestTR.find('td:nth-child(7)').text(notifyURL);
            
                //Setting action buttons
                closestTR.find('td:last-child .edit_tbl_icon').show();
                closestTR.find('td:last-child .delete_tbl_icon').show();
                closestTR.find('td:last-child .save_edit_icon').hide();
                closestTR.find('td:last-child .reset_edit_icon').hide();
            	//} else {
            	//	alert("Server Error!");
            	//}
            } else {
            	alert("Error! :: "+result.message);
            }
        }, "json");
    });
    
    function setDestinationAddressesToCombo() {
    	var action = "getDestinationAddresses";
    	jagg.post("/site/blocks/sms-to-application/ajax/sms-to-application.jag", {
    		action : action
    	}, function(result) {
    		if (!result.error) {
    			if (result.data != "null") {
    				var nubmers = result.data.replace(/\[/, "").replace(/\]/, "")
    						.split(",");
    				fillDestinationAddressesCombo(nubmers);
    			} else {

    			}
    		} else {
    			alert("Error");
    		}
    	}, "json");
    }

    function fillDestinationAddressesCombo(numbers) {
    	var option = '';
    	var i;
    	for (i = 0; i < numbers.length-1; i++) {
    		option += '<option value="' + numbers[i].replace(/\"/, "").replace(/\"/, "") + '">' + numbers[i].replace(/\"/, "").replace(/\"/, "")
    				+ '</option>';
    	}
    	$('#senderaddr').append(option);
    }
    
    
    //Reset clicked row data
    $('#data_tbl_body').on( 'click', 'a.reset_edit_icon', function () {
    	var closestTr = $(this).closest('tr');
    	var request_url = "/site/blocks/sms-task/ajax/sms.jag";
        var key = closestTr.find('.row_data_key').val();

        jagg.post(request_url, {'action':"SearchSubscribeSMS", 'id':key}, function (result) {
    		if(!result.error){
    			var responseData = result.data.split(",");
                closestTr.find('td').children().each(function() {
                    $(this).parent().removeClass("cellEditing");
                });
                
                closestTr.find('td:nth-child(2)').text(responseData[0]);
                closestTr.find('td:nth-child(3)').text(responseData[1]);
                closestTr.find('td:nth-child(4)').text(responseData[2]);
                closestTr.find('td:nth-child(5)').text(responseData[3]);
                closestTr.find('td:nth-child(6)').text(responseData[4]);
                closestTr.find('td:nth-child(7)').text(responseData[5]);
                
                //Setting action buttons
                closestTr.find('td:last-child .edit_tbl_icon').show();
                closestTr.find('td:last-child .delete_tbl_icon').show();
                closestTr.find('td:last-child .save_edit_icon').hide();
                closestTr.find('td:last-child .reset_edit_icon').hide();
                
    		} else {
    			alert("Error! :: "+result.message);
    		}
    	}, "json");
    });

});
