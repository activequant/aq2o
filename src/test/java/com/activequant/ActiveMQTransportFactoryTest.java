package com.activequant;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.transport.IPublisher;
import com.activequant.transport.IReceiver;
import com.activequant.transport.ITransportFactory;
import com.activequant.transport.activemq.ActiveMQTransportFactory;
import com.activequant.utils.events.IEventListener;

/**
 * Unit test for simple App.
 */
public class ActiveMQTransportFactoryTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public ActiveMQTransportFactoryTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ActiveMQTransportFactoryTest.class);
    }

    public void testLocalInstantiation() throws Exception {
        ITransportFactory factory = new ActiveMQTransportFactory();
        IPublisher publisher = factory.getPublisher("THISISALONGTEST");
        IReceiver rec2 = factory.getReceiver("THISISALONGTEST");

        final Map<String, Object> receivedMap = new HashMap<String, Object>();
        final Map<String, Object> sentMap = new HashMap<String, Object>();
        sentMap.put("KEY", "VALUE");
        IEventListener<Map<String, Object>> localListener = new IEventListener<Map<String, Object>>() {
            public void eventFired(final Map<String, Object> event) {
                receivedMap.putAll(event);
            }
        };
        rec2.getRawMsgRecEvent().addEventListener(localListener);
        publisher.send(sentMap);
        //
        Thread.sleep(1000);
        assertEquals(sentMap.get("KEY"), receivedMap.get("KEY"));

    }

}
