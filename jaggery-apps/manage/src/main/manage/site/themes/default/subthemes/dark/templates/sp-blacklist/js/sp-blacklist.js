var s = document.createElement("script");
s.type = "text/javascript";
s.src = "../themes/default/subthemes/dark/templates/sp-blacklist/js/bootbox.js";
$("head").append(s);
	
var selectedSP = null;

$(document).ready(function() {
	selectedSP = $("#spSelect").val();
	populateBlacklistData($("#spSelect").val());

	$("#add-new").click(function() {
		selectedSP = $("#spSelect").val();

		jagg.message({content:'Are you sure you want to blacklist SP',type:'confirm',okCallback:function(){addNewToBlacklist(selectedSP);
		populateBlacklistData($("#spSelect").val());
		},cancelCallback:function(){}});
	});
});

function populateBlacklistData(api) {
	jagg.post("/site/blocks/sp-blacklist/ajax/sp-blacklist.jag", {
		action : "getSPforBlacklist",
		api : api
	}, function(result) {
		if (!result.error) {
			var blacklist = result.blacklist;
			var select = document.getElementById('spSelect');
			select.options.length = 0; // clear out existing items
			for (var i = 0; i < result.blacklist.data.length; i++) {
			    var d = result.blacklist.data[i];
			    select.options.add(new Option(d.spName,d.appId))
			}
                       	if(result.blacklist.data.length==0){
                           document.getElementById("add-new").style.visibility = "hidden";
			}

		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};

function addNewToBlacklist(selectedSP) {

	jagg.post("/site/blocks/sp-blacklist/ajax/sp-blacklist.jag", {
		action : "addNewSPToBlacklist",
		spList : selectedSP
	}, function(result) {
		if (!result.error && result.code == 1) {
			populateBlacklistData(selectedSP);
		} else {
			jagg.message({
				content : result.message,
				type : "error"
			});
		}
	}, "json");
};


