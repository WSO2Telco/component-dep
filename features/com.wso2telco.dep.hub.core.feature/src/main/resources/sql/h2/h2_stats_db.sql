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

 --
 --
 --

 CREATE TABLE IF NOT EXISTS "api_destination_summary"(
  "api" varchar(100) NOT NULL DEFAULT '',
  "version" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "context" varchar(100) NOT NULL DEFAULT '',
  "destination" varchar(100) NOT NULL DEFAULT '',
  "total_request_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NOT NULL DEFAULT '',
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (
    "api", 
    "version", 
    "apipublisher", 
    "context", 
    "destination", 
    "hostname", 
    "time"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_fault_summary"(
  "api" varchar(100) NOT NULL DEFAULT '',
  "version" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "consumerkey" varchar(100) NULL DEFAULT NULL,
  "context" varchar(100) NOT NULL DEFAULT '',
  "total_fault_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NOT NULL DEFAULT '',
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (
    "api", 
    "version", 
    "apipublisher", 
    "context", 
    "hostname", 
    "time"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_request_summary"(
  "api" varchar(100) NOT NULL DEFAULT '',
  "api_version" varchar(100) NOT NULL DEFAULT '',
  "version" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "consumerkey" varchar(100) NOT NULL DEFAULT '',
  "userid" varchar(100) NOT NULL DEFAULT '',
  "context" varchar(100) NOT NULL DEFAULT '',
  "max_request_time" bigint NULL DEFAULT NULL,
  "total_request_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NOT NULL DEFAULT '',
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (
    "api", 
    "api_version", 
    "version", 
    "apipublisher", 
    "consumerkey", 
    "userid", 
    "context", 
    "hostname", 
    "time"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_resource_usage_summary"(
  "api" varchar(100) NOT NULL DEFAULT '',
  "version" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "consumerkey" varchar(100) NOT NULL DEFAULT '',
  "resourcepath" varchar(100) NOT NULL DEFAULT '',
  "context" varchar(100) NOT NULL DEFAULT '',
  "method" varchar(100) NOT NULL DEFAULT '',
  "total_request_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NOT NULL DEFAULT '',
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (
    "api", 
    "version", 
    "apipublisher", 
    "consumerkey", 
    "context", 
    "resourcepath", 
    "method", 
    "hostname", 
    "time"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_response_summary"(
  "api_version" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "context" varchar(100) NOT NULL DEFAULT '',
  "servicetime" int NULL DEFAULT NULL,
  "total_response_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NOT NULL DEFAULT '',
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (
    "api_version", 
    "apipublisher", 
    "context", 
    "hostname", 
    "time"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_version_usage_summary"(
  "api" varchar(100) NOT NULL DEFAULT '',
  "version" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "context" varchar(100) NOT NULL DEFAULT '',
  "total_request_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NOT NULL DEFAULT '',
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (
    "api", 
    "version", 
    "apipublisher", 
    "context", 
    "hostname", 
    "time"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_throttled_out_summary"(
  "api" varchar(100) NOT NULL DEFAULT '',
  "api_version" varchar(100) NOT NULL DEFAULT '',
  "context" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "applicationname" varchar(100) NOT NULL DEFAULT '',
  "tenantdomain" varchar(100) NOT NULL DEFAULT '',
  "year" smallint NULL,
  "month" smallint NULL,
  "day" smallint NULL,
  "week" int NULL,
  "time" varchar(30) NOT NULL DEFAULT '',
  "success_request_count" int NULL DEFAULT NULL,
  "throttleout_count" int NULL DEFAULT NULL,
  PRIMARY KEY (
    "api", 
    "api_version", 
    "context", 
    "apipublisher", 
    "applicationname", 
    "tenantdomain", 
    "year", 
    "month", 
    "day", 
    "time"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_last_access_time_summary"(
  "tenantdomain" varchar(100) NOT NULL DEFAULT '',
  "apipublisher" varchar(100) NOT NULL DEFAULT '',
  "api" varchar(100) NOT NULL DEFAULT '',
  "version" varchar(100) NULL DEFAULT NULL,
  "userid" varchar(100) NULL DEFAULT NULL,
  "context" varchar(100) NULL DEFAULT NULL,
  "max_request_time" bigint NULL DEFAULT NULL,
  PRIMARY KEY (
    "tenantdomain", 
    "apipublisher", 
    "api"
  )
);

--
-- Table structure for table `NB_API_REQUEST_SUMMARY`
--

DROP TABLE IF EXISTS "nb_api_request_summary";
CREATE TABLE "nb_api_request_summary"(
  "messagerowid" varchar(100) NOT NULL,
  "api" varchar(100) NULL DEFAULT NULL,
  "api_version" varchar(100) NULL DEFAULT NULL,
  "version" varchar(100) NULL DEFAULT NULL,
  "apipublisher" varchar(100) NULL DEFAULT NULL,
  "consumerkey" varchar(100) NULL DEFAULT NULL,
  "userid" varchar(100) NULL DEFAULT NULL,
  "context" varchar(100) NULL DEFAULT NULL,
  "request_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NULL DEFAULT NULL,
  "resourcepath" varchar(100) NULL DEFAULT NULL,
  "method" varchar(10) NULL DEFAULT NULL,
  "requestid" varchar(100) NULL DEFAULT NULL,
  "chargeamount" varchar(20) NULL DEFAULT NULL,
  "purchasecategorycode" varchar(40) NULL DEFAULT NULL,
  "jsonbody" clob NULL,
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NULL DEFAULT NULL,
  PRIMARY KEY ("messagerowid")
);

--
-- Table structure for table `NB_API_RESPONSE_SUMMARY`
--

DROP TABLE IF EXISTS "nb_api_response_summary";
CREATE TABLE "nb_api_response_summary"(
  "messagerowid" varchar(100) NOT NULL,
  "api" varchar(100) NULL DEFAULT NULL,
  "api_version" varchar(100) NULL DEFAULT NULL,
  "version" varchar(100) NULL DEFAULT NULL,
  "apipublisher" varchar(100) NULL DEFAULT NULL,
  "consumerkey" varchar(100) NULL DEFAULT NULL,
  "userid" varchar(100) NULL DEFAULT NULL,
  "context" varchar(100) NULL DEFAULT NULL,
  "servicetime" int NULL DEFAULT NULL,
  "response_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NULL DEFAULT NULL,
  "resourcepath" varchar(100) NULL DEFAULT NULL,
  "method" varchar(10) NULL DEFAULT NULL,
  "requestid" varchar(100) NULL DEFAULT NULL,
  "responsecode" varchar(5) NULL DEFAULT NULL,
  "msisdn" varchar(20) NULL DEFAULT NULL,
  "operatorref" varchar(100) NULL DEFAULT NULL,
  "chargeamount" varchar(20) NULL DEFAULT NULL,
  "purchasecategorycode" varchar(40) NULL DEFAULT NULL,
  "exceptionid" varchar(10) NULL DEFAULT NULL,
  "exceptionmessage" varchar(255) NULL DEFAULT NULL,
  "jsonbody" clob NULL,
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NULL DEFAULT NULL,
  "operationtype" int NULL DEFAULT NULL,
  "merchantid" varchar(100) NULL DEFAULT NULL,
  "category" varchar(100) NULL DEFAULT NULL,
  "subcategory" varchar(100) NULL DEFAULT NULL,
  PRIMARY KEY ("messagerowid")
);

--
-- Table structure for table `SB_API_REQUEST_SUMMARY`
--

DROP TABLE IF EXISTS "sb_api_request_summary";
CREATE TABLE "sb_api_request_summary"(
  "messagerowid" varchar(100) NOT NULL,
  "api" varchar(100) NULL DEFAULT NULL,
  "api_version" varchar(100) NULL DEFAULT NULL,
  "version" varchar(100) NULL DEFAULT NULL,
  "apipublisher" varchar(100) NULL DEFAULT NULL,
  "consumerkey" varchar(100) NULL DEFAULT NULL,
  "userid" varchar(100) NULL DEFAULT NULL,
  "context" varchar(100) NULL DEFAULT NULL,
  "request_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NULL DEFAULT NULL,
  "resourcepath" varchar(100) NULL DEFAULT NULL,
  "method" varchar(10) NULL DEFAULT NULL,
  "requestid" varchar(100) NULL DEFAULT NULL,
  "operatorid" varchar(100) NULL DEFAULT NULL,
  "chargeamount" varchar(20) NULL DEFAULT NULL,
  "purchasecategorycode" varchar(40) NULL DEFAULT NULL,
  "jsonbody" clob NULL,
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NULL DEFAULT NULL,
  PRIMARY KEY ("messagerowid")
);

--
-- Table structure for table `SB_API_RESPONSE_SUMMARY`
--

DROP TABLE IF EXISTS "sb_api_response_summary";
CREATE TABLE "sb_api_response_summary"(
  "messagerowid" varchar(100) NOT NULL,
  "api" varchar(100) NULL DEFAULT NULL,
  "api_version" varchar(100) NULL DEFAULT NULL,
  "version" varchar(100) NULL DEFAULT NULL,
  "apipublisher" varchar(100) NULL DEFAULT NULL,
  "consumerkey" varchar(100) NULL DEFAULT NULL,
  "userid" varchar(100) NULL DEFAULT NULL,
  "context" varchar(100) NULL DEFAULT NULL,
  "servicetime" int NULL DEFAULT NULL,
  "response_count" int NULL DEFAULT NULL,
  "hostname" varchar(100) NULL DEFAULT NULL,
  "resourcepath" varchar(100) NULL DEFAULT NULL,
  "method" varchar(10) NULL DEFAULT NULL,
  "requestid" varchar(100) NULL DEFAULT NULL,
  "operatorid" varchar(100) NULL DEFAULT NULL,
  "responsecode" varchar(5) NULL DEFAULT NULL,
  "msisdn" varchar(20) NULL DEFAULT NULL,
  "operatorref" varchar(100) NULL DEFAULT NULL,
  "chargeamount" varchar(20) NULL DEFAULT NULL,
  "purchasecategorycode" varchar(40) NULL DEFAULT NULL,
  "exceptionid" varchar(10) NULL DEFAULT NULL,
  "exceptionmessage" varchar(255) NULL DEFAULT NULL,
  "jsonbody" clob NULL,
  "year" smallint NULL DEFAULT NULL,
  "month" smallint NULL DEFAULT NULL,
  "day" smallint NULL DEFAULT NULL,
  "time" varchar(30) NULL DEFAULT NULL,
  "operationtype" int NULL DEFAULT NULL,
  "merchantid" varchar(100) NULL DEFAULT NULL,
  "category" varchar(100) NULL DEFAULT NULL,
  "subcategory" varchar(100) NULL DEFAULT NULL,
  PRIMARY KEY ("messagerowid")
);

--
--
--

DROP TABLE IF EXISTS "sub_approval_audit";
CREATE TABLE "sub_approval_audit"(
  "sub_approval_id" bigint NOT NULL AUTO_INCREMENT,
  "api_provider" varchar(200) NOT NULL DEFAULT '',
  "api_name" varchar(200) NOT NULL DEFAULT '',
  "api_version" varchar(30) NOT NULL DEFAULT '',
  "app_id" int NULL DEFAULT NULL,
  "sub_status" varchar(50) NULL DEFAULT 'ON_HOLD',
  "sub_approval_type" varchar(50) NULL DEFAULT NULL,
  "completed_by_role" varchar(50) NOT NULL DEFAULT '',
  "completed_by_user" varchar(50) NULL DEFAULT NULL,
  "completed_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("sub_approval_id")
);

--
--
--

DROP TABLE IF EXISTS "app_approval_audit";
CREATE TABLE "app_approval_audit"(
  "app_approval_id" bigint NOT NULL AUTO_INCREMENT,
  "app_name" varchar(100) NULL DEFAULT NULL,
  "app_creator" varchar(50) NULL DEFAULT NULL,
  "app_status" varchar(50) NULL DEFAULT 'ON_HOLD',
  "app_approval_type" varchar(50) NULL DEFAULT NULL,
  "completed_by_role" varchar(50) NULL DEFAULT NULL,
  "completed_by_user" varchar(50) NULL DEFAULT NULL,
  "completed_on" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY ("app_approval_id")
);

--
-- Table for tax type definition
--

CREATE TABLE IF NOT EXISTS "tax"(
  "id" int NOT NULL AUTO_INCREMENT,
  "type" varchar(25) NOT NULL,
  "effective_from" date NULL,
  "effective_to" date NULL,
  "value" decimal(7, 6) NOT NULL,
  PRIMARY KEY ("id")
);

--
-- Table for API subscriptions to tax type mapping
--

CREATE TABLE IF NOT EXISTS "subscription_tax"(
  "application_id" int NOT NULL,
  "api_id" int NOT NULL,
  "tax_type" varchar(25) NOT NULL,
  PRIMARY KEY (
    "application_id", 
    "api_id", 
    "tax_type"
  )
);

--
-- Table for API subscriptions to charge rate mapping
--

CREATE TABLE IF NOT EXISTS "subscription_rates"(
  "application_id" int NOT NULL,
  "api_id" int NOT NULL,
  "operator_name" varchar(45) NOT NULL,
  "rate_name" varchar(50) NULL DEFAULT NULL,
  "rate_id_sb" varchar(50) NULL DEFAULT NULL,
  "operation_id" int NOT NULL,
  PRIMARY KEY (
    "application_id", 
    "api_id", 
    "operator_name"
  )
);

--
-- Tables for whitelist & blacklist
--

CREATE TABLE IF NOT EXISTS "blacklistmsisdn"(
  "index" int NOT NULL AUTO_INCREMENT,
  "prefix" varchar(45) NOT NULL,
  "msisdn" varchar(45) NOT NULL,
  "api_id" varchar(45) NOT NULL,
  "api_name" varchar(45) NOT NULL,
  "user_id" varchar(45) NOT NULL,
  "validation_regex" varchar(300) NULL,
  PRIMARY KEY ("index"),
  CONSTRAINT "unq_blacklistmsisdn"
    UNIQUE (
      "api_name", 
      "msisdn"
    )
);

CREATE TABLE IF NOT EXISTS "subscription_whitelist"(
  "index" int NOT NULL AUTO_INCREMENT,
  "subscriptionid" varchar(45) NOT NULL,
  "prefix" varchar(45) NOT NULL,
  "msisdn" varchar(45) NOT NULL,
  "api_id" varchar(45) NOT NULL,
  "application_id" varchar(45) NOT NULL,
  "validation_regex" varchar(300) NULL,
  PRIMARY KEY ("index"),
  CONSTRAINT "white_label_unique_con"
    UNIQUE (
      "subscriptionid", 
      "msisdn", 
      "api_id", 
      "application_id"
    )
);

--
--
--

CREATE TABLE IF NOT EXISTS "admin_comments"(
  "taskid" int NOT NULL,
  "comment" varchar(255) NULL DEFAULT NULL,
  "status" varchar(255) NULL DEFAULT NULL,
  "description" varchar(1000) NOT NULL,
  PRIMARY KEY ("taskid")
);

--
--
--

CREATE TABLE IF NOT EXISTS "subscription_comments"(
  "taskid" varchar(255) NOT NULL,
  "comment" varchar(1024) NOT NULL,
  "status" varchar(255) NOT NULL,
  "description" varchar(1024) NOT NULL,
  PRIMARY KEY ("taskid")
);

--
--
--

CREATE TABLE IF NOT EXISTS "rates_percentages"(
  "id" bigint NOT NULL AUTO_INCREMENT,
  "merchant_code" varchar(45) NOT NULL,
  "app_id" bigint NOT NULL,
  "sp_commission" double NOT NULL,
  "ads_commission" double NOT NULL,
  "opco_commission" double NOT NULL,
  PRIMARY KEY ("id")
);

--
--
--

CREATE TABLE IF NOT EXISTS "subscription_rates_nb"(
  "application_id" int NOT NULL,
  "api_id" int NOT NULL,
  "rate_id_nb" varchar(50) NULL DEFAULT NULL,
  "operation_id" int NOT NULL,
  PRIMARY KEY (
    "application_id", 
    "api_id", 
    "operation_id"
  )
);

--
--
--

CREATE TABLE IF NOT EXISTS "api_operation_types"(
  "operation_id" int NOT NULL DEFAULT '0',
  "api" varchar(225) NULL DEFAULT NULL,
  "operation" varchar(225) NULL DEFAULT NULL,
  "default_rate" varchar(255) NOT NULL,
  PRIMARY KEY ("operation_id")
);

--
--
--

CREATE TABLE IF NOT EXISTS "merchant_rates_percentages"(
  "id" bigint NOT NULL AUTO_INCREMENT,
  "subscriber" varchar(45) NOT NULL,
  "merchant_code" varchar(45) NOT NULL,
  "app_id" bigint NOT NULL,
  "sp_commission" double NOT NULL,
  "ads_commission" double NOT NULL,
  "opco_commission" double NOT NULL,
  PRIMARY KEY ("id")
);

--
--
--

INSERT INTO "api_operation_types" ("operation_id", "api", "operation", "default_rate")
            VALUES (100, 'payment', 'Charge', 'p1'), (101, 'payment', 'Refund', 'RF2'), 
                   (200, 'smsmessaging', 'Send SMS', 'SM1'), (201, 'smsmessaging', 'Retrive SMS', 'SM2'), 
                   (202, 'smsmessaging', 'Query SMS Delivery', 'SM2'), (203, 'smsmessaging', 'Delivery Subscription', 'SM2'), 
                   (204, 'smsmessaging', 'Stop Delivery Subscription', 'SM2'), (205, 'smsmessaging', 'Retrive SMS Subscription', 'SM2'), 
                   (206, 'smsmessaging ', 'Stop Retrive SMS Subscription', 'SM2'), (207, 'smsmessaging', 'SMS Inbound Notification', 'SM2'), 
                   (300, 'location', 'Location', 'lb1'), (400, 'ussd', 'Send USSD', 'u1');
