package com.activequant.utils.snmp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;

/**
 * Copy-paste.
 * 
 * @author ustaudinger
 * 
 */
class SNMP4JFacade extends BaseAgent {

    private static Logger log = Logger.getLogger(SNMP4JFacade.class);
    private String address = null;
    private HashMap<String, MOScalar> scalars = new HashMap<String, MOScalar>();
    // static final OID interfacesTable = new OID(".1.3.6.1.2.1.2.2.1");
    static final String baseOID = new String("");
    private Map<String, OID> oidMap = new HashMap<String, OID>();

    /**
     * Constructor
     * 
     * @param add
     * @throws IOException
     */
    SNMP4JFacade(String address) throws IOException {
        // These files does not exist and are not used but has to be specified
        // Read snmp4j docs for more info
        super(new File("conf.agent"), new File("bootCounter.agent"), new CommandProcessor(new OctetString(
                MPv3.createLocalEngineID())));
        this.address = address;
        start();

    }

    public void registerOID(String key, String suffix) {
        String oidID = baseOID + suffix;
        log.info("Registering OID " + oidID);
        OID oid = new OID(oidID);
        oidMap.put(key, oid);
    }

    /**
     * The table of community strings configured in the SNMP engine's Local
     * Configuration Datastore (LCD).
     * 
     * We only configure one, "public".
     */
    protected void addCommunities(SnmpCommunityMIB communityMIB) {
        Variable[] com2sec = new Variable[] { new OctetString("public"), // community
                                                                         // name
                new OctetString("cpublic"), // security name
                getAgent().getContextEngineID(), // local engine ID
                new OctetString("public"), // default context name
                new OctetString(), // transport tag
                new Integer32(StorageType.nonVolatile), // storage type
                new Integer32(RowStatus.active) // row status
        };
        MOTableRow row = communityMIB.getSnmpCommunityEntry().createRow(
                new OctetString("public2public").toSubIndex(true), com2sec);
        communityMIB.getSnmpCommunityEntry().addRow(row);
    }

    /*
     * Empty implementation
     */
    @Override
    protected void addNotificationTargets(SnmpTargetMIB targetMIB, SnmpNotificationMIB notificationMIB) {
    }

    /**
     * User based Security Model, only applicable to SNMP v.3
     * 
     */
    protected void addUsmUser(USM usm) {
    }

    /**
     * Minimal View based Access Control
     * 
     * http://www.faqs.org/rfcs/rfc2575.html
     */
    @Override
    protected void addViews(VacmMIB vacm) {

        vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString("cpublic"), new OctetString("v1v2group"),
                StorageType.nonVolatile);

        vacm.addAccess(new OctetString("v1v2group"), new OctetString("public"), SecurityModel.SECURITY_MODEL_ANY,
                SecurityLevel.NOAUTH_NOPRIV, MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
                new OctetString("fullWriteView"), new OctetString("fullNotifyView"), StorageType.nonVolatile);

        vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"), new OctetString(),
                VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
    }

    protected void initTransportMappings() throws IOException {
        transportMappings = new TransportMapping[1];
        Address addr = GenericAddress.parse(address);
        TransportMapping tm = TransportMappings.getInstance().createTransportMapping(addr);
        transportMappings[0] = tm;
    }

    /**
     * Clients can register the MO they need
     */
    public void registerManagedObject(ManagedObject mo, String oid) {
        try {
            server.register(mo, new OctetString("public"));
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * We let clients of this agent register the MO they need so this method
     * does nothing
     */
    @Override
    protected void registerManagedObjects() {
    }

    /**
     * Method to set an SNMP value on the facade.
     * 
     * @param key
     * @param value
     */
    public void setValue(String key, Integer value) throws Exception {
        if (!scalars.containsKey(key)) {
            if (!oidMap.containsKey(key))
                throw new Exception("OID for " + key + " not registered. ");
            OID oid = oidMap.get(key);
            MOScalar mo = MOScalarFactory.createReadOnly(oid, key);
            scalars.put(key, mo);
            registerManagedObject(mo, oid.toString());
        }
        MOScalar mo = scalars.get(key);
        if (mo != null) {
            mo.setValue(new Integer32(value));
        }
    }

    /**
     * Start method invokes some initialization methods needed to start the
     * agent
     * 
     * @throws IOException
     */
    private void start() throws IOException {

        init();
        // This method reads some old config from a file and causes
        // unexpected behavior.
        // loadConfig(ImportModes.REPLACE_CREATE);
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
        unregisterManagedObject(getSnmpv2MIB());

    }

    private void unregisterManagedObject(MOGroup moGroup) {
        moGroup.unregisterMOs(server, getContext(moGroup));
    }

    protected void unregisterManagedObjects() {
        // here we should unregister those objects previously registered...
    }
}
