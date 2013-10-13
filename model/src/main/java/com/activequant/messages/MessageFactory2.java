package com.activequant.messages;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.streaming.PositionEvent;
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
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.messages.AQMessages.BaseMessage;

/**
 * Basic message factory for protocol buffer messages. Underdeveloped in this
 * project. At one point, we will have to copy over the factory code from one of
 * our other projects into this one.
 * 
 * @author GhostRider
 * 
 */
public class MessageFactory2 {
	private <Type> BaseMessage wrap(BaseMessage.CommandType type,
			BaseMessage.GeneratedExtension<BaseMessage, Type> extension,
			Type cmd) {
		return BaseMessage.newBuilder().setType(type)
				.setExtension(extension, cmd).build();
	}

	public BaseMessage buildMds(String mdiId, Iterable<? extends Double> bid,
			Iterable<? extends Double> ask, Iterable<? extends Double> bidQ,
			Iterable<? extends Double> askQ) {
		AQMessages.MarketDataSnapshot m = AQMessages.MarketDataSnapshot
				.newBuilder().setMdiId(mdiId).addAllBidPx(bid).addAllAskPx(ask)
				.addAllBidQ(bidQ).addAllAskQ(askQ)
				.setTimestamp(new TimeStamp().getNanoseconds()).build();
		return wrap(BaseMessage.CommandType.MDS,
				AQMessages.MarketDataSnapshot.cmd, m);
	}

	public BaseMessage buildMds(String mdiId, Iterable<? extends Double> bid,
			Iterable<? extends Double> ask, Iterable<? extends Double> bidQ,
			Iterable<? extends Double> askQ, boolean resend) {
		AQMessages.MarketDataSnapshot m = AQMessages.MarketDataSnapshot
				.newBuilder().setMdiId(mdiId).addAllBidPx(bid).addAllAskPx(ask)
				.addAllBidQ(bidQ).addAllAskQ(askQ)
				.setTimestamp(new TimeStamp().getNanoseconds()).setResend(resend).build();
		return wrap(BaseMessage.CommandType.MDS,
				AQMessages.MarketDataSnapshot.cmd, m);
	}


	
	public BaseMessage buildServerTime(long timestamp) {
		AQMessages.ServerTime l = AQMessages.ServerTime.newBuilder()
				.setTimestamp(timestamp).build();
		return wrap(BaseMessage.CommandType.SERVER_TIME,
				AQMessages.ServerTime.cmd, l);
	}

	/**
	 * 
	 * @param msg
	 * @return
	 */
	public BaseMessage buildCustomCommand(String msg) {
		AQMessages.CustomCommand l = AQMessages.CustomCommand.newBuilder()
				.setCommand(msg).build();
		return wrap(BaseMessage.CommandType.CUST_CMD,
				AQMessages.CustomCommand.cmd, l);
	}

	/**
	 * Builds a login message.
	 * 
	 * 
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	public BaseMessage buildLogin(String username, String password,
			String session) {
		AQMessages.Login l = AQMessages.Login.newBuilder()
				.setPassword(password).setUserId(username)
				.setSessionType(session).build();
		return wrap(BaseMessage.CommandType.LOGIN, AQMessages.Login.cmd, l);
	}

	/**
	 * converts a position report to google protocol buffers.
	 * 
	 * @param pe
	 * @return
	 */
	public BaseMessage convert(PositionEvent pe) {
		//
		AQMessages.PositionReport l = AQMessages.PositionReport.newBuilder()
				.setEntryPrice(pe.getPrice()).setOpenDate("")
				.setQuantity(pe.getQuantity())
				.setTradInstId(pe.getTradInstId()).build();
		return wrap(BaseMessage.CommandType.POSITION_REPORT,
				AQMessages.PositionReport.cmd, l);

	}

