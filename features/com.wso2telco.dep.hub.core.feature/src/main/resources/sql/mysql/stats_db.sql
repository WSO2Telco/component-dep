/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

CREATE TABLE IF NOT EXISTS `API_DESTINATION_SUMMARY` (
  `api` varchar(100) NOT NULL DEFAULT '',
  `version` varchar(100) NOT NULL DEFAULT '',
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `context` varchar(100) NOT NULL DEFAULT '',
  `destination` varchar(100) NOT NULL DEFAULT '',
  `total_request_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) NOT NULL DEFAULT '',
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`api`,`version`,`apiPublisher`,`context`,`destination`,`hostName`,`time`)
); 

CREATE TABLE IF NOT EXISTS `API_FAULT_SUMMARY` (
  `api` varchar(100) NOT NULL DEFAULT '',
  `version` varchar(100) NOT NULL DEFAULT '',	
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `consumerKey` varchar(100) DEFAULT NULL,
  `context` varchar(100) NOT NULL DEFAULT '',
  `total_fault_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) NOT NULL DEFAULT '',
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`api`,`version`,`apiPublisher`,`context`,`hostName`,`time`)
); 


CREATE TABLE IF NOT EXISTS `API_REQUEST_SUMMARY` (
  `api` varchar(100) NOT NULL DEFAULT '',
  `api_version` varchar(100) NOT NULL DEFAULT '',
  `version` varchar(100) NOT NULL DEFAULT '',
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `consumerKey` varchar(100) NOT NULL DEFAULT '',
  `userId` varchar(100) NOT NULL DEFAULT '',
  `context` varchar(100) NOT NULL DEFAULT '',
  `max_request_time` bigint(20) DEFAULT NULL,
  `total_request_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) NOT NULL DEFAULT '',
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`api`,`api_version`,`version`,`apiPublisher`,`consumerKey`,`userId`,`context`,`hostName`,`time`)
); 

CREATE TABLE IF NOT EXISTS `API_Resource_USAGE_SUMMARY` (
  `api` varchar(100) NOT NULL DEFAULT '',
  `version` varchar(100) NOT NULL DEFAULT '',
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `consumerKey` varchar(100) NOT NULL DEFAULT '',
  `resourcePath` varchar(100) NOT NULL DEFAULT '',
  `context` varchar(100) NOT NULL DEFAULT '',
  `method` varchar(100) NOT NULL DEFAULT '',
  `total_request_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) NOT NULL DEFAULT '',
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`api`,`version`,`apiPublisher`,`consumerKey`,`context`,`resourcePath`,`method`,`hostName`,`time`)
); 

CREATE TABLE IF NOT EXISTS `API_RESPONSE_SUMMARY` (
  `api_version` varchar(100) NOT NULL DEFAULT '',
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `context` varchar(100) NOT NULL DEFAULT '',
  `serviceTime` int(11) DEFAULT NULL,
  `total_response_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) NOT NULL DEFAULT '',
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`api_version`,`apiPublisher`,`context`,`hostName`,`time`)
); 


CREATE TABLE IF NOT EXISTS `API_VERSION_USAGE_SUMMARY` (
  `api` varchar(100) NOT NULL DEFAULT '',
  `version` varchar(100) NOT NULL DEFAULT '',
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `context` varchar(100) NOT NULL DEFAULT '',
  `total_request_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) NOT NULL DEFAULT '',
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`api`,`version`,`apiPublisher`,`context`,`hostName`,`time`)
); 

