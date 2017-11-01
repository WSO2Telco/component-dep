package org.workflow.core.model;

public class TaskSerchDTO {
	private byte batchSize = 10;
	private int start = 0;
	private String orderBy = "desc";
	private String sortBy = "createTime";
	private String filterBy;
	private static final byte MINBATCHSIZE = 0;
	private static final byte MAXBATCHSIZE = 0;

	private static final int MINSTARTINDEX = 0;

	
	public String getFilterBy() {
		return filterBy;
	}

	public void setFilterBy(String filterBy) {
		this.filterBy = filterBy;
	}

	public byte getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(byte batchSize) {
		if (batchSize > MINBATCHSIZE && batchSize < MAXBATCHSIZE) {
			this.batchSize = batchSize;
		}
	}

	public int getStart() {

		return start;
	}

	public void setStart(int start) {
		if (start > MINSTARTINDEX) {
			this.start = start;
		}
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		if (orderBy!=null 
				&& !orderBy.isEmpty() 
				&& (orderBy.trim().equalsIgnoreCase("desc") 
						|| orderBy.trim().equalsIgnoreCase("asc"))) {
			this.orderBy = orderBy;
		}
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		if(sortBy!=null 
				&& !sortBy.isEmpty()
				&& (sortBy.equalsIgnoreCase("createTime")
						|| sortBy.equalsIgnoreCase("name"))) {
			this.sortBy = sortBy;
		}
	}

}
