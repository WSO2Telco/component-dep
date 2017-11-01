package org.workflow.core.service;

import java.util.List;

public interface ReturnableResponse {
	int getTotal();
	int getStrat();
	int getBatchSize();
	String getFilterBy();
	String getOrderBy();
	List<ReturnableTaskResponse> getTasks();
	
	interface ReturnableTaskResponse{
		
	}
}

