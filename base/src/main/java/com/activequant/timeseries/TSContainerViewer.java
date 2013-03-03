package com.activequant.timeseries;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.activequant.trading.datamodel.AQTableDataBase;

/**
 * Provides the table model for displaying a TSContainer2 in a VisualTable.
 * Inject a different SimpleDateFormat to change the format of timestamps. 
 * Inject a TSContainer2 to display the information in it.  
 * 
 * 
 * @author GhostRider
 * 
 */
public class TSContainerViewer extends AQTableDataBase {

	private static final long serialVersionUID = 1L;
	private TSContainer2 container;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private Object[][] data = new Object[][] {};
	private String[] header = new String[] {};;

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public DecimalFormat getDcf() {
		return dcf;
	}

	public void setDcf(DecimalFormat dcf) {
		this.dcf = dcf;
	}

	private DecimalFormat dcf = new DecimalFormat("#.########");

	public void setTSContainer2(TSContainer2 container) {
		this.container = container;

		data = new Object[container.getNumRows()][container.getNumColumns() + 1];
		for (int row = 0; row < container.getNumRows(); row++) {
			for (int col = 0; col < (container.getNumColumns()+1); col++) {
				if (col == 0) {
					data[row][col] = sdf.format(container.getTimeStamps()
							.get(row).getCalendar().getTime());
				} else {
					data[row][col] = container.getColumns().get(col - 1)
							.get(row);
				}
			}
		}

		List<String> headers = new ArrayList<String>();
		headers.add("TimeStamp");
		headers.addAll(container.getColumnHeaders());
		header = headers.toArray(new String[] {});

		super.fireTableStructureChanged();
	}

	@Override
	public Object[][] getData() {
		return data;
	}

	@Override
	public String[] getHeader() {
		return header;
	}

}
