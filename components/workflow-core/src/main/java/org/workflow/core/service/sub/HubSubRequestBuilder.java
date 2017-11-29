package org.workflow.core.service.sub;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.ApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.activity.ApplicationApprovalRequest;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.*;
import org.workflow.core.service.AbsractQueryBuilder;
import org.workflow.core.service.ReturnableResponse;
import org.workflow.core.util.AppVariable;
import org.workflow.core.util.DeploymentTypes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class HubSubRequestBuilder extends AbsractQueryBuilder {

	private static HubSubRequestBuilder instance;
	{
		log = LogFactory.getLog(HubSubRequestBuilder.class);
	}

	private HubSubRequestBuilder(DeploymentTypes depType) throws BusinessException {
		super.depType = depType;
		super.initialize();
	}

	public static HubSubRequestBuilder getInstace(DeploymentTypes depType) throws BusinessException {
		if (instance == null) {
			instance = new HubSubRequestBuilder(depType);
		}
		return instance;
	}


	private ReturnableResponse generateResponse(final TaskSerchDTO searchDTO,final TaskList taskList ,final UserProfileDTO userProfile) throws ParseException {

		return  new ReturnableResponse() {

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",Locale.ENGLISH);
			
			@Override
			public int getTotal() {
				return taskList.getTotal();
			}

			@Override
			public int getStrat() {
				return taskList.getStart();
			}

			@Override
			public int getBatchSize() {
				return taskList.getSize();
			}

			@Override
			public String getFilterBy() {
				return searchDTO.getFilterBy();
			}

			@Override
			public String getOrderBy() {
				return searchDTO.getOrderBy();
			}

			@Override
			public List<ReturnableTaskResponse> getTasks() {
				List<ReturnableTaskResponse> temptaskList = new ArrayList<ReturnableResponse.ReturnableTaskResponse>();

				for (final Task task : taskList.getData()) {
					final Map<AppVariable, TaskVariableResponse> varMap = new HashMap<AppVariable, TaskVariableResponse>();
					for (final TaskVariableResponse var : task.getVars()) {
						varMap.put(AppVariable.getByKey(var.getName()), var);
					}

					ReturnableTaskResponse responseTask = new ReturnableTaskResponse() {
						/**
						 * return task ID
						 */
						public int getID() {
							return task.getId();
						}

						public String getName() {
							return varMap.get(AppVariable.NAME).getValue();
						}

						public String getDescription() {
							return varMap.get(AppVariable.DESCRIPTION).getValue();
						}

						public String getCreatedDate() {
							return format.format(task.getCreateTime());
						}

						public String getTier() {
							return varMap.get(AppVariable.TIER).getValue();
						}

						public String getAssinee() {
							return task.getAssignee();
						}
					};
					temptaskList.add(responseTask);
				}
				return temptaskList;
			}
		};
	}
	
	@Override
	protected Callback buildResponse(TaskSerchDTO searchDTO, TaskList taskList, UserProfileDTO userProfile)
			throws BusinessException {
		ReturnableResponse payload;
		Callback returnCall;
		try {
			payload = generateResponse( searchDTO,taskList, userProfile);
			returnCall= new Callback().setPayload(payload)
					.setSuccess(true)
					.setMessage("Subscription Taks listed success ");
		} catch (ParseException e) {
			returnCall= new Callback().setPayload(null)
					.setSuccess(false)
					.setMessage("Subscription Taks listed fail ");
		}
		
		 return returnCall;
	}

	@Override
	protected DeploymentTypes getDeployementType() {
		return depType;
	}

	@Override
	protected Map<String, String> getFilterMap() {
		Map<String, String> filter = new HashMap<String, String>();
		filter.put("name", AppVariable.NAME.key());
		filter.put("applicationname", AppVariable.NAME.key());
		filter.put("appname", AppVariable.NAME.key());
		filter.put("tier", AppVariable.TIER.key());
		filter.put("createdby", AppVariable.USERNAME.key());
		filter.put("owner", AppVariable.USERNAME.key());
		return filter;

	}

	@Override
	protected Callback getHistoricalData(String user, List<Range> months, List<String> xAxisLabels) throws BusinessException {
		String process = "subscription_approval_process";
		List<Integer> data = new ArrayList();

		TaskDetailsResponse taskList = null;

		for (Range month : months) {
			taskList = activityClient.getHistoricTasks(month.getStart(), month.getEnd(), process, user);
			data.add(taskList.getTotal());

		}

		if (!data.isEmpty()) {
			GraphData graphData = new GraphData();
			graphData.setData(data);
			graphData.setLabel("subscriptions".toUpperCase());
			List<GraphData> graphDataList = new ArrayList();
			graphDataList.add(graphData);
			GraphResponse graphResponse = new GraphResponse();
			graphResponse.setXAxisLabels(xAxisLabels);
			graphResponse.setGraphData(graphDataList);
			return new Callback().setPayload(graphResponse).setSuccess(true).setMessage("Subscription Approval History Loaded Successfully");
		} else {
			return new Callback().setPayload(Collections.emptyList()).setSuccess(false).setMessage("Error Loading Subscription Approval History");
		}
	}

	@Override
	protected Callback buildApprovalRequest(ApprovalRequest request) throws BusinessException {
		List<RequestVariable> variables = new ArrayList();

		boolean isAdmin = true; //dummy variable
		final String type = "string";
		final String user = "admin";

		if (isAdmin) {
			variables.add(new RequestVariable().setName("hubAdminApproval").setValue(request.getStatus()).setType(type));
			variables.add(new RequestVariable().setName("completedByUser").setValue(user).setType(type));
			variables.add(new RequestVariable().setName("status").setValue(request.getStatus()).setType(type));
			variables.add(new RequestVariable().setName("completedOn").setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).format(new Date())).setType(type));
			variables.add(new RequestVariable().setName("description").setValue(request.getDescription()).setType(type));
			variables.add(new RequestVariable().setName("selectedTier").setValue(request.getSelectedTier()).setType(type));
			variables.add(new RequestVariable().setName("selectedRate").setValue(request.getSelectedRate()).setType(type));
		} else {
			variables.add(new RequestVariable().setName("operatorAdminApproval").setValue(request.getStatus()).setType(type));
			variables.add(new RequestVariable().setName("completedByUser").setValue(user).setType(type));
			variables.add(new RequestVariable().setName("status").setValue(request.getStatus()).setType(type));
			variables.add(new RequestVariable().setName("completedOn").setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).format(new Date())).setType(type));
			variables.add(new RequestVariable().setName("description").setValue(request.getDescription()).setType(type));
			variables.add(new RequestVariable().setName("selectedRate").setValue(request.getSelectedRate()).setType(type));
		}

		ApplicationApprovalRequest applicationApprovalRequest = new ApplicationApprovalRequest();
		applicationApprovalRequest.setAction("complete");
		applicationApprovalRequest.setVariables(variables);

		try {
			activityClient.approveTask(request.getTaskId(), applicationApprovalRequest);
			return new Callback().setPayload(null).setSuccess(true).setMessage("Subscription Approved Successfully");
		} catch (WorkflowExtensionException e) {
			log.error("", e);
			return new Callback().setPayload(null).setSuccess(false).setMessage("Error While Subscription Approval");
		}
	}

	protected String getProcessDefinitionKey() {
		return depType.getSubscriptoinProcessType();
	}

}
