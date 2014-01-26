package com.activequant.messages;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.messages.AQMessages.BaseMessage;

/**
 * Used to construct the different messages.
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

	public BaseMessage buildLogin(String username, String password,
			String sessionType) {
		AQMessages.Login l = AQMessages.Login.newBuilder().setUserId(username)
				.setPassword(password).setSessionType(sessionType).build();
		return wrap(BaseMessage.CommandType.LOGIN, AQMessages.Login.cmd, l);
	}

	public BaseMessage buildLoginResponse(String status) {
		AQMessages.LoginResponse l = AQMessages.LoginResponse.newBuilder()
				.setStatus(status).build();
		return wrap(BaseMessage.CommandType.LOGIN_RESPONSE,
				AQMessages.LoginResponse.cmd, l);
	}

	public BaseMessage buildMds(String mdiId, double bid, double ask,
			double bidQ, double askQ) {
		AQMessages.MarketDataSnapshot l = AQMessages.MarketDataSnapshot
				.newBuilder().setMdiId(mdiId).addBidPx(bid).addAskPx(ask)
				.addBidQ(bidQ).addAskQ(askQ)
				.setTimestamp(new TimeStamp().getNanoseconds()).build();
		return wrap(BaseMessage.CommandType.MDS,
				AQMessages.MarketDataSnapshot.cmd, l);
	}

	public BaseMessage buildOhlc(String mdiId, int timeFrameMinutes,
			double open, double high, double low, double close, double volume) {
		AQMessages.OHLC l = AQMessages.OHLC.newBuilder()
				.setMdiId(mdiId).setOpen(open).setHigh(high).setLow(low)
				.setClose(close).setVolume(volume).setTimeframe(timeFrameMinutes)
				.setTimestamp(new TimeStamp().getNanoseconds()).build();
		return wrap(BaseMessage.CommandType.OHLC,
				AQMessages.OHLC.cmd, l);
	}

	public BaseMessage buildServerTime(long t) {
		AQMessages.ServerTime l = AQMessages.ServerTime.newBuilder()
				.setTimestamp(t).build();
		return wrap(BaseMessage.CommandType.SERVER_TIME,
				AQMessages.ServerTime.cmd, l);
	}

	public BaseMessage buildAccountDataMessage(String key, String value) {
		AQMessages.AccountDataMessage l = AQMessages.AccountDataMessage
				.newBuilder().setValue(value).setType(key).build();
		return wrap(BaseMessage.CommandType.ACCT_DATA,
				AQMessages.AccountDataMessage.cmd, l);
	}

	public BaseMessage buildPositionReport(String mdiId, String entryDate,
			Double entryPrice, Double quantity) {
		AQMessages.PositionReport l = AQMessages.PositionReport.newBuilder()
				.setTradInstId(mdiId).setOpenDate(entryDate)
				.setEntryPrice(entryPrice).setQuantity(quantity).build();
		return wrap(BaseMessage.CommandType.POSITION_REPORT,
				AQMessages.PositionReport.cmd, l);
	}

	public BaseMessage buildExecReport(String clOrdId, String execId,
			OrderSide side, String currency, Double orderQty, Double price,
			String acmTdiId, String transactTime, String orderId,
			String execType, Double cumQty, Double leavesQty, Double avgPx,
			String ordType, String timeInForce, String text, String massReqId,
			int ordStatus) {

		//
		AQMessages.ExecutionReport.Builder l = AQMessages.ExecutionReport
				.newBuilder();
		l.setClOrdId(clOrdId);
		l.setExecId(execId);
		l.setSide(side.getSide());
		l.setCurrency(currency);
		l.setOrderQty(orderQty);
		l.setPrice(price);
		l.setOrdStatus(ordStatus);
		l.setTradInstId(acmTdiId);
		l.setTransactTime(transactTime);
		l.setOrderId(orderId);
		l.setExecType(Integer.parseInt(execType));
		l.setCumQty(cumQty);
		l.setLeavesQty(leavesQty);
		l.setAvgPx(avgPx);
		l.setOrdType(ordType);
		if (text != null)
			l.setText(text);
		if (massReqId != null)
			l.setMassStatusReqId(massReqId);
		//
		return wrap(BaseMessage.CommandType.EXECUTION_REPORT,
				AQMessages.ExecutionReport.cmd, l.build());
	}

	public BaseMessage OrderCancelRejected(String mdiId, String entryDate,
			Double entryPrice, Double quantity) {
		AQMessages.PositionReport l = AQMessages.PositionReport.newBuilder()
				.setTradInstId(mdiId).setOpenDate(entryDate)
				.setEntryPrice(entryPrice).setQuantity(quantity).build();
		return wrap(BaseMessage.CommandType.POSITION_REPORT,
				AQMessages.PositionReport.cmd, l);
	}

}
