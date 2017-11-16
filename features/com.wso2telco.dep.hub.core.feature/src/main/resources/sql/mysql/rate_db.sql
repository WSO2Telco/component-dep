-- MySQL dump 10.16  Distrib 10.1.8-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: rate_db
-- ------------------------------------------------------
-- Server version	10.1.8-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api`
--

DROP TABLE IF EXISTS `api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api` (
  `apiid` int(11) NOT NULL AUTO_INCREMENT,
  `apiname` varchar(45) NOT NULL,
  `apidesc` varchar(45) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`apiid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api`
--

LOCK TABLES `api` WRITE;
/*!40000 ALTER TABLE `api` DISABLE KEYS */;
INSERT INTO `api` 
(`apiid`, `apiname`, `apidesc`, `createdby`) VALUES 
(1,'payment','payment','admin'),
(2,'smsmessaging','smsmessaging','admin'),
(3,'location','location','admin'),
(4,'ussd','ussd','admin'),
(5,'credit','credit','admin'),
(6,'customerinfo','customerinfo','admin'),
(7,'provisioning','provisioning','admin'),
(8,'wallet','wallet','admin');
/*!40000 ALTER TABLE `api` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_operation`
--

DROP TABLE IF EXISTS `api_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_operation` (
  `api_operationid` int(11) NOT NULL AUTO_INCREMENT,
  `apiid` int(11) NOT NULL,
  `api_operation` varchar(45) NOT NULL,
  `api_operationcode` varchar(45) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`api_operationid`),
  UNIQUE KEY `apiid_UNIQUE` (`api_operation`),
  KEY `fk_api_operation_1_idx` (`apiid`),
  CONSTRAINT `fk_api_operation_1` FOREIGN KEY (`apiid`) REFERENCES `api` (`apiid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Dumping data for table `api_operation`
--

LOCK TABLES `api_operation` WRITE;
/*!40000 ALTER TABLE `api_operation` DISABLE KEYS */;
INSERT INTO `api_operation` 
(`api_operationid`, `apiid`, `api_operation`, `api_operationcode`, `createdby`) VALUES 
(1,1,'Charge','Charge','admin'),
(2,1,'Refund','Refund','admin'),
(3,1,'ReleaseReservation','ReleaseReservation','admin'),
(4,1,'ReserveAdditionalAmount','ReserveAdditionalAmount','admin'),
(5,1,'ReserveAmount','ReserveAmount','admin'),
(6,1,'ChargeAgainstReservation','ChargeAgainstReservation','admin'),
(7,1,'ListChargeOperations','ListChargeOperations','admin'),
(8,2,'SendSMS','SendSMS','admin'),
(9,2,'ReceiveSMS','ReceiveSMS','admin'),
(10,2,'DeliveryInfo','DeliveryInfo','admin'),
(11,2,'SubscribeToDeliveryNotifications','SubscribeToDeliveryNotifications','admin'),
(12,2,'StopSubscriptionToDeliveryNotifications','StopSubscriptionToDeliveryNotifications','admin'),
(13,2,'SubscribetoMessageNotifcations','SubscribetoMessageNotifcations','admin'),
(14,2,'StopSubscriptionToMessageNotifcations','StopSubscriptionToMessageNotifcations','admin'),
(15,3,'Location','Location','admin'),
(16,4,'USSDInboundCont','USSDInboundCont','admin'),
(17,4,'USSDInboundInit','USSDInboundInit','admin'),
(18,4,'USSDInboundFin','USSDInboundFin','admin'),
(19,4,'USSDOutboundCont','USSDOutboundCont','admin'),
(20,4,'USSDOutboundFin','USSDOutboundFin','admin'),
(21,4,'USSDOutboundInit','USSDOutboundInit','admin'),
(22,4,'USSDSubscription','USSDSubscription','admin'),
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
/*!40000 ALTER TABLE `api_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `categoryid` int(11) NOT NULL AUTO_INCREMENT,
  `categoryname` varchar(45) NOT NULL,
  `categorycode` varchar(45) NOT NULL,
  `categorydesc` varchar(45) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`categoryid`),
  CONSTRAINT `CategorynameK` Unique KEY (`categoryname`),
  CONSTRAINT `CategorycodeK` Unique KEY (`categorycode`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `currencyid` int(11) NOT NULL AUTO_INCREMENT,
  `currencycode` varchar(45) NOT NULL,
  `currencydesc` varchar(45) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`currencyid`),
  CONSTRAINT `CurrencyK` Unique KEY (`currencycode`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operation_rate`
--

DROP TABLE IF EXISTS `operation_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operation_rate` (
  `operation_rateid` int(11) NOT NULL AUTO_INCREMENT,
  `operator_id` int(11) DEFAULT NULL,
  `api_operationid` int(11) NOT NULL,
  `rate_defid` int(11) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`operation_rateid`),
  UNIQUE KEY `operator_id_UNIQUE` (`operator_id`,`api_operationid`,`rate_defid`),
  KEY `fk_operation_rate_1_idx` (`api_operationid`),
  KEY `fk_operation_rate_2_idx` (`rate_defid`),
  KEY `fk_operation_rate_3_idx` (`operator_id`),
  CONSTRAINT `fk_operation_rate_1` FOREIGN KEY (`operator_id`) REFERENCES `operator` (`operatorId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_operation_rate_2` FOREIGN KEY (`api_operationid`) REFERENCES `api_operation` (`api_operationid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_operation_rate_3` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator`
--

DROP TABLE IF EXISTS `operator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator` (
  `operatorId` int(11) NOT NULL AUTO_INCREMENT,
  `operatorname` varchar(45) NOT NULL,
  `operatordesc` varchar(45) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`operatorId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_category`
--

DROP TABLE IF EXISTS `rate_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_category` (
  `rate_category_id` int(11) NOT NULL AUTO_INCREMENT,
  `rate_defid` int(11) NOT NULL,
  `parentcategoryid` int(11) NOT NULL,
  `childcategoryid` int(11) DEFAULT NULL,
  `tariffid` int(11) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rate_category_id`),
  KEY `fk_rate_category_1_idx` (`rate_defid`),
  KEY `fk_rate_category_2_idx` (`tariffid`),
  KEY `fk_rate_category_3_idx` (`parentcategoryid`),
  KEY `fk_rate_category_4_idx` (`childcategoryid`),
  CONSTRAINT `fk_rate_category_1` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_category_2` FOREIGN KEY (`tariffid`) REFERENCES `tariff` (`tariffid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_category_3` FOREIGN KEY (`parentcategoryid`) REFERENCES `category` (`categoryid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_category_4` FOREIGN KEY (`childcategoryid`) REFERENCES `category` (`categoryid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_def`
--

DROP TABLE IF EXISTS `rate_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_def` (
  `rate_defid` int(11) NOT NULL AUTO_INCREMENT,
  `rate_defname` varchar(45) NOT NULL,
  `rate_defdesc` varchar(45) DEFAULT NULL,
  `rate_defdefault` tinyint(4) NOT NULL,
  `currencyid` int(11) NOT NULL,
  `rate_typeid` int(11) NOT NULL,
  `rate_defcategorybase` tinyint(4) NOT NULL,
  `tariffid` int(11) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rate_defid`),
  CONSTRAINT `Rate_defK` Unique KEY (`rate_defname`),
  KEY `fk_rate_def_1_idx` (`rate_typeid`),
  KEY `fk_rate_def_2_idx` (`currencyid`),
  KEY `fk_rate_def_3_idx` (`tariffid`),
  CONSTRAINT `fk_rate_def_1` FOREIGN KEY (`rate_typeid`) REFERENCES `rate_type` (`rate_typeid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_def_2` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_def_3` FOREIGN KEY (`tariffid`) REFERENCES `tariff` (`tariffid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_taxes`
--

DROP TABLE IF EXISTS `rate_taxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_taxes` (
  `rate_taxesid` int(11) NOT NULL AUTO_INCREMENT,
  `rate_defid` int(11) NOT NULL,
  `taxid` int(11) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rate_taxesid`),
  KEY `fk_rate_taxes_1_idx` (`rate_defid`),
  KEY `fk_rate_taxes_2_idx` (`taxid`),
  CONSTRAINT `fk_rate_taxes_1` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_taxes_2` FOREIGN KEY (`taxid`) REFERENCES `tax` (`taxid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_type`
--

DROP TABLE IF EXISTS `rate_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_type` (
  `rate_typeid` int(11) NOT NULL AUTO_INCREMENT,
  `rate_typecode` varchar(45) NOT NULL,
  `rate_typedesc` varchar(45) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`rate_typeid`),
  CONSTRAINT `Rate_typeK` Unique KEY (`rate_typecode`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rate_type`
--

LOCK TABLES `rate_type` WRITE;
/*!40000 ALTER TABLE `rate_type` DISABLE KEYS */;
INSERT INTO `rate_type` VALUES (1,'CONSTANT','Constant Charge Per Month',NULL,'2017-09-22 14:27:29',NULL,'2017-09-22 14:27:29'),(2,'QUOTA','Quota Based Charging per Month',NULL,'2017-09-22 14:27:29',NULL,'2017-09-22 14:27:29'),(3,'PERCENTAGE','Revenue Share Charging',NULL,'2017-09-22 14:27:30',NULL,'2017-09-22 14:27:30'),(4,'PER_REQUEST','Request Based Charging',NULL,'2017-09-22 14:27:30',NULL,'2017-09-22 14:27:30');
/*!40000 ALTER TABLE `rate_type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `sub_rate_nb`
--

DROP TABLE IF EXISTS `sub_rate_nb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sub_rate_nb` (
  `sub_rate_nbid` int(11) NOT NULL AUTO_INCREMENT,
  `api_operationid` int(11) NOT NULL,
  `applicationid` int(11) NOT NULL,
  `rate_defid` int(11) NOT NULL,
  `sub_rate_nbactdate` date DEFAULT NULL,
  `sub_rate_nbdisdate` date DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sub_rate_nbid`),
  UNIQUE KEY `api_operationid_UNIQUE` (`api_operationid`,`applicationid`,`rate_defid`),
  KEY `fk_sub_rate_nb_1_idx` (`api_operationid`),
  KEY `fk_sub_rate_nb_2_idx` (`rate_defid`),
  CONSTRAINT `fk_sub_rate_nb_1` FOREIGN KEY (`api_operationid`) REFERENCES `api_operation` (`api_operationid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sub_rate_nb_2` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sub_rate_sb`
--

DROP TABLE IF EXISTS `sub_rate_sb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sub_rate_sb` (
  `sub_rate_sbid` int(11) NOT NULL AUTO_INCREMENT,
  `operatorid` int(11) NOT NULL,
  `api_operationid` int(11) NOT NULL,
  `applicationid` int(11) NOT NULL,
  `rate_defid` int(11) NOT NULL,
  `sub_rate_sbactdate` date DEFAULT NULL,
  `sub_rate_sbdisdate` date DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`sub_rate_sbid`),
  UNIQUE KEY `operatorid_UNIQUE` (`operatorid`,`api_operationid`,`applicationid`,`rate_defid`),
  KEY `fk_sub_rate_sb_1_idx` (`operatorid`),
  KEY `fk_sub_rate_sb_2_idx` (`api_operationid`),
  KEY `fk_sub_rate_sb_3_idx` (`rate_defid`),
  CONSTRAINT `fk_sub_rate_sb_1` FOREIGN KEY (`operatorid`) REFERENCES `operator` (`operatorId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sub_rate_sb_2` FOREIGN KEY (`api_operationid`) REFERENCES `api_operation` (`api_operationid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sub_rate_sb_3` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tariff`
--

DROP TABLE IF EXISTS `tariff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tariff` (
  `tariffid` int(11) NOT NULL AUTO_INCREMENT,
  `tariffname` varchar(45) NOT NULL,
  `tariffdesc` varchar(45) DEFAULT NULL,
  `tariffdefaultval` double DEFAULT NULL,
  `tariffmaxcount` int(11) DEFAULT NULL,
  `tariffexcessrate` double DEFAULT NULL,
  `tariffdefrate` double DEFAULT NULL,
  `tariffspcommission` double DEFAULT NULL,
  `tariffadscommission` double DEFAULT NULL,
  `tariffopcocommission` double DEFAULT NULL,
  `tariffsurchargeval` double DEFAULT NULL,
  `tariffsurchargeAds` double DEFAULT NULL,
  `tariffsurchargeOpco` double DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tariffid`),
  CONSTRAINT `Rate_tariffK` Unique KEY (`tariffname`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax`
--

DROP TABLE IF EXISTS `tax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax` (
  `taxid` int(11) NOT NULL AUTO_INCREMENT,
  `taxcode` varchar(45) NOT NULL,
  `taxname` varchar(45) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`taxid`),
  CONSTRAINT `TaxK` Unique KEY (`taxname`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_validity`
--

DROP TABLE IF EXISTS `tax_validity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax_validity` (
  `idtax_validityid` int(11) NOT NULL,
  `tax_validityactdate` date NOT NULL,
  `tax_validitydisdate` date NOT NULL,
  `tax_validityval` double NOT NULL,
  `taxid` int(11) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updateddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idtax_validityid`),
  KEY `fk_tax_validity_1_idx` (`taxid`),
  CONSTRAINT `fk_tax_validity_1` FOREIGN KEY (`taxid`) REFERENCES `tax` (`taxid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-05 15:01:47
DROP TABLE IF EXISTS audit;
CREATE TABLE audit (
  `tbl_name` VARCHAR(255) DEFAULT NULL,
  `id` VARCHAR(255) DEFAULT NULL,
  `col_name` VARCHAR (255) DEFAULT NULL,
  `old_data` VARCHAR(255) DEFAULT NULL,
  `new_data` VARCHAR(255) DEFAULT NULL,
  `action` VARCHAR(255) DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

DELIMITER //
DROP TRIGGER IF EXISTS update_tariff_trigger;
CREATE TRIGGER update_tariff_trigger BEFORE UPDATE ON tariff
  FOR EACH ROW BEGIN
    IF OLD.tariffname <> NEW.tariffname THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffname',
      OLD.tariffname, NEW.tariffname, 'update' ,NOW());
    END IF;
    IF OLD.tariffdesc <> NEW.tariffdesc THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffdesc', OLD.tariffdesc, NEW.tariffdesc, 'update' ,NOW());
    END IF;
    IF OLD.tariffdefaultval <> NEW.tariffdefaultval THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffdefaultval', OLD.tariffdefaultval, NEW.tariffdefaultval, 'update' ,NOW());
    END IF;
    IF OLD.tariffmaxcount <> NEW.tariffmaxcount THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffmaxcount', OLD.tariffmaxcount, NEW.tariffmaxcount, 'update' ,NOW());
    END IF;
    IF OLD.tariffexcessrate <> NEW.tariffexcessrate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffexcessrate', OLD.tariffexcessrate, NEW.tariffexcessrate, 'update' ,NOW());
    END IF;
    IF OLD.tariffdefrate <> NEW.tariffdefrate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffdefrate', OLD.tariffdefrate, NEW.tariffdefrate, 'update' ,NOW());
    END IF;
    IF OLD.tariffname <> NEW.tariffname THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffspcommission', OLD.tariffspcommission, NEW.tariffspcommission, 'update' ,NOW());
    END IF;
    IF OLD.tariffadscommission <> NEW.tariffadscommission THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffadscommission', OLD.tariffadscommission, NEW.tariffadscommission, 'update' ,NOW());
    END IF;
    IF OLD.tariffopcocommission <> NEW.tariffopcocommission THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffopcocommission', OLD.tariffopcocommission, NEW.tariffopcocommission, 'update' ,NOW());
    END IF;
    IF OLD.tariffsurchargeval <> NEW.tariffsurchargeval THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffsurchargeval', OLD.tariffsurchargeval, NEW.tariffsurchargeval, 'update' ,NOW());
    END IF;
    IF OLD.tariffsurchargeAds <> NEW.tariffsurchargeAds THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffsurchargeAds', OLD.tariffsurchargeAds, NEW.tariffsurchargeAds, 'update' ,NOW());
    END IF;
    IF OLD.tariffsurchargeOpco <> NEW.tariffsurchargeOpco THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'tariffsurchargeOpco', OLD.tariffsurchargeOpco, NEW.tariffsurchargeOpco, 'update' ,NOW());
    END IF;
    IF OLD.createdby <> NEW.createdby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'createdby', OLD.createdby, NEW.createdby, 'update' ,NOW());
    END IF;
    IF OLD.createddate <> NEW.createddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'createddate', OLD.createddate, NEW.createddate, 'update' ,NOW());
    END IF;
    IF OLD.updatedby <> NEW.updatedby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'updatedby', OLD.updatedby, NEW.updatedby, 'update' ,NOW());
    END IF;
    IF OLD.updateddate <> NEW.updateddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,  'updateddate', OLD.updateddate, NEW.updateddate, 'update' ,NOW());
    END IF;
END ;

DROP TRIGGER IF EXISTS delete_tariff_trigger;
CREATE TRIGGER delete_tariff_trigger AFTER DELETE ON tariff
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', OLD.tariffid,
   'tariffid', OLD.tariffid, 'null' , 'delete' ,NOW());
END;

DROP TRIGGER IF EXISTS insert_tariff_trigger;
CREATE TRIGGER insert_tariff_trigger AFTER INSERT ON tariff
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('tarrif', NEW.tariffid,
   'tariffid', 'null', NEW.tariffid , 'insert' ,NOW());
END ;

DROP TRIGGER IF EXISTS insert_tax_validity_trigger;
CREATE TRIGGER insert_tax_validity_trigger AFTER INSERT ON tax_validity
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', NEW
   .idtax_validityid, 'idtax_validityid', 'null', NEW.idtax_validityid , 'insert' ,NOW());
END ;

DROP TRIGGER IF EXISTS delete_tax_validity_trigger;
CREATE TRIGGER delete_tax_validity_trigger AFTER DELETE ON tax_validity
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD
   .idtax_validityid, 'idtax_validityid', OLD.idtax_validityid , 'null' , 'delete' ,NOW());
END ;

DROP TRIGGER IF EXISTS update_tax_validity_trigger;
CREATE TRIGGER update_tax_validity_trigger BEFORE UPDATE ON tax_validity
  FOR EACH ROW BEGIN

    IF OLD.tax_validityactdate <> NEW.tax_validityactdate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.idtax_validityid, 'tax_validityactdate', OLD.tax_validityactdate, NEW.tax_validityactdate, 'update' ,NOW());
    END IF;
    IF OLD.tax_validitydisdate <> NEW.tax_validitydisdate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.idtax_validityid, 'tax_validitydisdate', OLD.tax_validitydisdate, NEW.tax_validitydisdate, 'update' ,NOW());
    END IF;
    IF OLD.tax_validityval <> NEW.tax_validityval THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.idtax_validityid, 'tax_validityval', OLD.tax_validityval, NEW.tax_validityval, 'update' ,NOW());
    END IF;
    IF OLD.taxid <> NEW.taxid THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.idtax_validityid, 'taxid', OLD.taxid, NEW.taxid, 'update' ,NOW());
    END IF;
    IF OLD.createdby <> NEW.createdby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.createdby, 'createdby', OLD.createdby, NEW.createdby, 'update' ,NOW());
    END IF;
     IF OLD.createddate <> NEW.createddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.createddate, 'createddate', OLD.createddate, NEW.createddate, 'update' ,NOW());
    END IF;
     IF OLD.updatedby <> NEW.updatedby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.updatedby, 'updatedby', OLD.updatedby, NEW.updatedby, 'update' ,NOW());
    END IF;
     IF OLD.updateddate <> NEW.updateddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('tax_validity', OLD.updateddate, 'updateddate', OLD.updateddate, NEW.updateddate, 'update' ,NOW());
    END IF;