CREATE TABLE IF NOT EXISTS `API_THROTTLED_OUT_SUMMARY` (
  `api` varchar(100) NOT NULL DEFAULT '',
  `api_version` varchar(100) NOT NULL DEFAULT '',
  `context` varchar(100) NOT NULL DEFAULT '',
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `applicationName` varchar(100) NOT NULL DEFAULT '',
  `tenantDomain` varchar(100) NOT NULL DEFAULT '',
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `week` int(11) DEFAULT NULL,
  `time` varchar(30) NOT NULL DEFAULT '',
  `success_request_count` int(11) DEFAULT NULL,
  `throttleout_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`api`,`api_version`,`context`,`apiPublisher`,`applicationName`,`tenantDomain`,`year`,`month`,`day`,`time`)
);

CREATE TABLE IF NOT EXISTS `API_LAST_ACCESS_TIME_SUMMARY` (
  `tenantDomain` varchar(100) NOT NULL DEFAULT '',
  `apiPublisher` varchar(100) NOT NULL DEFAULT '',
  `api` varchar(100) NOT NULL DEFAULT '',
  `version` varchar(100) DEFAULT NULL,
  `userId` varchar(100) DEFAULT NULL,
  `context` varchar(100) DEFAULT NULL,
  `max_request_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tenantDomain`,`apiPublisher`,`api`)
);


DROP TABLE IF EXISTS `NB_API_REQUEST_SUMMARY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NB_API_REQUEST_SUMMARY` (
  `messageRowID` varchar(100) NOT NULL,
  `api` varchar(100) DEFAULT NULL,
  `api_version` varchar(100) DEFAULT NULL,
  `version` varchar(100) DEFAULT NULL,
  `apiPublisher` varchar(100) DEFAULT NULL,
  `consumerKey` varchar(100) DEFAULT NULL,
  `userId` varchar(100) DEFAULT NULL,
  `context` varchar(100) DEFAULT NULL,
  `request_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) DEFAULT NULL,
  `resourcePath` varchar(100) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `requestId` varchar(100) DEFAULT NULL,
  `chargeAmount` varchar(20) DEFAULT NULL,
  `purchaseCategoryCode` varchar(40) DEFAULT NULL,
  `jsonBody` text,
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`messageRowID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NB_API_RESPONSE_SUMMARY`
--

DROP TABLE IF EXISTS `NB_API_RESPONSE_SUMMARY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NB_API_RESPONSE_SUMMARY` (
  `messageRowID` varchar(100) NOT NULL,
  `api` varchar(100) DEFAULT NULL,
  `api_version` varchar(100) DEFAULT NULL,
  `version` varchar(100) DEFAULT NULL,
  `apiPublisher` varchar(100) DEFAULT NULL,
  `consumerKey` varchar(100) DEFAULT NULL,
  `userId` varchar(100) DEFAULT NULL,
  `context` varchar(100) DEFAULT NULL,
  `serviceTime` int(11) DEFAULT NULL,
  `response_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) DEFAULT NULL,
  `resourcePath` varchar(100) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `requestId` varchar(100) DEFAULT NULL,
  `responseCode` varchar(5) DEFAULT NULL,
  `msisdn` varchar(20) DEFAULT NULL,
  `operatorRef` varchar(100) DEFAULT NULL,
  `chargeAmount` varchar(20) DEFAULT NULL,
  `purchaseCategoryCode` varchar(40) DEFAULT NULL,
  `exceptionId` varchar(10) DEFAULT NULL,
  `exceptionMessage` varchar(255) DEFAULT NULL,
  `jsonBody` text,
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) DEFAULT NULL,
  `operationType` int(11) DEFAULT NULL,
  `merchantId` varchar(100) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `subCategory` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`messageRowID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SB_API_REQUEST_SUMMARY`
--

DROP TABLE IF EXISTS `SB_API_REQUEST_SUMMARY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SB_API_REQUEST_SUMMARY` (
  `messageRowID` varchar(100) NOT NULL,
  `api` varchar(100) DEFAULT NULL,
  `api_version` varchar(100) DEFAULT NULL,
  `version` varchar(100) DEFAULT NULL,
  `apiPublisher` varchar(100) DEFAULT NULL,
  `consumerKey` varchar(100) DEFAULT NULL,
  `userId` varchar(100) DEFAULT NULL,
  `context` varchar(100) DEFAULT NULL,
  `request_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) DEFAULT NULL,
  `resourcePath` varchar(100) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `requestId` varchar(100) DEFAULT NULL,
  `operatorId` varchar(100) DEFAULT NULL,
  `chargeAmount` varchar(20) DEFAULT NULL,
  `purchaseCategoryCode` varchar(40) DEFAULT NULL,
  `jsonBody` text,
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`messageRowID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SB_API_RESPONSE_SUMMARY`
--

