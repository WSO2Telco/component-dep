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
import com.wso2telco.dep.databasecreator.dto.TelcoDatasources;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class OsgiProducerConfiguration {


	private static Log log = LogFactory.getLog(OsgiProducerConfiguration.class);
	private boolean initialized;
	private TelcoDatasources telcoDatasources;

	public void load(String filePath) {

		if (initialized) {

			return;
		}

		File file = new File(filePath);

		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(TelcoDatasources.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			telcoDatasources = (TelcoDatasources) jaxbUnmarshaller.unmarshal(file);
			initialized = true;
		} catch (JAXBException e) {

			e.printStackTrace();
			log.error("Error occurred while parsing custom XML configuration file.", e);
		}
	}

	public TelcoDatasources getTelcoDatasources() {

		return telcoDatasources;
	}
}
