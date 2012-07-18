package com.activequant.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.hbase.TagDao;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.dao.IDaoFactory;

/**
 * Can handle only Instruments and MarketDataInstruments.  
 * Calls tagDao.tag(Instrument.class.getSimpleName(), s, tag); 
 * or the pendant for MarketDataInstrument.  
 * 
 * @author ustaudinger
 *
 */
public class AutoTagger {

	class TagDesc{
		String tableName; 
		String pattern; 
		String[] tagValues;
	}
	
	private String tagDescriptionFile = "tags.def";
	private List<TagDesc> tagDescs = new ArrayList<TagDesc>();
	private IDaoFactory idf;
	private TagDao tagDao; 
	
	private void parseTagDescFile() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(AutoTagger.class.getResourceAsStream(tagDescriptionFile)));
		String l = br.readLine();
		while(l!=null){
			// 
			if(!l.startsWith("#"))
			{
				String[] p = l.split(";");
				// ok, parse it. 
				String tableName = p[0];
				String pattern = p[1];
				String[] tags = p[2].split(" ");
				TagDesc td = new TagDesc();
				td.tableName = tableName;
				td.pattern = pattern; 
				td.tagValues = tags; 
				tagDescs.add(td);
			}
			// 
			l = br.readLine();
		}
		
	}
	
	public AutoTagger() throws IOException, DaoException{
		
        ApplicationContext appContext = new ClassPathXmlApplicationContext("fwspring.xml");
        idf = (IDaoFactory) appContext.getBean("ibatisDao");
        tagDao = appContext.getBean("tagDao", TagDao.class);
		parseTagDescFile();
		for(TagDesc td : tagDescs){
			process(td);
		}
	}
	
	private void process(TagDesc td) throws DaoException{
		// find all IDs that match
		// get the corresponding mapper ... 
		
		if(td.tableName.equals(Instrument.class.getSimpleName())){
			// get the instrument dao.
			String[] ids = idf.instrumentDao().findIdsLike(td.pattern);
			for(String s : ids){
				for(String tag : td.tagValues)
					tagDao.tag(Instrument.class.getSimpleName(), s, tag);
			}
		}
		else if(td.tableName.equals(MarketDataInstrument.class.getSimpleName())){
			// get the instrument dao.
			String[] ids = idf.mdiDao().findIdsLike(td.pattern);
			for(String s : ids){
				for(String tag : td.tagValues)
					tagDao.tag(MarketDataInstrument.class.getSimpleName(), s, tag);
			}
		}
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws DaoException 
	 */
	public static void main(String[] args) throws IOException, DaoException {
		new AutoTagger();
	}

}
