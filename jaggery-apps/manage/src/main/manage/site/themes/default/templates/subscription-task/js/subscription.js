$(document).ready(function(){
    $('.js_startBtn').click(function(){
        var btn = $(this);
        var taskId=btn.attr("data");
        var iteration=btn.attr("iteration");
        btn.attr("disabled","disabled");
        jagg.post("/site/blocks/task-manager/ajax/task.jag", { action:"startTask",taskId:taskId,taskType:"subscription" },
            function (json) {
                if (!json.error) {
                    btn.next().show();
                    $('#js_completeBtn'+iteration).show();
                    btn.hide();
                    $('#status'+iteration).text("IN_PROGRESS");
                } else {
                jagg.showLogin();
                }
            }, "json");

        // Display the list of tiers for the task.
        displayTiers(taskId);
        
    }).removeAttr("disabled","disabled");

    $('.js_completeBtn').click(function(){
        var btn = $(this);
        var taskId=btn.attr("data");
        var iteration=btn.attr("iteration");
        var description=$('#desc'+iteration).text();
        var status=$('.js_stateDropDown').val();
        btn.attr("disabled","disabled");
        jagg.post("/site/blocks/task-manager/ajax/task.jag", { action:"completeTask",status:status,taskId:taskId,taskType:"subscription",description:description },
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
        
        // Sets the selected tier for the current completed subscription.
        var tierId = $("#tiers_list_" + taskId).val();
        setSubscriptionTier(taskId, tierId);

    }).removeAttr("disabled","disabled");

    $('.js_assignBtn').click(function(){
        var btn = $(this);
        var taskId=btn.attr("data");
        var iteration=btn.attr("iteration");
        btn.attr("disabled","disabled");
        jagg.post("/site/blocks/task-manager/ajax/task.jag", { action:"assignTask",taskId:taskId,taskType:"subscription" },
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
    
    $('.js_toggle').click(function(){
        var $i = $('i',this);
        if($i.hasClass('icon-chevron-right')){
            $(this).next().show();
            $i.removeClass('icon-chevron-right');
            $i.addClass('icon-chevron-down');
            $.cookie($(this).attr('data-section'),'show');
        }else{
            $(this).next().hide();
            $i.removeClass('icon-chevron-down');
            $i.addClass('icon-chevron-right');
            $.cookie($(this).attr('data-section'),'hide');
        }
        var taskId = $(this).attr('id');
        $('tiersDiv_' + taskId).show();
        //console.log($('tiersDiv_' + taskId));
        
    });
    
    $('.js_toggle').each(function(){
        if($.cookie($(this).attr('data-section'))=="hide"){
            var $i = $('i',this);
            $(this).next().hide();
            $i.removeClass('icon-chevron-down');
            $i.addClass('icon-chevron-right');
        }
    });
    
    // Loads and displays the tiers for IN_PROGRESS tasks in the current page.
    displayTiersForInProgresstasks();
});

//Displays the list of tiers for the specified task.
function displayTiers(taskId) {

	jagg.post("/site/blocks/subscription-task/ajax/subscription-task.jag", { action:"getSubscriptionTiers",taskId:taskId },
		function (json) {
    		if (!json.error) {
    			var tiers = json.tiers;
    			var tiersList = document.getElementById('tiers_list_' + taskId);

    			$.each(tiers, function(key, tier) {
    				tiersList.options[tiersList.options.length] = new Option(tier.displayName, tier.name);
    			})
    			
    		} else {
                jagg.showLogin();
    		}
    	}, "json");
    
	document.getElementById('tiersDiv_' + taskId).style.display = 'block';
}

// Loads the tiers for IN_PROGRESS tasks.
function displayTiersForInProgresstasks() {
	
	var inProgressTasks = document.getElementsByName("in_progress_task");
	
	for(var i =0; i < inProgressTasks.length; i++) {
		var taskId = inProgressTasks[i].value;
		displayTiers(taskId);
	}
}

// Sets the selected tier for the current completed subscription.
function setSubscriptionTier(taskId, tierId) {
	
	jagg.post("/site/blocks/subscription-task/ajax/subscription-task.jag", { action:"setSubscriptionTier",taskId:taskId,tierId:tierId,taskType:"subscription" },
			function (json) {
                if (!json.error) {
                } else {
                    jagg.showLogin();
                }
	}, "json");
}