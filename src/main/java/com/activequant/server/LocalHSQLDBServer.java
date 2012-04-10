package com.activequant.server;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;

public class LocalHSQLDBServer {

    public void start(int port) throws Exception {
        HsqlProperties p = new HsqlProperties();
        // hsqldb/data 
        p.setProperty("server.database.0", "file:.;user=aq2o;password=aq2o");
        p.setProperty("server.dbname.0", "aq2o");
        p.setProperty("server.port", ""+port);
        Server server = new Server();
        server.setProperties(p);
        server.setLogWriter(null); // can use custom writer
        server.setErrWriter(null); // can use custom writer
        server.start();        
    }
    
}
