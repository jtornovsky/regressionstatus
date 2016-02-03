package com.regressionstatus.collectorandparser;

import java.util.Map;

/**
 * Interface responsible to parse report file taken from jsystem 
 * @author jtornovsky
 *
 */
public interface DataParser {

	/**
	 * parses report file taken from jsystem
	 * and returns it as an hash map
	 * @param reportTargetLocation fetched report file in xml or json format with its location
	 * @return hash map with a parsed data
	 */
	public Map<SummaryReportField, String> parseAutomationReport(String reportTargetLocation);
}

