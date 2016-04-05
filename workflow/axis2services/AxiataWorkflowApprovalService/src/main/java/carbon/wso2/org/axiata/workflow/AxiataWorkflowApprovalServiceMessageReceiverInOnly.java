

/**
 * AxiataWorkflowApprovalServiceMessageReceiverInOnly.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:02:54 UTC)
 */
        package carbon.wso2.org.axiata.workflow;

        /**
        *  AxiataWorkflowApprovalServiceMessageReceiverInOnly message receiver
        */

        public class AxiataWorkflowApprovalServiceMessageReceiverInOnly extends org.apache.axis2.receivers.AbstractInMessageReceiver{

        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext inMessage) throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(inMessage);

        AxiataWorkflowApprovalServiceSkeleton skel = (AxiataWorkflowApprovalServiceSkeleton)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = inMessage.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){

        
            if("insertAppApprovalAuditRecord".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest wrappedParam = (carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.insertAppApprovalAuditRecord(wrappedParam);
                                                } else 
            if("updateDBAppHubApproval".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest wrappedParam = (carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.updateDBAppHubApproval(wrappedParam);
                                                } else 
            if("updateDBSubOpApproval".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest wrappedParam = (carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.updateDBSubOpApproval(wrappedParam);
                                                } else 
            if("insertValidatorForSubscription".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest wrappedParam = (carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.insertValidatorForSubscription(wrappedParam);
                                                } else 
            if("updateDBAppOpApproval".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest wrappedParam = (carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.updateDBAppOpApproval(wrappedParam);
                                                } else 
            if("updateDBSubHubApproval".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest wrappedParam = (carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.updateDBSubHubApproval(wrappedParam);
                                                } else 
            if("insertSubApprovalAuditRecord".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest wrappedParam = (carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.insertSubApprovalAuditRecord(wrappedParam);
                                                
                } else {
                  throw new java.lang.RuntimeException("method not found");
                }
            

        }
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }


        
        //
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse wrapretrieveSubOperatorList(){
                                carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse wrappedElement = new carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse wrapretrieveOperatorList(){
                                carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse wrappedElement = new carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    



        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }



        }//end of class

    