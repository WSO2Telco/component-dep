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
package com.wso2telco.dep.workflow;

import org.apache.axiom.om.util.AXIOMUtil;
import org.wso2.carbon.apimgt.impl.workflow.GeneralWorkflowResponse;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.Tier;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.SubscriptionWorkflowDTO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowConstants;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowStatus;

import com.wso2telco.dep.workflow.impl.WorkflowAPIConsumerImpl;
import com.wso2telco.dep.workflow.interfaces.WorkflowAPIConsumer;
import com.wso2telco.dep.workflow.internal.ServiceReferenceHolder;

import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.api.APIConsumer;

import javax.xml.stream.XMLStreamException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubscriptionCreationWSWorkflowExecutor extends WorkflowExecutor {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5971125822992744788L;

	private static final Log log = LogFactory.getLog(SubscriptionCreationWSWorkflowExecutor.class);

    private String serviceEndpoint;

    private String username;

    private String password;

    private String contentType;

    @Override
    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_AM_SUBSCRIPTION_CREATION;
    }

    @Override
    public List<WorkflowDTO> getWorkflowDetails(String workflowStatus) throws WorkflowException {
        return null;
    }

    @Override
    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        try {
            ServiceClient client = new ServiceClient(ServiceReferenceHolder.getInstance()
                    .getContextService().getClientConfigContext(), null);
            Options options = new Options();
            options.setAction("http://workflow.subscription.apimgt.carbon.wso2.org/initiate");
            options.setTo(new EndpointReference(serviceEndpoint));
            if (contentType != null) {
                options.setProperty(Constants.Configuration.MESSAGE_TYPE, contentType);
            }

            HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();

            //Consider this as a secured service if username and password are not null. Unsecured if not.
            if (username != null && password != null) {
                auth.setUsername(username);
                auth.setPassword(password);
                auth.setPreemptiveAuthentication(true);
                List<String> authSchemes = new ArrayList<String>();
                authSchemes.add(HttpTransportProperties.Authenticator.BASIC);
                auth.setAuthSchemes(authSchemes);

                if (contentType == null) {
                    options.setProperty(Constants.Configuration.MESSAGE_TYPE, HTTPConstants.MEDIA_TYPE_APPLICATION_XML);
                }
                options.setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
                options.setManageSession(true);
            }

            client.setOptions(options);

            String payload = "<wor:SubscriptionApprovalWorkFlowProcessRequest xmlns:wor=\"http://workflow.subscription.apimgt.carbon.wso2.org\">\n"
                    + "         <wor:apiName>$1</wor:apiName>\n"
                    + "         <wor:apiVersion>$2</wor:apiVersion>\n"
                    + "         <wor:apiContext>$3</wor:apiContext>\n"
                    + "         <wor:apiProvider>$4</wor:apiProvider>\n"
                    + "         <wor:subscriber>$5</wor:subscriber>\n"
                    + "         <wor:applicationName>$6</wor:applicationName>\n"
                    + "         <wor:tierName>$7</wor:tierName>\n"
                    + "         <wor:workflowExternalRef>$8</wor:workflowExternalRef>\n"
                    + "         <wor:callBackURL>$9</wor:callBackURL>\n"
                    + "         <wor:applicationId>$a</wor:applicationId>\n"
                    + "         <wor:applicationDescription>$b</wor:applicationDescription>\n"
					+ "         <wor:apiID>$c</wor:apiID>\n"
                    + "         <wor:tiers>$d</wor:tiers>\n"
                    + "      </wor:SubscriptionApprovalWorkFlowProcessRequest>";

            SubscriptionWorkflowDTO subsWorkflowDTO = (SubscriptionWorkflowDTO) workflowDTO;
            String callBackURL = subsWorkflowDTO.getCallbackUrl();

            // Obtain application id.
            int applicationId = APIUtil.getApplicationId(subsWorkflowDTO.getApplicationName(), subsWorkflowDTO.getSubscriber());
            String applicationIdStr = String.valueOf(applicationId);

            // Obtain application details.
            APIConsumer consumer = APIManagerFactory.getInstance().getAPIConsumer(username);
            Application[] applications = consumer.getApplications(new Subscriber(subsWorkflowDTO.getSubscriber()), applicationIdStr);
            Application app = null;

            for (Application application : applications) {
                if (application.getId() == applicationId) {
                    app = application;
                    break;
                }
            }

            // Obtain list of tiers.
            String providerName = subsWorkflowDTO.getApiProvider();
            String apiName = subsWorkflowDTO.getApiName();
            String version = subsWorkflowDTO.getApiVersion();
            APIIdentifier apiIdentifier = new APIIdentifier(providerName, apiName, version);
            API api = consumer.getAPI(apiIdentifier);
            Set<Tier> tierSet = api.getAvailableTiers();

            String tiersStr = "";

            for (Iterator iterator = tierSet.iterator(); iterator.hasNext();) {
                Tier tier = (Tier) iterator.next();
                String tierName = tier.getName();
                String tierDisplayName = tier.getDisplayName();
                String tierAttributesString = "";
                if(tier.getTierAttributes() != null){
                   tierAttributesString = getTierAttributesString(tier.getTierAttributes()); 
                }

                tiersStr = tiersStr + "         <wor:tier>\n"
                        + "             <wor:tierName>$e</wor:tierName>\n"
                        + "             <wor:tierDisplayName>$f</wor:tierDisplayName>\n"
                        + "             <wor:tierAttributes>$g</wor:tierAttributes>\n"
                        + "         </wor:tier>\n";

                tiersStr = tiersStr.replace("$e", tierName);
                tiersStr = tiersStr.replace("$f", tierDisplayName);
                tiersStr = tiersStr.replace("$g", tierAttributesString);
            }

    		WorkflowAPIConsumer axiataAPIConsumer = new WorkflowAPIConsumerImpl();
            int apiID = axiataAPIConsumer.getAPIID(apiIdentifier);

            payload = payload.replace("$1", apiName);
            payload = payload.replace("$2", version);
            payload = payload.replace("$3", subsWorkflowDTO.getApiContext());
            payload = payload.replace("$4", providerName);
            payload = payload.replace("$5", subsWorkflowDTO.getSubscriber());
            payload = payload.replace("$6", subsWorkflowDTO.getApplicationName());
            payload = payload.replace("$7", subsWorkflowDTO.getTierName());
            payload = payload.replace("$8", subsWorkflowDTO.getExternalWorkflowReference());
            payload = payload.replace("$9", callBackURL != null ? callBackURL : "?");
            payload = payload.replace("$a", applicationIdStr);
            payload = payload.replace("$b", app.getDescription());
            payload = payload.replace("$c", Integer.toString(apiID));
            payload = payload.replace("$d", tiersStr);

            client.fireAndForget(AXIOMUtil.stringToOM(payload));

            super.execute(workflowDTO);
        } catch (AxisFault axisFault) {
            log.error("Error sending out message", axisFault);
            throw new WorkflowException("Error sending out message", axisFault);
        } catch (XMLStreamException e) {
            log.error("Error converting String to OMElement", e);
            throw new WorkflowException("Error converting String to OMElement", e);
        } catch (APIManagementException e) {
            log.error("Error retrieving application id", e);
            throw new WorkflowException("Error retrieving application id", e);
        }
		return new GeneralWorkflowResponse();
    }

    @Override
    public WorkflowResponse complete(WorkflowDTO workflowDTO) throws WorkflowException {

        workflowDTO.setUpdatedTime(System.currentTimeMillis());
        super.complete(workflowDTO);
        log.info("Subscription Creation [Complete] Workflow Invoked. Workflow ID : " + workflowDTO.getExternalWorkflowReference() + "Workflow State : " + workflowDTO.getStatus());

        if (WorkflowStatus.APPROVED.equals(workflowDTO.getStatus()) || WorkflowStatus.REJECTED.equals(workflowDTO.getStatus())) {
        	String status = null;
        	
        	if("APPROVED".equals(workflowDTO.getStatus().toString())) {
        		status = APIConstants.SubscriptionStatus.UNBLOCKED;
        		
        	} else if("REJECTED".equals(workflowDTO.getStatus().toString())) {
        		status = APIConstants.SubscriptionStatus.REJECTED;
        	}
        	
        	if(status != null || status.length() > 0) {
        		ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
                try {
                    apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workflowDTO.getWorkflowReference()),
                            status);
                } catch (APIManagementException e) {
                    log.error("Could not complete subscription creation workflow", e);
                    throw new WorkflowException("Could not complete subscription creation workflow", e);
                }
                
        	} else {
        		log.error("Could not complete subscription creation workflow. Approval status is invalid.");
        	}
        }
		return null;
        
