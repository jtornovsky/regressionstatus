package com.regressionstatus.data.current.summaryjson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.regressionstatus.collectorandparser.DataCollector;
import com.regressionstatus.collectorandparser.DataParser;
import com.regressionstatus.collectorandparser.SummaryReportField;
import com.regressionstatus.collectorandparser.summaryjson.JsystemSummaryJsonReportField;
import com.regressionstatus.data.current.AbstractCurrentRegressionStatusDataUpdaterSummaryReport;
import com.regressionstatus.data.current.CurrentStatusTableField;

/**
 * Class holds functionality to retrieve and calculate current regression status 
 * based on data found in summary.html report of jsystem
 * @author jtornovsky
 *
 */
//@Component("currentRegressionStatusDataUpdaterSummaryJsonReport")
public class CurrentRegressionStatusDataUpdaterSummaryJsonReport extends AbstractCurrentRegressionStatusDataUpdaterSummaryReport {
	
	@Autowired
	@Qualifier("jsystemSummaryJsonReportCollector")
	private DataCollector dataCollector;
	
	@Autowired
	@Qualifier("jsystemSummaryJsonReportParser")
	private DataParser dataParser;
	
	@Override
	public void fetchStatusData() {
		generalFetchStatusData(dataCollector, dataParser);
	}

	/**
	 * calculating values of a gathered data
	 */
	@Override
	protected Map<CurrentStatusTableField, String> calculateValuesForSingleStationStatus(Map<SummaryReportField, String> reportData) throws Exception {
		
		Map<CurrentStatusTableField, String> statusMap = initSingleSetupCurrentStatusMap();
		
		String saVersion = reportData.get(JsystemSummaryJsonReportField.SMARTAIR_VERSION);
		String runType = reportData.get(JsystemSummaryJsonReportField.SCENARIO).substring(reportData.get(JsystemSummaryJsonReportField.SCENARIO).indexOf('-')+1);
		String totalTestsInRunInStringFormat = reportData.get(JsystemSummaryJsonReportField.TOTAL_ENABLED_TESTS);
			if (totalTestsInRunInStringFormat == null) {
				totalTestsInRunInStringFormat = reportData.get(JsystemSummaryJsonReportField.TESTS_IN_RUN);
			}
		
		if (saVersion != null && runType != null && totalTestsInRunInStringFormat != null) {
			statusMap.put(CurrentStatusTableField.SA_VERSION, saVersion);
			statusMap.put(CurrentStatusTableField.RUN_TYPE, runType);
		} else {
			return statusMap;	// if values above are null, no sense to do further calculations, returning map as it is 
		}
		
		double numberOfTests = 0;
		int numberOfPassedTests = 0;
		double totalTestsInRun = 0;
		
		try {
			numberOfTests = Double.parseDouble(reportData.get(JsystemSummaryJsonReportField.TOTAL_COMPLETED));
			numberOfPassedTests = Integer.parseInt(reportData.get(JsystemSummaryJsonReportField.TEST_PASSED));
			totalTestsInRun = Double.parseDouble(totalTestsInRunInStringFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return statusMap;	// if values above are null, no sense to do further calculations, returning map as it is
		}
		
		double passPercentage = numberOfPassedTests/numberOfTests*100;
		double progressPercentageRealValue = numberOfTests/totalTestsInRun*100;
		String runStatus = runStatusGeneralCalculator.calculateRunStatus(progressPercentageRealValue).toString();
		
		statusMap.put(CurrentStatusTableField.PASS_PERCENTAGE, String.format("%.2f", passPercentage) + "%");
		statusMap.put(CurrentStatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, numberOfPassedTests + " out of " + reportData.get(JsystemSummaryJsonReportField.TOTAL_COMPLETED));
		statusMap.put(CurrentStatusTableField.PROGRESS_PERCENTAGE, String.format("%.2f", progressPercentageRealValue) + "%");
		statusMap.put(CurrentStatusTableField.TOTAL_TESTS_IN_RUN, totalTestsInRunInStringFormat);
		statusMap.put(CurrentStatusTableField.RUN_STATUS, runStatus);
//		statusMap.put(CurrentStatusTableField.COMMENTS, "no details");	// the column temporary disabled
		
		URL url = null;
		try {
			url = new URL("http://"+reportData.get(JsystemSummaryJsonReportField.STATION));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return statusMap;	// if values above are null, no sense to do further calculations, returning map as it is
		}
		statusMap.put(CurrentStatusTableField.URL, url.toString());
		return statusMap;
	}
}
