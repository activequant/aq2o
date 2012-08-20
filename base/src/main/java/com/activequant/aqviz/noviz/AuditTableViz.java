package com.activequant.aqviz.noviz;

import com.activequant.interfaces.aqviz.IAuditTableViz;
import com.activequant.interfaces.aqviz.IPositionTableViz;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.trading.datamodel.AQTableDataBase;

/**
 * intentionally left blank, naked and shaved. 
 * 
 * @author GhostRider
 *
 */
public class AuditTableViz extends VisualTable implements IAuditTableViz{

	public AuditTableViz(String stub, AQTableDataBase tableBase ){
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
