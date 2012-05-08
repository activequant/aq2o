package com.activequant.domainmodel;

/**
 * Holds a variety of performance related figures, so that 
 * an automated report can be generated, for example with BIRT. 
 * 
 * @author GhostRider
 *
 */
public class PerformanceReport extends PersistentEntity {

	private String reportId; 
	private Long startDate8, endDate8;
	private Double  
			annualizedRateOfReturn, avgMonthlyReturn, 
			last12MthsReturn, last25MthsReturn, percPosMonths,
			returnSinceStratInception, 
			sharpeRatio, sortinoRatio, worstDrawdown, 
			skewness, excessKurtosis, annualizedVolatility; 
	
	private Long[] date8Time6;
	private Double[] performance; 
	
	private String[] instrumentUniverse; 
	private Double[] instrUniverseWeight;
	
	public PerformanceReport() {
		super(PerformanceReport.class.getCanonicalName());
	}
	
	public PerformanceReport(String className){
		super(className);
	}

	
	@Override
	public String getId() {
		return nullSafe(reportId);
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Long getStartDate8() {
		return startDate8;
	}

	public void setStartDate8(Long startDate8) {
		this.startDate8 = startDate8;
	}

	public Long getEndDate8() {
		return endDate8;
	}

	public void setEndDate8(Long endDate8) {
		this.endDate8 = endDate8;
	}

	public Double getAnnualizedRateOfReturn() {
		return annualizedRateOfReturn;
	}

	public void setAnnualizedRateOfReturn(Double annualizedRateOfReturn) {
		this.annualizedRateOfReturn = annualizedRateOfReturn;
	}

	public Double getAvgMonthlyReturn() {
		return avgMonthlyReturn;
	}

	public void setAvgMonthlyReturn(Double avgMonthlyReturn) {
		this.avgMonthlyReturn = avgMonthlyReturn;
	}

	public Double getLast12MthsReturn() {
		return last12MthsReturn;
	}

	public void setLast12MthsReturn(Double last12MthsReturn) {
		this.last12MthsReturn = last12MthsReturn;
	}

	public Double getLast25MthsReturn() {
		return last25MthsReturn;
	}

	public void setLast25MthsReturn(Double last25MthsReturn) {
		this.last25MthsReturn = last25MthsReturn;
	}

	public Double getPercPosMonths() {
		return percPosMonths;
	}

	public void setPercPosMonths(Double percPosMonths) {
		this.percPosMonths = percPosMonths;
	}

	public Double getReturnSinceStratInception() {
		return returnSinceStratInception;
	}

	public void setReturnSinceStratInception(Double returnSinceStratInception) {
		this.returnSinceStratInception = returnSinceStratInception;
	}

	public Double getSharpeRatio() {
		return sharpeRatio;
	}

	public void setSharpeRatio(Double sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}

	public Double getSortinoRatio() {
		return sortinoRatio;
	}

	public void setSortinoRatio(Double sortinoRatio) {
		this.sortinoRatio = sortinoRatio;
	}

	public Double getWorstDrawdown() {
		return worstDrawdown;
	}

	public void setWorstDrawdown(Double worstDrawdown) {
		this.worstDrawdown = worstDrawdown;
	}

	public Double getSkewness() {
		return skewness;
	}

	public void setSkewness(Double skewness) {
		this.skewness = skewness;
	}

	public Double getExcessKurtosis() {
		return excessKurtosis;
	}

	public void setExcessKurtosis(Double excessKurtosis) {
		this.excessKurtosis = excessKurtosis;
	}

	public Double getAnnualizedVolatility() {
		return annualizedVolatility;
	}

	public void setAnnualizedVolatility(Double annualizedVolatility) {
		this.annualizedVolatility = annualizedVolatility;
	}

	public Long[] getDate8Time6() {
		return date8Time6;
	}

	public void setDate8Time6(Long[] date8Time6) {
		this.date8Time6 = date8Time6;
	}

	public Double[] getPerformance() {
		return performance;
	}

	public void setPerformance(Double[] performance) {
		this.performance = performance;
	}

	public String[] getInstrumentUniverse() {
		return instrumentUniverse;
	}

	public void setInstrumentUniverse(String[] instrumentUniverse) {
		this.instrumentUniverse = instrumentUniverse;
	}

	public Double[] getInstrUniverseWeight() {
		return instrUniverseWeight;
	}

	public void setInstrUniverseWeight(Double[] instrUniverseWeight) {
		this.instrUniverseWeight = instrUniverseWeight;
	}
	
	

}
