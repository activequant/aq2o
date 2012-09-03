package com.activequant.messages;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.messages.AQMessages.BaseMessage;

/**
 * Basic message factory for protocol buffer messages. Underdeveloped in this project. 
 * At one point, we will have to copy over the factory code from one of our other projects into this one. 
 *  
 * @author GhostRider
 *
 */
public class MessageFactory {
	private <Type> BaseMessage wrap(BaseMessage.CommandType type,
			BaseMessage.GeneratedExtension<BaseMessage, Type> extension,
			Type cmd) {
		return BaseMessage.newBuilder().setType(type)
				.setExtension(extension, cmd).build();
	}

	public BaseMessage buildMds(String mdiId, Iterable<? extends Double> bid, Iterable<? extends Double> ask,
			Iterable<? extends Double> bidQ, Iterable<? extends Double> askQ) {
		AQMessages.MarketDataSnapshot m = AQMessages.MarketDataSnapshot
				.newBuilder().setMdiId(mdiId).addAllBidPx(bid).addAllAskPx(ask)
				.addAllBidQ(bidQ).addAllAskQ(askQ)
				.setTimestamp(new TimeStamp().getNanoseconds()).build();
		return wrap(BaseMessage.CommandType.MDS,
				AQMessages.MarketDataSnapshot.cmd, m);
	}

}
