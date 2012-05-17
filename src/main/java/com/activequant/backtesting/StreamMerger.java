package com.activequant.backtesting;

import java.util.PriorityQueue;

import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;

/**
 * 
 * @author GhostRider
 *
 */
public class StreamMerger {

	class FastStreamEventContainer implements Comparable<FastStreamEventContainer> {
		public final int internalStreamId;
		public Tuple<TimeStamp,Double> streamEvent; 
		FastStreamEventContainer(int streamId, Tuple<TimeStamp,Double> streamEvent) {
			this.internalStreamId = streamId;
			this.streamEvent = streamEvent; 
		}
		public int compareTo(FastStreamEventContainer other){			
			if(streamEvent==null || other.streamEvent==null)return -1; 			
			return streamEvent.getA().compareTo(other.streamEvent.getA());			
		}
	}

	private PriorityQueue<FastStreamEventContainer> fastQueue = new PriorityQueue<FastStreamEventContainer>();
	private final TimeSeriesIterator[] iterators;

	public StreamMerger(  TimeSeriesIterator[] it) {
		this.iterators = it;
		// initialize also the headstart data. 
		
		for (int i = 0; i < it.length; i++) {
			if (it[i].hasNext()) {
				
			    Tuple<TimeStamp,Double> payload = it[i].next();
			    if(payload == null)break; 
			    if(payload.getA()==null || payload.getB()==null)break;
				FastStreamEventContainer fs = new FastStreamEventContainer(i, payload);
				fastQueue.add(fs);
			}
		}
	}

	public FastStreamEventContainer getOneFromPipes()
	{
	    FastStreamEventContainer event = null; 
		if(!fastQueue.isEmpty()){
			event = fastQueue.poll();
			if(event==null)return event;
			if(iterators[event.internalStreamId].hasNext())
			{ 				
			    Tuple<TimeStamp,Double> payload = iterators[event.internalStreamId].next();
			    if(payload == null)return null;
                if(payload.getA()==null || payload.getB()==null)return null;
			    event.streamEvent=payload;
				fastQueue.add(event);
			}
		}
		return event; 
	}
	
	public boolean moreDataInPipe() {
		for (TimeSeriesIterator it : iterators)
			if (it.hasNext())
				return true;
		return false;
	}

}