END ;

DROP TRIGGER IF EXISTS insert_rate_taxes_trigger;
CREATE TRIGGER insert_rate_taxes_trigger AFTER INSERT ON rate_taxes
  FOR EACH ROW BEGIN
    INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('rate_taxes', NEW.rate_taxesid, 'rate_taxesid', 'null', NEW.rate_taxesid , 'insert' ,NOW());
END ;

DROP TRIGGER IF EXISTS delete_rate_taxes_trigger;
CREATE TRIGGER delete_rate_taxes_trigger AFTER DELETE ON rate_taxes
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('rate_taxes', OLD.rate_taxesid,
   'rate_taxesid', OLD.rate_taxesid , 'null' , 'delete' ,NOW());
END ;


DROP TRIGGER IF EXISTS update_rate_taxes_trigger;
CREATE TRIGGER update_rate_taxes_trigger BEFORE UPDATE ON rate_taxes
  FOR EACH ROW BEGIN

    IF OLD.rate_defid <> NEW.rate_defid THEN
      INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('rate_taxes', OLD
      .rate_taxesid,
      'rate_defid', OLD.rate_defid, NEW.rate_defid, 'update' ,NOW());
    END IF;
    IF OLD.taxid <> NEW.taxid THEN
      INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('rate_taxes', OLD
      .rate_taxesid, 'taxid', OLD.taxid, NEW.taxid, 'update' ,NOW());
    END IF;
