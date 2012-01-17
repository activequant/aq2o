package com.activequant.tools.mapping;

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
public class Mdi2InstrumentMapper {

	private IMarketDataInstrumentDao mdidao;
	private IMapper[] mappers;

	public Mdi2InstrumentMapper(IDaoFactory idf, IMapper[] mappers) throws DaoException {
		mdidao = idf.mdiDao();
		this.mappers = mappers;
		map();
	}

	private void map() throws DaoException {
		int totalMDIs = mdidao.count();
		int sliceSize = 200;
		int slices = totalMDIs / sliceSize + 1;
		for (int i = 0; i < (slices); i++) {
			int startId = i * sliceSize;
			int endId = (i + 1) * sliceSize - 1;
			//
			String[] mdiIds = mdidao.findIDs(startId, endId);
			for (String mdiId : mdiIds) {
				MarketDataInstrument mdi = mdidao.load(mdiId);
				for (IMapper mapper : mappers) {
					mapper.map(mdi);
				}
				mdidao.update(mdi);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String springFile = "automapper.xml";
		if (args.length == 1)
			springFile = args[0];
		new ClassPathXmlApplicationContext(springFile);
	}

}
