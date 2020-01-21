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

/*
* ==========================================
* H2 script related to tables in
* DEP database
* Database: Digital Enable Platform database
* ==========================================
*/

--
-- Table structure for table `ussd_request_entry`
--

CREATE TABLE IF NOT EXISTS "ussd_request_entry"(
  "ussd_request_did" int NOT NULL AUTO_INCREMENT,
  "notifyurl" varchar(255) NULL DEFAULT NULL,
  "sp_consumerKey" varchar(100) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  "operatorId" varchar(45) NULL DEFAULT NULL,
  "userId" varchar(45) NULL DEFAULT NULL,
  PRIMARY KEY ("ussd_request_did")
);

--
-- Table structure for table `endpointapps`
--

CREATE TABLE IF NOT EXISTS "endpointapps"(
  "ID" int NOT NULL AUTO_INCREMENT,
  "endpointid" int NULL DEFAULT NULL,
  "applicationid" int NULL DEFAULT NULL,
  "isactive" int NULL DEFAULT '0',
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("ID"),
  CONSTRAINT "uk_apps_endpoint"
    UNIQUE (
      "endpointid", 
      "applicationid"
    )
);

--
-- Table structure for table `operatorapps`
--

CREATE TABLE IF NOT EXISTS "operatorapps"(
  "ID" int NOT NULL AUTO_INCREMENT,
  "applicationid" int NULL DEFAULT NULL,
  "operatorid" int NULL DEFAULT NULL,
  "isactive" int NULL DEFAULT '0',
  "note" varchar(255) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("ID")
);

--
-- Table structure for table `operatorendpoints`
--

CREATE TABLE IF NOT EXISTS "operatorendpoints"(
  "ID" int NOT NULL AUTO_INCREMENT,
  "operatorid" int NULL DEFAULT NULL,
  "api" varchar(25) NULL DEFAULT NULL,
  "isactive" int NULL DEFAULT '0',
  "endpoint" varchar(255) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("ID")
);

--
-- Table structure for table `operators`
--

CREATE TABLE IF NOT EXISTS "operators"(
  "ID" int NOT NULL AUTO_INCREMENT,
  "operatorname" varchar(45) NULL DEFAULT NULL,
  "description" varchar(255) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  "refreshtoken" varchar(255) NULL DEFAULT NULL,
  "tokenvalidity" double NULL DEFAULT NULL,
  "tokentime" double NULL DEFAULT NULL,
  "token" varchar(255) NULL DEFAULT NULL,
  "tokenurl" varchar(255) NULL DEFAULT NULL,
  "tokenauth" varchar(255) NULL DEFAULT NULL,
  "mcc" varchar(25) NULL,
  "mnc" varchar(25) NULL,
  UNIQUE ("mcc"),
  PRIMARY KEY ("ID"),
  CONSTRAINT "operatorname"
    UNIQUE ("operatorname")
);

--
-- Table structure for table `operatorsubs`
--

CREATE TABLE IF NOT EXISTS "operatorsubs"(
  "id" int NOT NULL AUTO_INCREMENT,
  "mo_subscription_did" int NULL DEFAULT NULL,
  "domainurl" varchar(255) NULL DEFAULT NULL,
  "operator" varchar(45) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);

--
-- Table structure for table `subscriptions`
--

CREATE TABLE IF NOT EXISTS "subscriptions"(
  "mo_subscription_did" int NOT NULL AUTO_INCREMENT,
  "notifyurl" varchar(255) NULL DEFAULT NULL,
  "service_provider" varchar(255) NULL DEFAULT NULL,
  "is_active" int NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("mo_subscription_did")
);

--
-- Table for subscription approval operator list.
--

CREATE TABLE IF NOT EXISTS "sub_approval_operators"(
  "API_NAME" varchar(200) NOT NULL,
  "API_VERSION" varchar(30) NOT NULL,
  "API_PROVIDER" varchar(200) NOT NULL,
  "APP_ID" int NOT NULL,
  "OPERATOR_LIST" varchar(512) NULL DEFAULT NULL,
  PRIMARY KEY (
    "API_NAME", 
    "API_VERSION", 
    "API_PROVIDER", 
    "APP_ID"
  )
);

--
-- Table for validator definitions used in Mediator
--

CREATE TABLE IF NOT EXISTS "validator"(
  "id" int NOT NULL AUTO_INCREMENT,
  "name" varchar(100) NOT NULL,
  "class" varchar(255) NOT NULL,
  PRIMARY KEY ("id")
);

--
-- Table for API subscriptions to validator mapping
--

CREATE TABLE IF NOT EXISTS "subscription_validator"(
  "application_id" int NOT NULL,
  "api_id" int NOT NULL,
  "validator_id" int NOT NULL,
  PRIMARY KEY (
    "application_id", 
    "api_id"
  ),
  FOREIGN KEY ("validator_id")
  REFERENCES "validator" ("id") ON UPDATE CASCADE
);

