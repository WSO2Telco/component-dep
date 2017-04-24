-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: wso2telco_rate_db
-- ------------------------------------------------------
-- Server version	5.7.17-0ubuntu0.16.04.1

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
-- Table structure for table `inmdapi`
--

DROP TABLE IF EXISTS `inmdapi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdapi` (
  `apiDid` int(11) NOT NULL,
  `code` varchar(45) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  PRIMARY KEY (`apiDid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdapi`
--

LOCK TABLES `inmdapi` WRITE;
/*!40000 ALTER TABLE `inmdapi` DISABLE KEYS */;
INSERT INTO `inmdapi` VALUES (1,'Payment','2017-03-29','2017-03-29');
/*!40000 ALTER TABLE `inmdapi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdoperator`
--

CREATE TABLE `inmdoperator` (
  `operatorDid` int(11) NOT NULL,
  `code` varchar(45) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  PRIMARY KEY (`operatorDid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `inmdoperator`
--

LOCK TABLES `inmdoperator` WRITE;
/*!40000 ALTER TABLE `inmdapi` DISABLE KEYS */;
INSERT INTO `inmdoperator` VALUES (1,'operator1','2017-03-29','2017-03-29');
/*!40000 ALTER TABLE `inmdapi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdcatagory`
--

DROP TABLE IF EXISTS `inmdcatagory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdcatagory` (
  `catagoryDid` int(11) NOT NULL,
  `parentCatagoryDid` int(11) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  PRIMARY KEY (`catagoryDid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdcatagory`
--

LOCK TABLES `inmdcatagory` WRITE;
/*!40000 ALTER TABLE `inmdcatagory` DISABLE KEYS */;
INSERT INTO `inmdcatagory` VALUES (1,1,'GAME','2017-03-29'),(2,1,'GAME','2017-03-30');
/*!40000 ALTER TABLE `inmdcatagory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdcommission`
--

DROP TABLE IF EXISTS `inmdcommission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdcommission` (
  `CommissionDid` int(11) NOT NULL,
  `sp` int(11) DEFAULT NULL,
  `hub` int(11) DEFAULT NULL,
  `opco` int(11) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  PRIMARY KEY (`CommissionDid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdcommission`
--

LOCK TABLES `inmdcommission` WRITE;
/*!40000 ALTER TABLE `inmdcommission` DISABLE KEYS */;
INSERT INTO `inmdcommission` VALUES (1,80,15,5,'2017-03-29'),(2,75,20,5,'2017-03-30'),(3,60,20,20,'2017-03-30');
/*!40000 ALTER TABLE `inmdcommission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdcurrency`
--

DROP TABLE IF EXISTS `inmdcurrency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdcurrency` (
  `currencyDid` int(11) NOT NULL,
  `code` varchar(45) DEFAULT NULL,
  `discription` varchar(45) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  PRIMARY KEY (`currencyDid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdcurrency`
--

LOCK TABLES `inmdcurrency` WRITE;
/*!40000 ALTER TABLE `inmdcurrency` DISABLE KEYS */;
INSERT INTO `inmdcurrency` VALUES (1,'Rs','LKR','2017-03-29','2017-03-29');
/*!40000 ALTER TABLE `inmdcurrency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdnbsubscriptionrate`
--

DROP TABLE IF EXISTS `inmdnbsubscriptionrate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdnbsubscriptionrate` (
  `NBSubscriptionRateDid` int(11) NOT NULL AUTO_INCREMENT,
  `servicesRateDid` int(11) NOT NULL,
  `applicationDid` int(11) NOT NULL,
  `apiDid` int(11) NOT NULL,
  PRIMARY KEY (`NBSubscriptionRateDid`,`servicesRateDid`,`applicationDid`,`apiDid`),
  KEY `fk_inmdNBSubscriptionRate_1_idx` (`servicesRateDid`),
  CONSTRAINT `fk_inmdNBSubscriptionRate_1` FOREIGN KEY (`servicesRateDid`) REFERENCES `inmdoperationrate` (`servicesRateDid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdnbsubscriptionrate`
--

LOCK TABLES `inmdnbsubscriptionrate` WRITE;
/*!40000 ALTER TABLE `inmdnbsubscriptionrate` DISABLE KEYS */;
INSERT INTO `inmdnbsubscriptionrate` VALUES (1,1,1,1),(2,2,5,1);
/*!40000 ALTER TABLE `inmdnbsubscriptionrate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdoperationrate`
--

DROP TABLE IF EXISTS `inmdoperationrate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdoperationrate` (
  `servicesRateDid` int(11) NOT NULL,
  `rateDid` int(11) DEFAULT NULL,
  `servicesDid` int(11) DEFAULT NULL,
  PRIMARY KEY (`servicesRateDid`),
  KEY `fk_inmdOperationRate_2_idx` (`rateDid`),
  KEY `fk_inmdOperationRate_1_idx` (`servicesDid`),
  CONSTRAINT `fk_inmdOperationRate_1` FOREIGN KEY (`servicesDid`) REFERENCES `inmdservices` (`servicesDid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inmdOperationRate_2` FOREIGN KEY (`rateDid`) REFERENCES `inmdrate` (`rateDid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdoperationrate`
--

LOCK TABLES `inmdoperationrate` WRITE;
/*!40000 ALTER TABLE `inmdoperationrate` DISABLE KEYS */;
INSERT INTO `inmdoperationrate` VALUES (1,1,1),(2,2,1);
/*!40000 ALTER TABLE `inmdoperationrate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdoperatorrate`
--

DROP TABLE IF EXISTS `inmdoperatorrate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdoperatorrate` (
  `operatorRateDid` int(11) NOT NULL,
  `rateDid` int(11) NOT NULL,
  `operatorDid` int(11) NOT NULL,
  `servicesDid` int(11) NOT NULL,
  PRIMARY KEY (`operatorRateDid`,`rateDid`,`operatorDid`,`servicesDid`),
  KEY `fk_inmdOperatorRate_1_idx` (`rateDid`),
  KEY `fk_inmdOperatorRate_2_idx` (`servicesDid`),
  KEY `fk_inmdOperatorRate_3_idx` (`operatorDid`),
  CONSTRAINT `fk_inmdOperatorRate_1` FOREIGN KEY (`rateDid`) REFERENCES `inmdpercentagerate` (`rateDid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inmdOperatorRate_2` FOREIGN KEY (`servicesDid`) REFERENCES `inmdservices` (`servicesDid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inmdOperatorRate_3` FOREIGN KEY (`operatorDid`) REFERENCES `inmdoperator` (`operatorDid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdoperatorrate`
--

LOCK TABLES `inmdoperatorrate` WRITE;
/*!40000 ALTER TABLE `inmdoperatorrate` DISABLE KEYS */;
INSERT INTO `inmdoperatorrate` VALUES (1,3,1,1);
/*!40000 ALTER TABLE `inmdoperatorrate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdpercentagerate`
--

DROP TABLE IF EXISTS `inmdpercentagerate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdpercentagerate` (
  `percentageRateDid` int(11) NOT NULL,
  `rateDid` int(11) DEFAULT NULL,
  `updateDate` date DEFAULT NULL,
  `isCatagoryBase` tinyint(1) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  `defaultCommision` int(11) DEFAULT NULL,
  PRIMARY KEY (`percentageRateDid`),
  KEY `fk_immdPracentageRate_1_idx` (`defaultCommision`),
  KEY `fk_immdPracentage_1_idx` (`rateDid`),
  CONSTRAINT `fk_immdPracentageRate_1` FOREIGN KEY (`defaultCommision`) REFERENCES `inmdcommission` (`CommissionDid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_immdPracentage_1` FOREIGN KEY (`rateDid`) REFERENCES `inmdrate` (`rateDid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdpercentagerate`
--

LOCK TABLES `inmdpercentagerate` WRITE;
/*!40000 ALTER TABLE `inmdpercentagerate` DISABLE KEYS */;
INSERT INTO `inmdpercentagerate` VALUES (1,1,'2017-03-29',1,'2017-03-29',1),(2,2,'2017-03-30',0,'2017-03-30',2),(3,3,'2017-03-30',1,'2017-03-30',1);
/*!40000 ALTER TABLE `inmdpercentagerate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdrate`
--

DROP TABLE IF EXISTS `inmdrate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdrate` (
  `rateDid` int(11) NOT NULL,
  `currencyDid` varchar(45) DEFAULT NULL,
  `isDefault` tinyint(1) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`rateDid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdrate`
--

LOCK TABLES `inmdrate` WRITE;
/*!40000 ALTER TABLE `inmdrate` DISABLE KEYS */;
INSERT INTO `inmdrate` VALUES (1,'1',1,'P1',20),(2,'1',1,'p2',20),(3,'1',1,'P1SB',20);
/*!40000 ALTER TABLE `inmdrate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdratecatagory`
--

DROP TABLE IF EXISTS `inmdratecatagory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdratecatagory` (
  `pracentageRateDid` int(11) NOT NULL,
  `catagoryDid` int(11) NOT NULL,
  `CommissionDid` int(11) NOT NULL,
  PRIMARY KEY (`pracentageRateDid`,`catagoryDid`,`CommissionDid`),
  KEY `fk_inmdRateCatagory_1_idx` (`catagoryDid`),
  KEY `fk_inmdRateCatagory_3_idx` (`CommissionDid`),
  CONSTRAINT `fk_inmdRateCatagory_1` FOREIGN KEY (`catagoryDid`) REFERENCES `inmdcatagory` (`catagoryDid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inmdRateCatagory_2` FOREIGN KEY (`pracentageRateDid`) REFERENCES `inmdpercentagerate` (`percentageRateDid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_inmdRateCatagory_3` FOREIGN KEY (`CommissionDid`) REFERENCES `inmdcommission` (`CommissionDid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdratecatagory`
--

LOCK TABLES `inmdratecatagory` WRITE;
/*!40000 ALTER TABLE `inmdratecatagory` DISABLE KEYS */;
INSERT INTO `inmdratecatagory` VALUES (1,1,1),(3,2,3);
/*!40000 ALTER TABLE `inmdratecatagory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdsbsubscriptions`
--

DROP TABLE IF EXISTS `inmdsbsubscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdsbsubscriptions` (
  `SBSubscriptionsRateDid` int(11) NOT NULL AUTO_INCREMENT,
  `operationRateDid` int(11) NOT NULL,
  `applicationDid` int(11) NOT NULL,
  PRIMARY KEY (`SBSubscriptionsRateDid`,`operationRateDid`,`applicationDid`),
  KEY `fk_inmdSBSubscriptions_1_idx` (`operationRateDid`),
  CONSTRAINT `fk_inmdSBSubscriptions_1` FOREIGN KEY (`operationRateDid`) REFERENCES `inmdoperatorrate` (`operatorRateDid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdsbsubscriptions`
--

LOCK TABLES `inmdsbsubscriptions` WRITE;
/*!40000 ALTER TABLE `inmdsbsubscriptions` DISABLE KEYS */;
INSERT INTO `inmdsbsubscriptions` VALUES (1,1,5);
/*!40000 ALTER TABLE `inmdsbsubscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inmdservices`
--

DROP TABLE IF EXISTS `inmdservices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inmdservices` (
  `servicesDid` int(11) NOT NULL,
  `apiDid` int(11) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `createDate` date DEFAULT NULL,
  PRIMARY KEY (`servicesDid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inmdservices`
--

LOCK TABLES `inmdservices` WRITE;
/*!40000 ALTER TABLE `inmdservices` DISABLE KEYS */;
INSERT INTO `inmdservices` VALUES (1,1,'Charge','2017-03-29');
/*!40000 ALTER TABLE `inmdservices` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-19 16:43:23
