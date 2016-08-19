/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.oneapivalidation.util;

import java.io.File;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.CarbonUtils;
import com.wso2telco.dbutils.fileutils.FileReader;

public class PropertyUtils {

	private Log log = LogFactory.getLog(PropertyUtils.class);

	/**
	 * Gets the SMS batch size.
	 *
	 * @return the SMS batch size
	 */
	public int getSMSBatchSize() {

		FileReader fileReader = new FileReader();
		String filePath = CarbonUtils.getCarbonConfigDirPath() + File.separator;

		int result = 0;

		try {

			HashMap<String, String> oneAPIValidationConfMap = fileReader.readPropertyFile(filePath, FileNames.ONEAPI_VALIDATION_CONF_FILE.getFileName());
			String batchSize = oneAPIValidationConfMap.get("sms.batchSize");

			if (batchSize == null || batchSize.trim().length() <= 0) {

				result = 25;
				log.debug("sms.batchSize property is empty. set default value 25 for sms batch size");
			} else {

				result = Integer.parseInt(batchSize);
				log.debug("set sms batch size to " + batchSize);
			}
		} catch (Exception e) {

			result = 25;
			log.error("error in accessing sms.batchSize property. set default value 25 for sms batch size");
		}

		return result;
	}

	public boolean getApiTypeAvailability(String apiTypeParam) {

		FileReader fileReader = new FileReader();
		String filePath = CarbonUtils.getCarbonConfigDirPath() + File.separator;
		boolean isAvailable = false;

		try {

			HashMap<String, String> oneAPIValidationConfMap = fileReader.readPropertyFile(filePath, FileNames.ONEAPI_VALIDATION_CONF_FILE.getFileName());
			String value = oneAPIValidationConfMap.get(apiTypeParam);

			if (!value.equals("")) {

				isAvailable = Boolean.valueOf(value);
			}
		} catch (Exception e) {

			log.error("error in getApiTypeAvailability ", e);
			isAvailable = false;
		}

		return isAvailable;
	}

	public boolean getIsSMSLimitToOne() {
		
		FileReader fileReader = new FileReader();
		String filePath = CarbonUtils.getCarbonConfigDirPath() + File.separator;
		boolean result = true;
		
		try {
			
			HashMap<String, String> oneAPIValidationConfMap = fileReader.readPropertyFile(filePath, FileNames.ONEAPI_VALIDATION_CONF_FILE.getFileName());
			String value = oneAPIValidationConfMap.get("sms.limitToOne");
			
			if (value.equals("false")) {
				
				result = false;
			}
		} catch (Exception e) {
			
			log.error("error in getIsSMSLimitToOne ", e);
		}
		
		return result;
	}
}
