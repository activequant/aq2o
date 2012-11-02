package com.activequant.domainmodel.backoffice;


public class BrokerAccount extends Account {
  
    
    public BrokerAccount(){
        super(BrokerAccount.class.getCanonicalName());
    }
    
    @Override
    public String getId() {
        return "BRKACT."+nullSafe(getAccountId())+"."+nullSafe(getLegalEntity());
    }

}