--
--
--

CREATE TABLE IF NOT EXISTS "merchantopco_blacklist"(
  "id" int NOT NULL AUTO_INCREMENT,
  "application_id" int NULL DEFAULT NULL,
  "operator_id" int NULL DEFAULT NULL,
  "subscriber" varchar(40) NULL DEFAULT NULL,
  "merchant" varchar(255) NULL DEFAULT NULL,
  "isactive" int NULL DEFAULT '1',
  "note" varchar(255) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "constr_ID"
    UNIQUE (
      "application_id", 
      "operator_id", 
      "subscriber", 
      "merchant"
    )
);

--
--
--

CREATE TABLE IF NOT EXISTS "mcc_number_ranges"(
  "id" int NOT NULL AUTO_INCREMENT,
  "mcccode" varchar(10) NULL DEFAULT NULL,
  "rangefrom" decimal(20, 0) NULL DEFAULT NULL,
  "rangeto" decimal(20, 0) NULL DEFAULT NULL,
  "mnccode" varchar(20) NULL DEFAULT NULL,
  "brand" varchar(20) NULL DEFAULT NULL,
  "isactive" int NULL DEFAULT '1',
  "note" varchar(255) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);

--
--
--

CREATE TABLE IF NOT EXISTS "valid_payment_categories"(
  "id" int NOT NULL AUTO_INCREMENT,
  "category" varchar(100) NOT NULL,
  "Note" varchar(255) NOT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);

--
-- Table structure for table `workflow_api_key_mappings`
--

CREATE TABLE IF NOT EXISTS "workflow_api_key_mappings"(
  "ID" int NOT NULL AUTO_INCREMENT,
  "API_NAME" varchar(200) NOT NULL,
  "API_KEY" varchar(200) NOT NULL,
  PRIMARY KEY ("ID")
);

--
-- Table structure for table `sendsms_reqid` used for delivery status query
--

CREATE TABLE IF NOT EXISTS "sendsms_reqid"(
  "ID" int NOT NULL AUTO_INCREMENT,
  "hub_requestid" varchar(255) NULL DEFAULT NULL,
  "sender_address" varchar(40) NULL DEFAULT NULL,
  "delivery_address" varchar(40) NULL DEFAULT NULL,
  "plugin_requestid" varchar(255) NULL DEFAULT NULL,
  "created_date" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("ID")
);

--
--
--

CREATE TABLE IF NOT EXISTS "outbound_subscriptions"(
  "dn_subscription_did" int NOT NULL AUTO_INCREMENT,
  "notifyurl" varchar(255) NULL DEFAULT NULL,
  "service_provider" varchar(255) NULL DEFAULT NULL,
  "is_active" int NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("dn_subscription_did")
);

--
--
--

CREATE TABLE IF NOT EXISTS "outbound_operatorsubs"(
  "id" int NOT NULL AUTO_INCREMENT,
  "dn_subscription_did" int NULL DEFAULT NULL,
  "domainurl" varchar(255) NULL DEFAULT NULL,
  "operator" varchar(45) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);

--
--
--

CREATE TABLE IF NOT EXISTS "provision_services"(
  "provision_service_did" int NOT NULL AUTO_INCREMENT,
  "notifyurl" varchar(255) NULL DEFAULT NULL,
  "service_provider" varchar(255) NULL DEFAULT NULL,
  "is_active" int NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("provision_service_did")
);

--
--
--

CREATE TABLE IF NOT EXISTS "provision_service_operator_endpoints"(
  "id" int NOT NULL AUTO_INCREMENT,
  "provision_service_did" int NULL DEFAULT NULL,
  "domainurl" varchar(255) NULL DEFAULT NULL,
  "operator" varchar(45) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id")
);

--
--
--

CREATE TABLE IF NOT EXISTS "mo_ussd_subscription"(
  "mo_ussd_request_did" int NOT NULL AUTO_INCREMENT,
  "ussd_request_did" int NULL DEFAULT NULL,
  "domainurl" varchar(255) NULL DEFAULT NULL,
  "operator" varchar(45) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("mo_ussd_request_did")
);

--
--
--

CREATE TABLE IF NOT EXISTS "mdtxmsgtype"(
  "msgtypedid" int NOT NULL AUTO_INCREMENT,
  "msgtype" varchar(45) NOT NULL,
  PRIMARY KEY ("msgtypedid"),
  CONSTRAINT "msgtype_UNIQUE"
    UNIQUE ("msgtype")
);

--
--
--

