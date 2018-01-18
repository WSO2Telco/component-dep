##################################################################################################################
####################   USER GUIDE  #####################################################################
#-- Do all db setups/configurations and start the server for the first time. #####################
#-- Upload the .bar files.
#-- Now stop the server.
#-- Use the provided “permission_tree.sql” and change the db name in the first line of the script to the name of your reg-db ( ex: prodregdb).
#-- Now execute the script.
#-- Now start the server again and check the server log for errors ( if exist something has gone wrong)
#-- Now go to carbon. ( https://localhost:9443/carbon )
#-- Now change the permissions of the “manage-app-admin” and view the permission tree.
#-- It should contain a new branch called “UI Module Permission”
#########################################################################################################################

USE YOUR_REG_DB_NAME;

SET @REG_PATH_PARENT_ID := (SELECT REG_PATH_ID
                            FROM REG_PATH
                            WHERE REG_PATH_VALUE = '/_system/governance/permission');

#create UI Module Permission collection #####################################33

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission', (SELECT @REG_PATH_PARENT_ID), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission'), NULL, NULL, 'admin', 'admin',
        'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission') AND REG_NAME IS NULL
      AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission'), NULL, NULL, 'admin', 'admin',
        'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'UI Module Permission', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE
                                            REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission', 'admin', 1, NULL, -1234);

####################################################################################
#crate sub menus ################3
SET @REG_PATH_PARENT_ID_UI_MODULE := (SELECT REG_PATH_ID
                                      FROM REG_PATH
                                      WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission');

#create Application collection######################################################

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/Application', (SELECT @REG_PATH_PARENT_ID_UI_MODULE), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'application', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Application'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application', 'admin', 1, NULL, -1234);

#create Sub Collections Application collection######################################################

SET @REG_PATH_PARENT_ID_APPLICATION := (SELECT REG_PATH_ID
                                        FROM REG_PATH
                                        WHERE REG_PATH_VALUE =
                                              '/_system/governance/permission/UI Module Permission/Application');

#collection to show application tiers

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/Application/Tiers', (SELECT @REG_PATH_PARENT_ID_APPLICATION),
   -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/Tiers'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application/Tiers', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/Tiers') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/Tiers'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'changeTiers', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Application/Tiers'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application/Tiers', 'admin', 1, NULL, -1234);

# to show application approval menu

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/Application/Visible', (SELECT @REG_PATH_PARENT_ID_APPLICATION),
   -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/Visible'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application/Visible', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/Visible')
      AND REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/Visible'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'visible', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Application/Visible'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application/Visible', 'admin', 1, NULL, -1234);

########## to show credit plan drop down

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application/CreditPlan',
        (SELECT @REG_PATH_PARENT_ID_APPLICATION), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/CreditPlan'), NULL,
        NULL, 'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application/CreditPlan', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE
                       REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/CreditPlan')
      AND REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Application/CreditPlan'), NULL,
        NULL, 'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'creditPlan', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Application/CreditPlan'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Application/CreditPlan', 'admin', 1, NULL, -1234);

###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33


#create Subscription collection #####################


INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/Subscription', (SELECT @REG_PATH_PARENT_ID_UI_MODULE), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'subscription', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Subscription'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription', 'admin', 1, NULL, -1234);

#create Sub Collections Application collection######################################################

SET @REG_PATH_PARENT_ID_SUBSCRIPTION := (SELECT REG_PATH_ID
                                         FROM REG_PATH
                                         WHERE REG_PATH_VALUE =
                                               '/_system/governance/permission/UI Module Permission/Subscription');

### show subscription tiers

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/Subscription/Tiers', (SELECT @REG_PATH_PARENT_ID_SUBSCRIPTION),
   -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Tiers'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription/Tiers', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Tiers')
      AND REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Tiers'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'changeTiers', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Subscription/Tiers'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription/Tiers', 'admin', 1, NULL, -1234);

