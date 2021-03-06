package com.regressionstatus.collectorandparser.summaryjson;

import com.regressionstatus.collectorandparser.SummaryReportField;

/**
 * Denotes the fields in jsystem report - summary.json 
 * @author jtornovsky
 *
 */

public enum JsystemSummaryJsonReportField implements SummaryReportField {

	FLOW("Flow"),
	SCENARIO("Scenario"),
	PASS_RATE("PassRate"),
	TEST_PASSED("TestPassed"),
	TOTAL_ENABLED_TESTS("TotalEnabledTests"),
	TESTS_IN_RUN("TestsInRun"),
	REPORT("Report"),
	COMMENT("Comment"),
	TOTAL_COMPLETED("TotalCompleted"),
	PROGRESS("Progress"),
	SMARTAIR_VERSION("SmartAirVersion"),
	STATION("Station"),
	TOTAL_TESTS("TotalTests"),
	APPPACK_VERSION("AppPackVersion"),
	MEDUSA("Medusa"),
	DATE("Date"),
	HYDRA("Hydra"),
	SMARTVISION_VERSION("SmartVisionVersion");
	
	private final String field;

	private JsystemSummaryJsonReportField(String field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		return field;
	}
	
	public static JsystemSummaryJsonReportField getEnumByString(String enumStringValue) {
		JsystemSummaryJsonReportField enumValue = null;
		for (JsystemSummaryJsonReportField value : JsystemSummaryJsonReportField.values()) {
			if (value.toString().equalsIgnoreCase(enumStringValue)) {
				enumValue = value;
			}
		}
		return enumValue;
	}
}
