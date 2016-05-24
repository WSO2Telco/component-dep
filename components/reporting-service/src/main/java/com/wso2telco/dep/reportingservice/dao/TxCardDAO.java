/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.reportingservice.dao;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class TxCardDAO.
 */
public class TxCardDAO {
	
		/** The tx operation type. */
		private int txOperationType;
		
		/** The header list. */
		private List<String> headerList;
		
		/** The data list. */
		private List<TxCardDAO.DataList> dataList;
		
		/**
		 * Gets the tx operation type.
		 *
		 * @return the tx operation type
		 */
		public int getTxOperationType() {
			return txOperationType;
		}
		
		/**
		 * Sets the tx operation type.
		 *
		 * @param txOperationType the new tx operation type
		 */
		public void setTxOperationType(int txOperationType) {
			this.txOperationType = txOperationType;
		}
		
		/**
		 * Gets the header list.
		 *
		 * @return the header list
		 */
		public List<String> getHeaderList() {
			return headerList;
		}
		
		/**
		 * Sets the header list.
		 *
		 * @param headerList the new header list
		 */
		public void setHeaderList(List<String> headerList) {
			this.headerList = headerList;
		}
		
		/**
		 * Gets the data list.
		 *
		 * @return the data list
		 */
		public List<DataList> getDataList() {
			return dataList;
		}
		
		/**
		 * Sets the data list.
		 *
		 * @param dataList the new data list
		 */
		public void setDataList(List<DataList> dataList) {
			this.dataList = dataList;
		}
		
		/**
		 * The Class DataList.
		 */
		public static class DataList{
			
			/** The manipulation type. */
			private String manipulationType;
			
			/** The manipulation field. */
			private String manipulationField;
			
			/** The manipulation reg ex. */
			private String manipulationRegEx;
			
			/**
			 * Instantiates a new data list.
			 */
			public DataList(){}
			
			/**
			 * Gets the manipulation type.
			 *
			 * @return the manipulation type
			 */
			public String getManipulationType() {
				return manipulationType;
			}
			
			/**
			 * Sets the manipulation type.
			 *
			 * @param manipulationType the new manipulation type
			 */
			public void setManipulationType(String manipulationType) {
				this.manipulationType = manipulationType;
			}
			
			/**
			 * Gets the manipulation field.
			 *
			 * @return the manipulation field
			 */
			public String getManipulationField() {
				return manipulationField;
			}
			
			/**
			 * Sets the manipulation field.
			 *
			 * @param manipulationField the new manipulation field
			 */
			public void setManipulationField(String manipulationField) {
				this.manipulationField = manipulationField;
			}
			
			/**
			 * Gets the manipulation reg ex.
			 *
			 * @return the manipulation reg ex
			 */
			public String getManipulationRegEx() {
				return manipulationRegEx;
			}
			
			/**
			 * Sets the manipulation reg ex.
			 *
			 * @param manipulationRegEx the new manipulation reg ex
			 */
			public void setManipulationRegEx(String manipulationRegEx) {
				this.manipulationRegEx = manipulationRegEx;
			}
			
		}
		
	}

	

