
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:03:08 UTC)
 */

        
            package com.wso2telco.workflow.notify;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/notify".equals(namespaceURI) &&
                  "AppApprovalStatusSPEmailNotificationRequestType".equals(typeName)){
                   
                            return  com.wso2telco.workflow.notify.AppApprovalStatusSPEmailNotificationRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/notify".equals(namespaceURI) &&
                  "PLUGINAdminAppApprovalEmailNotificationRequestType".equals(typeName)){
                   
                            return  com.wso2telco.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/notify".equals(namespaceURI) &&
                  "SubApprovalStatusSPEmailNotificationRequestType".equals(typeName)){
                   
                            return  com.wso2telco.workflow.notify.SubApprovalStatusSPEmailNotificationRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/notify".equals(namespaceURI) &&
                  "HUBAdminAppApprovalEmailNotificationRequestType".equals(typeName)){
                   
                            return  com.wso2telco.workflow.notify.HUBAdminAppApprovalEmailNotificationRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/notify".equals(namespaceURI) &&
                  "PLUGINAdminSubApprovalEmailNotificationRequestType".equals(typeName)){
                   
                            return  com.wso2telco.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/notify".equals(namespaceURI) &&
                  "HUBAdminSubApprovalEmailNotificationRequestType".equals(typeName)){
                   
                            return  com.wso2telco.workflow.notify.HUBAdminSubApprovalEmailNotificationRequestType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    