END ;

DROP TRIGGER IF EXISTS insert_sub_rate_nb_trigger;
CREATE TRIGGER insert_sub_rate_nb_trigger AFTER INSERT ON sub_rate_nb
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', NEW
   .sub_rate_nbid, 'sub_rate_nbid', 'null' , NEW.sub_rate_nbid , 'insert' ,NOW());
END ;

DROP TRIGGER IF EXISTS delete_sub_rate_nb_trigger;
CREATE TRIGGER delete_sub_rate_nb_trigger AFTER DELETE ON sub_rate_nb
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id, col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD
   .sub_rate_nbid, 'sub_rate_nbid', OLD.sub_rate_nbid , 'null' , 'delete' ,NOW());
END ;

DROP TRIGGER IF EXISTS update_sub_rate_nb_trigger;
CREATE TRIGGER update_sub_rate_nb_trigger BEFORE UPDATE ON sub_rate_nb
  FOR EACH ROW BEGIN

    IF OLD.api_operationid <> NEW.api_operationid THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.sub_rate_nbid, 'api_operationid', OLD.api_operationid, NEW.api_operationid, 'update' ,NOW());
    END IF;
    IF OLD.applicationid <> NEW.applicationid THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.sub_rate_nbid, 'applicationid', OLD.applicationid, NEW.applicationid, 'update' ,NOW());
    END IF;
    IF OLD.rate_defid <> NEW.rate_defid THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.sub_rate_nbid, 'rate_defid', OLD.rate_defid, NEW.rate_defid, 'update' ,NOW());
    END IF;
    IF OLD.sub_rate_nbactdate <> NEW.sub_rate_nbactdate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.sub_rate_nbid, 'sub_rate_nbactdate', OLD.sub_rate_nbactdate, NEW.sub_rate_nbactdate, 'update' ,NOW());
    END IF;
    IF OLD.sub_rate_nbdisdate <> NEW.sub_rate_nbdisdate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.sub_rate_nbid, 'sub_rate_nbdisdate', OLD.sub_rate_nbdisdate, NEW.sub_rate_nbdisdate, 'update' ,NOW());
    END IF;
     IF OLD.createdby <> NEW.createdby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.createdby, 'createdby', OLD.createdby, NEW.createdby, 'update' ,NOW());
    END IF;
     IF OLD.createddate <> NEW.createddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.createddate, 'createddate', OLD.createddate, NEW.createddate, 'update' ,NOW());
    END IF;
     IF OLD.updatedby <> NEW.updatedby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.updatedby, 'updatedby', OLD.updatedby, NEW.updatedby, 'update' ,NOW());
    END IF;
     IF OLD.updateddate <> NEW.updateddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_nb', OLD.updateddate, 'updateddate', OLD.updateddate, NEW.updateddate, 'update' ,NOW());
    END IF;