CREATE TABLE IF NOT EXISTS "mdtrequestmessage"(
  "messegeId" int NOT NULL AUTO_INCREMENT,
  "msgtypeId" int NOT NULL,
  "mdtrequestId" varchar(45) NULL DEFAULT NULL,
  "internalclientrefcode" varchar(100) NULL DEFAULT NULL,
  "message" blob NULL,
  "clientrefcode" varchar(45) NULL DEFAULT NULL,
  "clientrefval" varchar(45) NULL DEFAULT NULL,
  "reportedtime" bigint NULL DEFAULT NULL,
  PRIMARY KEY ("messegeId"),
  CONSTRAINT "fk_mdtrequestmessage_1"
    FOREIGN KEY ("msgtypeId")
    REFERENCES "mdtxmsgtype" ("msgtypedid") ON DELETE CASCADE ON UPDATE CASCADE,
  INDEX "fk_mdtrequestmessage_1_idx" ("msgtypeId"),
  INDEX "index_msgtypeId" ("msgtypeId"),
  INDEX "index_clientrefcode" ("clientrefcode")
);

--
--
--

CREATE TABLE IF NOT EXISTS "spendlimitdata"(
  "msgType" int NULL DEFAULT NULL,
  "groupName" varchar(255) NULL DEFAULT NULL,
  "consumerKey" varchar(255) NULL DEFAULT NULL,
  "operatorId" varchar(255) NULL DEFAULT NULL,
  "msisdn" varchar(255) NULL DEFAULT NULL,
  "amount" decimal(40, 15) NOT NULL,
  "currentDateTime" bigint NULL DEFAULT NULL,
  "effectiveTime" bigint NULL DEFAULT NULL,
  INDEX "index_msisdn" ("msisdn"),
  INDEX "index_msgType" ("msgType")
);

--
--
--

CREATE TABLE IF NOT EXISTS "workflow_reference"(
  "workflow_ref_id" varchar(255) NOT NULL,
  "application_id" varchar(45) NULL DEFAULT NULL,
  "api_name" varchar(255) NULL DEFAULT NULL,
  "api_version" varchar(45) NULL DEFAULT NULL,
  "status" varchar(45) NULL DEFAULT NULL,
  "service_endpoint" varchar(45) NULL DEFAULT NULL,
  PRIMARY KEY ("workflow_ref_id")
);

--
--
--

CREATE TABLE IF NOT EXISTS "deptnotificationurls"(
  "notifyurldid" int NOT NULL AUTO_INCREMENT,
  "apiname" varchar(255) NOT NULL,
  "notifyurl" varchar(255) NOT NULL,
  "serviceprovider" varchar(255) NOT NULL,
  "clientCorrelator" varchar(500) NULL,
  "operatorName" varchar(255) NULL,
  "notifystatus" int NOT NULL DEFAULT 0,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "consumerkey" varchar(100) NULL,
  PRIMARY KEY ("notifyurldid")
);

--
--
--

