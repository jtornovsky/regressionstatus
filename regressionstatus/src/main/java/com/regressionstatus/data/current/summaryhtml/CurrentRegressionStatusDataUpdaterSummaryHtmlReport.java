package com.regressionstatus.data.current.summaryhtml;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.regressionstatus.collectorandparser.DataCollector;
import com.regressionstatus.collectorandparser.DataParser;
import com.regressionstatus.collectorandparser.SummaryReportField;
import com.regressionstatus.collectorandparser.summaryhtml.JsystemSummaryHtmlReportField;
import com.regressionstatus.data.current.AbstractCurrentRegressionStatusDataUpdaterSummaryReport;
import com.regressionstatus.data.current.CurrentStatusTableField;
import com.regressionstatus.data.current.runstatus.RunStatusCalculator;

/**
 * Class holds functionality to retrieve and calculate current regression status 
 * based on data found in summary.html report of jsystem
 * @author jtornovsky
 *
 */
@Component("currentRegressionStatusDataUpdaterSummaryReport")
public class CurrentRegressionStatusDataUpdaterSummaryHtmlReport extends AbstractCurrentRegressionStatusDataUpdaterSummaryReport {
	
	@Autowired
	@Qualifier("jsystemSummaryHtmlReportCollector")
	private DataCollector dataCollector;
	
	@Autowired
	@Qualifier("jsystemSummaryHtmlReportParser")
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
		
		String saVersion = reportData.get(JsystemSummaryHtmlReportField.SA_CORE_VERSION)+"-"+reportData.get(JsystemSummaryHtmlReportField.SA_CORE_VERSION_BUILD);
		String runType = reportData.get(JsystemSummaryHtmlReportField.SCENARIO).substring(reportData.get(JsystemSummaryHtmlReportField.SCENARIO).indexOf('-')+1);
		String totalTestsInRunInStringFormat = reportData.get(JsystemSummaryHtmlReportField.TOTAL_ENABLED_TESTS);
			if (totalTestsInRunInStringFormat == null) {
				totalTestsInRunInStringFormat = reportData.get(JsystemSummaryHtmlReportField.TESTS_IN_RUN);
			}
		
		if (saVersion != null && runType != null && totalTestsInRunInStringFormat != null) {
			statusMap.put(CurrentStatusTableField.SA_VERSION, saVersion);
			statusMap.put(CurrentStatusTableField.RUN_TYPE, runType);
		} else {
			return statusMap;	// if values above are null, no sense to do further calculations, returning map as it is 
		}
		
		double numberOfTests = 0;
		double numberOfFails = 0;
		double totalTestsInRun = 0;
		
		try {
			numberOfTests = Double.parseDouble(reportData.get(JsystemSummaryHtmlReportField.NUMBER_OF_TESTS));
			numberOfFails = Double.parseDouble(reportData.get(JsystemSummaryHtmlReportField.NUMBER_OF_FAILS));
			totalTestsInRun = Double.parseDouble(totalTestsInRunInStringFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return statusMap;	// if values above are null, no sense to do further calculations, returning map as it is
		}
		
//		String passPercentage = reportData.get(JsystemSummaryReportField.PASS_RATE) + "%";	commented out as a value not calculated well by jsystem
		int numberOfPassedTests = (int) (numberOfTests - numberOfFails);
		double passPercentage = numberOfPassedTests/numberOfTests*100;
		double progressPercentageRealValue = numberOfTests/totalTestsInRun*100;
		String runStatus = runStatusGeneralCalculator.calculateRunStatus(progressPercentageRealValue).toString();
		
		statusMap.put(CurrentStatusTableField.PASS_PERCENTAGE, String.format("%.2f", passPercentage) + "%");
		statusMap.put(CurrentStatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, numberOfPassedTests + " out of " + reportData.get(JsystemSummaryHtmlReportField.NUMBER_OF_TESTS));
		statusMap.put(CurrentStatusTableField.PROGRESS_PERCENTAGE, String.format("%.2f", progressPercentageRealValue) + "%");
		statusMap.put(CurrentStatusTableField.TOTAL_TESTS_IN_RUN, totalTestsInRunInStringFormat);
		statusMap.put(CurrentStatusTableField.RUN_STATUS, runStatus);
//		statusMap.put(CurrentStatusTableField.COMMENTS, "no details");	// the column temporary disabled
		
		URL url = null;
		try {
			url = new URL("http://"+reportData.get(JsystemSummaryHtmlReportField.STATION));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return statusMap;	// if values above are null, no sense to do further calculations, returning map as it is
		}
		statusMap.put(CurrentStatusTableField.URL, url.toString());
		return statusMap;
	}
}
