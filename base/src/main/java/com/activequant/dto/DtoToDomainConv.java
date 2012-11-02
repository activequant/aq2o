package com.activequant.dto;

import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import com.activequant.domainmodel.backoffice.ClearedTrade;
import com.activequant.domainmodel.backoffice.ClearerAccountSnap;
import com.activequant.domainmodel.backoffice.OrderFill;
import com.activequant.domainmodel.exceptions.InvalidDataException;

public class DtoToDomainConv {

    // see http://oval.sourceforge.net/userguide.html
    private Validator validator = new Validator();
	private Util util = new Util();

    public OrderFill convert(OrderFillDto dto) throws InvalidDataException {
    	
        OrderFill ret = new OrderFill();
        ret.setTradeableId(util.ensureDoubleDigitBBID(dto.tradeableId.toUpperCase()));
        ret.setPrice(dto.price);
        ret.setQuantity(dto.quantity);
        ret.setBrokerId(dto.brokerId);
        ret.setProviderId(dto.providerId);
        ret.setTimeStampInNanos(dto.timeStampInNanos);
        ret.setOrderSide(dto.orderSide.toString());
        ret.setProviderAccountId(dto.providerAccountId);
        ret.setBrokerAccountId(dto.brokerAccountId);        
        ret.setOrderFillId(dto.orderFillId);
        ret.setOrderId(dto.orderId);        
        ret.setRouteId(dto.routeId);
        
        validate(ret);
        return ret;
    }
    
    private void validate(Object ret) throws InvalidDataException
    {
        List<ConstraintViolation> violations = validator.validate(ret);
        if (violations.size() > 0) {
            throw new InvalidDataException(violations.toString());
        }
    }

    public ClearedTrade convert(ClearedTradeDto dto) throws InvalidDataException {
        ClearedTrade ret = new ClearedTrade();
        ret.setTradeableId(util.ensureDoubleDigitBBID(dto.tradeableId.toUpperCase()));
        ret.setPrice(dto.price);
        ret.setQuantity(dto.quantity);        
        ret.setClearedTradeId(dto.clearedTradeId);
        ret.setTimeStampInNanos(dto.timeStampInNanos);
        ret.setOrderSide(dto.orderSide.toString());
        ret.setClearingAccountId(dto.clearingAccountId);
        ret.setStatus(dto.status.toString());
        ret.setClearingFee(dto.clearingFee);
        ret.setBrokerFee(dto.brokerFee);
        ret.setExchangeFee(dto.exchangeFee);
        ret.setClearingFeeCurrency(dto.clearingFeeCurrency);
        ret.setBrokerFeeCurrency(dto.brokerFeeCurrency);
        ret.setExchangeFeeCurrency(dto.exchangeFeeCurrency);
        ret.setCusip(dto.cusip);      
        ret.setUniqueId(dto.uniqueId);
        ret.setDate8(dto.date8);
        ret.setSubAccountId(dto.subAccountId);
        validate(ret);
        return ret;
    }
    
    public ClearerAccountSnap convert(ClearerAccountStatementDto dto) throws InvalidDataException {    
    	ClearerAccountSnap ccs = new ClearerAccountSnap();      	
    	ccs.setAccountId(dto.accountId);
    	ccs.setSubAccountId(dto.subAccountId);
    	ccs.setCurrency(dto.currency);    	
    	ccs.setDate8(dto.date8);
    	ccs.setAssumedTargetCurrency(dto.assumedTargetCurrency);
    	ccs.setCrossRate(dto.crossRate);
    	ccs.setInitialMargin(dto.initialMargin);
    	ccs.setMaintenanceMargin(dto.maintenanceMargin);
    	ccs.setAvailableMargin(dto.availableMargin);
    	ccs.setBeginningAccountBalance(dto.beginningAccountBalance);
    	ccs.setEndingAccountBalance(dto.endingAccountBalance);
    	ccs.setOpenTradeEquity(dto.openTradeEquity);
    	ccs.setTotalEquity(dto.totalEquity); 	   
        validate(ccs);
        return ccs;
    }
    
}
