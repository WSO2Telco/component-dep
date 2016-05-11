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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Tier;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.ApplicationWorkflowDTO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowConstants;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowStatus;

import com.wso2telco.dep.workflow.internal.ServiceReferenceHolder;

import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;

public class AxiataApplicationCreationWSWorkflowExecutor extends WorkflowExecutor {

    private String serviceEndpoint;

    private String username;

    private String password;

    private String contentType;

    private static final Log log = LogFactory.getLog(AxiataApplicationCreationWSWorkflowExecutor.class);

    @Override
    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_AM_APPLICATION_CREATION;
    }

    @Override
    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        if (log.isDebugEnabled()) {
            log.debug("Executing Axiata Application creation Workflow..");
        }
        super.execute(workflowDTO);
        try {
            ServiceClient client = new ServiceClient(ServiceReferenceHolder.getContextService()
                    .getClientConfigContext(),
                    null);
            
            log.debug("client : " + client);

            Options options = new Options();
            options.setAction("http://workflow.application.apimgt.carbon.wso2.org/initiate");
            
            log.debug("serviceEndpoint : " + serviceEndpoint);
            options.setTo(new EndpointReference(serviceEndpoint));

            if (contentType != null) {
                options.setProperty(Constants.Configuration.MESSAGE_TYPE, contentType);
            } else {
                options.setProperty(Constants.Configuration.MESSAGE_TYPE,
                        HTTPConstants.MEDIA_TYPE_APPLICATION_XML);
            }
            
            log.debug("contentType : " + contentType);

            HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();

            log.debug("username : " + username);
            log.debug("password : " + password);
            
            if (username != null && password != null) {
                auth.setUsername(username);
                auth.setPassword(password);
                auth.setPreemptiveAuthentication(true);
                List<String> authSchemes = new ArrayList<String>();
                authSchemes.add(HttpTransportProperties.Authenticator.BASIC);
                auth.setAuthSchemes(authSchemes);
                options.setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,
                        auth);
                options.setManageSession(true);
            }

            client.setOptions(options);

            String payload
                    = "<wor:ApplicationApprovalWorkFlowProcessRequest xmlns:wor=\"http://workflow.application.apimgt.carbon.wso2.org\">\n"
                    + "        <wor:applicationName>$1</wor:applicationName>\n"
                    + "        <wor:applicationId>$2</wor:applicationId>\n"
                    + "        <wor:applicationTier>$3</wor:applicationTier>\n"
                    + "        <wor:applicationCallbackUrl>$4</wor:applicationCallbackUrl>\n"
                    + "        <wor:applicationDescription>$5</wor:applicationDescription>\n"
                    + "        <wor:tenantDomain>$6</wor:tenantDomain>\n"
                    + "        <wor:userName>$7</wor:userName>\n"
                    + "        <wor:workflowExternalRef>$8</wor:workflowExternalRef>\n"
                    + "        <wor:callBackURL>$9</wor:callBackURL>\n"
                    + "        <wor:tiers>$a</wor:tiers>\n"
                    + "      </wor:ApplicationApprovalWorkFlowProcessRequest>";

            // Obtain application details.
            ApplicationWorkflowDTO appWorkFlowDTO = (ApplicationWorkflowDTO) workflowDTO;
            Application application = appWorkFlowDTO.getApplication();
            String callBackURL = appWorkFlowDTO.getCallbackUrl();
            String appID = appWorkFlowDTO.getWorkflowReference();
            
            log.debug("callBackURL : " + callBackURL);
            log.debug("appID : " + appID);

            // Obtain list of tiers.
            APIConsumer consumer = APIManagerFactory.getInstance().getAPIConsumer();
            Set<Tier> tierSet = consumer.getTiers();

            String tiersStr = "";

            for (Iterator iterator = tierSet.iterator(); iterator.hasNext();) {
                Tier tier = (Tier) iterator.next();
                String tierName = tier.getName();
                String tierDisplayName = tier.getDisplayName();
                
                log.debug("tierName : " + tierName);
                log.debug("tierDisplayName : " + tierDisplayName);
                
                String tierAttributesString = "";
                if(tier.getTierAttributes() != null){
                   tierAttributesString = getTierAttributesString(tier.getTierAttributes()); 
                }  
                log.debug("tierAttributesString : " + tierAttributesString);

                tiersStr = tiersStr + "         <wor:tier>\n"
                        + "             <wor:tierName>$b</wor:tierName>\n"
                        + "             <wor:tierDisplayName>$c</wor:tierDisplayName>\n"
                        + "             <wor:tierAttributes>$d</wor:tierAttributes>\n"
                        + "         </wor:tier>\n";

                tiersStr = tiersStr.replace("$b", tierName);
                tiersStr = tiersStr.replace("$c", tierDisplayName);
                tiersStr = tiersStr.replace("$d", tierAttributesString);
            }

            log.debug("application.getName() : " + application.getName());
            log.debug("appID : " + appID);
            log.debug("application.getTier() : " + application.getTier());
            log.debug("application.getCallbackUrl() : " + application.getCallbackUrl());
            log.debug("application.getDescription() : " + application.getDescription());
            log.debug("appWorkFlowDTO.getTenantDomain() : " + appWorkFlowDTO.getTenantDomain());
            log.debug("appWorkFlowDTO.getUserName() : " + appWorkFlowDTO.getUserName());
            log.debug("appWorkFlowDTO.getExternalWorkflowReference() : " + appWorkFlowDTO.getExternalWorkflowReference());
            log.debug("callBackURL : " + callBackURL);
            log.debug("tiersStr : " + tiersStr);
            
            payload = payload.replace("$1", application.getName());
            payload = payload.replace("$2", appID);
            payload = payload.replace("$3", application.getTier());
            payload = payload.replace("$4", application.getCallbackUrl());
            payload = payload.replace("$5", application.getDescription());
            payload = payload.replace("$6", appWorkFlowDTO.getTenantDomain());
            payload = payload.replace("$7", appWorkFlowDTO.getUserName());
            payload = payload.replace("$8", appWorkFlowDTO.getExternalWorkflowReference());
            payload = payload.replace("$9", callBackURL != null ? callBackURL : "?");
            payload = payload.replace("$a", tiersStr);

            log.debug("[END] AxiataApplicationCreationWSWorkflowExecutor.execute ");
            client.fireAndForget(AXIOMUtil.stringToOM(payload));

        } catch (AxisFault axisFault) {
            log.error("Error sending out message", axisFault);
            throw new WorkflowException("Error sending out message", axisFault);
        } catch (XMLStreamException e) {
            log.error("Error converting String to OMElement", e);
            throw new WorkflowException("Error converting String to OMElement", e);
        } catch (APIManagementException e) {
            log.error("Error in obtaining APIConsumer", e);
            throw new WorkflowException("Error in obtaining APIConsumer", e);
        }
		return null;
    }

    
    @Override
    public WorkflowResponse complete(WorkflowDTO workFlowDTO) throws WorkflowException {
        workFlowDTO.setUpdatedTime(System.currentTimeMillis());
        super.complete(workFlowDTO);
        log.info("Application Creation [Complete] Workflow Invoked. Workflow ID : " + workFlowDTO.getExternalWorkflowReference() + "Workflow State : " + workFlowDTO.getStatus());

        if (WorkflowStatus.APPROVED.equals(workFlowDTO.getStatus()) || WorkflowStatus.REJECTED.equals(workFlowDTO.getStatus())) {
            String status = null;
            if ("CREATED".equals(workFlowDTO.getStatus().toString())) {
                status = APIConstants.ApplicationStatus.APPLICATION_CREATED;
            } else if ("REJECTED".equals(workFlowDTO.getStatus().toString())) {
                status = APIConstants.ApplicationStatus.APPLICATION_REJECTED;
            } else if ("APPROVED".equals(workFlowDTO.getStatus().toString())) {
                status = APIConstants.ApplicationStatus.APPLICATION_APPROVED;
            }

            if(status != null || status.length() > 0) {
                ApiMgtDAO dao = new ApiMgtDAO();

                try {
                    dao.updateApplicationStatus(Integer.parseInt(workFlowDTO.getWorkflowReference()),
                            status);
                } catch (APIManagementException e) {
                    String msg = "Error occured when updating the status of the Application creation "
                            + "process";
                    log.error(msg, e);
                    throw new WorkflowException(msg, e);
                }
                
            } else {
        		log.error("Could not complete Application creation workflow. Approval status is invalid.");
        	}
        }
		return null;
    }

    @Override
    public List<WorkflowDTO> getWorkflowDetails(String workflowStatus) throws WorkflowException {
        // TODO Auto-generated method stub
        return null;
    }

    private String getTierAttributesString(Map<String, Object> tierAttributesMap) {
    	log.debug("[START] getTierAttributesString");
    	
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
        log.debug("tierAttributesString : " + tierAttributesString);
        log.debug("[END] getTierAttributesString");

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
