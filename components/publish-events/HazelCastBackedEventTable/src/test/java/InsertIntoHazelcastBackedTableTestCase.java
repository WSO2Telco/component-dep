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

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.OperationNotSupportedException;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.event.table.extension.HazelcastBackedEventTable;

import java.util.ArrayList;
import java.util.List;

public class 	 {
    static final Logger log = Logger.getLogger(InsertIntoHazelcastBackedTableTestCase.class);

    private int count;
    private boolean eventArrived;
    private List<Class> classList;

    @Before
    public void init() {
        count = 0;
        eventArrived = false;
        classList = new ArrayList<Class>();
        classList.add(HazelcastBackedEventTable.class);
    }

    @Test
    public void testQuery1() throws InterruptedException {
        log.info("InsertIntoTableTestCase test1");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long) " + createFromClause("cepEventTable", "symbol"));

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"IBM", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();
    }

    @Test
    public void testQuery2() throws InterruptedException {
        log.info("InsertIntoTableTestCase test2");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long)  " + createFromClause("cepEventTable", "symbol"));
        siddhiManager.defineTable("define table cseEventTable2 (symbol string, price float, volume long)  " + createFromClause("cepEventTable2", "symbol"));

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");
        siddhiManager.addQuery("from cseEventStream insert into cseEventTable2;");

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"IBM", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();

    }

    @Test
    public void testQuery3() throws InterruptedException {
        log.info("InsertIntoTableTestCase test3");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseEventStream2 (symbol string, price float, volume long) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long)  " + createFromClause("cepEventTable", "symbol"));
        siddhiManager.defineTable("define table cseEventTable2 (symbol string, price float, volume long) " + createFromClause("cepEventTable2", "symbol"));

        siddhiManager.addQuery("from cseEventStream insert into cseEventTable;");
        siddhiManager.addQuery("from cseEventStream2 insert into cseEventTable2;");

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"IBM", 75.6f, 100l});
        cseEventStream.send(new Object[]{"WSO2", 57.6f, 100l});
        Thread.sleep(500);
        siddhiManager.shutdown();

    }

    @Test
    public void testQuery4() throws InterruptedException {
        log.info("InsertIntoTableTestCase test4");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseEventCheckStream (symbol string) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long) " + createFromClause("cepEventTableCheck", "symbol"));

        siddhiManager.addQuery("from cseEventStream " +
                "insert into cseEventTable;");
        String queryReference = siddhiManager.addQuery("from cseEventCheckStream[cseEventTable.symbol==symbol in cseEventTable] " +
                "insert into outStream;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                Assert.assertTrue("WSO2".equals(inEvents[0].getData(0)));
                count++;
                eventArrived = true;
            }

        });

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseEventCheckStream = siddhiManager.getInputHandler("cseEventCheckStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventCheckStream.send(new Object[]{"IBM"});
        cseEventCheckStream.send(new Object[]{"WSO2"});
        Thread.sleep(500);
        Assert.assertEquals(1, count);
        Assert.assertEquals("Event arrived", true, eventArrived);
        siddhiManager.shutdown();
    }

    @Test
    public void testQuery5() throws InterruptedException {
        log.info("InsertIntoTableTestCase test5");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseEventCheckStream (symbol string) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long) " + createFromClause("cepEventTable", "symbol"));

        siddhiManager.addQuery("from cseEventStream " +
                "insert into cseEventTable;");
        String queryReference = siddhiManager.addQuery("from cseEventCheckStream[cseEventTable.symbol==symbol in cseEventTable] " +
                "insert into outStream;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)));
                count++;
                eventArrived = true;
            }

        });

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseEventCheckStream = siddhiManager.getInputHandler("cseEventCheckStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"IBM", 55.6f, 100l});
        cseEventCheckStream.send(new Object[]{"IBM"});
        Thread.sleep(500);
        Assert.assertEquals(1, count);
        Assert.assertEquals("Event arrived", true, eventArrived);
        siddhiManager.shutdown();
    }

    @Test
    public void testQuery6() throws InterruptedException {
        log.info("InsertIntoTableTestCase test6");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseEventCheckStream (symbol string) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long)  " + createFromClause("cepEventTable", "symbol"));

        siddhiManager.addQuery("from cseEventStream " +
                "insert into cseEventTable;");
        String queryReference = siddhiManager.addQuery("from cseEventCheckStream[cseEventTable.symbol==symbol in cseEventTable] " +
                "insert into outStream;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                count++;
                eventArrived = true;
            }

        });

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseEventCheckStream = siddhiManager.getInputHandler("cseEventCheckStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"IBM", 55.6f, 100l});
        cseEventCheckStream.send(new Object[]{"IBM"});
        cseEventCheckStream.send(new Object[]{"WSO2"});
        Thread.sleep(500);
        Assert.assertEquals(2, count);
        Assert.assertEquals("Event arrived", true, eventArrived);
        siddhiManager.shutdown();
    }

    @Test(expected = OperationNotSupportedException.class)
    public void testQuery7() throws InterruptedException {
        log.info("InsertIntoTableTestCase test7");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseEventCheckStream (price float) ");
        siddhiManager.defineTable("define table cseEventTable (symbol string, price float, volume long) " + createFromClause("cepEventTable", "price"));

        siddhiManager.addQuery("from cseEventStream " +
                "insert into cseEventTable;");
        String queryReference = siddhiManager.addQuery("from cseEventCheckStream[cseEventTable.price<price in cseEventTable] " +
                "insert into outStream;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                count++;
                eventArrived = true;
            }

        });

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseEventCheckStream = siddhiManager.getInputHandler("cseEventCheckStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"IBM", 155.6f, 100l});
        cseEventStream.send(new Object[]{"GOOG", 255.6f, 100l});
        cseEventCheckStream.send(new Object[]{200f});
        Thread.sleep(500);
        Assert.assertEquals(1, count);
        Assert.assertEquals("Event arrived", true, eventArrived);
        siddhiManager.shutdown();
    }

    @Test
    public void testQuery8() throws InterruptedException {
        log.info("InsertIntoTableTestCase test8");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.getSiddhiContext().setSiddhiExtensions(classList);

        siddhiManager.defineStream("define stream cseEventStream (symbol string, price float, volume long) ");
        siddhiManager.defineStream("define stream cseEventCheckStream (price float) ");
        String table = "define table cseEventTable (symbol string, price float, volume long) " + createFromClause("cepEventTable", "price");
        siddhiManager.defineTable(table);

        siddhiManager.addQuery("from cseEventStream " +
                "insert into cseEventTable;");
        String queryReference = siddhiManager.addQuery("from cseEventCheckStream[cseEventTable.price==price in cseEventTable] " +
                "insert into outStream;");

        siddhiManager.addCallback(queryReference, new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                count++;
                eventArrived = true;
            }

        });

        InputHandler cseEventStream = siddhiManager.getInputHandler("cseEventStream");
        InputHandler cseEventCheckStream = siddhiManager.getInputHandler("cseEventCheckStream");
        cseEventStream.send(new Object[]{"WSO2", 55.6f, 100l});
        cseEventStream.send(new Object[]{"IBM", 155.6f, 100l});
        cseEventStream.send(new Object[]{"GOOG", 255.6f, 100l});
        cseEventCheckStream.send(new Object[]{55.6f});
        Thread.sleep(1000);
        Assert.assertEquals(1, count);
        Assert.assertEquals("Event arrived", true, eventArrived);
        siddhiManager.shutdown();
    }

    private String createFromClause(String tableName, String primaryKey) {
        String query = "from ('namespace'='eventTable', 'function'='hazelcast', 'map.name'='" + tableName + "' , 'primary.key' = '"+primaryKey+"'";
        query = query + ")";
        return query;
    }



}