	public BaseMessage convert(OrderEvent oe) {
		// have to convert all possible order event types
		BaseMessage ret = null;
		if (oe instanceof OrderSubmittedEvent) {
			ret = orderSubmitted(oe.getRefOrderId());
		} else if (oe instanceof OrderUpdateSubmittedEvent) {
			ret = orderUpdateSubmitted(oe.getRefOrderId());
		} else if (oe instanceof OrderCancelSubmittedEvent) {
			ret = orderCancelSubmitted(oe.getRefOrderId());
		} else if (oe instanceof OrderAcceptedEvent) {
			ret = orderAccepted(oe.getRefOrderId());
		} else if (oe instanceof OrderUpdateRejectedEvent) {
			OrderUpdateRejectedEvent oure = (OrderUpdateRejectedEvent) oe;
			ret = orderUpdateRejected(oure.getRefOrderId(), oure.getReason());
		} else if (oe instanceof OrderCancellationRejectedEvent) {
			OrderCancellationRejectedEvent ocre = (OrderCancellationRejectedEvent) oe;
			ret = OrderCancelRejected(ocre.getOptionalInstId(),
					ocre.getRefOrderId(), ocre.getRefOrderId(), "",
					ocre.getReason(), ocre.getReason(), ocre.getReason(),
					ocre.getReason());
		} else if (oe instanceof OrderRejectedEvent) {
			OrderRejectedEvent ore = (OrderRejectedEvent) oe;
			ret = orderRejected(ore.getRefOrderId(),
					((OrderRejectedEvent) oe).getReason());
		} else if (oe instanceof OrderReplacedEvent) {
			ret = orderUpdated(oe.getRefOrderId());
		} else if (oe instanceof OrderCancelledEvent) {
			ret = orderCancelled(oe.getRefOrderId());
		} else if (oe instanceof OrderFillEvent) {
			OrderFillEvent ofe = (OrderFillEvent) oe;
			ret = executionReport2(ofe.getRefOrderId(), ofe.getExecId(),
					ofe.getSide(), ofe.getFillPrice(), ofe.getOptionalInstId(),
					ofe.getTimeStamp().getNanoseconds(), ofe.getFillAmount(),
					ofe.getLeftQuantity(), ofe.getResend());
		}

		// return it.
		return ret;
	}

	public BaseMessage OrderCancelRequest(String requestId,
			String originalClientOrderId, String symbol, OrderSide side,
			Double orderQty) {
		int s = side.getSide() == OrderSide.BUY.getSide() ? 1 : 2;
		AQMessages.OrderCancelRequest l = AQMessages.OrderCancelRequest
				.newBuilder().setClOrdId(requestId)
				.setOrgCldOrdId(originalClientOrderId).setOrderQty(orderQty)
				.setTradInstId(symbol).setSide(s).build();
		return wrap(BaseMessage.CommandType.ORD_CNCL_REQ,
				AQMessages.OrderCancelRequest.cmd, l);
	}

	public BaseMessage orderUpdateRejected(String refOrderId, String reason) {
		AQMessages.OrderUpdateRejected l = AQMessages.OrderUpdateRejected
				.newBuilder().setClOrdId(refOrderId).setReason(reason).build();
		return wrap(BaseMessage.CommandType.ORD_UPD_REJECTED,
				AQMessages.OrderUpdateRejected.cmd, l);
	}

	public BaseMessage OrderCancelRejected(String tradInstId, String clOrdId,
			String orgClOrdId, String ordStatus, String CxlRejResponseTo,
			String clxRejReason, String text, String comment) {
		//
		AQMessages.OrderCancelReject l = AQMessages.OrderCancelReject
				.newBuilder().setClOrdId(clOrdId).setClxRejReason(clxRejReason)
				.setTradInstId(tradInstId).setOrgClOrdId(orgClOrdId)
				.setOrdStatus(ordStatus).setCxlRejResponseTo(CxlRejResponseTo)
				.setText(text).setComment(comment).build();
		//
		return wrap(BaseMessage.CommandType.ORD_CNCL_REJ,
				AQMessages.OrderCancelReject.cmd, l);
	}

