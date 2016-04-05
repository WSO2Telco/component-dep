
/**
 * AxiataWorkflowApprovalServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:02:54 UTC)
 */
        package carbon.wso2.org.axiata.workflow;

        /**
        *  AxiataWorkflowApprovalServiceMessageReceiverInOut message receiver
        */

        public class AxiataWorkflowApprovalServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        AxiataWorkflowApprovalServiceSkeleton skel = (AxiataWorkflowApprovalServiceSkeleton)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){


        

            if("retrieveSubOperatorList".equals(methodName)){
                
                carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse subOperatorRetrievalResponse1 = null;
	                        carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest wrappedParam =
                                                             (carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               subOperatorRetrievalResponse1 =
                                                   
                                                   
                                                         skel.retrieveSubOperatorList(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), subOperatorRetrievalResponse1, false, new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow",
                                                    "retrieveSubOperatorList"));
                                    } else 

            if("retrieveOperatorList".equals(methodName)){
                
                carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse operatorRetrievalResponse3 = null;
	                        carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest wrappedParam =
                                                             (carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               operatorRetrievalResponse3 =
                                                   
                                                   
                                                         skel.retrieveOperatorList(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), operatorRetrievalResponse3, false, new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow",
                                                    "retrieveOperatorList"));
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
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

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    