package com.activequant.messages;

import java.util.List;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.utils.ArrayUtils;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Marshaller for MDS messages. 
 * @author GhostRider
 *
 */
public class Marshaller {
	MessageFactory mf = new MessageFactory();

	ExtensionRegistry registry;

	public Marshaller() {
		registry = ExtensionRegistry.newInstance();
		AQMessages.registerAllExtensions(registry);
	}

	/**
	 * faster marshaller for MDS.  
	 * 
	 * @param mdiId
	 * @param bidPrices
	 * @param askPrices
	 * @param bidSizes
	 * @param askSizes
	 * @return
	 */
	public byte[] marshallToMDS(String mdiId, List<Double> bidPrices, List<Double> askPrices, List<Double> bidSizes, List<Double> askSizes){
		BaseMessage mdsm = mf.buildMds(mdiId, bidPrices, askPrices, bidSizes, askSizes);
		return mdsm.toByteArray();
	}
	
	public byte[] marshall(MarketDataSnapshot mds)
			throws InvalidProtocolBufferException {
		BaseMessage mdsm = mf.buildMds(mds.getMdiId(),
				ArrayUtils.toDoubleList(mds.getBidPrices()),
				ArrayUtils.toDoubleList(mds.getAskPrices()),
				ArrayUtils.toDoubleList(mds.getBidSizes()),
				ArrayUtils.toDoubleList(mds.getAskSizes()));
		return mdsm.toByteArray();
	}

	public AQMessages.BaseMessage demarshall(byte[] in)
			throws InvalidProtocolBufferException {
		return AQMessages.BaseMessage.parseFrom(in, registry);
	}

	public MarketDataSnapshot demarshall(AQMessages.MarketDataSnapshot mdsm) {
		MarketDataSnapshot mds = new MarketDataSnapshot();
		mds.setMdiId(mdsm.getMdiId());
		mds.setTimeStamp(new TimeStamp(mdsm.getTimestamp()));
		mds.setBidPrices(ArrayUtils.toPrimArray(mdsm.getBidPxList()));
		mds.setAskPrices(ArrayUtils.toPrimArray(mdsm.getAskPxList()));
		mds.setBidSizes(ArrayUtils.toPrimArray(mdsm.getBidQList()));
		mds.setAskSizes(ArrayUtils.toPrimArray(mdsm.getAskQList()));
		return mds;
	}

}
