package com.activequant.backtesting.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import com.activequant.domainmodel.trade.event.OrderFillEvent;

public class CSVFileFillExporter {

	private DecimalFormat dcf = new DecimalFormat("#.######");
	public void export(String targetFolder, List<OrderFillEvent> fills) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(targetFolder + File.separator + "report.csv"));
			bw.write("'InstId';'RefOrderID';'Side';'creationTimeInMilliseconds';'FillAmount';'FillPrice';'LeftQuantity';");
			bw.newLine();			
			for (OrderFillEvent ofe : fills) {
				bw.write(ofe.getOptionalInstId());
				bw.write(";");
				bw.write(ofe.getRefOrderId());
				bw.write(";");
				bw.write(ofe.getSide());
				bw.write(";");
				bw.write(dcf.format(ofe.getCreationTimeStamp().getMilliseconds()));
				bw.write(";");
				bw.write(dcf.format(ofe.getFillAmount()));
				bw.write(";");
				bw.write(dcf.format(ofe.getFillPrice()));
				bw.write(";");
				bw.write(dcf.format(ofe.getLeftQuantity()));
				bw.write(";");
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