	public BaseMessage orderAccepted(String clOrdId) {
		//
		AQMessages.OrderAccepted l = AQMessages.OrderAccepted.newBuilder()
				.setClOrdId(clOrdId).build();
		//
		return wrap(BaseMessage.CommandType.ORD_ACCPTD,
				AQMessages.OrderAccepted.cmd, l);
	}

	public BaseMessage orderRejected(String clOrdId, String reason) {
		//
		AQMessages.OrderRejected l = AQMessages.OrderRejected.newBuilder()
				.setClOrdId(clOrdId).setReason(reason).build();
		//
		return wrap(BaseMessage.CommandType.ORD_REJ,
				AQMessages.OrderRejected.cmd, l);
	}

	//
	public BaseMessage orderSubmitted(String clOrdId) {
		//
		AQMessages.OrderSubmitted l = AQMessages.OrderSubmitted.newBuilder()
				.setClOrdId(clOrdId).build();
		//
		return wrap(BaseMessage.CommandType.ORD_SUBMITTED,
				AQMessages.OrderSubmitted.cmd, l);
	}

	//
	public BaseMessage orderUpdateSubmitted(String clOrdId) {
		//
		AQMessages.OrderUpdateSubmitted l = AQMessages.OrderUpdateSubmitted
				.newBuilder().setClOrdId(clOrdId).build();
		//
		return wrap(BaseMessage.CommandType.ORD_UPDATE_SUBMITTED,
				AQMessages.OrderUpdateSubmitted.cmd, l);
	}

	//
	public BaseMessage orderCancelSubmitted(String clOrdId) {
		//
		AQMessages.OrderCancelSubmitted l = AQMessages.OrderCancelSubmitted
				.newBuilder().setClOrdId(clOrdId).build();
		//
		return wrap(BaseMessage.CommandType.ORD_CANCEL_SUBMITTED,
				AQMessages.OrderCancelSubmitted.cmd, l);
	}

	//
	public BaseMessage orderCancelled(String clOrdId) {
		//
		AQMessages.OrderCancelled l = AQMessages.OrderCancelled.newBuilder()
				.setClOrdId(clOrdId).build();
		//
		return wrap(BaseMessage.CommandType.ORD_CANCELLED,
				AQMessages.OrderCancelled.cmd, l);
	}

	public BaseMessage orderUpdated(String clOrdId) {
		//
		AQMessages.OrderUpdated l = AQMessages.OrderUpdated.newBuilder()
				.setClOrdId(clOrdId).build();
		//
		return wrap(BaseMessage.CommandType.ORD_UPDATED,
				AQMessages.OrderUpdated.cmd, l);
	}

	//
	public BaseMessage OrderCancelReplaceReq(String mdiId, String entryDate,
			Double entryPrice, Double quantity) {
		AQMessages.PositionReport l = AQMessages.PositionReport.newBuilder()
				.setTradInstId(mdiId).setOpenDate(entryDate)
				.setEntryPrice(entryPrice).setQuantity(quantity).build();
		return wrap(BaseMessage.CommandType.POSITION_REPORT,
				AQMessages.PositionReport.cmd, l);
	}

	//
	public BaseMessage securityStatus(String tdiId, String status) {
		AQMessages.SecurityStatus n = AQMessages.SecurityStatus.newBuilder()
				.setTdiId(tdiId).setStatus(status).build();
		return wrap(BaseMessage.CommandType.SECURITY_STATUS,
				AQMessages.SecurityStatus.cmd, n);
	}

