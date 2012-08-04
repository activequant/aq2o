package com.activequant.aqviz.noviz;

import com.activequant.interfaces.aqviz.IAccountTableViz;
import com.activequant.trading.datamodel.AQTableDataBase;
/**
 * intentionally left blank, naked and shaved. 
 * 
 * @author GhostRider
 *
 */
public class AccountTableViz extends VisualTable implements IAccountTableViz  {

	public AccountTableViz(String stub, AQTableDataBase tableBase){
		super(stub, tableBase);
	}
	
	@Override
	public void setTitle(String title) {		
	}

	@Override
	public void changeSelection(int row, int col, boolean toogle, boolean extend) {
	}

}
