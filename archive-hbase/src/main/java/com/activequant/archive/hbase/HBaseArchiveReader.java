package com.activequant.archive.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.archive.TSContainer;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.utils.UniqueTimeStampGenerator;

/**
 * Reader class to read from HBase. All timestamps are in UTC and in Date8Time6
 * format. Due to performance reasons, the reader will not return date8time6
 * objects.
 * 
 * Visibility on package level only is intended! compare the corresponding
 * factory class.
 * 
 * @author GhostRider
 * 
 */
class HBaseArchiveReader extends HBaseBase implements IArchiveReader {

    private UniqueTimeStampGenerator timeStampGenerator = new UniqueTimeStampGenerator();
    private Logger log = Logger.getLogger(HBaseArchiveReader.class);
    private long slotSizeInHours = 24l * 30l; // 30 days at once.  

    HBaseArchiveReader(final String zookeeperHost, final TimeFrame tf) throws IOException {
        super(zookeeperHost, 2181, "TSDATA_" + tf.toString());
    }

    HBaseArchiveReader(final String zookeeperHost, int zookeeperPort, final TimeFrame tf) throws IOException {
        super(zookeeperHost, zookeeperPort, "TSDATA_" + tf.toString());
    }

    @Deprecated
    HBaseArchiveReader(final TimeFrame tf) throws IOException {
        super("TSDATA_" + tf.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.activequant.archive.IArchiveReader#getTimeSeries(java.lang.String,
     * java.lang.String, java.lang.Long)
     */
    public TSContainer getTimeSeries(final String seriesId, final String value, final TimeStamp startTimeStamp) throws Exception {
        return getTimeSeries(seriesId, value, startTimeStamp, timeStampGenerator.now());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.activequant.archive.IArchiveReader#getTimeSeries(java.lang.String,
     * java.lang.String, java.lang.Long, java.lang.Long)
     */
    public TSContainer getTimeSeries(final String seriesId, final String value, final TimeStamp startTimeStamp, final TimeStamp stopTimeStamp)
            throws Exception {
        ResultScanner scanner = getScanner(seriesId, startTimeStamp, stopTimeStamp);
        

        List<TimeStamp> timeStamps = new ArrayList<TimeStamp>();
        List<Double> values = new ArrayList<Double>();
        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                if (rr.containsColumn("numbers".getBytes(), value.getBytes())) {
                    byte[] valB = rr.getValue("numbers".getBytes(), value.getBytes());
                    byte[] tsB = rr.getValue("numbers".getBytes(), "ts".getBytes());
                    Double val = Bytes.toDouble(valB);
                    Long ts = Bytes.toLong(tsB);
                    timeStamps.add(new TimeStamp(ts));
                    values.add(val);
                }
            }
        } finally {
            scanner.close();
        }
        TSContainer ret = new TSContainer(timeStamps.toArray(new TimeStamp[] {}), values.toArray(new Double[] {}));
        return ret;
    }


    public TimeSeriesIterator getTimeSeriesStream(final String seriesId, final String key, final TimeStamp startTimeStamp, final TimeStamp stopTimeStamp) throws Exception {
        

        return new TimeSeriesIterator() {
        	ResultScanner scanner = null; 
            Iterator<Result> resultIterator;
            TimeStamp start = null, end = null; 

            private void prepareNextScanner(){
            	try {
            		if(scanner!=null)scanner.close();

            		if(start==null)
            			start = startTimeStamp; 
            		else
            			start = new TimeStamp(start.getNanoseconds() + slotSizeInHours * 60 * 60 * 1000 * 1000 * 1000);
            		end = new TimeStamp(start.getNanoseconds() + slotSizeInHours * 60 * 60 * 1000 * 1000 * 1000);
            		log.info("Prepared scanner from " + start.getCalendar().getTime()+ " to " + end.getCalendar().getTime());
					scanner = getScanner(seriesId, start, end);
					resultIterator = scanner.iterator();
				} catch (IOException e) {
					e.printStackTrace();
				}	
            }
            
            @Override
            public boolean hasNext() {
            	if(scanner==null){
            		prepareNextScanner();
            	}
            	
            	boolean state = resultIterator.hasNext();
            	while(!state && start.isBefore(stopTimeStamp)){
            		prepareNextScanner();
            		state = resultIterator.hasNext();
            	}                
                if(scanner==null)return false;
                return state; 
            }

            @Override
            public Tuple<TimeStamp, Double> next() {
                Result rr = resultIterator.next();
                if (rr != null) {
                    if (rr.containsColumn("numbers".getBytes(), key.getBytes())) {
                        byte[] valB = rr.getValue("numbers".getBytes(), key.getBytes());
                        byte[] tsB = rr.getValue("numbers".getBytes(), "ts".getBytes());
                        Double val = Bytes.toDouble(valB);
                        Long ts = Bytes.toLong(tsB);
                        return new Tuple<TimeStamp, Double>(new TimeStamp(ts), val);
                    }
                    return null;
                } else {
                    scanner.close();
                    scanner = null;
                }
                return null;
            }
        };

    }

    @Override
    public MultiValueTimeSeriesIterator getMultiValueStream(final String streamId, final TimeStamp startTimeStamp, final TimeStamp stopTimeStamp) throws Exception {

        
        return new MultiValueTimeSeriesIterator() {
        	
        	
        	ResultScanner scanner = null; 
            Iterator<Result> resultIterator;
            TimeStamp start = null, end = null; 

            private void prepareNextScanner(){
            	try {
            		if(scanner!=null)scanner.close();
            		if(start==null)
            			start = startTimeStamp;
            		else
            			start = new TimeStamp(start.getNanoseconds() + slotSizeInHours * 60l * 60l * 1000l * 1000l * 1000l);
            		end = new TimeStamp(start.getNanoseconds() + slotSizeInHours * 60l * 60l * 1000l * 1000l * 1000l);
            		log.info("Prepared scanner from " + start.getCalendar().getTime()+ " to " + end.getCalendar().getTime());
					scanner = getScanner(streamId, start, end);
					resultIterator = scanner.iterator();
				} catch (IOException e) {
					e.printStackTrace();
				}	
            }
            
            @Override
            public boolean hasNext() {
            	if(scanner==null){
            		prepareNextScanner();
            	}            	
            	boolean state = resultIterator.hasNext();
            	while(!state && start.isBefore(stopTimeStamp)){
            		prepareNextScanner();
            		state = resultIterator.hasNext();
            	}                
                if(scanner==null)return false;
                return state; 
            }

            @Override
            public Tuple<TimeStamp, Map<String, Double>> next() {
            	Result rr = resultIterator.next();
                Tuple<TimeStamp, Map<String, Double>> resultTuple = new Tuple<TimeStamp, Map<String, Double>>();
                Map<String, Double> resultMap = new HashMap<String, Double>();
                resultTuple.setB(resultMap);
            
                if (rr != null) {
                    NavigableMap<byte[],NavigableMap<byte[],byte[]>> valueMap = rr.getNoVersionMap();
                    byte[] tsB = rr.getValue("numbers".getBytes(), "ts".getBytes());
                    Long ts = Bytes.toLong(tsB);
                    TimeStamp timeStamp = new TimeStamp(ts);
                    resultTuple.setA(timeStamp);
                    
                    NavigableMap<byte[], byte[]> numbersMap = valueMap.get("numbers".getBytes());
                    Iterator<Entry<byte[], byte[]>> numbersIt = numbersMap.entrySet().iterator();
                    while(numbersIt.hasNext()){
                        Entry<byte[], byte[]> entry = numbersIt.next();
                        String key = Bytes.toString(entry.getKey());
                        if(key.equals("ts"))continue; 
                        Double value = Bytes.toDouble(entry.getValue());
                        resultMap.put(key,  value);
                    }
                    
                } else {
                    scanner.close();
                    scanner = null;
                }
                return resultTuple;
            }
        };
    }

}
