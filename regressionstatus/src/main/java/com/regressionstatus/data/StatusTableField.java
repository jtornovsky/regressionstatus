package com.regressionstatus.data;

public enum StatusTableField {

	SA_VERSION("SA Version"),
	RUN_TYPE("Run Type"),
	PASS_PERCENTAGE("Pass percentage"),
	PASSED_TESTS_OUT_OF_RUN_TESTS("Passed tests out of run tests"),
	PROGRESS_PERCENTAGE("Progress percentage"),
	TOTAL_TESTS_IN_RUN("Total tests in run"),
	RUN_STATUS("Run status"),
	URL("Url"),
	DETAILS("Details");
	
	private final String field;

	private StatusTableField(String field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		return field;
	}
}
