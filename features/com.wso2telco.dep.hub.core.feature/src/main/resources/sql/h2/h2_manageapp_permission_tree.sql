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

##################################################################################################################
####################   USER GUIDE  #####################################################################
#-- Do all db setups/configurations and start the server for the first time. #####################
#-- Upload the .bar files.
#-- Now stop the server.
#-- Use the provided “manageapp_permission_tree.sql” and change the db name in the first line of the script to the name of your reg-db ( eg: prodregdb).
#-- Now execute the script.
#-- Now start the server again and check the server log for errors ( if exist something has gone wrong)
#-- Now go to carbon. ( https://localhost:9443/carbon )
#-- Now change the permissions of the “manage-app-admin” and view the permission tree.
#-- It should contain a new branch called “UI Module Permission”
#########################################################################################################################

