

/**
 * AxiataWorkflowNotificationServiceMessageReceiverInOnly.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:02:54 UTC)
 */
        package carbon.wso2.org.axiata.workflow.notify;

        /**
        *  AxiataWorkflowNotificationServiceMessageReceiverInOnly message receiver
        */

        public class AxiataWorkflowNotificationServiceMessageReceiverInOnly extends org.apache.axis2.receivers.AbstractInMessageReceiver{

        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext inMessage) throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(inMessage);

        AxiataWorkflowNotificationServiceSkeleton skel = (AxiataWorkflowNotificationServiceSkeleton)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = inMessage.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){

        
            if("sendSubApprovalStatusSPEmailNotification".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest wrappedParam = (carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.sendSubApprovalStatusSPEmailNotification(wrappedParam);
                                                } else 
            if("sendPLUGINAdminAppApprovalEmailNotification".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest wrappedParam = (carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.sendPLUGINAdminAppApprovalEmailNotification(wrappedParam);
                                                } else 
            if("sendPLUGINAdminSubApprovalEmailNotification".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest wrappedParam = (carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.sendPLUGINAdminSubApprovalEmailNotification(wrappedParam);
                                                } else 
            if("sendHUBAdminAppApprovalEmailNotification".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest wrappedParam = (carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.sendHUBAdminAppApprovalEmailNotification(wrappedParam);
                                                } else 
            if("sendAppApprovalStatusSPEmailNotification".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest wrappedParam = (carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.sendAppApprovalStatusSPEmailNotification(wrappedParam);
                                                } else 
            if("sendHUBAdminSubApprovalEmailNotification".equals(methodName)){
            
            carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest wrappedParam = (carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest)fromOM(
                                                        inMessage.getEnvelope().getBody().getFirstElement(),
                                                        carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest.class,
                                                        getEnvelopeNamespaces(inMessage.getEnvelope()));
                                            
                                                     skel.sendHUBAdminSubApprovalEmailNotification(wrappedParam);
                                                
                } else {
                  throw new java.lang.RuntimeException("method not found");
                }
            

        }
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }


        
        //
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

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
        
                if (carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest.class.equals(type)){
                
                           return carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

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

    