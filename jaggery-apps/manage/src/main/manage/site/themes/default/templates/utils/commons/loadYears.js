/*
    Load the years to dropdown list in report module.
    Number of years to load, can be configured in #numberOfYearsToLoad
*/
function loadYearDropDownList(docElement, parentId){
    var numberOfYearsToLoad = 3;
	var menu = docElement.getElementById(parentId);
    var currentYear = new Date().getFullYear();
	for(var i = currentYear; i > (currentYear-numberOfYearsToLoad); i--) {
	    var newOption = docElement.createElement("option");
		newOption.value = i;
        newOption.innerHTML = i;
        menu.appendChild(newOption);
    }
}
