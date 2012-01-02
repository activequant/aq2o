package com.activequant.archive.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;

class HBaseBase {

    private Logger log = Logger.getLogger(HBaseBase.class);
    protected HTable htable;

    HBaseBase(final String zookeeperQuorumHost, final String tableName) throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperQuorumHost);
        HBaseAdmin admin = new HBaseAdmin(config);
        if (!admin.tableExists(tableName.getBytes())) {
            log.info("HTable doesn't exist. Creating it.");
            // create the table.
            HTableDescriptor desc = new HTableDescriptor(tableName.getBytes());
            HColumnDescriptor col1 = new HColumnDescriptor("numbers".getBytes());
            desc.addFamily(col1);
            admin.createTable(desc);
            boolean avail = admin.isTableAvailable(tableName.getBytes());
            log.info("HTable is available: " + avail);
        }
        htable = new HTable(config, tableName.getBytes());
        htable.setAutoFlush(false);
    }

    @Deprecated
    HBaseBase(final String tableName) throws IOException {
        this("localhost", tableName);
    }
    //
    //
    // public void test() throws Exception {
    // // HBaseArchiveWriter tsw = new HBaseArchiveWriter();
    // long l1 = System.currentTimeMillis();
    // Tuple<String, Double> t = new Tuple<String, Double>();
    // for(int i = 0; i<100000;i++)
    // {
    // t.setA("BID");
    // t.setB(Math.random());
    // //tsw.write("Instrument1", i*1000L, t);
    // // System.out.print(".");
    // }
    // long l1a = System.currentTimeMillis();
    // //tsw.commit();
    // long l2 = System.currentTimeMillis();
    // System.out.println( (l1a - l1)+"ms " + (l2 - l1)+"ms");
    // IArchiveReader tsr = new HBaseArchiveReader(TimeFrame.EOD);
    // TSContainer ret = tsr.getTimeSeries("BBGT_ADH2 CURNCY", "BID", new
    // Date8Time6(19700101000000.0));
    // System.out.println(ret.timeStamps.length);
    // System.out.println(ret.values.length);
    // long l3 = System.currentTimeMillis();
    // System.out.println( (l2 - l1)+"ms to " + (l3-l2)+" ms");
    // }
    //
    // /**
    // * @param args
    // */
    // public static void main(String[] args) throws Exception {
    // new HBaseBase("testdata").test();
    // }

}
