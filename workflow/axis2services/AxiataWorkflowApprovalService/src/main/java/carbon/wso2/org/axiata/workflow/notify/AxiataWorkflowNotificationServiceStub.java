
/**
 * AxiataWorkflowNotificationServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:02:54 UTC)
 */
        package carbon.wso2.org.axiata.workflow.notify;

        

        /*
        *  AxiataWorkflowNotificationServiceStub java implementation
        */

        
        public class AxiataWorkflowNotificationServiceStub extends org.apache.axis2.client.Stub
        implements AxiataWorkflowNotificationService{
        protected org.apache.axis2.description.AxisOperation[] _operations;

        //hashmaps to keep the fault mapping
        private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
        private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
        private java.util.HashMap faultMessageMap = new java.util.HashMap();

        private static int counter = 0;

        private static synchronized java.lang.String getUniqueSuffix(){
            // reset the counter if it is greater than 99999
            if (counter > 99999){
                counter = 0;
            }
            counter = counter + 1; 
            return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
        }

    
    private void populateAxisService() throws org.apache.axis2.AxisFault {

     //creating the Service with a unique name
     _service = new org.apache.axis2.description.AxisService("AxiataWorkflowNotificationService" + getUniqueSuffix());
     addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[6];
        
                    __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify", "sendSubApprovalStatusSPEmailNotification"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[0]=__operation;
            
        
                    __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify", "sendPLUGINAdminAppApprovalEmailNotification"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[1]=__operation;
            
        
                    __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify", "sendPLUGINAdminSubApprovalEmailNotification"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[2]=__operation;
            
        
                    __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify", "sendHUBAdminAppApprovalEmailNotification"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[3]=__operation;
            
        
                    __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify", "sendAppApprovalStatusSPEmailNotification"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[4]=__operation;
            
        
                    __operation = new org.apache.axis2.description.OutOnlyAxisOperation();
                

            __operation.setName(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify", "sendHUBAdminSubApprovalEmailNotification"));
	    _service.addOperation(__operation);
	    

	    
	    
            _operations[5]=__operation;
            
        
        }

    //populates the faults
    private void populateFaults(){
         


    }

    /**
      *Constructor that takes in a configContext
      */

    public AxiataWorkflowNotificationServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
       java.lang.String targetEndpoint)
       throws org.apache.axis2.AxisFault {
         this(configurationContext,targetEndpoint,false);
   }


   /**
     * Constructor that takes in a configContext  and useseperate listner
     */
   public AxiataWorkflowNotificationServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
        java.lang.String targetEndpoint, boolean useSeparateListener)
        throws org.apache.axis2.AxisFault {
         //To populate AxisService
         populateAxisService();
         populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext,_service);
        
	
        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(
                targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
        
    
    }

    /**
     * Default Constructor
     */
    public AxiataWorkflowNotificationServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {
        
                    this(configurationContext,"http://localhost:8280/services/AxiataWorkflowNotificationService" );
                
    }

    /**
     * Default Constructor
     */
    public AxiataWorkflowNotificationServiceStub() throws org.apache.axis2.AxisFault {
        
                    this("http://localhost:8280/services/AxiataWorkflowNotificationService" );
                
    }

    /**
     * Constructor taking the target endpoint
     */
    public AxiataWorkflowNotificationServiceStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null,targetEndpoint);
    }



        
                 
                /**
                  * Auto generated method signature
                  * 
                  */
                public void  sendSubApprovalStatusSPEmailNotification(
                 carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest subApprovalStatusSPEmailNotificationRequest42

                ) throws java.rmi.RemoteException
                
                
                {
                org.apache.axis2.context.MessageContext _messageContext = null;

                
                org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
                _operationClient.getOptions().setAction("http://org.wso2.carbon/subscriptions/axiata/workflow/notify/sendSubApprovalStatusSPEmailNotification");
                _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

                
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              
                org.apache.axiom.soap.SOAPEnvelope env = null;
                 _messageContext = new org.apache.axis2.context.MessageContext();

                
                                                    //Style is Doc.
                                                    
                                                                    
                                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                                    subApprovalStatusSPEmailNotificationRequest42,
                                                                    optimizeContent(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendSubApprovalStatusSPEmailNotification")),new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendSubApprovalStatusSPEmailNotification"));
                                                                

              //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
                // create message context with that soap envelope

            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

             _operationClient.execute(true);

           
              if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
              }
           
             return;
           }
            
                 
                /**
                  * Auto generated method signature
                  * 
                  */
                public void  sendPLUGINAdminAppApprovalEmailNotification(
                 carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest pLUGINAdminAppApprovalEmailNotificationRequest43

                ) throws java.rmi.RemoteException
                
                
                {
                org.apache.axis2.context.MessageContext _messageContext = null;

                
                org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
                _operationClient.getOptions().setAction("http://org.wso2.carbon/applications/axiata/workflow/notify/sendPLUGINAdminAppApprovalEmailNotification");
                _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

                
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              
                org.apache.axiom.soap.SOAPEnvelope env = null;
                 _messageContext = new org.apache.axis2.context.MessageContext();

                
                                                    //Style is Doc.
                                                    
                                                                    
                                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                                    pLUGINAdminAppApprovalEmailNotificationRequest43,
                                                                    optimizeContent(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendPLUGINAdminAppApprovalEmailNotification")),new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendPLUGINAdminAppApprovalEmailNotification"));
                                                                

              //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
                // create message context with that soap envelope

            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

             _operationClient.execute(true);

           
              if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
              }
           
             return;
           }
            
                 
                /**
                  * Auto generated method signature
                  * 
                  */
                public void  sendPLUGINAdminSubApprovalEmailNotification(
                 carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest pLUGINAdminSubApprovalEmailNotificationRequest44

                ) throws java.rmi.RemoteException
                
                
                {
                org.apache.axis2.context.MessageContext _messageContext = null;

                
                org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
                _operationClient.getOptions().setAction("http://org.wso2.carbon/subscriptions/axiata/workflow/notify/sendPLUGINAdminSubApprovalEmailNotification");
                _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

                
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              
                org.apache.axiom.soap.SOAPEnvelope env = null;
                 _messageContext = new org.apache.axis2.context.MessageContext();

                
                                                    //Style is Doc.
                                                    
                                                                    
                                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                                    pLUGINAdminSubApprovalEmailNotificationRequest44,
                                                                    optimizeContent(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendPLUGINAdminSubApprovalEmailNotification")),new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendPLUGINAdminSubApprovalEmailNotification"));
                                                                

              //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
                // create message context with that soap envelope

            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

             _operationClient.execute(true);

           
              if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
              }
           
             return;
           }
            
                 
                /**
                  * Auto generated method signature
                  * 
                  */
                public void  sendHUBAdminAppApprovalEmailNotification(
                 carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest hUBAdminAppApprovalEmailNotificationRequest45

                ) throws java.rmi.RemoteException
                
                
                {
                org.apache.axis2.context.MessageContext _messageContext = null;

                
                org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
                _operationClient.getOptions().setAction("http://org.wso2.carbon/applications/axiata/workflow/notify/sendHUBAdminAppApprovalEmailNotification");
                _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

                
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              
                org.apache.axiom.soap.SOAPEnvelope env = null;
                 _messageContext = new org.apache.axis2.context.MessageContext();

                
                                                    //Style is Doc.
                                                    
                                                                    
                                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                                    hUBAdminAppApprovalEmailNotificationRequest45,
                                                                    optimizeContent(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendHUBAdminAppApprovalEmailNotification")),new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendHUBAdminAppApprovalEmailNotification"));
                                                                

              //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
                // create message context with that soap envelope

            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

             _operationClient.execute(true);

           
              if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
              }
           
             return;
           }
            
                 
                /**
                  * Auto generated method signature
                  * 
                  */
                public void  sendAppApprovalStatusSPEmailNotification(
                 carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest appApprovalStatusSPEmailNotificationRequest46

                ) throws java.rmi.RemoteException
                
                
                {
                org.apache.axis2.context.MessageContext _messageContext = null;

                
                org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[4].getName());
                _operationClient.getOptions().setAction("http://org.wso2.carbon/applications/axiata/workflow/notify/sendAppApprovalStatusSPEmailNotification");
                _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

                
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              
                org.apache.axiom.soap.SOAPEnvelope env = null;
                 _messageContext = new org.apache.axis2.context.MessageContext();

                
                                                    //Style is Doc.
                                                    
                                                                    
                                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                                    appApprovalStatusSPEmailNotificationRequest46,
                                                                    optimizeContent(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendAppApprovalStatusSPEmailNotification")),new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendAppApprovalStatusSPEmailNotification"));
                                                                

              //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
                // create message context with that soap envelope

            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

             _operationClient.execute(true);

           
              if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
              }
           
             return;
           }
            
                 
                /**
                  * Auto generated method signature
                  * 
                  */
                public void  sendHUBAdminSubApprovalEmailNotification(
                 carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest hUBAdminSubApprovalEmailNotificationRequest47

                ) throws java.rmi.RemoteException
                
                
                {
                org.apache.axis2.context.MessageContext _messageContext = null;

                
                org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[5].getName());
                _operationClient.getOptions().setAction("http://org.wso2.carbon/subscriptions/axiata/workflow/notify/sendHUBAdminSubApprovalEmailNotification");
                _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

                
              
                  addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");
              
                org.apache.axiom.soap.SOAPEnvelope env = null;
                 _messageContext = new org.apache.axis2.context.MessageContext();

                
                                                    //Style is Doc.
                                                    
                                                                    
                                                                    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                                                                    hUBAdminSubApprovalEmailNotificationRequest47,
                                                                    optimizeContent(new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendHUBAdminSubApprovalEmailNotification")),new javax.xml.namespace.QName("http://org.wso2.carbon/axiata/workflow/notify",
                                                                    "sendHUBAdminSubApprovalEmailNotification"));
                                                                

              //adding SOAP soap_headers
         _serviceClient.addHeadersToEnvelope(env);
                // create message context with that soap envelope

            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

             _operationClient.execute(true);

           
              if (_messageContext.getTransportOut() != null) {
                      _messageContext.getTransportOut().getSender().cleanup(_messageContext);
              }
           
             return;
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

    
    
    private javax.xml.namespace.QName[] opNameArray = null;
    private boolean optimizeContent(javax.xml.namespace.QName opName) {
        

        if (opNameArray == null) {
            return false;
        }
        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;   
            }
        }
        return false;
    }
     //http://localhost:8280/services/AxiataWorkflowNotificationService
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
        
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             
                                    
                                        private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                                        throws org.apache.axis2.AxisFault{

                                             
                                                    try{

                                                            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                                                            emptyEnvelope.getBody().addChild(param.getOMElement(carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest.MY_QNAME,factory));
                                                            return emptyEnvelope;
                                                        } catch(org.apache.axis2.databinding.ADBException e){
                                                            throw org.apache.axis2.AxisFault.makeFault(e);
                                                        }
                                                

                                        }
                                
                             
                             /* methods to provide back word compatibility */

                             


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



    
   }
   