package com.activequant.archive;

import java.io.IOException;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.interfaces.archive.IArchiveWriter;

public class MockArchiveFactory implements IArchiveFactory {

    @Override
    public IArchiveReader getReader(TimeFrame tf) {
        // TODO Auto-generated method stub
        return new IArchiveReader() {
            
            @Override
            public TimeSeriesIterator getTimeSeriesStream(String streamId, String key, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TSContainer getTimeSeries(String streamId, String key, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public TSContainer getTimeSeries(String streamId, String key, TimeStamp startTimeStamp) throws Exception {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public MultiValueTimeSeriesIterator getMultiValueStream(String streamId, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception {
                // TODO Auto-generated method stub
                return null;
            }

			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
        };
    }

    @Override
    public IArchiveWriter getWriter(TimeFrame tf) {
        // TODO Auto-generated method stub
        return new IArchiveWriter() {
            
            @Override
            public void write(String instrumentId, TimeStamp timeStamp, String key, Double value) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void write(String instrumentId, TimeStamp timeStamp, String[] keys, Double[] values) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void write(String instrumentId, TimeStamp timeStamp, Tuple<String, Double>... value) throws IOException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void commit() throws IOException {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void delete(String seriesKey, String valueKey) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void delete(String seriesKey, String valueKey, TimeStamp from, TimeStamp to) {
                // TODO Auto-generated method stub
                
            }

			@Override
			public void delete(String seriesKey) throws IOException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void delete(String seriesKey, TimeStamp from, TimeStamp to)
					throws IOException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
        };
    }

}
