/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.exception.DifferentDefinitionAlreadyExistException;
import org.wso2.siddhi.core.exception.QueryCreationException;
import org.wso2.siddhi.event.table.extension.HazelcastBackedEventTable;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.TableDefinition;

import java.util.ArrayList;
import java.util.List;

public class DefineHazelcastBackedTableTestCase {
    List<Class> classList;
    
    @Before
    public void init() {
        classList = new ArrayList<Class>();
        classList.add(HazelcastBackedEventTable.class);
     }


    @Test
    public void testSingleDefinition() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        TableDefinition tableDefinition = QueryFactory.createTableDefinition();
        tableDefinition.name("cseEventTable").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.INT).attribute("volume", Attribute.Type.FLOAT);
        tableDefinition.from("namespace", "eventTable").from("function", "hazelcast").from("map.name", "testTable").from("primary.key","symbol");
        siddhiManager.defineTable(tableDefinition);
    }

    @Test
    public void testSingleDefinitionQuery() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineTable("define table cseEventTable(symbol string, price int, volume float) from ( 'namespace'='eventTable', 'function'='hazelcast', 'map.name'='cepEventTable','primary.key' = 'symbol')");
    }

    @Test(expected = DifferentDefinitionAlreadyExistException.class)
    public void testAddingTwoSameDefinition1() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineTable("define table cseEventTable(symbol string, price int, volume float) from ('namespace'='eventTable', 'function'='hazelcast','map.name'='cepEventTable0','primary.key' = 'symbol')");
        siddhiManager.defineTable("define table cseEventTable(symbols string, price int, volume float) from ('namespace'='eventTable', 'function'='hazelcast','map.name'='cepEventTable0','primary.key' = 'symbol')");
    }

    @Test(expected = DifferentDefinitionAlreadyExistException.class)
    public void testAddingTwoSameDefinition2() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineTable("define table cseEventTable(symbol string, volume float) from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol', 'map.name'='cepEventTableDiff')");
        siddhiManager.defineTable("define table cseEventTable(symbols string, price int, volume float) from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol', 'map.name'='cepEventTableDiff')");
    }

    @Test
    public void testAddingTwoSameDefinition3() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineTable("define table cseEventTable(symbol string, price int, volume float) ");
        siddhiManager.defineTable("define table cseEventTable(symbol string, price int, volume float) ");
    }

    @Test(expected = DifferentDefinitionAlreadyExistException.class)
    public void testAddingTwoSameDefinition4() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineStream("define stream cseEventTable(symbol string, price int, volume float) ");
        siddhiManager.defineTable("define table cseEventTable(symbol string, price int, volume float) from ('namespace'='eventTable', 'function'='hazelcast', 'map.name'='cepEventTableTwoDiff') ");
    }

    @Test(expected = DifferentDefinitionAlreadyExistException.class)
    public void testAddingTwoSameDefinition5() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineTable("define table cseEventTable(symbol string, price int, volume float)  from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol', 'map.name'='cepEventTableTwoDiff')");
        siddhiManager.defineStream("define stream cseEventTable(symbol string, price int, volume float)");
    }

    @Test(expected = DifferentDefinitionAlreadyExistException.class)
    public void testOutputDefinition() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.addQuery("from cseEventStream " +
                "select * " +
                "insert into OutputStream ;");


        siddhiManager.defineTable("define table OutputStream (symbol string, price float, volume long) from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol','map.name'='cepEventTable1')");
    }

    @Test
    public void testOutputDefinition2() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineTable("define table OutputStream (symbol string, price float, volume long) from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol', 'map.name'='cepEventTable2')");
        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.addQuery("from cseEventStream " +
                "select * " +
                "insert into OutputStream ;");

    }

    @Test(expected = QueryCreationException.class)
    public void testOutputDefinition3() {
        String ts = Float.toString(Float.MIN_VALUE);
        System.out.println(ts.length());
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineTable("define table OutputStream (symbol string, price float, volume long) from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol', 'map.name'='cepEventTable') ");
        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long, time long) ");
        siddhiManager.addQuery("from cseEventStream " +
                "select * " +
                "insert into OutputStream ;");
    }

    @Test(expected = QueryCreationException.class)
    public void testOutputDefinition4() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineTable("define table OutputStream (symbol string, price float, volume long) from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol', 'map.name'='cepEventTable4') ");
        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume int) ");
        siddhiManager.addQuery("from cseEventStream " +
                "select * " +
                "insert into OutputStream ;");

    }

    @Test
    public void testOutputDefinition5() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);
        siddhiManager.defineTable("define table OutputStream (symbol string, price float, volume long) from ('namespace'='eventTable', 'function'='hazelcast', 'primary.key' = 'symbol', 'map.name'='cepEventTable5') ");
        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, test long) ");
        siddhiManager.addQuery("from cseEventStream " +
                "select * " +
                "insert into OutputStream ;");

    }

    @Test
    public void testSingleerDefinition() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        TableDefinition tableDefinition = QueryFactory.createTableDefinition();
        tableDefinition.name("cseEventTable").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.INT).attribute("volume", Attribute.Type.FLOAT).from("source", "hazelcast");
        tableDefinition.from("namespace", "eventTable").from("function", "hazelcast").from("primary.key","symbol").from("map.name", "testEventTable");

        siddhiManager.defineTable(tableDefinition);
    }


}