DROP TABLE IF EXISTS `SB_API_RESPONSE_SUMMARY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SB_API_RESPONSE_SUMMARY` (
  `messageRowID` varchar(100) NOT NULL,
  `api` varchar(100) DEFAULT NULL,
  `api_version` varchar(100) DEFAULT NULL,
  `version` varchar(100) DEFAULT NULL,
  `apiPublisher` varchar(100) DEFAULT NULL,
  `consumerKey` varchar(100) DEFAULT NULL,
  `userId` varchar(100) DEFAULT NULL,
  `context` varchar(100) DEFAULT NULL,
  `serviceTime` int(11) DEFAULT NULL,
  `response_count` int(11) DEFAULT NULL,
  `hostName` varchar(100) DEFAULT NULL,
  `resourcePath` varchar(100) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `requestId` varchar(100) DEFAULT NULL,
  `operatorId` varchar(100) DEFAULT NULL,
  `responseCode` varchar(5) DEFAULT NULL,
  `msisdn` varchar(20) DEFAULT NULL,
  `operatorRef` varchar(100) DEFAULT NULL,
  `chargeAmount` varchar(20) DEFAULT NULL,
  `purchaseCategoryCode` varchar(40) DEFAULT NULL,
  `exceptionId` varchar(10) DEFAULT NULL,
  `exceptionMessage` varchar(255) DEFAULT NULL,
  `jsonBody` text,
  `year` smallint(6) DEFAULT NULL,
  `month` smallint(6) DEFAULT NULL,
  `day` smallint(6) DEFAULT NULL,
  `time` varchar(30) DEFAULT NULL,
  `operationType` int(11) DEFAULT NULL,
  `merchantId` varchar(100) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `subCategory` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`messageRowID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `sub_approval_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sub_approval_audit` (
  `API_PROVIDER` varchar(200) NOT NULL DEFAULT '',
  `API_NAME` varchar(200) NOT NULL DEFAULT '',
  `API_VERSION` varchar(30) NOT NULL DEFAULT '',
  `APP_ID` int(11) NOT NULL,
  `SUB_STATUS` varchar(50) DEFAULT 'ON_HOLD',
  `SUB_APPROVAL_TYPE` varchar(50) DEFAULT NULL,
  `COMPLETED_BY_ROLE` varchar(50) NOT NULL DEFAULT '',
  `COMPLETED_BY_USER` varchar(50) DEFAULT NULL,
  `COMPLETED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`APP_ID`,`API_PROVIDER`,`API_NAME`,`API_VERSION`,`COMPLETED_BY_ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `app_approval_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_approval_audit` (
  `APP_APPROVAL_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `APP_NAME` varchar(100) DEFAULT NULL,
  `APP_CREATOR` varchar(50) DEFAULT NULL,
  `APP_STATUS` varchar(50) DEFAULT 'ON_HOLD',
  `APP_APPROVAL_TYPE` varchar(50) DEFAULT NULL,
  `COMPLETED_BY_ROLE` varchar(50) DEFAULT NULL,
  `COMPLETED_BY_USER` varchar(50) DEFAULT NULL,
  `COMPLETED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`APP_APPROVAL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;


/*
* Table for tax type definition
*/
CREATE TABLE IF NOT EXISTS tax ( 
    id INT NOT NULL AUTO_INCREMENT,
    type VARCHAR(25) NOT NULL,
    effective_from DATE,
    effective_to DATE,
    value DECIMAL(7,6) NOT NULL,
    PRIMARY KEY (id)
);


/*
* Table for API subscriptions to tax type mapping
*/
CREATE TABLE IF NOT EXISTS subscription_tax ( 
    application_id INT NOT NULL,
    api_id INT NOT NULL,
    tax_type VARCHAR(25) NOT NULL,
    PRIMARY KEY (application_id, api_id, tax_type)
);


/*
* Table for API subscriptions to charge rate mapping
*/
CREATE TABLE IF NOT EXISTS subscription_rates ( 
    application_id INT NOT NULL,
    api_id INT NOT NULL,
    operator_name varchar(45) NOT NULL,
	rate_name varchar(50) DEFAULT NULL,
	rate_id_sb varchar(50) DEFAULT NULL,
	operation_id int(11) NOT NULL,
    PRIMARY KEY (application_id, api_id, operator_name)
);

/*
* Tables for whitelist & blacklist
*/
CREATE TABLE `blacklistmsisdn` (
 `Index` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 `MSISDN` varchar(45) NOT NULL,
 `API_ID` varchar(45) NOT NULL,
 `API_NAME` varchar(45) NOT NULL,
 `USER_ID` varchar(45) NOT NULL,
  UNIQUE KEY `UNQ_blacklistmsisdn` (`API_NAME`, `MSISDN`));

CREATE TABLE IF NOT EXISTS`subscription_WhiteList` (
  `index` int(11) NOT NULL AUTO_INCREMENT,
  `subscriptionID` varchar(45) NOT NULL,
  `msisdn` varchar(45) NOT NULL ,
  `api_id` varchar(45) NOT NULL,
  `application_id` varchar(45) NOT NULL,
  PRIMARY KEY (`index`),
  UNIQUE white_label_unique_con(subscriptionID, msisdn, api_id, application_id)
);

CREATE TABLE IF NOT EXISTS `admin_comments` (
  `TaskID` int(11) NOT NULL,
  `Comment` varchar(255) DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL,
  `Description` varchar(1000) NOT NULL,
  PRIMARY KEY (`TaskID`)
);
CREATE TABLE IF NOT EXISTS `subscription_comments` (
  `TaskID` varchar(255) NOT NULL,
  `Comment` varchar(1024) NOT NULL,
  `Status` varchar(255) NOT NULL,
  `Description` varchar(1024) NOT NULL,
  PRIMARY KEY (`TaskID`)
);

CREATE TABLE  `rates_percentages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_code` varchar(45) NOT NULL,
  `app_id` int(10) unsigned NOT NULL,
  `sp_commission` double NOT NULL,
  `ads_commission` double NOT NULL,
  `opco_commission` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `subscription_rates_nb` (
  `application_id` int(11) NOT NULL,
  `api_id` int(11) NOT NULL,
  `rate_id_nb` varchar(50) DEFAULT NULL,
  `operation_id` int(11) NOT NULL,
  PRIMARY KEY (`application_id`,`api_id`,`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `api_operation_types` (
  `operation_id` int(11) NOT NULL DEFAULT '0',
  `api` varchar(225) DEFAULT NULL,
  `operation` varchar(225) DEFAULT NULL,
  `default_rate` varchar(255) NOT NULL,
  PRIMARY KEY (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `api_operation_types` (`operation_id`, `api`, `operation`,`default_rate`) VALUES
(100, 'payment', 'Charge', 'p1'),
(101, 'payment', 'Refund', 'RF2'),
(200, 'smsmessaging', 'Send SMS' ,'SM1'),
(201, 'smsmessaging', 'Retrive SMS','SM2'),
(202, 'smsmessaging', 'Query SMS Delivery','SM2'),
(203, 'smsmessaging', 'Delivery Subscription','SM2'),
(204, 'smsmessaging', 'Stop Delivery Subscription','SM2'),
(205, 'smsmessaging', 'Retrive SMS Subscription','SM2'),
(206, 'smsmessaging ', 'Stop Retrive SMS Subscription','SM2'),
(207, 'smsmessaging', 'SMS Inbound Notification','SM2'),
(300, 'location', 'Location','lb1'),
(400, 'ussd', 'Send USSD','u1');


CREATE TABLE `merchant_rates_percentages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subscriber` varchar(45) NOT NULL,
  `merchant_code` varchar(45) NOT NULL,
  `app_id` int(10) unsigned NOT NULL,
  `sp_commission` double NOT NULL,
  `ads_commission` double NOT NULL,
  `opco_commission` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