####show subscription approval menu

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription/Visible',
        (SELECT @REG_PATH_PARENT_ID_SUBSCRIPTION), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Visible'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription/Visible', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Visible')
      AND REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Visible'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'visible', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Subscription/Visible'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription/Visible', 'admin', 1, NULL, -1234);

#show subscription rates

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/Subscription/Rates', (SELECT @REG_PATH_PARENT_ID_SUBSCRIPTION),
   -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Rates'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription/Rates', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Rates')
      AND REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Subscription/Rates'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'changeRates', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Subscription/Rates'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Subscription/Rates', 'admin', 1, NULL, -1234);

###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33
###########################################################################################################################33


#create Approval History Collection#####################

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/App Approval History', (SELECT @REG_PATH_PARENT_ID_UI_MODULE),
   -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/App Approval History'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/App Approval History', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/App Approval History')
      AND REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/App Approval History'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'workFlowHistory', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/App Approval History'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/App Approval History', 'admin', 1, NULL, -1234);

###################################################################################33
#add sub collections of approval history collection

SET @REG_PATH_PARENT_ID_AppApprovalHistory := (SELECT REG_PATH_ID
                                               FROM REG_PATH
                                               WHERE REG_PATH_VALUE =
                                                     '/_system/governance/permission/UI Module Permission/App Approval History');

#############################################################

#for show history menu

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/App Approval History/Visible',
        (SELECT @REG_PATH_PARENT_ID_AppApprovalHistory), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/App Approval History/Visible'),
        NULL, NULL, 'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/App Approval History/Visible', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE =
                           '/_system/governance/permission/UI Module Permission/App Approval History/Visible') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/App Approval History/Visible'),
        NULL, NULL, 'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'visible', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/App Approval History/Visible'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/App Approval History/Visible', 'admin', 1, NULL, -1234);

######### to show approved on table

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/App Approval History/Approved On',
        (SELECT @REG_PATH_PARENT_ID_AppApprovalHistory), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/App Approval History/Approved On'),
        NULL, NULL, 'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/App Approval History/Approved On', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE =
                           '/_system/governance/permission/UI Module Permission/App Approval History/Approved On') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/App Approval History/Approved On'),
        NULL, NULL, 'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'showApprovedOn', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/App Approval History/Approved On'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/App Approval History/Approved On', 'admin', 1, NULL, -1234);

####################################################################################
####################################################################################
####################################################################################
####################################################################################

#####Collection for rates#################

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate', (SELECT @REG_PATH_PARENT_ID_UI_MODULE), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'rate', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Rate'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate', 'admin', 1, NULL, -1234);

###################################################################################33
#add sub collections of rate collection

SET @REG_PATH_PARENT_ID_Rate := (SELECT REG_PATH_ID
                                 FROM REG_PATH
                                 WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate');

#############################################################

#for view rate sub menu

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/View', (SELECT @REG_PATH_PARENT_ID_Rate), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/View'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/View', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/View') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/View'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'view', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Rate/View'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/View', 'admin', 1, NULL, -1234);

##################### for add new rate

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/Add', (SELECT @REG_PATH_PARENT_ID_Rate), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/Add'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/Add', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/Add') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/Add'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'add', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Rate/Add'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/Add', 'admin', 1, NULL, -1234);

########## for assign rates

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/Assign', (SELECT @REG_PATH_PARENT_ID_Rate), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/Assign'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/Assign', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/Assign') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Rate/Assign'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'assign', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Rate/Assign'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Rate/Assign', 'admin', 1, NULL, -1234);

##############################################################################################################################
##############################################################################################################################
###############################################################################################################################
###############################################################################################################################
###############################################################################################################################

# for quota

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota', (SELECT @REG_PATH_PARENT_ID_UI_MODULE), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'quota', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Quota'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota', 'admin', 1, NULL, -1234);

###################################################################################33
#add sub collections of rate collection

