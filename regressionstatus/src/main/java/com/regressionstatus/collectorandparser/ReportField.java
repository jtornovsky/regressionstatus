package com.regressionstatus.collectorandparser;

/**
 * Denotes the fields in jsystem report - summary.html 
 * @author jtornovsky
 *
 */
public enum ReportField {

	NUMBER_OF_TESTS("Number of tests"),	
	NUMBER_OF_FAILS("Number of fails"),	
	PASS_RATE("PassRate"),	
	PROGRESS("Progress"),	
	TOTAL_ENABLED_TESTS("TotalEnabledTests"),	
	TOTAL_TESTS("TotalTests");	
	
	private final String field;

	private ReportField(String field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		return field;
	}
}
