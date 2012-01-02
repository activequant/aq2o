package com.activequant.tools.mapping;

import com.activequant.domainmodel.MarketDataInstrument;

public class CSIMapper implements IMapper {

    public void map(MarketDataInstrument mdi) {
        if (mdi.getMdProvider().equals("CSI")) {
            String psid = mdi.getProviderSpecificId();
            // try to guess it.

        }
    }

}
