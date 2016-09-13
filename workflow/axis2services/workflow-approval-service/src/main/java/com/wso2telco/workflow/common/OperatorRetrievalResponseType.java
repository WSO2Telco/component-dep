
/**
 * OperatorRetrievalResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:03:08 UTC)
 */

            
                package com.wso2telco.workflow.common;


import javax.xml.bind.annotation.XmlRootElement;

/**
            *  OperatorRetrievalResponseType bean class
            */
            @SuppressWarnings({"unchecked","unused"})

            @XmlRootElement(name = "OperatorRetrievalResponse")
        public  class OperatorRetrievalResponseType
        {
        /* This type was generated from the piece of schema that had
                name = OperatorRetrievalResponseType
                Namespace URI = http://org.wso2.carbon/axiata/workflow/common
                Namespace Prefix = ns1
                */
            

                        /**
                        * field for Operator
                        * This was an Array!
                        */

                        
                                    protected com.wso2telco.workflow.common.OperatorType[] localOperator ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localOperatorTracker = false ;

                           public boolean isOperatorSpecified(){
                               return localOperatorTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return carbon.wso2.org.axiata.workflow.common.OperatorType[]
                           */
                           public  com.wso2telco.workflow.common.OperatorType[] getOperator(){
                               return localOperator;
                           }

                             /**
                              * Auto generated setter method
                              * @param param Operator
                              */
                              public void setOperator(com.wso2telco.workflow.common.OperatorType[] param){

                                   validateOperator(param);

                               localOperatorTracker = param != null;

                                      this.localOperator=param;
                              }

                              /**
                               * validate the array for Operator
                               */
                              protected void validateOperator(com.wso2telco.workflow.common.OperatorType[] param){

                              }

                             /**
                             * Auto generated add method for the array for convenience
                             * @param param carbon.wso2.org.axiata.workflow.common.OperatorType
                             */
                             public void addOperator(com.wso2telco.workflow.common.OperatorType param){
                                   if (localOperator == null){
                                   localOperator = new com.wso2telco.workflow.common.OperatorType[]{};
                                   }

                            
                                 //update the setting tracker
                                localOperatorTracker = true;
                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localOperator);
                               list.add(param);
                               this.localOperator =
                             (com.wso2telco.workflow.common.OperatorType[])list.toArray(
                            new com.wso2telco.workflow.common.OperatorType[list.size()]);

                             }
                             
     



        }
           
    