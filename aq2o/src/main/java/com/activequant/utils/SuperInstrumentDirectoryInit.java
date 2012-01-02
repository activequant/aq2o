package com.activequant.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.DaoException;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.SuperInstrument;

public class SuperInstrumentDirectoryInit {
    private Logger log = Logger.getLogger(SuperInstrumentDirectoryInit.class);
    private ApplicationContext appContext = new ClassPathXmlApplicationContext("initdbtest.xml");
    private IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
    private IInstrumentDao iDao = idf.instrumentDao();
    private final IMarketDataInstrumentDao mdiDao = idf.mdiDao();

    static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    static boolean nonEmpty(String string) {
        return !isEmpty(string);
    }

    public SuperInstrumentDirectoryInit(String fileName) throws FileNotFoundException, IOException {

        // initialize the dao layer.
        /*
         * final CsvLabeledReader reader = Csv.readWithLabels(new
         * FileReader(fileName)); int failed = 0; int updated = 0;
         * 
         * for (Map<String, String> map : reader.rows()) { final String symbol =
         * map.get("Symbol");
         * 
         * SuperInstrument si = new SuperInstrument();
         * 
         * try { String currency = map.get("Currency"); Double tickSize =
         * Double.valueOf(map.get("TickSize")); Double fpv =
         * Double.valueOf(map.get("PointValue"));
         * 
         * String exchange = map.get("Exchange"); String name =
         * map.get("Futures"); String type = map.get("Type");
         * 
         * // String csiSymbol = map.get("CSISymbol"); String twSymbol =
         * map.get("TWSymbol"); String aimSymbol = map.get("AIMSymbol"); String
         * bloombergSymbol = map.get("BloombergSymbol"); String bbYellowKey =
         * map.get("YellowKey");
         * 
         * si.setShortName(symbol); si.setName(name); si.setType(type);
         * si.setExchange(exchange); si.setTickSize(tickSize);
         * si.setTickValue(fpv / (1.0 / tickSize)); si.setCurrency(currency);
         * 
         * si.setTwSymbol(twSymbol); si.setAimSymbol(aimSymbol);
         * si.setBbYellowKey(bbYellowKey);
         * si.setBloombergSymbol(bloombergSymbol); si.setCsiSymbol(csiSymbol);
         * // si.setValidInstrument(new String[]{"ABCD", "DEF"});
         * 
         * updated++; iDao.update(si); // iDao.create(si);
         * 
         * // for now ... check if we have an MDI with the aim symbol for // CSI
         * ... so that we can link it. // will not create one, just load one.
         * 
         * try { MarketDataInstrument mdi = mdiDao.load("CSI_" + aimSymbol); if
         * (mdi != null) { String instrumentId = mdi.getInstrumentId(); if
         * (instrumentId == null) { mdi.setInstrumentId(si.getId());
         * mdiDao.update(mdi); } else {
         * log.info("Mapping exists already, not overwriting the existing mapping."
         * ); } } } catch (DaoException e1) { log.error("Exception", e1); }
         * 
         * } catch (RuntimeException e) { failed++; log.error("Asset [" + symbol
         * + "] update failed. ", e); } catch (DaoException e) { failed++;
         * log.error("Asset [" + symbol + "] update failed. ", e); } }
         * log.info("Import statistics: Success: " + updated + " / failed: " +
         * failed);
         */
    }

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length == 1) {
            new SuperInstrumentDirectoryInit(args[0]);
        } else
            System.out.println("Provide filename to import. ");
    }

}
