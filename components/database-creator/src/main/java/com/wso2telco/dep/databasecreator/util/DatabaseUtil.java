/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.wso2telco.dep.databasecreator.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.CarbonUtils;
import com.wso2telco.dep.databasecreator.dto.TelcoDatasource;
import com.wso2telco.dep.databasecreator.dto.TelcoDatasources;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.util.List;

public class DatabaseUtil {

    private static final Log log = LogFactory.getLog(DatabaseUtil.class);
    private static volatile DataSource dataSource = null; // are there any issue with volatile

    public static void initialize() throws Exception {
        if (dataSource != null) {

            return;
        }

        synchronized (DatabaseUtil.class) {

            if (dataSource == null) {

                if (log.isDebugEnabled()) {

                    log.debug("Initializing data source");
                }

                OsgiProducerConfiguration config = new OsgiProducerConfiguration();
                config.load(getConfigFilePath());
                TelcoDatasources telcoDatasources = config.getTelcoDatasources();

                if (telcoDatasources != null) {

                    List<TelcoDatasource> telcoDatasource = telcoDatasources.getTelcoDatasource();

                    for (TelcoDatasource customDatasource : telcoDatasource) {

                        try {

                            Context ctx = new InitialContext();
                            dataSource = (DataSource) ctx.lookup(customDatasource.getName());
                            String databaseName = customDatasource.getName().replace("jdbc/", "");
                            String query = customDatasource.getQuery();
                            setupCustomDatabase(databaseName, query);
                        } catch (NamingException e) {

                            throw new Exception("Error while looking up the data source: " + customDatasource.getName(),
                                    e);
                        }
                    }
                } else {

                    log.error("Telco datasources not defined in wso2telco-datasources.xml");
                }
            }
        }
    }

    private static String getConfigFilePath() {

        return CarbonUtils.getCarbonConfigDirPath() + File.separator + "wso2telco-datasources.xml";
    }

    private static void setupCustomDatabase(String databaseName, String query) throws Exception {

        String value = System.getProperty("setup"); 

        if (value != null) {

            LocalDatabaseCreator telcoDatabaseCreator = new LocalDatabaseCreator(dataSource, databaseName);

            try {

                if (!telcoDatabaseCreator.isDatabaseStructureCreated(query)) {

                    telcoDatabaseCreator.createCustomDatabase();
                } else {

                    log.info(databaseName + " database already exists. Not creating a new database.");
                }
            } catch (Exception e) {

                String msg = "Error in creating the " + databaseName + " database";
                log.fatal(msg, e);
                throw new Exception(msg, e);
            }
        }
    }
}
