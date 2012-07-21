package com.activequant.tools.mapping;

import com.activequant.dao.mybatis.InstrumentDao;
import com.activequant.domainmodel.MarketDataInstrument;

public class BBMapper implements IMapper {

	private InstrumentDao idao; 
	
	public BBMapper(InstrumentDao idao){
		this.idao = idao; 
	}
	
    public void map(MarketDataInstrument mdi) {
        if (mdi.getMdProvider().equals("BBGT")) {
            String psid = mdi.getProviderSpecificId();
            // try to guess it.
            if(psid.contains(" ")){
            	String[] parts = psid.split(" ");
            	if(parts.length==2){
            		String ticker = parts[0];
            		// look if we have an instrument where the name corresponds to the ticker. 
            		String[] ids = idao.findIDs("NAME", ticker);
            		if(ids.length==1){
            			mdi.setInstrumentId(ids[0]);
            		}
            	}
            }

        }
    }

}
