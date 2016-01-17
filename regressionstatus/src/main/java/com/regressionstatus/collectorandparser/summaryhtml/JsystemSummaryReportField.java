package com.regressionstatus.collectorandparser.summaryhtml;

/**
 * Denotes the fields in jsystem report - summary.html 
 * @author jtornovsky
 *
 */
public enum JsystemSummaryReportField {

	NUMBER_OF_TESTS("Number of tests"),	
	NUMBER_OF_FAILS("Number of fails"),
	NUMBER_OF_WARNINGS("Number of warnings"),
	RUNNING_TIME("Running time"),
	PASS_RATE("PassRate"),	
	PROGRESS("Progress"),	
	TOTAL_ENABLED_TESTS("TotalEnabledTests"),	
	TOTAL_TESTS("TotalTests"),
	TESTS_IN_RUN("TestsInRun"),
	DATE("Date"),
	HYDRA_CHIMERA_VERSION("Hydra/Chimera version"),
	MEDUSA_VERSION("Medusa Content Server version"),
	SA_APPS_BUILD("SA1000 Application Pack Build"),
	SA_APPS_VERSION("SA1000 Application Pack version"),
	SA_CORE_VERSION("SA1000 Core version"),
	SA_CORE_VERSION_BUILD("SA1000 Core version Build"),
	SCENARIO("Scenario"),
	SV_CORE_VERSION("SmartVision Core version"),
	STATION("Station"),
	SV_CORE_VERSION_BUILD("SmartVision Core version Build");
	
	private final String field;

	private JsystemSummaryReportField(String field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		return field;
	}
	
	public static JsystemSummaryReportField getEnumByString(String enumStringValue) {
		JsystemSummaryReportField enumValue = null;
		for (JsystemSummaryReportField value : JsystemSummaryReportField.values()) {
			if (value.toString().equalsIgnoreCase(enumStringValue)) {
				enumValue = value;
			}
		}
		return enumValue;
	}
}
