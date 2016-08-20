/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
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

package com.wso2telco.dep.mediator.unmarshaler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.CarbonUtils;

import com.wso2telco.dep.mediator.entity.cep.Application;
import com.wso2telco.dep.mediator.entity.cep.ConsumerSecretWrapperDTO;
import com.wso2telco.dep.mediator.entity.cep.Group;
import com.wso2telco.dep.mediator.entity.cep.GroupList;
import com.wso2telco.dep.mediator.entity.cep.ServiceProvider;

public class GroupEventUnmarshaller {

    Log log = LogFactory.getLog(GroupEventUnmarshaller.class);
    String configPath ;
    File file ;
    JAXBContext jaxbContext;
    Unmarshaller jaxbUnmarshaller;
    private static GroupEventUnmarshaller instance;
    private Map<String , Set<GroupDTO>> consumerKeyVsGroup = new HashMap<String, Set<GroupDTO>>() ;
   private Map<String ,  Set<ServiceProviderDTO>> consumerKeyVsSp = new HashMap<String,  Set<ServiceProviderDTO>>();



    public static GroupEventUnmarshaller getInstance(){
        return instance;
    }

    private GroupEventUnmarshaller() throws JAXBException{
        init();
        unmarshall();
    }
    public  static void startGroupEventUnmarshaller() throws JAXBException{
        if(instance==null){
           instance = new GroupEventUnmarshaller();
        }

    }

private  void init() throws JAXBException {
   configPath =  CarbonUtils.getCarbonConfigDirPath() + File.separator + "mifeGroupEventSpendLimit.xml";
    file = new File(configPath);
    jaxbContext = JAXBContext.newInstance(GroupList.class);
  jaxbUnmarshaller = jaxbContext.createUnmarshaller();

}
    private    void  unmarshall() throws JAXBException{

       GroupList groupList = (GroupList) jaxbUnmarshaller.unmarshal(file);

        for(Iterator iterator = groupList.getGroupList().iterator(); iterator.hasNext();){
            Group group = (Group) iterator.next();
            GroupDTO gpDTO = new GroupDTO();
            gpDTO.setDayAmount(group.getDayAmount());
           gpDTO.setGroupName(group.getGroupName());
            gpDTO.setMonthAmount(group.getMonthAmount());
            gpDTO.setOperator(group.getOperator());
            for(ServiceProvider sp : group.getServiceProviderList()){
                ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO();
                serviceProviderDTO.setSpName(sp.getSpName());


                for (Application app : sp.getApplicationList()){
                    if(consumerKeyVsGroup.containsKey(app.getConsumerKey())){
                       consumerKeyVsGroup.get(app.getConsumerKey()).add(gpDTO);
                    }else{
                        Set<GroupDTO> grpstack =new HashSet<GroupDTO>();
                        grpstack.add(gpDTO);
                        consumerKeyVsGroup.put(app.getConsumerKey(),grpstack);
                    }

                    if(consumerKeyVsSp.containsKey(app.getConsumerKey())){
                    consumerKeyVsSp.get(app.getConsumerKey()).add(serviceProviderDTO);
                    }else{
                       Set<ServiceProviderDTO> spStack =new HashSet<ServiceProviderDTO>();
                        spStack.add(serviceProviderDTO);
                        consumerKeyVsSp.put(app.getConsumerKey(),spStack);
                    }


                }
                gpDTO.getServiceProviderList().add(serviceProviderDTO);
            }

        }
    }

public ConsumerSecretWrapperDTO getGroupEventDetailDTO(final String consumerKey) throws Exception{
   if(consumerKey==null|| consumerKey.trim().length()<=0){
        throw new Exception("Invalid consumerKey");
    }
    ConsumerSecretWrapperDTO dto = new ConsumerSecretWrapperDTO();

    dto.setConsumerKey(consumerKey.trim());
    if(consumerKeyVsGroup.get(consumerKey.trim()) != null){
        dto.setConsumerKeyVsSp(new ArrayList<ServiceProviderDTO>(consumerKeyVsSp.get(consumerKey.trim() )));
    }
    if( consumerKeyVsGroup.get(consumerKey.trim())!=null){
        dto.setConsumerKeyVsGroup(new ArrayList<GroupDTO>( consumerKeyVsGroup.get(consumerKey.trim()) ));
    }
    return  dto;
}

}
