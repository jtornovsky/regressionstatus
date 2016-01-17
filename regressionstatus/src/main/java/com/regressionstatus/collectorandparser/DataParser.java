package com.regressionstatus.collectorandparser;

import java.util.Map;

import com.regressionstatus.collectorandparser.summaryhtml.JsystemSummaryReportField;


public interface DataParser {

	public Map<JsystemSummaryReportField, String> parseAutomationReport(String reportTargetLocation);
}
