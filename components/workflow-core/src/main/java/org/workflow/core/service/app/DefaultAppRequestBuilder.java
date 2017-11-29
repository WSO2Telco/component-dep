package org.workflow.core.service.app;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.model.UserProfileDTO;
import com.wso2telco.core.dbutils.util.AppApprovalRequest;
import com.wso2telco.core.dbutils.util.Callback;
import org.apache.commons.logging.LogFactory;
import org.workflow.core.activity.ApplicationApprovalRequest;
import org.workflow.core.model.*;
import org.workflow.core.model.Task;
import org.workflow.core.model.TaskList;
import org.workflow.core.model.TaskSerchDTO;
import org.workflow.core.model.TaskVariableResponse;
import org.workflow.core.service.ReturnableResponse;
import org.workflow.core.util.AppVariable;
import org.workflow.core.util.DeploymentTypes;
import org.workflow.core.util.WorkFlowHealper;
import org.workflow.core.service.AbsractQueryBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class DefaultAppRequestBuilder extends AbsractQueryBuilder {

	

	private static DefaultAppRequestBuilder instance;

	private DefaultAppRequestBuilder(DeploymentTypes depType) throws BusinessException {
		super.log = LogFactory.getLog(DefaultAppRequestBuilder.class);
		super.depType = depType;
	}

	public static DefaultAppRequestBuilder getInstace(DeploymentTypes depType) throws BusinessException {
		if (instance == null) {
			instance = new DefaultAppRequestBuilder(depType);
		}
		return instance;
	}

	private ReturnableResponse generateResponse(final TaskSerchDTO searchDTO, final TaskList taskList,
			final UserProfileDTO userProfile) throws ParseException {

		return new ReturnableResponse() {

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);

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
							if (varMap.containsKey(AppVariable.NAME)) {
								return varMap.get(AppVariable.NAME).getValue();
							} else {
								return null;
							}
						}

						public String getDescription() {
							if (varMap.containsKey(AppVariable.DESCRIPTION)) {
								return varMap.get(AppVariable.DESCRIPTION).getValue();
							} else {
								return null;
							}
						}

						public String getCreatedDate() {
							return format.format(task.getCreateTime());
						}

						public String getTier() {
							if (varMap.containsKey(AppVariable.TIER)) {
								return varMap.get(AppVariable.TIER).getValue();
							} else {
								return null;
							}
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
			payload = generateResponse(searchDTO, taskList, userProfile);
			returnCall = new Callback().setPayload(payload).setSuccess(true)
					.setMessage("Application Taks listed success ");
		} catch (ParseException e) {
			returnCall = new Callback().setPayload(null).setSuccess(false).setMessage("Application Taks listed fail ");
		}

		return returnCall;
	}

	@Override
	protected DeploymentTypes getDeployementType() {
		// TODO Auto-generated method stub
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
	protected List<Integer> getHistoricalData(String user, List<Range> months) throws BusinessException {
		String process = "application_creation_approval_process";
		List<Integer> data = new ArrayList();

		TaskDetailsResponse taskList = null;

		for (Range month : months) {
			taskList = activityClient.getHistoricTasks(month.getStart(), month.getEnd(), process, user);
			data.add(taskList.getTotal());

		}

		return data;
	}

	@Override
	protected ApplicationApprovalRequest buildApprovalRequest(AppApprovalRequest request) throws BusinessException {
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
			variables.add(new RequestVariable().setName("creditPlan").setValue(request.getCreditPlan()).setType(type));
		} else {
			variables.add(new RequestVariable().setName("operatorAdminApproval").setValue(request.getStatus()).setType(type));
			variables.add(new RequestVariable().setName("completedByUser").setValue(user).setType(type));
			variables.add(new RequestVariable().setName("status").setValue(request.getStatus()).setType(type));
			variables.add(new RequestVariable().setName("completedOn").setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH).format(new Date())).setType(type));
			variables.add(new RequestVariable().setName("description").setValue(request.getDescription()).setType(type));
		}

		ApplicationApprovalRequest applicationApprovalRequest= new ApplicationApprovalRequest();
		applicationApprovalRequest.setAction("complete");
		applicationApprovalRequest.setVariables(variables);
		return applicationApprovalRequest;
	}

	protected String getProcessDefinitionKey() {
		return depType.getAppProcessType();
	}

}
