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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.event.table.extension.HazelcastBackedEventTable;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.TableDefinition;

import java.util.ArrayList;
import java.util.List;

public class HazelcastBackedTableDeleteTestCase {
    static final Logger log = Logger.getLogger(HazelcastBackedTableDeleteTestCase.class);


    List<Class> classList;

    @Before
    public void init() {
        classList = new ArrayList<Class>();
        classList.add(HazelcastBackedEventTable.class);
    }

    @Test
    public void testQuery1() throws InterruptedException {
        log.info("DeleteFromTableTestCase testQuery1");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseDeleteEventStream (symbol string, price float, volume long) ");

        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long) " + createFromClause("cepEventTable", "symbol"));

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");

        siddhiManager.addQuery("from cseDeleteEventStream " +
                "delete cseEventTable " +
                "    on cseEventTable.symbol=='GOOG';");
        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseDeleteEventStream = siddhiManager.getInputHandler("cseDeleteEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"GOOG", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        cseDeleteEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();

    }


    @Test
    public void testQuery2() throws InterruptedException {
        log.info("DeleteFromTableTestCase test2");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseDeleteEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long)  " + createFromClause("cepEventTable", "symbol"));

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");

        siddhiManager.addQuery("from cseDeleteEventStream " +
                "delete cseEventTable " +
                "    on cseEventTable.symbol!='GOOG';");
        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseDeleteEventStream = siddhiManager.getInputHandler("cseDeleteEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"GOOG", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        cseDeleteEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();

    }

    @Test
    /*
     delete with AND condition. condition uses same parameter twice.
     */
    public void testQuery3() throws InterruptedException {
        log.info("DeleteFromTableTestCase test3");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseDeleteEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long) " + createFromClause("cepEventTable", "symbol"));

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");

        siddhiManager.addQuery("from cseDeleteEventStream " +
                "delete cseEventTable " +
                "    on cseEventTable.symbol != 'WSO2' ;");
        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseDeleteEventStream = siddhiManager.getInputHandler("cseDeleteEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"GOOG", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        cseDeleteEventStream.send(new Object[]{"GOOG", 57.6f, 100l});
        cseDeleteEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();

    }


    @Test
    public void
    testQuery4() throws InterruptedException {
        log.info("test query 4");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseDeleteEventStream (symbol string, price float, volume long) ");

        String table = "define table cseEventTable (symbol string, price float, volume long) from ( 'namespace'='eventTable', 'function'='hazelcast', 'map.name'='cepEventTable','primary.key' = 'symbol')";
        siddhiManager.defineTable(table);

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");

        siddhiManager.addQuery("from cseDeleteEventStream " +
                "delete cseEventTable " +
                "  on cseEventTable.symbol=='GOOG' or cseEventTable.symbol=='WSO2';");
        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseDeleteEventStream = siddhiManager.getInputHandler("cseDeleteEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"GOOG", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        cseDeleteEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();

    }

    @Test
    public void
    testQuery5() throws InterruptedException {
        log.info("test query 5");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseDeleteEventStream (symbol string, price float, volume long) ");

        String table = "define table cseEventTable (symbol string, price float, volume long) from ('namespace'='eventTable', 'function'='hazelcast', 'map.name'='cepEventTable','primary.key' = 'symbol')";
        siddhiManager.defineTable(table);

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");

        siddhiManager.addQuery("from cseDeleteEventStream " +
                "delete cseEventTable " +
                "  ;");
        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseDeleteEventStream = siddhiManager.getInputHandler("cseDeleteEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"GOOG", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        cseDeleteEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();

    }

    @Test
    public void testSingleerDefinition() {
        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        TableDefinition tableDefinition = QueryFactory.createTableDefinition();
        tableDefinition.name("cseEventTable").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.INT).attribute("volume", Attribute.Type.FLOAT).from("namespace", "eventTable").from("function", "hazelcast").from("map.name", "cepEventTable").from("primary.key","symbol");
        siddhiManager.defineTable(tableDefinition);
    }

    private String createFromClause(String tableName, String primaryKey) {
        String query = "from ( 'namespace'='eventTable', 'function'='hazelcast', 'map.name'='" + tableName + "' , 'primary.key' = '"+primaryKey+"'";
        query = query + ")";
        return query;
    }

}
