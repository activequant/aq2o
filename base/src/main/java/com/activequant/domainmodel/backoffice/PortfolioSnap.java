package com.activequant.domainmodel.backoffice;

import java.util.List;

import com.activequant.domainmodel.annotations.Property;
import com.activequant.dto.PositionDto;
import com.activequant.utils.ArrayUtils;

public class PortfolioSnap extends Snapshot {

	public PortfolioSnap(String className) {
		super(className);
	}
	
	public PortfolioSnap(){
		super(PortfolioSnap.class.getCanonicalName());		
	}

	public String ownerObjectId;
	public String[] tradeableId = new String[0];
	public Double[] valuationPrice = new Double[0];
	public Double[] quantity = new Double[0];
	public Double[] entryPrice = new Double[0];
	public String[] side = new String[0];
	public String[] subAcctId = new String[0];
	public String[] clearerAcctId = new String[0];
	public String[] clearer = new String[0]; 
	public Long[] entryDate8 = new Long[0];
	public Long[] positionDate8 = new Long[0];
	public String[] uniqueId = new String[0];
	public Double[] marketValue = new Double[0];

	public void addPosition(PositionDto dto){
		tradeableId = add(tradeableId, dto.tradeableId);              
		valuationPrice = add(valuationPrice, dto.valuationPrice);
		quantity = add(quantity, dto.quantity);
		entryPrice = add(entryPrice, dto.entryPrice);
		side = add(side, dto.side.toString());
		subAcctId = add(subAcctId, dto.subAcctId);
		clearerAcctId =  add(clearerAcctId, dto.clearerAcctId);
		clearer =  add(clearer, dto.clearer);
		entryDate8 = add(entryDate8, dto.entryDate8);
		positionDate8 = add(positionDate8, dto.positionDate8);
		uniqueId = add(uniqueId, dto.uniqueId);
		marketValue = add(marketValue, dto.marketValue);
	}
	
	private String[] add(String[] in, String val)
	{
		List<String> ls = ArrayUtils.asList(in);
		ls.add(val);
		return ArrayUtils.asArray(ls, String.class);
	}
	

	private Double[] add(Double[] in, Double val)
	{
		List<Double> ls = ArrayUtils.asList(in);
		ls.add(val);
		return ArrayUtils.asArray(ls, Double.class);
	}

	private Long[] add(Long[] in, Long val)
	{
		List<Long> ls = ArrayUtils.asList(in);
		ls.add(val);
		return ArrayUtils.asArray(ls, Long.class);
	}
	

	@Property
	public String getOwnerObjectId() {
		return ownerObjectId;
	}

	public void setOwnerObjectId(String ownerObjectId) {
		this.ownerObjectId = ownerObjectId;
	}
	@Property
	public String[] getTradeableId() {
		return tradeableId;
	}

	public void setTradeableId(String[] tradeableId) {
		this.tradeableId = tradeableId;
	}
	@Property
	public Double[] getValuationPrice() {
		return valuationPrice;
	}

	public void setValuationPrice(Double[] valuationPrice) {
		this.valuationPrice = valuationPrice;
	}
	@Property
	public Double[] getQuantity() {
		return quantity;
	}

	public void setQuantity(Double[] quantity) {
		this.quantity = quantity;
	}
	@Property
	public Double[] getEntryPrice() {
		return entryPrice;
	}

	public void setEntryPrice(Double[] entryPrice) {
		this.entryPrice = entryPrice;
	}
	@Property
	public String[] getSide() {
		return side;
	}

	public void setSide(String[] side) {
		this.side = side;
	}
	@Property
	public String[] getSubAcctId() {
		return subAcctId;
	}

	public void setSubAcctId(String[] subAcctId) {
		this.subAcctId = subAcctId;
	}
	@Property
	public String[] getClearerAcctId() {
		return clearerAcctId;
	}

	public void setClearerAcctId(String[] clearerAcctId) {
		this.clearerAcctId = clearerAcctId;
	}
	@Property
	public String[] getClearer() {
		return clearer;
	}

	public void setClearer(String[] clearer) {
		this.clearer = clearer;
	}
	@Property
	public Long[] getEntryDate8() {
		return entryDate8;
	}

	public void setEntryDate8(Long[] entryDate8) {
		this.entryDate8 = entryDate8;
	}
	@Property
	public Long[] getPositionDate8() {
		return positionDate8;
	}

	public void setPositionDate8(Long[] positionDate8) {
		this.positionDate8 = positionDate8;
	}
	@Property
	public String[] getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String[] uniqueId) {
		this.uniqueId = uniqueId;
	}
	@Property
	public Double[] getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(Double[] marketValue) {
		this.marketValue = marketValue;
	}

	@Override
	@Property
	public String getNonUniqueID() {
		return nullSafe("PSNAP." + nullSafe(ownerObjectId));
	}
	
	

}
