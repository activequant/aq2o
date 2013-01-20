package com.activequant.messages;

import java.util.List;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.streaming.AccountDataEvent;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.domainmodel.streaming.TimeStreamEvent;
import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderCancellationRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelledEvent;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderReplacedEvent;
import com.activequant.domainmodel.trade.event.OrderSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderUpdateRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderUpdateSubmittedEvent;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.utils.ArrayUtils;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Marshaller for MDS messages. TODO: fix the functionality clash between this
 * and the message factory. Should be merged.
 * 
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
	public byte[] marshallToMDS(String mdiId, List<Double> bidPrices,
			List<Double> askPrices, List<Double> bidSizes, List<Double> askSizes) {
		BaseMessage mdsm = mf.buildMds(mdiId, bidPrices, askPrices, bidSizes,
				askSizes);
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

	public OrderEvent demarshallOrderEvent(AQMessages.BaseMessage bm) {
		switch (bm.getType()) {
		case ORD_ACCPTD:
			return demarshall((AQMessages.OrderAccepted) bm
					.getExtension(AQMessages.OrderAccepted.cmd));
		case ORD_SUBMITTED:
			return demarshall((AQMessages.OrderSubmitted) bm
					.getExtension(AQMessages.OrderSubmitted.cmd));
		case ORD_CANCEL_SUBMITTED:
			return demarshall((AQMessages.OrderCancelSubmitted) bm
					.getExtension(AQMessages.OrderCancelSubmitted.cmd));
		case ORD_CANCELLED:
			return demarshall((AQMessages.OrderCancelled) bm
					.getExtension(AQMessages.OrderCancelled.cmd));
		case ORD_CNCL_REJ:
			return demarshall((AQMessages.OrderCancelReject) bm
					.getExtension(AQMessages.OrderCancelReject.cmd));
		case ORD_REJ:
			return demarshall((AQMessages.OrderRejected) bm
					.getExtension(AQMessages.OrderRejected.cmd));
		case ORD_UPDATE_SUBMITTED:
			return demarshall((AQMessages.OrderUpdateSubmitted) bm
					.getExtension(AQMessages.OrderUpdateSubmitted.cmd));
		case ORD_UPD_REJECTED:
			return demarshall((AQMessages.OrderUpdateRejected) bm
					.getExtension(AQMessages.OrderUpdateRejected.cmd));
		case ORD_UPDATED:
			return demarshall((AQMessages.OrderUpdated) bm
					.getExtension(AQMessages.OrderUpdated.cmd));
		}
		return null;
	}

	public TimeStreamEvent demarshall(AQMessages.ServerTime in) {
		TimeStreamEvent tse = new TimeStreamEvent(new TimeStamp(
				in.getTimestamp()));
		return tse;
	}

	public AccountDataEvent demarshall(AQMessages.AccountDataMessage adm) {
		AccountDataEvent ad = new AccountDataEvent(new TimeStamp(),
				adm.getType(), adm.getValue());
		return ad;
	}

	public OrderFillEvent demarshall(AQMessages.ExecutionReport2 adm) {
		OrderFillEvent ofe = new OrderFillEvent();

		return ofe;
	}

	public void demarshall(AQMessages.Login adm) {

	}

	public void demarshall(AQMessages.LoginResponse adm) {

	}

	public OrderAcceptedEvent demarshall(AQMessages.OrderAccepted adm) {
		OrderAcceptedEvent oae = new OrderAcceptedEvent();
		oae.setRefOrderId(adm.getClOrdId());
		oae.setTimeStamp(new TimeStamp());
		return oae;
	}

	public OrderCancelledEvent demarshall(AQMessages.OrderCancelled adm) {
		OrderCancelledEvent oce = new OrderCancelledEvent();
		oce.setRefOrderId(adm.getClOrdId());
		oce.setTimeStamp(new TimeStamp());
		return oce;
	}

	public OrderCancellationRejectedEvent demarshall(
			AQMessages.OrderCancelReject adm) {
		OrderCancellationRejectedEvent ocr = new OrderCancellationRejectedEvent();
		ocr.setRefOrderId(adm.getClOrdId());
		ocr.setTimeStamp(new TimeStamp());
		ocr.setReason(adm.getClxRejReason());
		return ocr;
	}

	public OrderCancelSubmittedEvent demarshall(
			AQMessages.OrderCancelSubmitted adm) {
		OrderCancelSubmittedEvent ocse = new OrderCancelSubmittedEvent();
		ocse.setRefOrderId(adm.getClOrdId());
		ocse.setTimeStamp(new TimeStamp());
		return ocse;
	}

	public OrderRejectedEvent demarshall(AQMessages.OrderRejected adm) {
		OrderRejectedEvent ore = new OrderRejectedEvent();
		ore.setRefOrderId(adm.getClOrdId());
		ore.setTimeStamp(new TimeStamp());
		ore.setReason(adm.getReason());
		return ore;
	}

	public OrderSubmittedEvent demarshall(AQMessages.OrderSubmitted adm) {
		OrderSubmittedEvent ose = new OrderSubmittedEvent();
		ose.setRefOrderId(adm.getClOrdId());
		ose.setTimeStamp(new TimeStamp());
		return ose;
	}

	public OrderReplacedEvent demarshall(AQMessages.OrderUpdated adm) {
		OrderReplacedEvent ore = new OrderReplacedEvent();
		ore.setRefOrderId(adm.getClOrdId());
		ore.setTimeStamp(new TimeStamp());
		return ore;
	}

	public OrderUpdateRejectedEvent demarshall(
			AQMessages.OrderUpdateRejected adm) {
		OrderUpdateRejectedEvent oure = new OrderUpdateRejectedEvent();
		oure.setRefOrderId(adm.getClOrdId());
		oure.setTimeStamp(new TimeStamp());				
		oure.setReason(adm.getReason());		
		return oure;
	}

	public OrderUpdateSubmittedEvent demarshall(
			AQMessages.OrderUpdateSubmitted adm) {
		OrderUpdateSubmittedEvent ouse = new OrderUpdateSubmittedEvent();
		ouse.setRefOrderId(adm.getClOrdId());
		ouse.setTimeStamp(new TimeStamp());
		return ouse;
	}

	public void demarshall(AQMessages.PositionReport adm) {

	}

	public void demarshall(AQMessages.SecurityStatus adm) {

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
