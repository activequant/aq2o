package com.activequant.tools.mapping;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.DaoException;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.MarketDataInstrument;

/**
 * 
 * this tool tries to map unassigned MDIs to Instruments.
 * 
 * @author ustaudinger
 * 
 */
public class AutoMapper {

    private ApplicationContext appContext = new ClassPathXmlApplicationContext("initdblive.xml");
    private IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
    private IMarketDataInstrumentDao mdidao = idf.mdiDao();
    private LinkedBlockingQueue<String> fileNameQueue = new LinkedBlockingQueue<String>();
    private String mdProvider;
    private IMapper[] mappers;

    public AutoMapper() throws DaoException {
        mappers = new IMapper[] { new CSIMapper() };
        map();
    }

    private void map() throws DaoException {
        int totalMDIs = mdidao.count();
        int sliceSize = 200;
        int c = 0;
        int slices = totalMDIs / sliceSize + 1;
        for (int i = 0; i < (slices); i++) {
            int startId = i * sliceSize;
            int endId = (i + 1) * sliceSize - 1;
            //
            String[] mdiIds = mdidao.findIDs(startId, endId);
            for (String mdiId : mdiIds) {
                MarketDataInstrument mdi = mdidao.load(mdiId);
                if (mdi.getInstrumentId() == null) {
                    for (IMapper mapper : mappers) {
                        mapper.map(mdi);
                    }
                }
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        new AutoMapper();
    }

}
