//var selectedAPI = null;

$(document).ready(function() {
	//selectedAPI = $("#apiSelect").val();
	loadAPIComboValue();
	populateBlacklistData();

    $("#apiSelect").click(function() {
		//loadAPIComboValue();
		populateBlacklistData();
	});

	$("#view").click(function() {
		populateBlacklistData();
	});

	$("#add-blacklist").click(function() {
		addNewToBlacklist();
	});
});


function populateBlacklistData() {
	var api = $("#apiSelect").val();

	if(api == null){
		return;
	}
	
	jagg.post("/site/blocks/apiwise-blacklist/ajax/apiwise-blacklist.jag", {
		action : "getBlacklistByAPI",
		api : api
	}, function(result) {
		if (!result.error) {
			var blacklist = result.blacklist;

			var tbody = document.getElementById("billingBody");
			tbody.innerHTML = "";

			var blacklistHeader = document.getElementById("blacklist-header");
			blacklistHeader.innerHTML = "<h3>Blacklisted subscribers ("
					+ $("#apiSelect").val() + ")</h3>";

			for (var i = 0; i < blacklist.length; i++) {

				var subscriber = blacklist[i];

				var row = document.createElement("tr");
				row.setAttribute("style","background-color: #f5f5f5;");
				var cell0 = document.createElement("td");
				cell0.innerHTML = subscriber;
				row.appendChild(cell0);

				var cell1 = document.createElement("td");
				//cell1.innerHTML = "<div><a onclick=\"removeFromBlacklist(" + subscriber + ")\">Remove</a><div>";
				//cell1.innerHTML = "<div><a onclick=\'removeFromBlacklist(\""+subscriber + "\")\'>Remove</a><div>";
				cell1.innerHTML = "<a class=\"operation-summary delete_resource\" onclick=\"removeFromBlacklist(" + subscriber + ")\"><span class=\"fw-stack\" style=\"font-size:10px\"><i class=\"fw fw-delete fw-stack-1x\"></i><i class=\"fw fw-circle-outline fw-stack-2x\"></i></a>";

				row.appendChild(cell1);

				tbody.appendChild(row);
			}
		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};

function addNewToBlacklist() {
	
	var api = $("#apiSelect").val();
	//var subscriberList = prompt("Please enter subscriber number", "Separated by comma");
	var subscriberList = $("#subscriber_black_list").val();
	jagg.post("/site/blocks/apiwise-blacklist/ajax/apiwise-blacklist.jag", {
		action : "addNewToBlacklist",
		api : api,
		subscriberList : subscriberList
	}, function(result) {
		if (!result.error && result.code == 1) {
			populateBlacklistData($("#apiSelect").val());
		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};

function removeFromBlacklist(subscriber) {
	var api = $("#apiSelect").val();
	console.info("subscriber: " + subscriber);
	jagg.post("/site/blocks/apiwise-blacklist/ajax/apiwise-blacklist.jag", {
		action : "removeFromBlacklist",
		api : api,
		subscriber : subscriber
	}, function(result) {
		if (!result.error && result.code == 1) {
			populateBlacklistData($("#apiSelect").val());
		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};


function loadAPIComboValue() {
	
	jagg.post("/site/blocks/apiwise-blacklist/ajax/apiwise-blacklist.jag", {
		action : "apis"
	}, function(result) {
			var apiSelect = $("#apiSelect");
			apiSelect.html("");
			//alert(result);
			var tempValue = null;
			for (var prop in result) {
				
	    		var apiArray = result[prop].split(':');
	    		//alert(apiArray[0]);

	    		if(tempValue == null){
	    			tempValue = apiArray[3];
	    		}

	    		apiSelect.append($("<option></option>")
	    	         .attr("value",apiArray[3])
	    	         .text(apiArray[1] + " - " + apiArray[2] + "  Provided by " + apiArray[0])); 
	    		//alert(result.code[prop]);
	    	}


	    	populateBlacklistData();
	}, "json");

	$('#apiSelect').val(9);	
};

