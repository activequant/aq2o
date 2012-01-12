package com.activequant.backtesting;

import java.util.PriorityQueue;

import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.DoubleValStreamEvent;
import com.activequant.tools.streaming.StreamEvent;

public class FastStreamer {

	class FastStreamEventContainer implements Comparable<FastStreamEventContainer> {
		private final int internalStreamId;
		private DoubleValStreamEvent streamEvent; 
		FastStreamEventContainer(int streamId) {
			this.internalStreamId = streamId;
		}
		public int compareTo(FastStreamEventContainer other){			
			if(streamEvent==null || other.streamEvent==null)return -1; 			
			return (int)(streamEvent.getTimeStamp().doubleValue() - other.streamEvent.getTimeStamp().doubleValue());
		}
	}

	private PriorityQueue<FastStreamEventContainer> fastQueue = new PriorityQueue<FastStreamEventContainer>();
	private final TimeSeriesIterator[] iterators;

	public FastStreamer(TimeSeriesIterator[] it) {
		this.iterators = it;
		// initialize also the headstart data. 
		
		for (int i = 0; i < it.length; i++) {
			if (it[i].hasNext()) {
				Tuple<Date8Time6, Double> payload = it[i].next();
				DoubleValStreamEvent dval = new DoubleValStreamEvent(payload.getA(), payload.getB());				
				FastStreamEventContainer fs = new FastStreamEventContainer(i);
				fs.streamEvent=dval;
				fastQueue.add(fs);
			}
		}
	}

	public DoubleValStreamEvent getOneFromPipes()
	{
		DoubleValStreamEvent ret = null; 
		if(!fastQueue.isEmpty()){
			FastStreamEventContainer event = fastQueue.poll();
			if(event==null)return ret;
			ret = event.streamEvent;
			if(iterators[event.internalStreamId].hasNext())
			{
				// TODO: OPTIMIZE. SUBOPTIMAL. 				
				Tuple<Date8Time6, Double> payload = iterators[event.internalStreamId].next();
				DoubleValStreamEvent dval = new DoubleValStreamEvent(payload.getA(), payload.getB());
				event.streamEvent=dval;
				fastQueue.add(event);
			}
		}
		return ret; 
	}
	
	public boolean moreDataInPipe() {
		for (TimeSeriesIterator it : iterators)
			if (it.hasNext())
				return true;
		return false;
	}

}
