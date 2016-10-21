var selectedAPI = null;

$(document).ready(function() {
	
	$("#add-new").click(function() {
		addNewToWhitelist();
	});
});

function addNewToWhitelist() {
	var subscriberList = prompt("Please enter subscriber names", "Separated by comma");
	
	jagg.post("/site/blocks/apiwise-whitelist/ajax/apiwise-whitelist.jag", {
		action : "addNewToWhitelist",
		//api : selectedAPI,
		subscriberList : subscriberList
	}, function(result) {
		if (!result.error && result.code == 1) {
			alert('Done');
		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};
