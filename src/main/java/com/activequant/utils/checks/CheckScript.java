package com.activequant.utils.checks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.utils.ArrayUtils;
import com.activequant.utils.mail.SendMail;

/**
 * 
 * Various database checks and statistics. Rather statistics than checks. 
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
	
	public CheckScript(String springInitFile, String target)
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
        
        // dump the providers.
        String[] providers = mdiDao.getProviders();
        setData(data, row++, "Providers", ""+ArrayUtils.toString(providers));
        
        for(int i=0;i<providers.length;i++)
        {
            setData(data, row++, " #MDIs for "+providers[i], ""+mdiDao.countForAttributeValue("MDPROVIDER", providers[i]));
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date midnightThisMorning = sdf.parse(sdf.format(new Date()));
        
        // dump instruments and MDIs that were generated today. 
        String[] mdiids = mdiDao.findIDsWhereCreationDateBetween(new TimeStamp(midnightThisMorning), new TimeStamp());
        StringBuffer mailBody = new StringBuffer("<h1>General information</h1>"+sendMail.generateHtmlTable(new String[]{"Property", "Value"}, data));
        
        mailBody.append("<h1>New MDIs</h1>"); 
        for(String id : mdiids)
        {
            mailBody.append(id + "<br/>");
        }
        
        String[] instruments = idao.findIDsWhereCreationDateBetween(new TimeStamp(midnightThisMorning), new TimeStamp());
        mailBody.append("<h1>New Instruments</h1>"); 
        for(String id : instruments)
        {
            mailBody.append(id + "<br/>");
        }
        
        System.out.println(mailBody);
        
        // dump the amount of MDIs created today. 
        if(target.contains(",")){
                sendMail.sendMail(target.split(","), "Statistics", mailBody.toString());
        }
        else
        {
            sendMail.sendMail(new String[]{target}, "Statistics",mailBody.toString());
        }
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
		String target = args[1];
		System.out.println("Using spring configuration " + springFile+". Sending to " + target);
		new CheckScript(springFile, target);
	}

}
