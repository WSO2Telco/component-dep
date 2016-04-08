package com.wso2telco.custom.dao;

import java.util.List;

public class TxCardDAO {
	
		private int txOperationType;
		
		private List<String> headerList;
		private List<TxCardDAO.DataList> dataList;
		
		public int getTxOperationType() {
			return txOperationType;
		}
		public void setTxOperationType(int txOperationType) {
			this.txOperationType = txOperationType;
		}
		public List<String> getHeaderList() {
			return headerList;
		}
		public void setHeaderList(List<String> headerList) {
			this.headerList = headerList;
		}
		public List<DataList> getDataList() {
			return dataList;
		}
		public void setDataList(List<DataList> dataList) {
			this.dataList = dataList;
		}
		
		public static class DataList{
			
			private String manipulationType;
			private String manipulationField;
			private String manipulationRegEx;
			
			public DataList(){}
			
			public String getManipulationType() {
				return manipulationType;
			}
			public void setManipulationType(String manipulationType) {
				this.manipulationType = manipulationType;
			}
			public String getManipulationField() {
				return manipulationField;
			}
			public void setManipulationField(String manipulationField) {
				this.manipulationField = manipulationField;
			}
			public String getManipulationRegEx() {
				return manipulationRegEx;
			}
			public void setManipulationRegEx(String manipulationRegEx) {
				this.manipulationRegEx = manipulationRegEx;
			}
			
		}
		
	}

	