	public BaseMessage updateMktOrder(String orderId, String orgClOrdId,
			String tdiId, Double quantity, OrderSide side) {
		int s = side.getSide();
		AQMessages.OrderCancelReplaceRequest n = AQMessages.OrderCancelReplaceRequest
				.newBuilder().setClOrdId(orderId).setOrderQty(quantity)
				.setOrgClOrdId(orgClOrdId).setTradInstId(tdiId)
				.setTimeInForce(0).setTransactTime("").setSide(s).setOrdType(1)
				.build();
		return wrap(BaseMessage.CommandType.ORD_CNCL_REPL_REQ,
				AQMessages.OrderCancelReplaceRequest.cmd, n);
	}

	public BaseMessage updateLimitOrder(String orderId, String orgClOrdId,
			String tdiId, Double quantity, Double limitPrice, OrderSide side) {
		int s = side.getSide();

		AQMessages.OrderCancelReplaceRequest n = AQMessages.OrderCancelReplaceRequest
				.newBuilder().setClOrdId(orderId).setOrderQty(quantity)
				.setTradInstId(tdiId).setTimeInForce(0).setTransactTime("")
				.setSide(s).setPrice(limitPrice).setOrdType(2)
				.setOrgClOrdId(orgClOrdId).build();
		return wrap(BaseMessage.CommandType.ORD_CNCL_REPL_REQ,
				AQMessages.OrderCancelReplaceRequest.cmd, n);
	}

	public BaseMessage updateStopOrder(String orderId, String orgClOrdId,
			String tdiId, Double quantity, Double limitPrice, OrderSide side) {
		int s = side.getSide();

		AQMessages.OrderCancelReplaceRequest n = AQMessages.OrderCancelReplaceRequest
				.newBuilder().setClOrdId(orderId).setOrderQty(quantity)
				.setTradInstId(tdiId).setTimeInForce(0).setTransactTime("")
				.setSide(s).setOrdType(3).setPrice(limitPrice)
				.setOrgClOrdId(orgClOrdId).build();
		return wrap(BaseMessage.CommandType.ORD_CNCL_REPL_REQ,
				AQMessages.OrderCancelReplaceRequest.cmd, n);
	}

	/**
	 * Creates a new NewOrder base message and encapsulates limit price, etc.
	 * Order Type ID: 1 (follows FIX conformance) .
	 * 
	 * @param orderId
	 * @param tdiId
	 * @param quantity
	 * @param limitPrice
	 * @param side
	 * @return
	 */
	public BaseMessage orderMktOrder(String orderId, String tdiId,
			Double quantity, OrderSide side, int resend) {
		int s = side.getSide();
		AQMessages.NewOrder n = AQMessages.NewOrder.newBuilder()
				.setClOrdId(orderId).setOrderQty(quantity).setTradInstId(tdiId)
				.setSide(s).setOrdType(1).setResend(resend).build();
		return wrap(BaseMessage.CommandType.NEW_ORDER, AQMessages.NewOrder.cmd,
				n);
	}

	/**
	 * Creates a new NewOrder base message and encapsulates limit price, etc.
	 * Order Type ID: 2 (follows FIX conformance) .
	 * 
	 * @param orderId
	 * @param tdiId
	 * @param quantity
	 * @param limitPrice
	 * @param side
	 * @return
	 */
	public BaseMessage orderLimitOrder(String orderId, String tdiId,
			Double quantity, Double limitPrice, OrderSide side, int resend) {
		int s = side.getSide();

		AQMessages.NewOrder n = AQMessages.NewOrder.newBuilder()
				.setClOrdId(orderId).setOrderQty(quantity).setTradInstId(tdiId)
				.setSide(s).setPrice(limitPrice).setOrdType(2)
				.setResend(resend).build();
		return wrap(BaseMessage.CommandType.NEW_ORDER, AQMessages.NewOrder.cmd,
				n);
	}

	/**
	 * Creates a new NewOrder base message and encapsulates limit price, etc.
	 * Order Type ID: 3 (follows FIX conformance) .
	 * 
	 * @param orderId
	 * @param tdiId
	 * @param quantity
	 * @param limitPrice
	 * @param side
	 * @return
	 */