END ;

DROP TRIGGER IF EXISTS insert_sub_rate_sb_trigger;
CREATE TRIGGER insert_sub_rate_sb_trigger AFTER INSERT ON sub_rate_sb
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', NEW.sub_rate_sbid, 'sub_rate_sbid', 'null'
   , NEW.sub_rate_sbid , 'insert' ,NOW());
END ;

DROP TRIGGER IF EXISTS delete_sub_rate_sb_trigger;
CREATE TRIGGER delete_sub_rate_sb_trigger AFTER DELETE ON sub_rate_sb
  FOR EACH ROW BEGIN

   INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', OLD.sub_rate_sbid,
   'sub_rate_sbid', OLD.sub_rate_sbid , 'null' , 'delete' ,NOW());
END ;

DROP TRIGGER IF EXISTS update_sub_rate_sb_trigger;
CREATE TRIGGER update_sub_rate_sb_trigger BEFORE UPDATE ON sub_rate_sb
  FOR EACH ROW BEGIN

    IF OLD.api_operationid <> NEW.api_operationid THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', NEW.sub_rate_sbid, 'api_operationid', OLD.api_operationid, NEW.api_operationid, 'update' ,NOW());
    END IF;
    IF OLD.applicationid <> NEW.applicationid THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', NEW.sub_rate_sbid, 'applicationid', OLD.applicationid, NEW.applicationid, 'update' ,NOW());
    END IF;
    IF OLD.rate_defid <> NEW.rate_defid THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', NEW.sub_rate_sbid, 'rate_defid', OLD.rate_defid, NEW.rate_defid, 'update' ,NOW());
    END IF;
    IF OLD.sub_rate_sbactdate <> NEW.sub_rate_sbactdate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', NEW.sub_rate_sbid, 'sub_rate_sbactdate', OLD.sub_rate_sbactdate, NEW.sub_rate_sbactdate, 'update' ,NOW());
    END IF;
    IF OLD.sub_rate_sbdisdate <> NEW.sub_rate_sbdisdate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', NEW.sub_rate_sbid, 'sub_rate_sbdisdate', OLD.sub_rate_sbdisdate, NEW.sub_rate_sbdisdate, 'update' ,NOW());
    END IF;
     IF OLD.createdby <> NEW.createdby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', OLD.createdby, 'createdby', OLD.createdby, NEW.createdby, 'update' ,NOW());
    END IF;
     IF OLD.createddate <> NEW.createddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', OLD.createddate, 'createddate', OLD.createddate, NEW.createddate, 'update' ,NOW());
    END IF;
     IF OLD.updatedby <> NEW.updatedby THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', OLD.updatedby, 'updatedby', OLD.updatedby, NEW.updatedby, 'update' ,NOW());
    END IF;
     IF OLD.updateddate <> NEW.updateddate THEN
      INSERT INTO audit(tbl_name, id,  col_name, old_data, new_data, action, updated_at) VALUES ('sub_rate_sb', OLD.updateddate, 'updateddate', OLD.updateddate, NEW.updateddate, 'update' ,NOW());
    END IF;
END //

DELIMITER ;