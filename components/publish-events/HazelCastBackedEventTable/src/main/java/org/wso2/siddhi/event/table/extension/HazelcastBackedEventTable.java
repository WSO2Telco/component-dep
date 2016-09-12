/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.event.table.extension;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import com.hazelcast.query.impl.QueryException;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.exception.OperationNotSupportedException;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.table.EventTable;
import org.wso2.siddhi.core.table.predicate.PredicateTreeNode;
import org.wso2.siddhi.core.table.predicate.sql.SQLPredicateBuilder;
import org.wso2.siddhi.query.api.definition.TableDefinition;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;
import org.wso2.siddhi.query.api.query.QueryEventSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SiddhiExtension(namespace = "eventTable", function = "hazelcast")


public class HazelcastBackedEventTable implements EventTable {
    static final String PARAM_MAP_NAME = "map.name";
    static final String PARAM_PRIMARY_KEY = "primary.key";
    static final String PARAM_HAZELCAST_CLUSTER_DOMAIN = "cluster.domain";
    static final String PARAM_HAZELCAST_CLUSTER_ADDRESSES = "cluster.addresses";

    private HazelcastInstance hazelcastInstance;
    private int primaryAttributePosition;
    private String primaryAttribute;
    private TableDefinition tableDefinition;
    private QueryEventSource queryEventSource;
//    private IMap<String, Object[]> map;
    private HazelcastInstance hazelcastClient;
    private String mapName;


    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void init(TableDefinition tableDefinition, SiddhiContext siddhiContext) {
//        hazelcastInstance = siddhiContext.getHazelcastInstance();
//        if(hazelcastInstance==null){
//            Config hazelcastConf = new Config();
//            hazelcastConf.setProperty("hazelcast.logging.type", "log4j");
//            hazelcastConf.getGroupConfig().setName(siddhiContext.getQueryPlanIdentifier());
//            hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConf);
//            siddhiContext.setHazelcastInstance(hazelcastInstance);
//        }
        this.tableDefinition = tableDefinition;
        mapName = tableDefinition.getExternalTable().getParameter(PARAM_MAP_NAME);
        this.queryEventSource = new QueryEventSource(mapName, tableDefinition.getTableId(), tableDefinition, null, null, null);
        primaryAttribute =  tableDefinition.getExternalTable().getParameter(PARAM_PRIMARY_KEY);
        primaryAttributePosition = tableDefinition.getAttributePosition(primaryAttribute);

        String clusterDomain = tableDefinition.getExternalTable().getParameter(PARAM_HAZELCAST_CLUSTER_DOMAIN);
        String[] clusterAddresses = tableDefinition.getExternalTable().getParameter(PARAM_HAZELCAST_CLUSTER_ADDRESSES).split("\\s*,\\s*");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName(clusterDomain);
        clientConfig.addAddress(clusterAddresses);
        hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
//        map = hazelcastInstance.getMap(mapName);
    }

    public void add(StreamEvent streamEvent) {
//        ClientConfig clientConfig = new ClientConfig();
//        clientConfig.getGroupConfig().setName("wso2.carbon.domain");
//        clientConfig.addAddress("192.168.1.11:4000");
//        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap<String, Object[]> map = hazelcastClient.getMap(mapName);

        if(streamEvent instanceof AtomicEvent){
            Event event = (Event) streamEvent;
            map.put(event.getData(primaryAttributePosition).toString(), event.getData());
        } else {
            ListEvent listEvent = (ListEvent) streamEvent;
            for (int i = 0, size = listEvent.getActiveEvents(); i < size; i++) {
                Event event = listEvent.getEvent(i);
                map.put(event.getData(primaryAttributePosition).toString(), event.getData());
            }
        }

    }

    public void delete(StreamEvent streamEvent, ConditionExecutor conditionExecutor) {
        IMap<String, Object[]> map = hazelcastClient.getMap(mapName);
        if(conditionExecutor == null){
            map.clear();
        } else {
            if (streamEvent instanceof AtomicEvent) {
                PredicateTreeNode predicate = conditionExecutor.constructPredicate((Event) streamEvent, tableDefinition, new SQLPredicateBuilder());

                for(String key:getKeySet(map, getPredicate(predicate))){
                    map.remove(key);
                }
            } else {
                ListEvent listEvent = (ListEvent) streamEvent;
                for (int i = 0, size = listEvent.getActiveEvents(); i < size; i++) {
                    PredicateTreeNode predicate = conditionExecutor.constructPredicate(listEvent.getEvent(i), tableDefinition, new SQLPredicateBuilder());
                    for(String key:getKeySet(map, getPredicate(predicate))){
                        map.remove(key);
                    }
                }
            }

        }
    }

    private Set<String> getKeySet(IMap<String, Object[]> map, SqlPredicate sqlPredicate){
        try{
            return map.keySet(sqlPredicate);
        } catch (com.hazelcast.query.impl.QueryException e){
            if(e.getCause().getCause() instanceof IllegalArgumentException){
                throw new OperationNotSupportedException("hazelcastBackedEventTable on condition should only include primary attribute :"+primaryAttribute,e);
            }
            throw new QueryException(e);
        }
    }

    private SqlPredicate getPredicate(PredicateTreeNode predicate){
        String statement = predicate.buildPredicateString();
        statement = statement.replace("<>","!=");
        if(statement.contains("<=") || statement.contains("<") || statement.contains(">=") || statement.contains(">")){
            throw new OperationNotSupportedException("hazelcastBackedEventTable only support equal(=) and not equal(!=) conditions");
        }
        List paramList = new ArrayList();
        predicate.populateParameters(paramList);
        statement = statement.replaceAll(primaryAttribute,"__key");
        for (Object aParamList : paramList) {
            statement = statement.replaceFirst("\\?", aParamList.toString());
        }
        return new SqlPredicate(statement);
    }


    public void update(StreamEvent streamEvent, ConditionExecutor conditionExecutor, int[] attributeUpdateMappingPosition) {
        throw new OperationNotSupportedException("hazelcastBackedEventTable "+tableDefinition.getTableId()+ " does not support UPDATE");
    }


    public boolean contains(AtomicEvent atomicEvent, ConditionExecutor conditionExecutor) {
        IMap<String, Object[]> map = hazelcastClient.getMap(mapName);
        //when giving compare conditions attribute of the hazelcast backed table should be  given first. e.g:- [cseEventTable.symbol==symbol in cseEventTable]
        PredicateTreeNode predicate = conditionExecutor.constructPredicate(atomicEvent, tableDefinition, new SQLPredicateBuilder());
        return getKeySet(map, getPredicate(predicate)).size()>0;
    }

    public QueryEventSource getQueryEventSource() {
        return queryEventSource;
    }

    public Iterator<StreamEvent> iterator() {
        return iterator(null);
    }

    public Iterator<StreamEvent> iterator(String SQLPredicate) {
        IMap<String, Object[]> map = hazelcastClient.getMap(mapName);
        SQLPredicate = SQLPredicate.replace(primaryAttribute,"__key");
        ArrayList<StreamEvent> eventList = new ArrayList<StreamEvent>();
        long timestamp = System.currentTimeMillis();
        for(String key:getKeySet(map, new SqlPredicate(SQLPredicate))){
            Event event = new InEvent(tableDefinition.getExternalTable().getParameter(PARAM_MAP_NAME), timestamp, map.get(key));
            eventList.add(event);
        }
        return eventList.iterator();
    }
}
