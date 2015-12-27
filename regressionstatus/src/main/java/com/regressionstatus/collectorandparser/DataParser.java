package com.regressionstatus.collectorandparser;

import java.util.Map;


public interface DataParser {

	public Map<ReportField, String> parseAutomationReport(String reportTargetLocation);
}