//        if (WorkflowStatus.APPROVED.equals(workflowDTO.getStatus())) {
//            ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
//            try {
//                apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workflowDTO.getWorkflowReference()),
//                        APIConstants.SubscriptionStatus.UNBLOCKED);
//            } catch (APIManagementException e) {
//                log.error("Could not complete subscription creation workflow", e);
//                throw new WorkflowException("Could not complete subscription creation workflow", e);
//            }
//        } 
    }

    private String getTierAttributesString(Map<String, Object> tierAttributesMap) {
        String tierAttributesString = "";
        Iterator tierAttributesIterator = tierAttributesMap.entrySet().iterator();
        while (tierAttributesIterator.hasNext()) {
            Map.Entry tierAttribute = (Map.Entry) tierAttributesIterator.next();
            tierAttributesString = tierAttributesString + "<wor:tierAttribute>"
                    + "<wor:tierAttributeName>$tierAttributeName</wor:tierAttributeName>"
                    + "<wor:tierAttributevalue>$tierAttributevalue</wor:tierAttributevalue>"
                    + "</wor:tierAttribute>";
            tierAttributesString = tierAttributesString.replace("$tierAttributeName", tierAttribute.getKey().toString());
            tierAttributesString = tierAttributesString.replace("$tierAttributevalue", tierAttribute.getValue().toString());
        }

        return tierAttributesString;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
