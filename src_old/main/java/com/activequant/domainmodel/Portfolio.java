package com.activequant.domainmodel;

import java.util.List;

import com.activequant.domainmodel.annotations.Property;
import com.activequant.utils.ArrayUtils;

public class Portfolio extends PersistentEntity {

    private String accountId;
    private String[] tradeableInstrumentIds = new String[0];
    private double[] positions = new double[0];
    private double[] entryPrice = new double[0];

    public Portfolio() {
        super(Portfolio.class.getCanonicalName());
    }

    @Property
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String getId() {
        return nullSafe(accountId);
    }

    @Property
    public String[] getTradeableInstrumentIds() {
        return tradeableInstrumentIds;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setTradeableInstrumentIds(String[] positionIds) {
        this.tradeableInstrumentIds = positionIds;
    }

    @Property
	public double[] getPositions() {
		return positions;
	}

	public void setPositions(double[] positions) {
		this.positions = positions;
	}

	@Property
	public double[] getEntryPrice() {
		return entryPrice;
	}

	public void setEntryPrice(double[] entryPrice) {
		this.entryPrice = entryPrice;
	}
	
	
	public double getPosition(String tradeableId){
		int index = -1;
		for(int i=0;i<tradeableInstrumentIds.length;i++){
			if(tradeableInstrumentIds[i].equals(tradeableId)){
				index = i;
				break;
			}
		}
		if(index!=-1){
			return positions[index];
		}
		return 0.0; 
	}


	public double getOpenPrice(String tradeableId){
		int index = -1;
		for(int i=0;i<tradeableInstrumentIds.length;i++){
			if(tradeableInstrumentIds[i].equals(tradeableId)){
				index = i;
				break;
			}
		}
		if(index!=-1){
			return entryPrice[index];
		}
		return 0.0; 
	}
	
	public void setPosition(String tradeableId, double price, double quantity){
		if(tradeableInstrumentIds==null){
			tradeableInstrumentIds = new String[0];
			positions = new double[0];
			entryPrice = new double[0];
		}
		int index = -1;
		for(int i=0;i<tradeableInstrumentIds.length;i++){
			if(tradeableInstrumentIds[i].equals(tradeableId)){
				index = i;
				break;
			}
		}
		if(index==-1){
			List<String> l = ArrayUtils.asList(tradeableInstrumentIds);
			l.add(tradeableId);
			tradeableInstrumentIds = ArrayUtils.asArray(l, String.class);
			
			double[] newPos = new double[positions.length+1];
			System.arraycopy(positions, 0, newPos, 0, positions.length);
			newPos[newPos.length-1] = quantity;
			positions = newPos;
			
			double[] newPrice = new double[entryPrice.length+1];
			System.arraycopy(entryPrice, 0, newPrice, 0, entryPrice.length);
			newPrice[newPrice.length-1] = price;
			entryPrice = newPrice;			
		}
		else{
			entryPrice[index] = price; 
			positions[index] = quantity; 
		}
	}

}
