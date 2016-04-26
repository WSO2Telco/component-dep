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