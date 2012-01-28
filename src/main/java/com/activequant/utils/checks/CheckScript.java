package com.activequant.utils.checks;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.utils.ArrayUtils;
import com.activequant.utils.mail.SendMail;

/**
 * 
 * Various database checks and statistics. 
 * 
 * @author ustaudinger
 * 
 */
public class CheckScript {

	private final ApplicationContext appContext;
	private final IDaoFactory idf;
	private final IInstrumentDao idao;
	private final IMarketDataInstrumentDao mdiDao; 
	private SendMail sendMail; 
	
	public CheckScript(String springInitFile)
			throws Exception {

		appContext = new ClassPathXmlApplicationContext(springInitFile);
		sendMail = (SendMail) appContext.getBean("sendMail");
		idf = (IDaoFactory) appContext.getBean("ibatisDao");
		idao = idf.instrumentDao();
		mdiDao = idf.mdiDao();
		
		int rows = 100; 
		int columns = 2; 
		Object[][] data = new Object[rows][columns];
		
		// 
		int row =0; 
		setData(data, row++, "#Instruments", ""+idao.count());
		setData(data, row++, "#MDIs", ""+mdiDao.count());
        setData(data, row++, "#SecChains", ""+idf.securityChainDao().count());
        setData(data, row++, "#Tradeables", ""+idf.tradeableDao().count());
        setData(data, row++, "#Countries", ""+idf.countryDao().count());
        setData(data, row++, "#Venues", ""+idf.venueDao().count());
        
        // try to load some data. 
        String[] providers = mdiDao.getProviders();
        setData(data, row++, "Providers", ""+ArrayUtils.toString(providers));
        
        for(int i=0;i<providers.length;i++)
        {
            setData(data, row++, " #MDIs for "+providers[i], ""+mdiDao.countForAttributeValue("MDPROVIDER", providers[i]));
        }
        
        sendMail.sendMail(new String[]{"ustaudinger@activequant.com"}, "Statistics", sendMail.generateHtmlTable(new String[]{"Property", "Value"}, data));
	}
	
	private void setData(Object[][] data, int row, String property, String value){
	    data[row][0] = property;
	    data[row][1] = value;
        
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String springFile = args[0];
		System.out.println("Using spring configuration " + springFile);
		new CheckScript(springFile);

	}

}
