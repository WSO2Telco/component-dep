
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:03:08 UTC)
 */

        
            package carbon.wso2.org.axiata.workflow.common;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/subscription".equals(namespaceURI) &&
                  "SubOperatorRetrievalResponseType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/common".equals(namespaceURI) &&
                  "OperatorType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.common.OperatorType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/subscription".equals(namespaceURI) &&
                  "SubHUBApprovalDBUpdateRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/common".equals(namespaceURI) &&
                  "OperatorRetrievalRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/application".equals(namespaceURI) &&
                  "AppHUBApprovalDBUpdateRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/application".equals(namespaceURI) &&
                  "AppOpApprovalDBUpdateRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/audit".equals(namespaceURI) &&
                  "AppApprovalAuditRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/audit".equals(namespaceURI) &&
                  "SubApprovalAuditRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/subscription".equals(namespaceURI) &&
                  "SubOperatorRetrievalRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/common".equals(namespaceURI) &&
                  "OperatorRetrievalResponseType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/subscription".equals(namespaceURI) &&
                  "HUBApprovalSubValidatorRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://org.wso2.carbon/axiata/workflow/subscription".equals(namespaceURI) &&
                  "SubOpApprovalDBUpdateRequestType".equals(typeName)){
                   
                            return  carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequestType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    