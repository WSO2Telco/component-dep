

--
-- Table structure for table `api`
--

DROP TABLE IF EXISTS "api";
CREATE TABLE "api"(
  "apiid" int NOT NULL AUTO_INCREMENT,
  "apiname" varchar(45) NOT NULL,
  "apidesc" varchar(45) NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("apiid")
);

--
-- Data for table `api`
--

INSERT INTO "api" ("apiid", "apiname", "apidesc", "createdby")
       VALUES (1, 'payment', 'payment', 'admin'), (2, 'smsmessaging', 'smsmessaging', 'admin'), 
              (3, 'location', 'location', 'admin'), (4, 'ussd', 'ussd', 'admin'), 
              (5, 'credit', 'credit', 'admin'), (6, 'customerinfo', 'customerinfo', 'admin'), 
              (7, 'provisioning', 'provisioning', 'admin'), (8, 'wallet', 'wallet', 'admin');

--
-- Table structure for table `api_operation`
--

DROP TABLE IF EXISTS "api_operation";
CREATE TABLE "api_operation"(
  "api_operationid" int NOT NULL AUTO_INCREMENT,
  "apiid" int NOT NULL,
  "api_operation" varchar(45) NOT NULL,
  "api_operationcode" varchar(45) NOT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("api_operationid"),
  CONSTRAINT "apiid_unique"
    UNIQUE ("api_operation"),
  CONSTRAINT "fk_api_operation_1"
    FOREIGN KEY ("apiid")
    REFERENCES "api" ("apiid") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX "fk_api_operation_1_idx" ON "api_operation" ("apiid");

--
-- Data for table `api_operation`
--

INSERT INTO "api_operation" ("api_operationid", "apiid", "api_operation", "api_operationcode", "createdby")
       VALUES (1, 1, 'Charge', 'Charge', 'admin'), (2, 1, 'Refund', 'Refund', 'admin'), (3, 1, 'ReleaseReservation', 'ReleaseReservation', 'admin'), 
              (4, 1, 'ReserveAdditionalAmount', 'ReserveAdditionalAmount', 'admin'), (5, 1, 'ReserveAmount', 'ReserveAmount', 'admin'), 
              (6, 1, 'ChargeAgainstReservation', 'ChargeAgainstReservation', 'admin'), (7, 1, 'ListChargeOperations', 'ListChargeOperations', 'admin'), 
              (8, 2, 'SendSMS', 'SendSMS', 'admin'), (9, 2, 'ReceiveSMS', 'ReceiveSMS', 'admin'), (10, 2, 'DeliveryInfo', 'DeliveryInfo', 'admin'), 
              (11, 2, 'SubscribeToDeliveryNotifications', 'SubscribeToDeliveryNotifications', 'admin'), (12, 2, 'StopSubscriptionToDeliveryNotifications', 'StopSubscriptionToDeliveryNotifications', 'admin'), 
              (13, 2, 'SubscribetoMessageNotifcations', 'SubscribetoMessageNotifcations', 'admin'), (14, 2, 'StopSubscriptionToMessageNotifcations', 'StopSubscriptionToMessageNotifcations', 'admin'), 
              (15, 3, 'Location', 'Location', 'admin'), (16, 4, 'USSDInboundCont', 'USSDInboundCont', 'admin'), (17, 4, 'USSDInboundInit', 'USSDInboundInit', 'admin'), 
              (18, 4, 'USSDInboundFin', 'USSDInboundFin', 'admin'), (19, 4, 'USSDOutboundCont', 'USSDOutboundCont', 'admin'), (20, 4, 'USSDOutboundFin', 'USSDOutboundFin', 'admin'), 
              (21, 4, 'USSDOutboundInit', 'USSDOutboundInit', 'admin'), (22, 4, 'USSDSubscription', 'USSDSubscription', 'admin'), (23, 5, 'Credit', 'Credit', 'admin'), 
              (24, 5, 'CreditRefund', 'CreditRefund', 'admin'), (25, 6, 'GetProfile', 'GetProfile', 'admin'), (26, 6, 'GetAttributes', 'GetAttributes', 'admin'), 
              (27, 7, 'QueryApplicableServices', 'QueryApplicableServices', 'admin'), (28, 7, 'ProvisionService', 'ProvisionService', 'admin'), (29, 7, 'RemoveProvisionService', 'RemoveProvisionService', 'admin'), 
              (30, 7, 'ListServiceByCustomer', 'ListServiceByCustomer', 'admin'), (31, 8, 'Payment', 'Payment', 'admin'), (32, 8, 'ListTransactions', 'ListTransactions', 'admin'), 
              (33, 8, 'RefundUser', 'RefundUser', 'admin'), (34, 8, 'BalanceLookup', 'BalanceLookup', 'admin'), (35, 1, 'DuplicateCharge', 'DuplicateCharge', 'admin');

--
-- Table structure for table `category`
--

--
-- Table structure for table `operator`
--

DROP TABLE IF EXISTS "operator";
CREATE TABLE "operator"(
  "operatorid" int NOT NULL AUTO_INCREMENT,
  "operatorname" varchar(45) NOT NULL,
  "operatordesc" varchar(45) NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("operatorid")
);

DROP TABLE IF EXISTS "category";
CREATE TABLE "category"(
  "categoryid" int NOT NULL AUTO_INCREMENT,
  "categoryname" varchar(45) NOT NULL,
  "categorycode" varchar(45) NOT NULL,
  "categorydesc" varchar(45) NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("categoryid")
);

--
-- Table structure for table `currency`
--

DROP TABLE IF EXISTS "currency";
CREATE TABLE "currency"(
  "currencyid" int NOT NULL AUTO_INCREMENT,
  "currencycode" varchar(45) NOT NULL,
  "currencydesc" varchar(45) NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("currencyid")
);

--
-- Table structure for table `rate_type`
--

DROP TABLE IF EXISTS "rate_type";
CREATE TABLE "rate_type"(
  "rate_typeid" int NOT NULL AUTO_INCREMENT,
  "rate_typecode" varchar(45) NOT NULL,
  "rate_typedesc" varchar(45) NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("rate_typeid")
);

--
-- Data for table `rate_type`
--

INSERT INTO "rate_type"
VALUES (1, 'CONSTANT', 'Constant Charge Per Month', NULL, '2017-09-22 14:27:29', NULL, '2017-09-22 14:27:29'), (2, 'QUOTA', 'Quota Based Charging per Month', NULL, '2017-09-22 14:27:29', NULL, '2017-09-22 14:27:29'), 
       (3, 'PERCENTAGE', 'Revenue Share Charging', NULL, '2017-09-22 14:27:30', NULL, '2017-09-22 14:27:30'), (4, 'PER_REQUEST', 'Request Based Charging', NULL, '2017-09-22 14:27:30', NULL, '2017-09-22 14:27:30');

--
-- Table structure for table `tariff`
--

DROP TABLE IF EXISTS "tariff";
CREATE TABLE "tariff"(
  "tariffid" int NOT NULL AUTO_INCREMENT,
  "tariffname" varchar(45) NOT NULL,
  "tariffdesc" varchar(45) NULL DEFAULT NULL,
  "tariffdefaultval" double NULL DEFAULT NULL,
  "tariffmaxcount" int NULL DEFAULT NULL,
  "tariffexcessrate" double NULL DEFAULT NULL,
  "tariffdefrate" double NULL DEFAULT NULL,
  "tariffspcommission" double NULL DEFAULT NULL,
  "tariffadscommission" double NULL DEFAULT NULL,
  "tariffopcocommission" double NULL DEFAULT NULL,
  "tariffsurchargeval" double NULL DEFAULT NULL,
  "tariffsurchargeads" double NULL DEFAULT NULL,
  "tariffsurchargeopco" double NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("tariffid")
);

--
-- Table structure for table `rate_def`
--

DROP TABLE IF EXISTS "rate_def";
CREATE TABLE "rate_def"(
  "rate_defid" int NOT NULL AUTO_INCREMENT,
  "rate_defname" varchar(45) NOT NULL,
  "rate_defdesc" varchar(45) NULL DEFAULT NULL,
  "rate_defdefault" tinyint NOT NULL,
  "currencyid" int NOT NULL,
  "rate_typeid" int NOT NULL,
  "rate_defcategorybase" tinyint NOT NULL,
  "tariffid" int NOT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("rate_defid"),
  CONSTRAINT "fk_rate_def_1"
    FOREIGN KEY ("rate_typeid")
    REFERENCES "rate_type" ("rate_typeid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_rate_def_2"
    FOREIGN KEY ("currencyid")
    REFERENCES "currency" ("currencyid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_rate_def_3"
    FOREIGN KEY ("tariffid")
    REFERENCES "tariff" ("tariffid") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX "fk_rate_def_1_idx" ON "rate_def" ("rate_typeid");
CREATE INDEX "fk_rate_def_2_idx" ON "rate_def" ("currencyid");
CREATE INDEX "fk_rate_def_3_idx" ON "rate_def" ("tariffid");

--
-- Table structure for table `operation_rate`
--

DROP TABLE IF EXISTS "operation_rate";
CREATE TABLE "operation_rate"(
  "operation_rateid" int NOT NULL AUTO_INCREMENT,
  "operator_id" int NULL DEFAULT NULL,
  "api_operationid" int NOT NULL,
  "rate_defid" int NOT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("operation_rateid"),
  CONSTRAINT "operator_id_unique"
    UNIQUE (
      "operator_id", 
      "api_operationid", 
      "rate_defid"
    ),
  CONSTRAINT "fk_operation_rate_1"
    FOREIGN KEY ("operator_id")
    REFERENCES "operator" ("operatorid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_operation_rate_2"
    FOREIGN KEY ("api_operationid")
    REFERENCES "api_operation" ("api_operationid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_operation_rate_3"
    FOREIGN KEY ("rate_defid")
    REFERENCES "rate_def" ("rate_defid") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX "fk_operation_rate_1_idx" ON "operation_rate" ("api_operationid");
CREATE INDEX "fk_operation_rate_2_idx" ON "operation_rate" ("rate_defid");
CREATE INDEX "fk_operation_rate_3_idx" ON "operation_rate" ("operator_id");

--
-- Table structure for table `rate_category`
--

DROP TABLE IF EXISTS "rate_category";
CREATE TABLE "rate_category"(
  "rate_category_id" int NOT NULL AUTO_INCREMENT,
  "rate_defid" int NOT NULL,
  "parentcategoryid" int NOT NULL,
  "childcategoryid" int NULL DEFAULT NULL,
  "tariffid" int NOT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("rate_category_id"),
  CONSTRAINT "fk_rate_category_1"
    FOREIGN KEY ("rate_defid")
    REFERENCES "rate_def" ("rate_defid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_rate_category_2"
    FOREIGN KEY ("tariffid")
    REFERENCES "tariff" ("tariffid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_rate_category_3"
    FOREIGN KEY ("parentcategoryid")
    REFERENCES "category" ("categoryid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_rate_category_4"
    FOREIGN KEY ("childcategoryid")
    REFERENCES "category" ("categoryid") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX "fk_rate_category_1_idx" ON "rate_category" ("rate_defid");
CREATE INDEX "fk_rate_category_2_idx" ON "rate_category" ("tariffid");
CREATE INDEX "fk_rate_category_3_idx" ON "rate_category" ("parentcategoryid");
CREATE INDEX "fk_rate_category_4_idx" ON "rate_category" ("childcategoryid");

--
-- Table structure for table `tax`
--

DROP TABLE IF EXISTS "tax";
CREATE TABLE "tax"(
  "taxid" int NOT NULL AUTO_INCREMENT,
  "taxcode" varchar(45) NOT NULL,
  "taxname" varchar(45) NOT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("taxid")
);

--
-- Table structure for table `rate_taxes`
--

DROP TABLE IF EXISTS "rate_taxes";
CREATE TABLE "rate_taxes"(
  "rate_taxesid" int NOT NULL AUTO_INCREMENT,
  "rate_defid" int NOT NULL,
  "taxid" int NOT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("rate_taxesid"),
  CONSTRAINT "fk_rate_taxes_1"
    FOREIGN KEY ("rate_defid")
    REFERENCES "rate_def" ("rate_defid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_rate_taxes_2"
    FOREIGN KEY ("taxid")
    REFERENCES "tax" ("taxid") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX "fk_rate_taxes_1_idx" ON "rate_taxes" ("rate_defid");
CREATE INDEX "fk_rate_taxes_2_idx" ON "rate_taxes" ("taxid");

--
-- Table structure for table `sub_rate_nb`
--

DROP TABLE IF EXISTS "sub_rate_nb";
CREATE TABLE "sub_rate_nb"(
  "sub_rate_nbid" int NOT NULL AUTO_INCREMENT,
  "api_operationid" int NOT NULL,
  "api_version" varchar(50) NOT NULL,
  "applicationid" int NOT NULL,
  "rate_defid" int NOT NULL,
  "sub_rate_nbactdate" date NULL DEFAULT NULL,
  "sub_rate_nbdisdate" date NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("sub_rate_nbid"),
  CONSTRAINT "api_operationid_unique"
    UNIQUE (
      "api_operationid", 
      "applicationid"
    ),
  CONSTRAINT "fk_sub_rate_nb_1"
    FOREIGN KEY ("api_operationid")
    REFERENCES "api_operation" ("api_operationid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_sub_rate_nb_2"
    FOREIGN KEY ("rate_defid")
    REFERENCES "rate_def" ("rate_defid") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX "fk_sub_rate_nb_1_idx" ON "sub_rate_nb" ("api_operationid");
CREATE INDEX "fk_sub_rate_nb_2_idx" ON "sub_rate_nb" ("rate_defid");

--
-- Table structure for table `sub_rate_sb`
--

DROP TABLE IF EXISTS "sub_rate_sb";
CREATE TABLE "sub_rate_sb"(
  "sub_rate_sbid" int NOT NULL AUTO_INCREMENT,
  "operatorid" int NOT NULL,
  "api_operationid" int NOT NULL,
  "api_version" varchar(50) NOT NULL,
  "applicationid" int NOT NULL,
  "rate_defid" int NOT NULL,
  "sub_rate_sbactdate" date NULL DEFAULT NULL,
  "sub_rate_sbdisdate" date NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("sub_rate_sbid"),
  CONSTRAINT "operatorid_unique"
    UNIQUE (
      "operatorid", 
      "api_operationid", 
      "applicationid"
    ),
  CONSTRAINT "fk_sub_rate_sb_1"
    FOREIGN KEY ("operatorid")
    REFERENCES "operator" ("operatorid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_sub_rate_sb_2"
    FOREIGN KEY ("api_operationid")
    REFERENCES "api_operation" ("api_operationid") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "fk_sub_rate_sb_3"
    FOREIGN KEY ("rate_defid")
    REFERENCES "rate_def" ("rate_defid") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX "fk_sub_rate_sb_1_idx" ON "sub_rate_sb" ("operatorid");
CREATE INDEX "fk_sub_rate_sb_2_idx" ON "sub_rate_sb" ("api_operationid");
CREATE INDEX "fk_sub_rate_sb_3_idx" ON "sub_rate_sb" ("rate_defid");

--
--
--

DROP TABLE IF EXISTS "subs_rate_updated";
CREATE TABLE "subs_rate_updated"(
  "id" int NOT NULL AUTO_INCREMENT,
  "sbnbid" int NOT NULL,
  "direction" varchar(11) NOT NULL,
  "oldrate_defid" int NULL DEFAULT NULL,
  "newrate_defid" int NULL DEFAULT NULL,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  "comment" varchar(255) NULL,
  PRIMARY KEY ("id")
);

--
--
--

DROP TABLE IF EXISTS "tax_validity";
CREATE TABLE "tax_validity"(
  "idtax_validityid" int NOT NULL,
  "tax_validityactdate" date NOT NULL,
  "tax_validitydisdate" date NOT NULL,
  "tax_validityval" double NOT NULL,
  "taxid" int NOT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("idtax_validityid"),
  CONSTRAINT "fk_tax_validity_1"
    FOREIGN KEY ("taxid")
    REFERENCES "tax" ("taxid") ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE INDEX "fk_tax_validity_1_idx" ON "tax_validity" ("taxid");

--
--
--

DROP TABLE IF EXISTS "audit";
CREATE TABLE "audit"(
  "tbl_name" varchar(255) NULL DEFAULT NULL,
  "id" varchar(255) NULL DEFAULT NULL,
  "col_name" varchar(255) NULL DEFAULT NULL,
  "old_data" varchar(255) NULL DEFAULT NULL,
  "new_data" varchar(255) NULL DEFAULT NULL,
  "action" varchar(255) NULL DEFAULT NULL,
  "updated_at" timestamp NULL DEFAULT NULL
);

--
--
--


