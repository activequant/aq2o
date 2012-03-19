package com.activequant.backtesting;

import java.util.PriorityQueue;

import org.apache.log4j.Logger;

import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.StreamEventIterator;

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
			return other.streamEvent.getTimeStamp().compareTo(streamEvent.getTimeStamp());			
		}
	}

	private PriorityQueue<FastStreamEventContainer> fastQueue = new PriorityQueue<FastStreamEventContainer>();
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

	public StreamEvent getOneFromPipes()
	{
		StreamEvent ret = null;
		if(!fastQueue.isEmpty()){
			FastStreamEventContainer event = fastQueue.poll();
			if(event==null)return null;
			ret = event.streamEvent;
			if(iterators[event.internalStreamId].hasNext())
			{ 				
				StreamEvent payload = iterators[event.internalStreamId].next();
				event.streamEvent=payload;
				fastQueue.add(event);
			}
			log.info(ret.getTimeStamp().getNanoseconds() + " -  " + ret.getTimeStamp().getDate() + " - " + ret.toString());
		}
		return ret; 
	}
	
	public boolean moreDataInPipe() {
		for (StreamEventIterator<StreamEvent> it : iterators)
			if (it.hasNext())
				return true;
		return false;
	}

}
