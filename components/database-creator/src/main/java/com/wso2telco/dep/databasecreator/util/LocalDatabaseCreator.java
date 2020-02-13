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
import org.wso2.carbon.utils.dbcreator.DatabaseCreator;
import javax.sql.DataSource;
import java.io.File;

public class LocalDatabaseCreator extends DatabaseCreator {

	private static final Log log = LogFactory.getLog(LocalDatabaseCreator.class);
	private DataSource dataSource;
	private String databaseName;

	public LocalDatabaseCreator(DataSource dataSource, String databaseName) {

		super(dataSource);
		this.dataSource = dataSource;
		this.databaseName = databaseName;
	}

	public void createCustomDatabase() throws Exception {

		String databaseType = DatabaseCreator.getDatabaseType(this.dataSource.getConnection());

		String scripPath = getDbScriptLocation(databaseType);
		File scripFile = new File(scripPath);

		if (scripFile.exists()) {

			super.createRegistryDatabase();
		}
	}

	protected String getDbScriptLocation(String databaseType) {

		String scriptName = databaseType + ".sql";

		if (log.isDebugEnabled()) {

			log.debug("Loading database script from :" + scriptName);
		}

		String carbonHome = System.getProperty("carbon.home");
		return carbonHome + "/dbscripts/wso2telco/" + databaseName.toLowerCase() + "/" + scriptName;
	}
}
