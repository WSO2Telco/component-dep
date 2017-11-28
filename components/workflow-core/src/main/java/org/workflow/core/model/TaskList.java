package org.workflow.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskList implements Serializable {

	/**
	*
	*/
	private static final long serialVersionUID = -3523557045856584393L;
	private int total;
	private int start;
	private String sort;
	private String order;
	private int sizel;
	private List<Task> data = new ArrayList<Task>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getSizel() {
		return sizel;
	}

	public void setSizel(int sizel) {
		this.sizel = sizel;
	}

	public List<Task> getData() {
		return data;
	}

	public void setData(List<Task> data) {
		this.data = data;
	}


}
