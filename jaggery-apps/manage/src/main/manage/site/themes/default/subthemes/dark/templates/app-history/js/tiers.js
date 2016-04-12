$(document).ready(function() {
	$('#tierattribute_tooltip').popup({
        type: 'tooltip',
        vertical: 'top',
        horizontal: 'center',
        transition: '0.3s all 0.1s'
    });
});

function getLblText(lblInfo){
		var lblText = $("#"+lblInfo.id).text();
		getAllTiers(lblText.trim());
}

function getAllTiers(lblText){
	var action = "getAllTiers";
	
	jagg.post("/site/blocks/app-history/ajax/tier-details.jag", {
		action : action,
		lblText : lblText
	}, function(result) {
		if (!result.error) {
			if (result.data != null) {
				setTierDetailsToToolTip(result.data);
			} else {
				jagg.showLogin();
			}
		} else {
			jagg.showLogin();
		}
	}, "json");
}

function setTierDetailsToToolTip(tierAttributevalue){
	$("#tierattribute_tooltip").html(tierAttributevalue);
}


