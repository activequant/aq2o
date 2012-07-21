package com.activequant.timeseries;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.activequant.domainmodel.TimeStamp;

public class ChartUtils {

	public static JFreeChart getStepChart(String title, TSContainer2 container) {

		TimeSeriesCollection tempDataSet = new TimeSeriesCollection();

		for (int i = 0; i < container.getNumColumns(); i++) {
			DoubleColumn dc = (DoubleColumn) container.getColumns().get(i);
			List<TimeStamp> ts = container.getTimeStamps();
			TimeSeries tsNew = new TimeSeries(container.getColumnHeaders().get(i));
			for (int j = 0; j < dc.size(); j++)
				tsNew.addOrUpdate(new Millisecond(ts.get(j).getDate()), dc.get(j));
			// add a new series.
			tempDataSet.addSeries(tsNew);

		}
		JFreeChart chart = ChartFactory.createXYStepChart(title, "Time", "Value", tempDataSet, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(Color.WHITE);
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		((XYPlot)chart.getPlot()).setDomainGridlinePaint(Color.LIGHT_GRAY);
		((XYPlot)chart.getPlot()).setRangeGridlinePaint(Color.LIGHT_GRAY);
		
		return chart;
	}
}
