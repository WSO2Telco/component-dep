package com.wso2telco.workflow.model;

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
	 private List<Task>data = new ArrayList<Task>();
	 
	  
	
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



	public class Task {
		
		private String assignee;
	    private Date createTime;
	    private String delegationState;
	    private String description;
	    private Date dueDate;
	    private String execution;
	    private int id;
	    private String name;
	    private String owner;
	    private String parentTask;
	    private int priority;
	    private String processDefinition;
	    private String processInstance;
	    private Boolean suspended;
	    private String taskDefinitionKey;
	    private String url;
	    private String tenantId;
		public String getAssignee() {
			return assignee;
		}
		public void setAssignee(String assignee) {
			this.assignee = assignee;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getDelegationState() {
			return delegationState;
		}
		public void setDelegationState(String delegationState) {
			this.delegationState = delegationState;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Date getDueDate() {
			return dueDate;
		}
		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}
		public String getExecution() {
			return execution;
		}
		public void setExecution(String execution) {
			this.execution = execution;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getParentTask() {
			return parentTask;
		}
		public void setParentTask(String parentTask) {
			this.parentTask = parentTask;
		}
		public int getPriority() {
			return priority;
		}
		public void setPriority(int priority) {
			this.priority = priority;
		}
		public String getProcessDefinition() {
			return processDefinition;
		}
		public void setProcessDefinition(String processDefinition) {
			this.processDefinition = processDefinition;
		}
		public String getProcessInstance() {
			return processInstance;
		}
		public void setProcessInstance(String processInstance) {
			this.processInstance = processInstance;
		}
		public Boolean getSuspended() {
			return suspended;
		}
		public void setSuspended(Boolean suspended) {
			this.suspended = suspended;
		}
		public String getTaskDefinitionKey() {
			return taskDefinitionKey;
		}
		public void setTaskDefinitionKey(String taskDefinitionKey) {
			this.taskDefinitionKey = taskDefinitionKey;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getTenantId() {
			return tenantId;
		}
		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}
	}
}

