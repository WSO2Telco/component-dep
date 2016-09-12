/*
* ==========================================
* MySQL script related to tables in
* Axiata database
* Database: Axiata database
* ==========================================
*/

--
-- Table structure for table `ussd_request_entry`
--

CREATE TABLE IF NOT EXISTS `ussd_request_entry` (
  `ussd_request_did` int(20) NOT NULL AUTO_INCREMENT,
  `notifyurl` varchar(255) DEFAULT NULL,
  `sp_consumerKey` varchar(100) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  `operatorId` varchar(45) DEFAULT NULL,
  `userId` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ussd_request_did`)
);


--
-- Table structure for table `endpointapps`
--

CREATE TABLE IF NOT EXISTS `endpointapps` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `endpointid` int(11) DEFAULT NULL,
  `applicationid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT '0',
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_apps_endpoint` (`endpointid`,`applicationid`)
);

--
-- Table structure for table `operatorapps`
--

CREATE TABLE IF NOT EXISTS `operatorapps` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `applicationid` int(11) DEFAULT NULL,
  `operatorid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT '0',
  `note` varchar(255) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
);

--
-- Table structure for table `operatorendpoints`
--

CREATE TABLE IF NOT EXISTS `operatorendpoints` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `operatorid` int(11) DEFAULT NULL,
  `api` varchar(25) DEFAULT NULL,
  `isactive` int(11) DEFAULT '0',
  `endpoint` varchar(255) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
);


--
-- Table structure for table `operators`
--

CREATE TABLE IF NOT EXISTS `operators` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `operatorname` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  `refreshtoken` varchar(255) DEFAULT NULL,
  `tokenvalidity` double DEFAULT NULL,
  `tokentime` double DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `tokenurl` varchar(255) DEFAULT NULL,
  `tokenauth` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `operatorname` (`operatorname`)
);

--
-- Table structure for table `operatorsubs`
--

CREATE TABLE IF NOT EXISTS `operatorsubs` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `mo_subscription_did` int(20) DEFAULT NULL,
  `domainurl` varchar(255) DEFAULT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `subscriptions`
--

CREATE TABLE IF NOT EXISTS `subscriptions` (
  `mo_subscription_did` int(20) NOT NULL AUTO_INCREMENT,
  `notifyurl` varchar(255) DEFAULT NULL,
  `service_provider` varchar(255) DEFAULT NULL,
  `is_active` int(10) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`mo_subscription_did`)
);

/*
* Table for subscription approval operator list.
*/
CREATE TABLE IF NOT EXISTS `sub_approval_operators` (
  `API_NAME` varchar(200) DEFAULT NULL,
  `API_VERSION` varchar(30) DEFAULT NULL,
  `API_PROVIDER` varchar(200) DEFAULT NULL,
  `APP_ID` int(11) DEFAULT NULL,
  `OPERATOR_LIST` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`API_NAME`, `API_VERSION`, `API_PROVIDER`, `APP_ID`)
);

/*
* Table for validator definitions used in AxiataMediator
*/
CREATE TABLE IF NOT EXISTS validator (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    class VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

/*
* Table for API subscriptions to validator mapping
*/
CREATE TABLE IF NOT EXISTS subscription_validator (
    application_id INT NOT NULL,
    api_id INT NOT NULL,
    validator_id INT NOT NULL,
    PRIMARY KEY (application_id, api_id),
    FOREIGN KEY (validator_id) REFERENCES validator (id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `merchantopco_blacklist` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `application_id` int(20) DEFAULT NULL,
  `operator_id` int(20) DEFAULT NULL,
  `subscriber` varchar(40) DEFAULT NULL,
  `merchant` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT '1',
  `note` varchar(255) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `constr_ID` (`application_id`,`operator_id`,`subscriber`,`merchant`)
);

CREATE TABLE IF NOT EXISTS `mcc_number_ranges` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `mcccode` varchar(10) DEFAULT NULL,
  `rangefrom` decimal(20,0) DEFAULT NULL,
  `rangeto` decimal(20,0) DEFAULT NULL,
  `mnccode` varchar(20) DEFAULT NULL,
  `brand` varchar(20) DEFAULT NULL,
  `isactive` int(2) DEFAULT '1',
  `note` varchar(255) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `valid_payment_categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(100) NOT NULL,
  `Note` varchar(255) NOT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `workflow_api_key_mappings`
--
CREATE TABLE IF NOT EXISTS `workflow_api_key_mappings` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `API_NAME` varchar(200) NOT NULL,
  `API_KEY` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
);

--
-- Table structure for table `sendsms_reqid` used for delivery status query
--

CREATE TABLE IF NOT EXISTS `sendsms_reqid` (
  `ID` int(20) NOT NULL AUTO_INCREMENT,
  `hub_requestid` varchar(255) DEFAULT NULL,
  `sender_address` varchar(40) DEFAULT NULL,
  `delivery_address` varchar(40) DEFAULT NULL,
  `plugin_requestid` varchar(255) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
);

CREATE TABLE IF NOT EXISTS `outbound_subscriptions` (
  `dn_subscription_did` int(20) NOT NULL AUTO_INCREMENT,
  `notifyurl` varchar(255) DEFAULT NULL,
  `service_provider` varchar(255) DEFAULT NULL,
  `is_active` int(10) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`dn_subscription_did`)
);

CREATE TABLE IF NOT EXISTS `outbound_operatorsubs` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `dn_subscription_did` int(20) DEFAULT NULL,
  `domainurl` varchar(255) DEFAULT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `provision_services` (
  `provision_service_did` int(20) NOT NULL AUTO_INCREMENT,
  `notifyurl` varchar(255) DEFAULT NULL,
  `service_provider` varchar(255) DEFAULT NULL,
  `is_active` int(10) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`provision_service_did`)
);

CREATE TABLE IF NOT EXISTS `provision_service_operator_endpoints` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `provision_service_did` int(20) DEFAULT NULL,
  `domainurl` varchar(255) DEFAULT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `mo_ussd_subscription` (
  `mo_ussd_request_did` int(20) NOT NULL AUTO_INCREMENT,
  `ussd_request_did` int(20) DEFAULT NULL,
  `domainurl` varchar(255) DEFAULT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `created` varchar(25) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `lastupdated` varchar(25) DEFAULT NULL,
  `lastupdated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`mo_ussd_request_did`)
) ENGINE=InnoDB;


