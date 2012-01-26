package com.activequant.domainmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.activequant.utils.annotations.Property;

public class SecurityChainByDate extends SecurityChain {

    private Long[] rollDates = null;
    private String[] validInstrumentIds = null;
    private Long lastHistFetchTime = null; 
    private Long lastChainUpdateTime = null;

    public SecurityChainByDate() {
        super(SecurityChainByDate.class.getCanonicalName());
        rollDates = new Long[0];
        validInstrumentIds = new String[0];
    }

    public void add(String instrumentId, Long rollDate8) {
        List<Tuple<Long, String>> chainList = new ArrayList<Tuple<Long, String>>();
        //
        for (int i = 0; i < rollDates.length; i++) {
            if(rollDates[i]!=rollDate8)chainList.add(new Tuple<Long, String>(rollDates[i], validInstrumentIds[i]));
        }
        chainList.add(new Tuple<Long, String>(rollDate8, instrumentId));
        //
        sortAndSetChainList(chainList);
    }

    public void add(String[] instrumentId, Long rollDate8[]) {
        for(int i=0;i<instrumentId.length;i++)
        {
            add(instrumentId[i], rollDate8[i]);            
        }
    }

    private void sortAndSetChainList(List<Tuple<Long, String>> chainList) {
        //
        Collections.sort(chainList, new Comparator<Tuple<Long, String>>() {
            @Override
            public int compare(Tuple<Long, String> o1, Tuple<Long, String> o2) {
                return (int) (o1.getA() - o2.getA());
            }
        });
        //
        rollDates = new Long[chainList.size()];
        validInstrumentIds = new String[chainList.size()];

        for (int i = 0; i < chainList.size(); i++) {
            rollDates[i] = chainList.get(i).getA();
            validInstrumentIds[i] = chainList.get(i).getB();
        }
    }

    @Override
    public String getId() {
        return "SECCHAIN.DATE." + super.getChainName();
    }

    @Property()
    public Long[] getRollDates() {
        return rollDates;
    }

    public void setRollDates(Long[] rollDates) {
        this.rollDates = rollDates;
    }

    @Property()
    public String[] getValidInstrumentIDs() {
        return validInstrumentIds;
    }

    public void setValidInstrumentIDs(String[] validInstrument) {
        this.validInstrumentIds = validInstrument;
    }

    @Property
    public Long getLastHistFetchTime() {
        return lastHistFetchTime;
    }

    public void setLastHistFetchTime(Long lastHistFetchTime) {
        this.lastHistFetchTime = lastHistFetchTime;
    }

    @Property
    public Long getLastChainUpdateTime() {
        return lastChainUpdateTime;
    }

    public void setLastChainUpdateTime(Long lastChainUpdateTime) {
        this.lastChainUpdateTime = lastChainUpdateTime;
    }

}
