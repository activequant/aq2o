package com.activequant.backtesting.reporting;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.exceptions.TransportException;
import com.activequant.timeseries.ChartUtils;
import com.activequant.timeseries.DoubleColumn;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TypedColumn;
import com.activequant.tools.streaming.PNLChangeEvent;
import com.activequant.transport.ETransportType;
import com.activequant.transport.IReceiver;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.events.IEventListener;

public class PNLMonitor {

	private TSContainer2 tsContainer;
	private TSContainer2 cumulatedTSContainer;
	private Map<String, Double> currentValues = new HashMap<String, Double>();
	private boolean liveEnabled = false;
	private TimeSeriesCollection dataset;
	private List<TimeSeries> timeSeries = new ArrayList<TimeSeries>();
	private AtomicBoolean dataChanged = new AtomicBoolean(false);

	public PNLMonitor(ITransportFactory transportFactory) throws TransportException {
		// add the pnl data listener.
		if (transportFactory != null) {
			IReceiver pnlChannel = transportFactory.getReceiver(ETransportType.RISK_DATA.toString());
			pnlChannel.getMsgRecEvent().addEventListener(new IEventListener<PersistentEntity>() {
				@Override
				public void eventFired(PersistentEntity event) {
					if (event instanceof PNLChangeEvent) {
						process((PNLChangeEvent) event);
					}
				}
			});
		}

		//
		tsContainer = new TSContainer2("PNL", Arrays.asList(new String[] { "TOTAL" }),
				Arrays.asList(new TypedColumn[] { new DoubleColumn() }));
		cumulatedTSContainer = new TSContainer2("PNL", Arrays.asList(new String[] { "TOTAL" }),
				Arrays.asList(new TypedColumn[] { new DoubleColumn() }));
		//
	}

	public void process(PNLChangeEvent delta) {
		dataChanged = new AtomicBoolean(true);
		tsContainer.setValue(delta.getTradInstId(), delta.getTimeStamp(), delta.getChange());
		if (liveEnabled) {
			double currentVal = 0.0;
			if (!currentValues.containsKey(delta.getTradInstId())) {
				// also add a new line chart.
				currentVal = delta.getChange();
				currentValues.put(delta.getTradInstId(), currentVal);
				cumulatedTSContainer.setValue(delta.getTradInstId(), delta.getTimeStamp(), currentVal);
			} else {
				currentVal = currentValues.get(delta.getTradInstId());
				currentVal += delta.getChange();
				currentValues.put(delta.getTradInstId(), currentVal);
				cumulatedTSContainer.setValue(delta.getTradInstId(), delta.getTimeStamp(), currentVal);
			}

			Iterator<Entry<String, Double>> it = currentValues.entrySet().iterator();
			double total = 0.0;
			while (it.hasNext()) {
				total += it.next().getValue();
			}

			cumulatedTSContainer.setValue("TOTAL", delta.getTimeStamp(), total);

		}

	}

	public TSContainer2 getTsContainer() {
		return tsContainer;
	}

	public void setTsContainer(TSContainer2 tsContainer) {
		this.tsContainer = tsContainer;
	}

	public void showLiveChart() {
		liveEnabled = true;
		//
		dataset = new TimeSeriesCollection();

		//
		JFreeChart chart = ChartFactory.createTimeSeriesChart("PNL", "Time", "Value", dataset, true, true, false);
		chart.setAntiAlias(true);
		chart.setBackgroundPaint(Color.WHITE);

		final XYPlot plot = chart.getXYPlot();

		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		// axis.setFixedAutoRange(600000.0);

		JFrame frame = new JFrame("GraphTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChartPanel label = new ChartPanel(chart);
		frame.getContentPane().add(label);
		// Suppose I add combo boxes and buttons here later

		frame.pack();
		frame.setVisible(true);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
						if (dataChanged.get() == true) {
							dataChanged = new AtomicBoolean(false);
							TimeSeriesCollection tempDataSet = new TimeSeriesCollection();

							for (int i = 0; i < cumulatedTSContainer.getNumColumns(); i++) {
								DoubleColumn dc = (DoubleColumn) cumulatedTSContainer.getColumns().get(i);
								List<TimeStamp> ts = cumulatedTSContainer.getTimeStamps();
								TimeSeries tsNew = new TimeSeries(cumulatedTSContainer.getColumnHeaders().get(i));
								for (int j = 0; j < dc.size(); j++)
									tsNew.addOrUpdate(new Millisecond(ts.get(j).getDate()), dc.get(j));
								// add a new series.
								tempDataSet.addSeries(tsNew);

							}
							plot.setDataset(tempDataSet);
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	public JFreeChart getStaticChart() {

		return ChartUtils.getStepChart("PNL", cumulatedTSContainer);
	}

	public TSContainer2 getCumulatedTSContainer() {
		return cumulatedTSContainer;
	}

	public void setCumulatedTSContainer(TSContainer2 cumulatedTSContainer) {
		this.cumulatedTSContainer = cumulatedTSContainer;
	}

}
