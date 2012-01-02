package com.activequant.utils.snmp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class SNMPReporter {

    private Logger log = Logger.getLogger(SNMPReporter.class);
    private SNMP4JFacade snmpFacade;
    private Map<String, Integer> valueMap = new ConcurrentHashMap<String, Integer>();
    private Map<String, ValueMode> modeMap = new ConcurrentHashMap<String, ValueMode>();

    public static enum ValueMode {
        COUNTER, VALUE
    };

    public SNMPReporter(String host, int port) throws UnknownHostException, IOException {

        snmpFacade = new SNMP4JFacade("udp:" + host + "/" + port);

        // schedule refetching through a new timer task.
        TimerTask refDataDownloadTask = new TimerTask() {
            public void run() {
                try {
                    // System.out.println("Republishing ... ");
                    updateSNMPDirectory();
                } catch (Exception e) {
                    log.error("Error while updating SNMP directory", e);
                }
            }
        };

        // we schedule a publishing of all counters for every minute.
        long currentMs = System.currentTimeMillis();
        long delay = (60 * 1000) - (currentMs % (60 * 1000));
        Timer refDataDownloadTimer = new Timer(true);
        refDataDownloadTimer.scheduleAtFixedRate(refDataDownloadTask, (long) (1 * delay), (long) (1 * 60 * 1000));

    }

    public SNMPReporter() throws UnknownHostException, IOException {
        this(InetAddress.getLocalHost().getHostAddress(), 65000);
    }

    /**
     * registers a suffix key combination.
     * 
     * @param key
     * @param suffix
     */
    public void registerOID(String key, String suffix, ValueMode valMode) {
        snmpFacade.registerOID(key, suffix);
        modeMap.put(key, valMode);
    }

    /**
     * Adds a value to a key. If the key does not yet exist, it will create it
     * with a start value of zero and then add the to-be-added value.
     * 
     * @param key
     * @param toBeAddedValue
     */
    public void addValue(String key, Integer toBeAddedValue) {
        Integer val = 0;
        if (valueMap.containsKey(key))
            val = valueMap.get(key);
        val += toBeAddedValue;
        setValue(key, val);
    }

    /**
     * sets a value in the value map.
     * 
     * @param key
     * @param value
     */
    public void setValue(String key, Integer value) {
        valueMap.put(key, value);
    }

    private void updateSNMPDirectory() throws Exception {

        Iterator<Entry<String, Integer>> s = valueMap.entrySet().iterator();
        while (s.hasNext()) {
            Entry<String, Integer> entry = s.next();
            snmpFacade.setValue(entry.getKey(), entry.getValue());
            ValueMode valMode = modeMap.get(entry.getKey());
            if (valMode.equals(ValueMode.COUNTER))
                entry.setValue(0);
        }

    }

    public static void main(String[] args) throws Exception {
        /**
         * Port 161 is used for Read and Other operations Port 162 is used for
         * the trap generation
         */
        SNMPReporter agent = new SNMPReporter();
        agent.registerOID("A", "1.3.6.1.1.0", ValueMode.VALUE);
        agent.registerOID("B", "1.3.6.1.2.0", ValueMode.VALUE);
        System.out.println("Done ....");
        while (true) {
            Thread.sleep(1000);
            agent.setValue("A", (int) (Math.random() * 1000));
            agent.setValue("B", (int) (Math.random() * 1000));
        }

        // Since BaseAgent registers some mibs by default we need to unregister
        // one before we register our own sysDescr. Normally you would
        // override that method and register the mibs that you need

    }

}
