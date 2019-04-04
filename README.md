# Digital Enable Platform (DEP)



| Branch | Build Status |
| :------------ |:-------------
| master | [![Build Status](http://ci.wso2telco.com/job/component-dep/badge/icon)](http://ci.wso2telco.com/job/component-dep/)


This is the platform for both the product Digital Enable Hub &  Digital Enable Gateway .This includes all the required components for managing web components , mediator component, analytic tools etc..

## Blacklist Improvements

This branch contains the following improvements to the Blacklist feature
* MSISDNs can be blacklisted on SP/APP/API-wise
* Changed Blacklist-APIs to work with new parameters

The following modules contains the changes described above
* operator-service
* webapps/blacklist-whitelist-service

Above changes require the following changes to statdb.blacklistmsisdn table
```sql
ALTER TABLE blacklistmsisdn ADD COLUMN SP_NAME VARCHAR(45) DEFAULT '_ALL_';
ALTER TABLE blacklistmsisdn ADD COLUMN APP_ID VARCHAR(45) DEFAULT '_ALL_';

ALTER TABLE blacklistmsisdn DROP INDEX UNQ_blacklistmsisdn;
ALTER TABLE blacklistmsisdn ADD UNIQUE KEY UNQ_blacklistmsisdn (SP_NAME, APP_ID, API_ID, MSISDN);
```

Add the following configuration to <DEP_HOME>/repository/conf/api-manager.xml
```xml
<MsisdnValidationService>http://<MEDIATOR_HOST>:<PORT>/services/msisdnValidation</MsisdnValidationService>
```