	public BaseMessage orderStopOrder(String orderId, String tdiId,
			Double quantity, Double limitPrice, OrderSide side, int resend) {
		int s = side.getSide();

		AQMessages.NewOrder n = AQMessages.NewOrder.newBuilder()
				.setClOrdId(orderId).setOrderQty(quantity).setTradInstId(tdiId)
				.setSide(s).setOrdType(3).setPrice(limitPrice)
				.setResend(resend).build();
		return wrap(BaseMessage.CommandType.NEW_ORDER, AQMessages.NewOrder.cmd,
				n);
	}

	public BaseMessage executionReport(String clOrdId, String execId, int side,
			String currency, double orderQty, double price, String tradInstId,
			String transactTime, String orderId, int execType, int ordStatus,
			double cumQty, double leavesQty, double avgPx, String ordType,
			String text, String comment) {

		//
		AQMessages.ExecutionReport n = AQMessages.ExecutionReport.newBuilder()
				.setClOrdId(clOrdId).setExecId(execId).setSide(side)
				.setCurrency(currency).setOrderQty(orderQty).setPrice(price)
				.setTradInstId(tradInstId).setTransactTime(transactTime)
				.setOrderId(orderId).setExecType(execType)
				.setOrdStatus(ordStatus).setCumQty(cumQty).setAvgPx(avgPx)
				.setOrdType(ordType).setText(text).setComment(comment).build();

		//
		return wrap(BaseMessage.CommandType.EXECUTION_REPORT,
				AQMessages.ExecutionReport.cmd, n);

	}

	public BaseMessage executionReport2(String clOrdId, String execId,
			String side, double price, String tradInstId, long transactTime,
			double qty, double leftQuantity, int resend) {

		AQMessages.ExecutionReport2 n = AQMessages.ExecutionReport2
				.newBuilder().setClOrdId(clOrdId).setExecId(execId)
				.setSide(side).setPrice(price).setTdiId(tradInstId)
				.setTransactTime(transactTime).setQty(qty)
				.setQuantityLeft(leftQuantity).setResend(resend).build();

		return wrap(BaseMessage.CommandType.EXECUTION_REPORT2,
				AQMessages.ExecutionReport2.cmd, n);

	}

	public BaseMessage infoEvent(TimeStamp ts, String event) {
		AQMessages.InfoEvent n = AQMessages.InfoEvent.newBuilder()
				.setTimestamp(ts.getNanoseconds()).setMessage(event).build();

		return wrap(BaseMessage.CommandType.INFO_EVENT,
				AQMessages.InfoEvent.cmd, n);
	}


	public BaseMessage tick(TimeStamp ts, String mdiId, double price, double quantity, int tickDirection) {
		AQMessages.Tick n = AQMessages.Tick.newBuilder()
				.setTimestamp(ts.getNanoseconds()).setMdiId(mdiId).
				setPrice(price).setQuantity(quantity).setTickDirection(tickDirection).build();

		return wrap(BaseMessage.CommandType.TICK,
				AQMessages.Tick.cmd, n);
	}
	
	public BaseMessage ohlc(TimeStamp ts, String mdiId, double open, double high, double low, double close) {
		AQMessages.OHLC n = AQMessages.OHLC.newBuilder()
				.setTimestamp(ts.getNanoseconds()).setMdiId(mdiId).
				setOpen(open).setHigh(high).setLow(low).setClose(close).build();

		return wrap(BaseMessage.CommandType.OHLC,
				AQMessages.OHLC.cmd, n);
	}
	
	public BaseMessage valueSet(TimeStamp ts, String type, String id, String field, String value) {
		AQMessages.ValueSet n = AQMessages.ValueSet.newBuilder()
				.setTimestamp(ts.getNanoseconds()).setTimestamp(ts.getNanoseconds()).
				setType(type).setId(id).setField(field).setValue(value).build();

		return wrap(BaseMessage.CommandType.VALUESET,
				AQMessages.ValueSet.cmd, n);
	}
	
}
