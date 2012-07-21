package com.activequant.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.SecurityChain;
import com.activequant.domainmodel.SecurityChainByDate;
import com.activequant.interfaces.dao.IDaoFactory;

/**
 * Imports recursively all ".csv" file (case sensitive) from a start folder.
 * Requires that the user specifies a market data provider name and a time
 * frame.
 * 
 * Date and time must be to separate columns.
 * 
 * Date and time have to be in the format "yyyyMMdd", respectively
 * "HH:mm:ss.SSS". The file must have a header and must be comma separated.
 * Decimal separator must be a ".". Fields must only contain double values.
 * Filename of csv file is used as market data instrument provider specific
 * name.
 * 
 * 
 * @author ustaudinger
 * 
 */
public class PrintSecurityChain {

    private final ApplicationContext appContext;

    public PrintSecurityChain(String chainName) throws Exception {

        appContext = new ClassPathXmlApplicationContext("fwspring.xml");
        IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
        SecurityChain sc = idf.securityChainDao().load(chainName);
        if(sc instanceof SecurityChainByDate)
        {
            SecurityChainByDate scbd = (SecurityChainByDate)sc;
            for(int i=0;i<scbd.getRollDates().length;i++)
            {
                System.out.println(scbd.getRollDates()[i]+"\t\t"+scbd.getValidInstrumentIDs()[i]);
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        new PrintSecurityChain(args[0]);
    }

}
