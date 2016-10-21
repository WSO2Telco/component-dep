function validationC(Obj){
	var validatorStateVal = 0;
	var returnParam = true;
	var closestTR = $(Obj).closest('tr');

	closestTR.find('td.editable_td').each(function() {
		var currentfieldValidation = true;

        if($($(this).children("label")[0]).length){
			$($(this).children("label")).remove();
			var classAttr = $($(this).children("input")).attr('class');
			if(classAttr.search("error") > -1){
				$($(this).children("input")).attr('class', classAttr.replace(/ error/i, ""));
			}
			
		}
        
        var addedTxtFieldClasses = $($(this).children()[0]).attr('class');
        var fieldValue = $($(this).children()[0]).val().trim();
        var element = $(this);
        
        
        if(addedTxtFieldClasses.indexOf("required") > -1){
        	if(fieldValue.length == 0){
        		if(currentfieldValidation == true){
        			var ErrorMsg = '<label class="error">Value is required!</label>';
        			element.append(ErrorMsg);
        			var classAttrToSet = $($(this).children("input")).attr('class')+" error";
        			$(element.children("input")[0]).attr('class', classAttrToSet);
        			validatorStateVal = validatorStateVal+1;
        			currentfieldValidation = false;
        		}
        	}
        }
        if(addedTxtFieldClasses.indexOf("email") > -1) {
        	var emailReg = /[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}/;
        	if(!emailReg.test(fieldValue)){
        		if(currentfieldValidation == true){
        			var ErrorMsg = '<label class="error">Please enter a valid Email!</label>';
        			element.append(ErrorMsg);
        			var classAttrToSet = $($(this).children("input")).attr('class')+" error";
        			$(element.children("input")[0]).attr('class', classAttrToSet);
        			validatorStateVal = validatorStateVal+1;
        			currentfieldValidation = false;
        		}
        	}
        }
        if(addedTxtFieldClasses.indexOf("balance") > -1) {
        	var currencyReg = /(^\d*\.?\d*[0-9]+\d*$)|(^[0-9]+\d*\.\d*$)/;
        	//alert(fieldValue+" :: "+ fieldValue.search(currencyReg) +" :P");
        	if(!currencyReg.test(fieldValue)){
        		if(currentfieldValidation == true){
        			var ErrorMsg = '<label class="error">Please enter a valid Balance!</label>';
        			element.append(ErrorMsg);
        			var classAttrToSet = $($(this).children("input")).attr('class')+" error";
        			$(element.children("input")[0]).attr('class', classAttrToSet);
        			validatorStateVal = validatorStateVal+1;
        			currentfieldValidation = false;
        		}
        	}
        }
        if(addedTxtFieldClasses.indexOf("number") > -1) {
        	var numberReg = /^(0|[1-9][0-9]*)$/;
        	if(!numberReg.test(fieldValue)){
        		if(currentfieldValidation == true){
        			var ErrorMsg = '<label class="error">Please enter a valid Number!</label>';
        			element.append(ErrorMsg);
        			var classAttrToSet = $($(this).children("input")).attr('class')+" error";
        			$(element.children("input")[0]).attr('class', classAttrToSet);
        			validatorStateVal = validatorStateVal+1;
        			currentfieldValidation = false;
        		}
        	}
        }
        if(addedTxtFieldClasses.indexOf("tel") > -1) {
        	
        	//var telReg = /tel\:\+[0-9]+/;			//removed due to request
        	var telReg = /^\d{11}$/;
        	if(!telReg.test(fieldValue)){
        		if(currentfieldValidation == true){
        			var ErrorMsg = '<label class="error">Please enter a valid Number!</label>';
        			element.append(ErrorMsg);
        			var classAttrToSet = $($(this).children("input")).attr('class')+" error";
        			$(element.children("input")[0]).attr('class', classAttrToSet);
        			validatorStateVal = validatorStateVal+1;
        			currentfieldValidation = false;
        		}
        	}
        }
        
        //custom validations
        if(addedTxtFieldClasses.indexOf("shortcode_exist") > -1) {
        	if(currentfieldValidation == true){
        		if(isShortcodeExists(fieldValue)){
        			var ErrorMsg = '<label class="error">Shortcode already exists!</label>';
    				element.append(ErrorMsg);
    				var classAttrToSet = $($(this).children("input")).attr('class')+" error";
    				$(element.children("input")[0]).attr('class', classAttrToSet);
    				validatorStateVal = validatorStateVal+1;
    				currentfieldValidation = false;
        		}
        	}
        }
        
        if(addedTxtFieldClasses.indexOf("managenum_exist") > -1) {
        	if(currentfieldValidation == true){
        		if(isNumberExists(fieldValue)){
        			var ErrorMsg = '<label class="error">Number already exists!</label>';
    				element.append(ErrorMsg);
    				var classAttrToSet = $($(this).children("input")).attr('class')+" error";
    				$(element.children("input")[0]).attr('class', classAttrToSet);
    				validatorStateVal = validatorStateVal+1;
    				currentfieldValidation = false;
        		}
        	}
        }

    });
	//alert(validatorStateVal);
    if(validatorStateVal > 0){
       	returnParam = false;
    }
	
	return returnParam;
	
}

function isShortcodeExists(fieldVal){
	var isExist = false;
	var duplicates = 0;
	$('#data_class_body').find('tr').each(function() {
		var trVal = $(this).find('td:first-child').text();
		if(trVal.trim() == fieldVal.trim()){
			duplicates = duplicates+1;
		}
	});
	if(duplicates > 0){
		isExist = true;
	}
	return isExist;
}

function isNumberExists(fieldVal){
	var isExist = false;
	var duplicates = 0;
	$('#data_class_body').find('tr').each(function() {
		var trVal = $(this).find('td:first-child').text();
		if(trVal.trim() == fieldVal.trim()){
			duplicates = duplicates+1;
		}
	});
	if(duplicates > 0){
		isExist = true;
	}
	return isExist;
}