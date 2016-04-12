var selectedAPI = null;

$(document).ready(function() {
	selectedAPI = $("#apiSelect").val();
	populateBlacklistData($("#apiSelect").val());

	$("#view").click(function() {
		selectedAPI = $("#apiSelect").val();
		populateBlacklistData($("#apiSelect").val());
	});

	$("#add-new").click(function() {
		addNewToBlacklist();
	});
});

function populateBlacklistData(api) {
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
					+ selectedAPI + ")</h3>";

			for (var i = 0; i < blacklist.length; i++) {

				var subscriber = blacklist[i];

				var row = document.createElement("tr");

				var cell0 = document.createElement("td");
				cell0.innerHTML = subscriber;
				row.appendChild(cell0);

				var cell1 = document.createElement("td");
				//cell1.innerHTML = "<div><a onclick=\"removeFromBlacklist(" + subscriber + ")\">Remove</a><div>";
				cell1.innerHTML = "<div><a onclick=\'removeFromBlacklist(\""+subscriber + "\")\'>Remove</a><div>";
				
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
	var subscriberList = prompt("Please enter subscriber number", "Separated by comma");
	
	jagg.post("/site/blocks/apiwise-blacklist/ajax/apiwise-blacklist.jag", {
		action : "addNewToBlacklist",
		api : selectedAPI,
		subscriberList : subscriberList
	}, function(result) {
		if (!result.error && result.code == 1) {
			populateBlacklistData(selectedAPI);
		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};

function removeFromBlacklist(subscriber) {
	console.info("subscriber: " + subscriber);
	jagg.post("/site/blocks/apiwise-blacklist/ajax/apiwise-blacklist.jag", {
		action : "removeFromBlacklist",
		api : selectedAPI,
		subscriber : subscriber
	}, function(result) {
		if (!result.error && result.code == 1) {
			populateBlacklistData(selectedAPI);
		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};
