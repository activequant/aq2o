package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

/**
 * Holds a variety of performance related figures, so that 
 * an automated report can be generated, for example with BIRT. 
 * 
 * @author GhostRider
 *
 */
public class PerformanceReport extends PersistentEntity {

	private String reportId; 
	private TimeStamp reportTimeStamp;
	private Long startDate8, endDate8;
	private Double  
			annualizedRateOfReturn, avgMonthlyReturn, 
			last12MthsReturn, last25MthsReturn, percPosMonths,
			returnSinceStratInception, 
			sharpeRatio, sortinoRatio, worstDrawdown, 
			skewness, excessKurtosis, annualizedVolatility; 
	
	
	
	/**
	 * TimeStamps contains the timestamps in nanoseconds that correspond to performance measures.
	 * They must be aligned.  
	 */
	private Long[] timestamps;
	private Double[] performance; 
	
	private String[] instrumentUniverse; 
	private Double[] instrUniverseWeight;
	private String seriesId; 
	
	public PerformanceReport() {
		super(PerformanceReport.class.getCanonicalName());
	}
	
	public PerformanceReport(String className){
		super(className);
	}

	
	@Override
	public String getId() {
		return nullSafe(reportId)+"."+ nullSafe(reportTimeStamp);
	}

	@Property
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	@Property
	public Long getStartDate8() {
		return startDate8;
	}

	public void setStartDate8(Long startDate8) {
		this.startDate8 = startDate8;
	}
	@Property
	public Long getEndDate8() {
		return endDate8;
	}

	public void setEndDate8(Long endDate8) {
		this.endDate8 = endDate8;
	}
	@Property
	public Double getAnnualizedRateOfReturn() {
		return annualizedRateOfReturn;
	}

	public void setAnnualizedRateOfReturn(Double annualizedRateOfReturn) {
		this.annualizedRateOfReturn = annualizedRateOfReturn;
	}
	@Property
	public Double getAvgMonthlyReturn() {
		return avgMonthlyReturn;
	}

	public void setAvgMonthlyReturn(Double avgMonthlyReturn) {
		this.avgMonthlyReturn = avgMonthlyReturn;
	}
	@Property
	public Double getLast12MthsReturn() {
		return last12MthsReturn;
	}

	public void setLast12MthsReturn(Double last12MthsReturn) {
		this.last12MthsReturn = last12MthsReturn;
	}
	@Property
	public Double getLast25MthsReturn() {
		return last25MthsReturn;
	}

	public void setLast25MthsReturn(Double last25MthsReturn) {
		this.last25MthsReturn = last25MthsReturn;
	}
	@Property
	public Double getPercPosMonths() {
		return percPosMonths;
	}

	public void setPercPosMonths(Double percPosMonths) {
		this.percPosMonths = percPosMonths;
	}
	@Property
	public Double getReturnSinceStratInception() {
		return returnSinceStratInception;
	}

	public void setReturnSinceStratInception(Double returnSinceStratInception) {
		this.returnSinceStratInception = returnSinceStratInception;
	}
	@Property
	public Double getSharpeRatio() {
		return sharpeRatio;
	}

	public void setSharpeRatio(Double sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}
	@Property
	public Double getSortinoRatio() {
		return sortinoRatio;
	}

	public void setSortinoRatio(Double sortinoRatio) {
		this.sortinoRatio = sortinoRatio;
	}
	@Property
	public Double getWorstDrawdown() {
		return worstDrawdown;
	}

	public void setWorstDrawdown(Double worstDrawdown) {
		this.worstDrawdown = worstDrawdown;
	}
	@Property
	public Double getSkewness() {
		return skewness;
	}

	public void setSkewness(Double skewness) {
		this.skewness = skewness;
	}
	@Property
	public Double getExcessKurtosis() {
		return excessKurtosis;
	}

	public void setExcessKurtosis(Double excessKurtosis) {
		this.excessKurtosis = excessKurtosis;
	}
	@Property
	public Double getAnnualizedVolatility() {
		return annualizedVolatility;
	}

	public void setAnnualizedVolatility(Double annualizedVolatility) {
		this.annualizedVolatility = annualizedVolatility;
	}
	@Property
	public Long[] getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(Long[] timestamps) {
		this.timestamps = timestamps;
	}
	@Property
	public Double[] getPerformance() {
		return performance;
	}

	public void setPerformance(Double[] performance) {
		this.performance = performance;
	}
	@Property
	public String[] getInstrumentUniverse() {
		return instrumentUniverse;
	}

	public void setInstrumentUniverse(String[] instrumentUniverse) {
		this.instrumentUniverse = instrumentUniverse;
	}
	@Property
	public Double[] getInstrUniverseWeight() {
		return instrUniverseWeight;
	}

	public void setInstrUniverseWeight(Double[] instrUniverseWeight) {
		this.instrUniverseWeight = instrUniverseWeight;
	}
	@Property
	public TimeStamp getReportTimeStamp() {
		return reportTimeStamp;
	}

	public void setReportTimeStamp(TimeStamp reportTimeStamp) {
		this.reportTimeStamp = reportTimeStamp;
	}
	@Property
	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}
	
	

}
