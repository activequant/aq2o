package com.activequant.server.trading;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage;

/**
 * Used to construct the different messages.
 * 
 * @author ustaudinger
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
	

	public BaseMessage buildMds(String mdiId, double bid, double ask, double bidQ, double askQ) {
		AQMessages.MarketDataSnapshot l = AQMessages.MarketDataSnapshot.newBuilder()
				.setMdiId(mdiId).addBidPx(bid).addAskPx(ask).addBidQ(bidQ).addAskQ(askQ).setTimestamp(new TimeStamp().getNanoseconds()).build();
		return wrap(BaseMessage.CommandType.MDS,
				AQMessages.MarketDataSnapshot.cmd, l);
	}

	public BaseMessage buildServerTime(long t) {
		AQMessages.ServerTime l = AQMessages.ServerTime.newBuilder()
				.setTimestamp(t).build();
		return wrap(BaseMessage.CommandType.SERVER_TIME,
				AQMessages.ServerTime.cmd, l);
	}

}
