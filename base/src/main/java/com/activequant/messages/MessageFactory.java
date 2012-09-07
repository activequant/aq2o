package com.activequant.messages;

import com.activequant.domainmodel.TimeStamp;
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
public class MessageFactory {
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

	public BaseMessage buildServerTime(long timestamp) {
		AQMessages.ServerTime l = AQMessages.ServerTime.newBuilder()
				.setTimestamp(timestamp).build();
		return wrap(BaseMessage.CommandType.SERVER_TIME,
				AQMessages.ServerTime.cmd, l);
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

	public BaseMessage orderMktOrder(String orderId, String tdiId,
			Double quantity, OrderSide side) {
		int s = side.getSide();
		AQMessages.NewOrder n = AQMessages.NewOrder.newBuilder()
				.setClOrdId(orderId).setOrderQty(quantity).setTradInstId(tdiId)
				.setSide(s).setOrdType(1).build();
		return wrap(BaseMessage.CommandType.NEW_ORDER, AQMessages.NewOrder.cmd,
				n);
	}

	public BaseMessage orderLimitOrder(String orderId, String tdiId,
			Double quantity, Double limitPrice, OrderSide side) {
		int s = side.getSide();

		AQMessages.NewOrder n = AQMessages.NewOrder.newBuilder()
				.setClOrdId(orderId).setOrderQty(quantity).setTradInstId(tdiId)
				.setSide(s).setPrice(limitPrice).setOrdType(2).build();
		return wrap(BaseMessage.CommandType.NEW_ORDER, AQMessages.NewOrder.cmd,
				n);
	}

	public BaseMessage orderStopOrder(String orderId, String tdiId,
			Double quantity, Double limitPrice, OrderSide side) {
		int s = side.getSide();

		AQMessages.NewOrder n = AQMessages.NewOrder.newBuilder()
				.setClOrdId(orderId).setOrderQty(quantity).setTradInstId(tdiId)
				.setSide(s).setOrdType(3).setPrice(limitPrice).build();
		return wrap(BaseMessage.CommandType.NEW_ORDER, AQMessages.NewOrder.cmd,
				n);
	}

	public BaseMessage executionReport(String clOrdId, String execId, int side,
			String currency, double orderQty, double price, String tradInstId,
			String transactTime, String orderId, int execType, int ordStatus,
			double cumQty, double leavesQty, double avgPx, String ordType,
			String text, String comment) {

		AQMessages.ExecutionReport n = AQMessages.ExecutionReport.newBuilder()
				.setClOrdId(clOrdId).setExecId(execId).setSide(side)
				.setCurrency(currency).setOrderQty(orderQty).setPrice(price)
				.setTradInstId(tradInstId).setTransactTime(transactTime)
				.setOrderId(orderId).setExecType(execType)
				.setOrdStatus(ordStatus).setCumQty(cumQty).setAvgPx(avgPx)
				.setOrdType(ordType).setText(text).setComment(comment).build();

		return wrap(BaseMessage.CommandType.EXECUTION_REPORT,
				AQMessages.ExecutionReport.cmd, n);

	}
}