/*
* Default validator types provided from mife-validator component
*/
insert into validator (name, class) values ('passthru','com.wso2telco.subscriptionvalidator.services.impl.PassThroughValidator');
insert into validator (name, class) values ('msisdn','com.wso2telco.subscriptionvalidator.services.impl.MSISDNValidator');

/*
* Default operators
*/
INSERT INTO `operators` (`ID`, `operatorname`, `description`, `created`, `created_date`, `lastupdated`, `lastupdated_date`, `refreshtoken`, `tokenvalidity`, `tokentime`, `token`, `tokenurl`, `tokenauth`) VALUES
  (1, 'DIALOG', 'Dialog Opearator', 'axatauser', NULL, 'axatauser', NULL, 'gGgvUANAGhRUzWTyXwYoGuk3WzQa', 157680000, 1395135145139, '4fb164d70def9f37b2f8e2f1daf467', 'http://localhost:8281/token', 'Basic U1JObDQzXzRTVks5MjZaVnNteXExOU1JNVFRYTpEV1Flb2NDeUVyN0lHYk8zRHJxRDc5SmtzVFVh'),
  (2, 'CELCOM', 'Celcom Opearator', 'axatauser', NULL, 'axatauser', NULL, 'gGgvUANAGhRUzWTyXwYoGuk3WzQa', 157680000, 1395135145139, '4fb164d70def9f37b2f8e2f1daf467', 'http://localhost:8281/token', 'Basic U1JObDQzXzRTVks5MjZaVnNteXExOU1JNVFRYTpEV1Flb2NDeUVyN0lHYk8zRHJxRDc5SmtzVFVh'),
  (3, 'ROBI', 'Robi Opearator', 'axatauser', NULL, 'axatauser', NULL, 'gGgvUANAGhRUzWTyXwYoGuk3WzQa', 157680000, 1395135145139, '4fb164d70def9f37b2f8e2f1daf467', 'http://localhost:8281/token', 'Basic U1JObDQzXzRTVks5MjZaVnNteXExOU1JNVFRYTpEV1Flb2NDeUVyN0lHYk8zRHJxRDc5SmtzVFVh');

/*
* Default operator endpoints point to the sandbox
*/
INSERT INTO `operatorendpoints` (`ID`, `operatorid`, `api`, `isactive`, `endpoint`, `created`, `created_date`, `lastupdated`, `lastupdated_date`) VALUES
  (1, 1, 'smsmessaging', 1, 'http://localhost:8081/mifesandbox/SandboxController/smsmessaging/1', NULL, '2014-03-04 11:36:23', NULL, NULL),
  (2, 1, 'payment', 1, 'http://localhost:8081/mifesandbox/SandboxController/payment/1', NULL, '2014-03-04 11:36:58', NULL, NULL),
  (3, 3, 'location', 1, 'http://localhost:8081/mifesandbox/SandboxController/location/1', NULL, '2014-10-02 10:33:41', NULL, NULL);

--
-- Initial Metadata script for table `workflow_api_key_mappings`
-- The data script needs to be updated to include any new api-key mappings.
--
INSERT INTO `workflow_api_key_mappings` (`API_NAME`, `API_KEY`) VALUES ('smsmessaging', 'smsmessaging');
INSERT INTO `workflow_api_key_mappings` (`API_NAME`, `API_KEY`) VALUES ('payment', 'payment');
INSERT INTO `workflow_api_key_mappings` (`API_NAME`, `API_KEY`) VALUES ('location', 'location');

