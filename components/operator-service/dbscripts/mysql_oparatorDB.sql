/*
* Tables for whitelist & blacklist
*/
CREATE TABLE `optBlacklistmsisdn` (
 `Index` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 `MSISDN` varchar(45) NOT NULL,
 `API_ID` varchar(45) NOT NULL,
 `API_NAME` varchar(45) NOT NULL,
 `USER_ID` varchar(45) NOT NULL,
  UNIQUE KEY `UNQ_blacklistmsisdn` (`API_NAME`, `MSISDN`));

  --
-- Table structure for table `operators`
--

CREATE TABLE IF NOT EXISTS `optoperators` (
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

CREATE TABLE IF NOT EXISTS `optmerchantopco_blacklist` (
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

CREATE TABLE IF NOT EXISTS`optsubscription_WhiteList` (
  `index` int(11) NOT NULL AUTO_INCREMENT,
  `subscriptionID` varchar(45) NOT NULL,
  `msisdn` varchar(45) NOT NULL ,
  `api_id` varchar(45) NOT NULL,
  `application_id` varchar(45) NOT NULL,
  PRIMARY KEY (`index`),
  UNIQUE white_label_unique_con(subscriptionID, msisdn, api_id, application_id)
);