SET @REG_PATH_PARENT_ID_Quota := (SELECT REG_PATH_ID
                                  FROM REG_PATH
                                  WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota');

#############################################################

#to show quota sub menu

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota/Visible', (SELECT @REG_PATH_PARENT_ID_Quota), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota/Visible'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota/Visible', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota/Visible') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota/Visible'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'visible', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Quota/Visible'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota/Visible', 'admin', 1, NULL, -1234);

# to show hide operator dropdown


INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota/Operator List', (SELECT @REG_PATH_PARENT_ID_Quota),
        -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota/Operator List'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota/Operator List', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota/Operator List')
      AND REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Quota/Operator List'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'operatorList', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Quota/Operator List'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Quota/Operator List', 'admin', 1, NULL, -1234);

####


##############################################################################################################################
##############################################################################################################################
###############################################################################################################################
###############################################################################################################################
###############################################################################################################################


##for sp black list

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/SP Blacklist', (SELECT @REG_PATH_PARENT_ID_UI_MODULE), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/SP Blacklist'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/SP Blacklist', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/SP Blacklist') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/SP Blacklist'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'spBlackList', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/SP Blacklist'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/SP Blacklist', 'admin', 1, NULL, -1234);

##########  for api black list


INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES
  ('/_system/governance/permission/UI Module Permission/API Blacklist', (SELECT @REG_PATH_PARENT_ID_UI_MODULE), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/API Blacklist'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/API Blacklist', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/API Blacklist') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/API Blacklist'), NULL, NULL,
        'admin', 'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'apiBlacklist', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/API Blacklist'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/API Blacklist', 'admin', 1, NULL, -1234);

##############################################################################################################################
##############################################################################################################################
###############################################################################################################################
###############################################################################################################################
###############################################################################################################################


### for whitelist

INSERT INTO REG_PATH (REG_PATH_VALUE, REG_PATH_PARENT_ID, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Whitelist', (SELECT @REG_PATH_PARENT_ID_UI_MODULE), -1234);

INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Whitelist'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Whitelist', 'admin', 0, NULL, -1234);


DELETE FROM REG_RESOURCE
WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                     FROM REG_PATH
                     WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Whitelist') AND
      REG_NAME IS NULL AND
      REG_TENANT_ID = -1234;


INSERT INTO REG_RESOURCE (REG_PATH_ID, REG_NAME, REG_MEDIA_TYPE, REG_CREATOR, REG_LAST_UPDATOR, REG_DESCRIPTION, REG_TENANT_ID, REG_UUID)
VALUES ((SELECT REG_PATH_ID
         FROM REG_PATH
         WHERE REG_PATH_VALUE = '/_system/governance/permission/UI Module Permission/Whitelist'), NULL, NULL, 'admin',
        'admin', 'fjo', -1234,
        UUID());

INSERT INTO REG_PROPERTY (REG_NAME, REG_VALUE, REG_TENANT_ID) VALUES ('name', 'whiteList', -1234);
SET @REG_PROPERTY_ID := (SELECT LAST_INSERT_ID());
SET @REG_VERSION := (SELECT REG_VERSION
                     FROM REG_RESOURCE
                     WHERE REG_PATH_ID = (SELECT REG_PATH_ID
                                          FROM REG_PATH
                                          WHERE REG_PATH_VALUE =
                                                '/_system/governance/permission/UI Module Permission/Whitelist'));


INSERT INTO REG_RESOURCE_PROPERTY (REG_PROPERTY_ID, REG_VERSION, REG_TENANT_ID)
VALUES ((SELECT @REG_PROPERTY_ID), (SELECT @REG_VERSION), -1234);

INSERT INTO REG_LOG (REG_PATH, REG_USER_ID, REG_ACTION, REG_ACTION_DATA, REG_TENANT_ID)
VALUES ('/_system/governance/permission/UI Module Permission/Whitelist', 'admin', 1, NULL, -1234);
