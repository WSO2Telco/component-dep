$(document).ready(function() {
	$('#tierRateCard_tooltip').popup({
        type: 'tooltip',
        vertical: 'bottom',
        horizontal: 'center',
        transition: '0.3s all 0.1s',
    });
});

function getLblText(lblInfo){
	var idParts = lblInfo.id.split("_");
	var hiddenFieldId = "tierAttributeHiddenField_"+idParts[1]+"_"+idParts[2];
	var hiddenFieldValue = $("#"+hiddenFieldId).val();
	setTierDetailsToToolTip(hiddenFieldValue, lblInfo);
}

function setTierDetailsToToolTip(tierAttributevalue, lblInfo){
	tierAttributevalue = tierAttributevalue.replace('<div xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tschema="http://workflow.subscription.apimgt.carbon.wso2.org" xmlns="">','<div>');
	$("#tierRateCard_tooltip").html(tierAttributevalue);
	$('#tierRateCard_tooltip').popup({
        type: 'tooltip',
        vertical: 'bottom',
        horizontal: 'center',
        transition: '0.3s all 0.1s',
        tooltipanchor: $(lblInfo)
    });
}


