package com.activequant.aqviz.noviz;

import com.activequant.interfaces.aqviz.IQuoteTableViz;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.trading.datamodel.AQTableDataBase;
/**
 * intentionally left blank. 
 * 
 * @author ustaudinger
 *
 */
public class QuoteTableViz extends VisualTable implements IQuoteTableViz  {

	public QuoteTableViz(String stub, AQTableDataBase tableBase, IExchange exchange ){
		super(stub, tableBase);
	}
	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeSelection(int row, int col, boolean toogle, boolean extend) {
		// TODO Auto-generated method stub
		
	}

}
