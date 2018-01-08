Instructions to Add New API to the Rate Database

1.	Add the new API name to the 'api' table in rate database.

	Example: 
	INSERT INTO `api` (`apiid`, `apiname`, `apidesc`, `createdby`) VALUES (9,'testapi','test api','admin');

2.  Add new API operations related to the previously added API to 'api_operation' table in rate database. 
    If that API doesn’t have multiple API operations, add a default API operation for that API.

	Example: 
	INSERT INTO `api_operation` (`api_operationid`, `apiid`, `api_operation`, `api_operationcode`, `createdby`) VALUES (35,9,'testapi default','test api default','admin');
