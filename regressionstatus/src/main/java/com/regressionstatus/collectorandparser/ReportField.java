package com.regressionstatus.collectorandparser;

/**
 * Denotes the fields in jsystem report - summary.html 
 * @author jtornovsky
 *
 */
public enum ReportField {

	NUMBER_OF_TESTS("Number of tests"),	
	NUMBER_OF_FAILS("Number of fails"),
	NUMBER_OF_WARNINGS("Number of warnings"),
	RUNNING_TIME("Running time"),
	PASS_RATE("PassRate"),	
	PROGRESS("Progress"),	
	TOTAL_ENABLED_TESTS("TotalEnabledTests"),	
	TOTAL_TESTS("TotalTests"),
	DATE("Date"),
	HYDRA_CHIMERA_VERSION("Hydra/Chimera version"),
	MEDUSA_VERSION("Medusa Content Server version"),
	SA_APPS_BUILD("SA1000 Application Pack Build"),
	SA_APPS_VERSION("SA1000 Application Pack version"),
	SA_CORE_VERSION("SA1000 Core version"),
	SA_CORE_VERSION_BUILD("SA1000 Core version Build"),
	SCENARIO("Scenario"),
	SV_CORE_VERSION("SmartVision Core version"),
	SV_CORE_VERSION_BUILD("SmartVision Core version Build");
	
	private final String field;

	private ReportField(String field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		return field;
	}
	
	public static ReportField getEnumByString(String enumStringValue) {
		ReportField enumValue = null;
		for (ReportField value : ReportField.values()) {
			if (value.toString().equalsIgnoreCase(enumStringValue)) {
				enumValue = value;
			}
		}
		return enumValue;
	}
}
