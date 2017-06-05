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
  PRIMARY KEY (`apiid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_operation`
--

DROP TABLE IF EXISTS `api_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_operation` (
  `api_operationid` int(11) NOT NULL AUTO_INCREMENT,
  `apiid` int(11) DEFAULT NULL,
  `api_operation` varchar(45) DEFAULT NULL,
  `api_operationcode` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`api_operationid`),
  KEY `fk_api_operation_1_idx` (`apiid`),
  CONSTRAINT `fk_api_operation_1` FOREIGN KEY (`apiid`) REFERENCES `api` (`apiid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `categoryid` int(11) NOT NULL AUTO_INCREMENT,
  `categoryname` varchar(45) DEFAULT NULL,
  `categorycode` varchar(45) DEFAULT NULL,
  `categorydesc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`categoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `currencyid` int(11) NOT NULL AUTO_INCREMENT,
  `currencycode` varchar(45) DEFAULT NULL,
  `currencydesc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`currencyid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
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
  `api_operationid` int(11) DEFAULT NULL,
  `rate_defid` int(11) DEFAULT NULL,
  PRIMARY KEY (`operation_rateid`),
  KEY `fk_operation_rate_1_idx` (`api_operationid`),
  KEY `fk_operation_rate_2_idx` (`rate_defid`),
  KEY `fk_operation_rate_3_idx` (`operator_id`),
  CONSTRAINT `fk_operation_rate_1` FOREIGN KEY (`api_operationid`) REFERENCES `api_operation` (`api_operationid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_operation_rate_2` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_operation_rate_3` FOREIGN KEY (`operator_id`) REFERENCES `operator` (`operatorId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator`
--

DROP TABLE IF EXISTS `operator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator` (
  `operatorId` int(11) NOT NULL AUTO_INCREMENT,
  `operatorname` varchar(45) DEFAULT NULL,
  `operatordesc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`operatorId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_category`
--

DROP TABLE IF EXISTS `rate_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_category` (
  `rate_category_id` int(11) NOT NULL AUTO_INCREMENT,
  `rate_defid` int(11) DEFAULT NULL,
  `parentcategoryid` int(11) NOT NULL,
  `childcategoryid` int(11) DEFAULT NULL,
  `tariffid` int(11) DEFAULT NULL,
  PRIMARY KEY (`rate_category_id`),
  KEY `fk_rate_category_1_idx` (`rate_defid`),
  KEY `fk_rate_category_2_idx` (`tariffid`),
  KEY `fk_rate_category_3_idx` (`parentcategoryid`),
  KEY `fk_rate_category_4_idx` (`childcategoryid`),
  CONSTRAINT `fk_rate_category_1` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_category_2` FOREIGN KEY (`tariffid`) REFERENCES `tariff` (`tariffid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_category_3` FOREIGN KEY (`parentcategoryid`) REFERENCES `category` (`categoryid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_category_4` FOREIGN KEY (`childcategoryid`) REFERENCES `category` (`categoryid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_def`
--

DROP TABLE IF EXISTS `rate_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_def` (
  `rate_defid` int(11) NOT NULL AUTO_INCREMENT,
  `rate_defname` varchar(45) DEFAULT NULL,
  `rate_defdesc` varchar(45) DEFAULT NULL,
  `rate_defdefault` tinyint(4) DEFAULT NULL,
  `currencyid` int(11) DEFAULT NULL,
  `rate_typeid` int(11) DEFAULT NULL,
  `rate_defcategorybase` tinyint(4) DEFAULT NULL,
  `tariffid` int(11) DEFAULT NULL,
  PRIMARY KEY (`rate_defid`),
  KEY `fk_rate_def_1_idx` (`rate_typeid`),
  KEY `fk_rate_def_2_idx` (`currencyid`),
  KEY `fk_rate_def_3_idx` (`tariffid`),
  CONSTRAINT `fk_rate_def_1` FOREIGN KEY (`rate_typeid`) REFERENCES `rate_type` (`rate_typeid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_def_2` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_def_3` FOREIGN KEY (`tariffid`) REFERENCES `tariff` (`tariffid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_taxes`
--

DROP TABLE IF EXISTS `rate_taxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_taxes` (
  `rate_taxesid` int(11) NOT NULL AUTO_INCREMENT,
  `rate_defid` int(11) DEFAULT NULL,
  `taxid` int(11) DEFAULT NULL,
  PRIMARY KEY (`rate_taxesid`),
  KEY `fk_rate_taxes_1_idx` (`rate_defid`),
  KEY `fk_rate_taxes_2_idx` (`taxid`),
  CONSTRAINT `fk_rate_taxes_1` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rate_taxes_2` FOREIGN KEY (`taxid`) REFERENCES `tax` (`taxid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rate_type`
--

DROP TABLE IF EXISTS `rate_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rate_type` (
  `rate_typeid` int(11) NOT NULL AUTO_INCREMENT,
  `rate_typecode` varchar(45) DEFAULT NULL,
  `rate_typedesc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`rate_typeid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sub_rate_nb`
--

DROP TABLE IF EXISTS `sub_rate_nb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sub_rate_nb` (
  `sub_rate_nbid` int(11) NOT NULL AUTO_INCREMENT,
  `api_operationid` int(11) DEFAULT NULL,
  `applicationid` int(11) DEFAULT NULL,
  `rate_defid` int(11) DEFAULT NULL,
  `sub_rate_nbactdate` date DEFAULT NULL,
  `sub_rate_nbdisdate` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sub_rate_nbid`),
  KEY `fk_sub_rate_nb_1_idx` (`api_operationid`),
  KEY `fk_sub_rate_nb_2_idx` (`rate_defid`),
  CONSTRAINT `fk_sub_rate_nb_1` FOREIGN KEY (`api_operationid`) REFERENCES `api_operation` (`api_operationid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sub_rate_nb_2` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sub_rate_sb`
--

DROP TABLE IF EXISTS `sub_rate_sb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sub_rate_sb` (
  `sub_rate_sbid` int(11) NOT NULL AUTO_INCREMENT,
  `operatorid` int(11) DEFAULT NULL,
  `api_operationid` int(11) DEFAULT NULL,
  `applicationid` int(11) DEFAULT NULL,
  `rate_defid` int(11) DEFAULT NULL,
  `sub_rate_sbactdate` date DEFAULT NULL,
  `sub_rate_sbdisdate` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sub_rate_sbid`),
  KEY `fk_sub_rate_sb_1_idx` (`operatorid`),
  KEY `fk_sub_rate_sb_2_idx` (`api_operationid`),
  KEY `fk_sub_rate_sb_3_idx` (`rate_defid`),
  CONSTRAINT `fk_sub_rate_sb_1` FOREIGN KEY (`operatorid`) REFERENCES `operator` (`operatorId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sub_rate_sb_2` FOREIGN KEY (`api_operationid`) REFERENCES `api_operation` (`api_operationid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sub_rate_sb_3` FOREIGN KEY (`rate_defid`) REFERENCES `rate_def` (`rate_defid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tariff`
--

DROP TABLE IF EXISTS `tariff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tariff` (
  `tariffid` int(11) NOT NULL AUTO_INCREMENT,
  `tariffname` varchar(45) DEFAULT NULL,
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
  PRIMARY KEY (`tariffid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax`
--

DROP TABLE IF EXISTS `tax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax` (
  `taxid` int(11) NOT NULL AUTO_INCREMENT,
  `taxcode` varchar(45) DEFAULT NULL,
  `taxname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`taxid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tax_validity`
--

DROP TABLE IF EXISTS `tax_validity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tax_validity` (
  `idtax_validityid` int(11) NOT NULL,
  `tax_validityactdate` date DEFAULT NULL,
  `tax_validitydisdate` date DEFAULT NULL,
  `tax_validityval` double DEFAULT NULL,
  `taxid` int(11) DEFAULT NULL,
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
