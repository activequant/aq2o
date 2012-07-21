package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.streaming.StreamEventIterator;

/**
 * 
 * Fast Streamer takes a set of stream iterators and replays events 
 * from these iterators in their chronological order. 
 * 
 * @author GhostRider
 *
 */
public class FastStreamer {

	private final Logger log = Logger.getLogger(FastStreamer.class);
	
	class FastStreamEventContainer implements Comparable<FastStreamEventContainer> {
		private final int internalStreamId;
		private StreamEvent streamEvent; 
		FastStreamEventContainer(int streamId) {
			this.internalStreamId = streamId;
		}
		public int compareTo(FastStreamEventContainer other){			
			if(streamEvent==null || other.streamEvent==null)return -1; 			
			return streamEvent.getTimeStamp().compareTo(other.streamEvent.getTimeStamp());			
		}
	}

	private List<FastStreamEventContainer> fastQueue = new ArrayList<FastStreamEventContainer>();
	private final FastStreamEventContainer[] containers;
	private final StreamEventIterator<StreamEvent>[] iterators;

	public FastStreamer(  StreamEventIterator<StreamEvent>[] it) {
		this.iterators = it;
		// initialize also the headstart data. 
		containers =  new FastStreamEventContainer[it.length]; 
		for (int i = 0; i < it.length; i++) {
			if (it[i].hasNext()) {
				
				StreamEvent payload = it[i].next();
				if(containers[i] == null)
					containers[i] = new FastStreamEventContainer(i);
				FastStreamEventContainer fs = containers[i];
				fs.streamEvent=payload;
				fastQueue.add(fs);
			}
		}
	}

	/**
	 * Fetches the next stream event from the pipe. 
	 * @return
	 */
	public StreamEvent getOneFromPipes()
	{
		StreamEvent ret = null;
		if(!fastQueue.isEmpty()){
			FastStreamEventContainer event = fastQueue.get(0);
			if(event==null)return null;
			fastQueue.remove(0);
			ret = event.streamEvent;
			if(iterators[event.internalStreamId].hasNext())
			{
				StreamEvent payload = iterators[event.internalStreamId].next();
				FastStreamEventContainer newEvent = new FastStreamEventContainer(event.internalStreamId); 						
				newEvent.streamEvent=payload;				
				fastQueue.add(newEvent);
				Collections.sort(fastQueue);
				
				//
				// dumpQueue();
				
			}
			// log.info(ret.getTimeStamp().getNanoseconds() + " -  " + ret.getTimeStamp().getDate() + " - " + ret.toString());
		}
		return ret; 
	}
	
	private void dumpQueue(){
		for(FastStreamEventContainer ec : fastQueue)
		{
			System.out.println(ec.streamEvent.getTimeStamp().getNanoseconds() + " - " + ec.streamEvent.getTimeStamp().getDate());
		}
	}
	
	public boolean moreDataInPipe() {
		for (StreamEventIterator<StreamEvent> it : iterators)
			if (it.hasNext())
				return true;
		return false;
	}

}
