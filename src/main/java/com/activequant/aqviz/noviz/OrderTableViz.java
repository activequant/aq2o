package com.activequant.aqviz.noviz;

import com.activequant.aqviz.interfaces.IOrderTableViz;
import com.activequant.trading.datamodel.AQTableDataBase;
import com.activequant.trading.virtual.IExchange;

/**
 * intentionally left blank. 
 * 
 * @author ustaudinger
 *
 */
public class OrderTableViz extends VisualTable implements IOrderTableViz {

	public OrderTableViz(String stub, AQTableDataBase tableBase, IExchange exchange ){
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