CREATE TABLE IF NOT EXISTS "tokeninfo"(
  "username" varchar(255) NOT NULL DEFAULT '',
  "password" varchar(255) NOT NULL DEFAULT '',
  "authtoken" varchar(255) NOT NULL DEFAULT '',
  "timestamp" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (
    "username", 
    "password", 
    "authtoken"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "userinfo"(
  "enduserid" varchar(16) NOT NULL DEFAULT '',
  "package" varchar(20) NOT NULL DEFAULT '',
  "timestamp" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("enduserid")
);

--
--
--

CREATE TABLE IF NOT EXISTS "mdxattribgroup"(
  "groupdid" int NOT NULL AUTO_INCREMENT,
  "code" varchar(100) NOT NULL,
  "description" varchar(400) NULL DEFAULT NULL,
  "createdtime" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT "pkmdxattribgroup"
    PRIMARY KEY ("groupdid")
);

--
--
--

CREATE TABLE IF NOT EXISTS "mdxattribute"(
  "attributedid" int NOT NULL AUTO_INCREMENT,
  "groupdid" int NOT NULL,
  "code" varchar(100) NOT NULL,
  "description" varchar(100) NOT NULL,
  "createdtime" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT "pkmdxattribute"
    PRIMARY KEY ("attributedid"),
  CONSTRAINT "fk01mdxattribute"
    FOREIGN KEY ("groupdid")
    REFERENCES "mdxattribgroup" ("groupdid") ON DELETE NO ACTION ON UPDATE NO ACTION
);

--
--
--

CREATE TABLE IF NOT EXISTS "mdtattributemap"(
  "attributemapdid" int NOT NULL AUTO_INCREMENT,
  "attributedid" int NOT NULL,
  "tobject" varchar(100) NOT NULL,
  "ownerdid" int NOT NULL,
  "value" varchar(100) NOT NULL,
  "createdtime" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT "pkmdtattributemap"
    PRIMARY KEY ("attributemapdid"),
  CONSTRAINT "fk01mdtattributemap"
    FOREIGN KEY ("attributedid")
    REFERENCES "mdxattribute" ("attributedid") ON DELETE NO ACTION ON UPDATE NO ACTION
);

--
-- Metadata inserts 
--

--
-- Default validator types provided from mife-validator component
--

INSERT INTO validator (name, class) VALUES ('passthru', 'com.wso2telco.dep.subscriptionvalidator.services.impl.PassThroughValidator');
INSERT INTO validator (name, class) VALUES ('msisdn', 'com.wso2telco.dep.subscriptionvalidator.services.impl.MSISDNValidator');

--
-- Default operator
--

INSERT INTO "operators" ("ID", "operatorname", "description", "created", "created_date", "lastupdated", "lastupdated_date", "refreshtoken", "tokenvalidity", "tokentime", "token", "tokenurl", "tokenauth")
VALUES (1, 'OPERATOR1', 'Opearator 1', 'depuser', NULL, 'depuser', NULL, 'gGgvUANAGhRUzWTyXwYoGuk3WzQa', 157680000, 1395135145139, '4fb164d70def9f37b2f8e2f1daf467', 'http://localhost:8281/token', 'Basic U1JObDQzXzRTVks5MjZaVnNteXExOU1JNVFRYTpEV1Flb2NDeUVyN0lHYk8zRHJxRDc5SmtzVFVh');

--
-- Default operator endpoints point to the sandbox
--

INSERT INTO "operatorendpoints" ("ID", "operatorid", "api", "isactive", "endpoint", "created", "created_date", "lastupdated", "lastupdated_date")
VALUES (1, 1, 'smsmessaging', 1, 'http://localhost:8081/mifesandbox/SandboxController/smsmessaging/1', NULL, '2014-03-04 11:36:23', NULL, NULL), 
       (2, 1, 'payment', 1, 'http://localhost:8081/mifesandbox/SandboxController/payment/1', NULL, '2014-03-04 11:36:58', NULL, NULL), 
       (3, 1, 'location', 1, 'http://localhost:8081/mifesandbox/SandboxController/location/1', NULL, '2014-10-02 10:33:41', NULL, NULL), 
       (4, 1, 'ussd', 1, 'http://localhost:8081/mifesandbox/SandboxController/ussd/1', NULL, '2014-10-02 10:33:41', NULL, NULL), 
       (5, 1, 'subscription', 1, 'http://localhost:8081/mifesandbox/SandboxController/subscription/1', NULL, '2014-10-02 10:33:45', NULL, NULL);

--
-- Initial Metadata script for table `workflow_api_key_mappings`
-- The data script needs to be updated to include any new api-key mappings.
--

INSERT INTO "workflow_api_key_mappings" ("API_NAME", "API_KEY") VALUES ('smsmessaging', 'smsmessaging');
INSERT INTO "workflow_api_key_mappings" ("API_NAME", "API_KEY") VALUES ('payment', 'payment');
INSERT INTO "workflow_api_key_mappings" ("API_NAME", "API_KEY") VALUES ('location', 'location');

--
-- Default message types
--

INSERT INTO "mdtxmsgtype" ("msgtypedid", "msgtype") VALUES ('1', 'paymentrequest'), ('2', 'paymentresponse'), ('3', 'refundrequest'), ('4', 'refundresponse');

--
-- Quota Limit table.
--

CREATE TABLE IF NOT EXISTS "sp_quota_limit"(
  "idsp_quota_limit" int NOT NULL AUTO_INCREMENT,
  "serviceProvider" varchar(45) NULL DEFAULT NULL,
  "operatorName" varchar(45) NULL DEFAULT NULL,
  "application" int NULL DEFAULT NULL,
  "apiName" varchar(45) NULL DEFAULT NULL,
  "quota_limit" int NULL DEFAULT NULL,
  "fromDate" date NULL DEFAULT NULL,
  "toDate" date NULL DEFAULT NULL,
  "createdby" varchar(255) NULL DEFAULT NULL,
  "createddate" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updatedby" varchar(255) NULL DEFAULT NULL,
  "updateddate" timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("idsp_quota_limit")
);

--
-- Operator Brand for OperatorCode.
--

CREATE TABLE "operator_brands"(
  "id" int NOT NULL AUTO_INCREMENT,
  "operatorcode" varchar(45) NULL DEFAULT NULL,
  "brand" varchar(255) NULL DEFAULT NULL,
  "created" varchar(25) NULL DEFAULT NULL,
  "created_date" timestamp NULL DEFAULT NULL,
  "lastupdated" varchar(25) NULL DEFAULT NULL,
  "lastupdated_date" timestamp NULL DEFAULT NULL,
  PRIMARY KEY ("id"),
  CONSTRAINT "operatorcode"
    UNIQUE ("operatorcode")
);