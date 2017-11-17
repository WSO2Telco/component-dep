INSERT INTO <WSO2TELCO_RATE_DB>.api
(`apiid`, `apiname`, `apidesc`, `createdby`) VALUES 
(5,'credit','credit','admin'),
(6,'customerinfo','customerinfo','admin'),
(7,'provisioning','provisioning','admin'),
(8,'wallet','wallet','admin');

===========================================

INSERT INTO <WSO2TELCO_RATE_DB>.api_operation
(`api_operationid`, `apiid`, `api_operation`, `api_operationcode`, `createdby`) VALUES
(23,5,'ApplyCredit','ApplyCredit','admin'),
(24,5,'PartialRefund','PartialRefund','admin'),
(25,6,'GetProfile','GetProfile','admin'),
(26,6,'GetAttributes','GetAttributes','admin'),
(27,7,'QueryApplicableServices','QueryApplicableServices','admin'),
(28,7,'ProvisionService','ProvisionService','admin'),
(29,7,'Un-ProvisionService','Un-ProvisionService','admin'),
(30,7,'ListServiceByCustomer','ListServiceByCustomer','admin'),
(31,8,'MakePayment','MakePayment','admin'),
(32,8,'ListTransactions','ListTransactions','admin'),
(33,8,'RefundUser','RefundUser','admin'),
(34,8,'BalanceLookup','BalanceLookup','admin');

===========================================

INSERT INTO <WSO2TELCO_RATE_DB>.rate_type VALUES 
(1,'CONSTANT','Constant Charge Per Month',NULL,'2017-09-22 14:27:29',NULL,'2017-09-22 14:27:29'),
(2,'QUOTA','Quota Based Charging per Month',NULL,'2017-09-22 14:27:29',NULL,'2017-09-22 14:27:29'),
(3,'PERCENTAGE','Revenue Share Charging',NULL,'2017-09-22 14:27:30',NULL,'2017-09-22 14:27:30'),
(4,'PER_REQUEST','Request Based Charging',NULL,'2017-09-22 14:27:30',NULL,'2017-09-22 14:27:30');

/* ALTER TABLE commads for the data source WSO2TELCO_RATE_DB */;

ALTER TABLE <WSO2TELCO_RATE_DB> .`category` ADD CONSTRAINT `CategorynameK` Unique KEY (`categoryname`);
ALTER TABLE <WSO2TELCO_RATE_DB> .`category` ADD CONSTRAINT `CategorycodeK` Unique KEY (`categorycode`);
ALTER TABLE <WSO2TELCO_RATE_DB> .`currency` ADD CONSTRAINT `CurrencyK` Unique KEY (`currencycode`);
ALTER TABLE <WSO2TELCO_RATE_DB> .`rate_def` ADD CONSTRAINT `Rate_defK` Unique KEY (`rate_defname`);
ALTER TABLE <WSO2TELCO_RATE_DB> .`rate_type` ADD CONSTRAINT `Rate_typeK` Unique KEY (`rate_typecode`);
ALTER TABLE <WSO2TELCO_RATE_DB> .`tariff` ADD CONSTRAINT `Rate_tariffK` Unique KEY (`tariffname`);
ALTER TABLE <WSO2TELCO_RATE_DB> .`tax` ADD CONSTRAINT `TaxK` Unique KEY (`taxname`);

