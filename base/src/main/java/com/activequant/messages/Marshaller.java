package com.activequant.messages;

import java.util.List;

import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.streaming.AccountDataEvent;
import com.activequant.domainmodel.streaming.InformationalEvent;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.domainmodel.streaming.PositionEvent;
import com.activequant.domainmodel.streaming.Tick;
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
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.MarketOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.domainmodel.trade.order.StopOrder;
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
@Deprecated
public class Marshaller {
	MessageFactory mf = new MessageFactory();

	ExtensionRegistry registry;

	public Marshaller() {
		registry = ExtensionRegistry.newInstance();
		AQMessages.registerAllExtensions(registry);
	}

	/**
	 * faster marshaller for MDS. Use message factory implementation instead
	 * 
	 * @param mdiId
	 * @param bidPrices
	 * @param askPrices
	 * @param bidSizes
	 * @param askSizes
	 * @return
	 */
	@Deprecated
	public byte[] marshallToMDS(String mdiId, List<Double> bidPrices,
			List<Double> askPrices, List<Double> bidSizes, List<Double> askSizes) {
		return (marshallToMDS(mdiId, bidPrices, askPrices, bidSizes, askSizes,
				false));
	}

	@Deprecated
	/**
	 * Use message factory implementation instead. 
	 * 
	 * @param mdiId
	 * @param bidPrices
	 * @param askPrices
	 * @param bidSizes
	 * @param askSizes
	 * @param resend
	 * @return
	 */
	public byte[] marshallToMDS(String mdiId, List<Double> bidPrices,
			List<Double> askPrices, List<Double> bidSizes,
			List<Double> askSizes, boolean resend) {
		BaseMessage mdsm = mf.buildMds(mdiId, bidPrices, askPrices, bidSizes,
				askSizes, resend);
		return mdsm.toByteArray();
	}

	public byte[] marshall(MarketDataSnapshot mds)
			throws InvalidProtocolBufferException {
		BaseMessage mdsm = mf.buildMds(mds.getMdiId(),
				ArrayUtils.toDoubleList(mds.getBidPrices()),
				ArrayUtils.toDoubleList(mds.getAskPrices()),
				ArrayUtils.toDoubleList(mds.getBidSizes()),
				ArrayUtils.toDoubleList(mds.getAskSizes()), mds.isResend());

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
		case EXECUTION_REPORT2:
			return demarshall((AQMessages.ExecutionReport2) bm
					.getExtension(AQMessages.ExecutionReport2.cmd));
		}
		// nothing ...
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
		ofe.setRefOrderId(adm.getClOrdId());
		ofe.setExecId(adm.getExecId());
		ofe.setFillPrice(adm.getPrice());
		ofe.setFillAmount(adm.getQty());
		ofe.setSide(adm.getSide());
		ofe.setOptionalInstId(adm.getTdiId());
		ofe.setLeftQuantity(adm.getQuantityLeft());
		ofe.setTimeStamp(new TimeStamp(adm.getTransactTime()));
		return ofe;
	}

	public void demarshall(AQMessages.Login adm) {

	}

	public void demarshall(AQMessages.LoginResponse adm) {

	}

	public String demarshall(AQMessages.CustomCommand adm) {
		return adm.getCommand();
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

	public SingleLegOrder demarshall(AQMessages.NewOrder newOrder) {

		Double limitPrice = null;
		Double stopPrice = null;

		SingleLegOrder slo = null;
		switch (newOrder.getOrdType()) {
		case 1: {
			// market order ...
			slo = new MarketOrder();
			slo.setOrderId(newOrder.getClOrdId());
			slo.setTradInstId(newOrder.getTradInstId());
			slo.setOrderSide(newOrder.getSide() == 1 ? OrderSide.BUY
					: OrderSide.SELL);
			slo.setQuantity(newOrder.getOrderQty());
			slo.setOpenQuantity(newOrder.getOrderQty());
			break;
		}
		case 2: {
			// limit order ...

			slo = new LimitOrder();
			slo.setOrderId(newOrder.getClOrdId());
			slo.setTradInstId(newOrder.getTradInstId());
			slo.setOrderSide(newOrder.getSide() == 1 ? OrderSide.BUY
					: OrderSide.SELL);
			slo.setQuantity(newOrder.getOrderQty());
			slo.setOpenQuantity(newOrder.getOrderQty());
			((LimitOrder) slo).setLimitPrice(newOrder.getPrice());
			break;
		}
		case 3: {
			// stop order ...

			slo = new StopOrder();
			slo.setOrderId(newOrder.getClOrdId());
			slo.setTradInstId(newOrder.getTradInstId());
			slo.setOrderSide(newOrder.getSide() == 1 ? OrderSide.BUY
					: OrderSide.SELL);
			slo.setQuantity(newOrder.getOrderQty());
			slo.setOpenQuantity(newOrder.getOrderQty());
			((StopOrder) slo).setStopPrice(newOrder.getPrice());
			break;
		}
		default: {
			break;
		}
		}
		return slo;
	}

	public OrderUpdateSubmittedEvent demarshall(
			AQMessages.OrderUpdateSubmitted adm) {
		OrderUpdateSubmittedEvent ouse = new OrderUpdateSubmittedEvent();
		ouse.setRefOrderId(adm.getClOrdId());
		ouse.setTimeStamp(new TimeStamp());
		return ouse;
	}

	public PositionEvent demarshall(AQMessages.PositionReport adm) {
		PositionEvent pos = new PositionEvent(adm.getTradInstId(),
				new TimeStamp(), adm.getEntryPrice(), adm.getQuantity());
		return pos;
	}

	public void demarshall(AQMessages.SecurityStatus adm) {

	}

	public InformationalEvent demarshall(AQMessages.InfoEvent adm) {
		InformationalEvent ie = new InformationalEvent(new TimeStamp(
				adm.getTimestamp()), adm.getMessage());
		return ie;
	}

	public OHLCV demarshall(AQMessages.OHLC adm) {
		OHLCV ie = new OHLCV();
		ie.setOpen(adm.getOpen());
		ie.setClose(adm.getClose());
		ie.setHigh(adm.getHigh());
		ie.setLow(adm.getLow());
		ie.setTimeStamp(new TimeStamp(adm.getTimestamp()));
		ie.setMdiId(adm.getMdiId());
		return ie;
	}

	public Tick demarshall(AQMessages.Tick tick) {
		Tick ret = new Tick(tick.getMdiId(),
				new TimeStamp(tick.getTimestamp()), tick.getPrice(),
				tick.getQuantity(), tick.getTickDirection());

		return ret;
	}

	public MarketDataSnapshot demarshall(AQMessages.MarketDataSnapshot mdsm) {
		MarketDataSnapshot mds = new MarketDataSnapshot();
		mds.setMdiId(mdsm.getMdiId());
		mds.setTimeStamp(new TimeStamp(mdsm.getTimestamp()));
		mds.setBidPrices(ArrayUtils.toPrimArray(mdsm.getBidPxList()));
		mds.setAskPrices(ArrayUtils.toPrimArray(mdsm.getAskPxList()));
		mds.setBidSizes(ArrayUtils.toPrimArray(mdsm.getBidQList()));
		mds.setAskSizes(ArrayUtils.toPrimArray(mdsm.getAskQList()));
		if (mdsm.hasResend())
			mds.setResend(mdsm.getResend());
		return mds;
	}

}
