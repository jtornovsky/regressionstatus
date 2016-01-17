package com.regressionstatus.data.current.summaryhtml;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.regressionstatus.collectorandparser.DataCollector;
import com.regressionstatus.collectorandparser.DataParser;
import com.regressionstatus.collectorandparser.summaryhtml.JsystemSummaryReportField;
import com.regressionstatus.data.current.CurrentStatusTableField;
import com.regressionstatus.data.current.runstatus.RunStatusCalculator;

/**
 * Class holds functionality to retrieve and calculate current regression status 
 * based on data found in summary.html report of jsystem
 * @author jtornovsky
 *
 */
@Component("currentRegressionStatusDataUpdaterSummaryReport")
public class CurrentRegressionStatusDataUpdaterSummaryReport extends AbstractCurrentRegressionStatusDataUpdaterSummaryReport {
	
	private final String MULTI_VALUES_PROPERTY_SEPARATOR = ",";
	
	@Resource(name="singleSetupCurrentStatusMap")
	private Map<CurrentStatusTableField, String> singleSetupCurrentStatusMap;
	
	@Value("${remote.station.property.ipdresses}")
	private String remoteStationsIpaddresses;
	
	@Value("${remote.station.property.report.source.summary.file}")
	private String remoteStationReportSourceFile;
	
	@Value("${local.station.property.report.target.summary.file.location}")
	private String localStationReportTargetLocation;
	
	@Autowired
	@Qualifier("jsystemSummaryReportCollector")
	private DataCollector dataCollector;
	
	@Autowired
	@Qualifier("jsystemSummaryReportParser")
	private DataParser dataParser;

	@Autowired
	@Qualifier("runStatusGeneralCalculator")
	private RunStatusCalculator runStatusGeneralCalculator;
	
	@Resource(name="parsedAutomationReport")
	private Map<JsystemSummaryReportField, String> parsedAutomationReport;	// to hold parsed report to calculate and fill singleSetupCurrentStatusMap
	
	@Bean(name="singleSetupCurrentStatusMap")
	public Map<CurrentStatusTableField, String> initSingleSetupCurrentStatusMap() {
		Map<CurrentStatusTableField, String> localSingleSetupsCurrentStatusMap = new HashMap<>();
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			localSingleSetupsCurrentStatusMap.put(statusTableField, "");
		}
		return localSingleSetupsCurrentStatusMap;
	}
	
	@Bean(name = "parsedAutomationReport")
	public Map<JsystemSummaryReportField, String> initParsedAutomationReportMap() {
		Map<JsystemSummaryReportField, String> pAutoReportMap = new HashMap<>();
		for (JsystemSummaryReportField reportField : JsystemSummaryReportField.values()) {
			pAutoReportMap.put(reportField, "");
		}
		return pAutoReportMap;
	}

	@Override
	public void fetchStatusData() {

		backUpOldDataAndClearOverallSetupsCurrentStatusMap();
		
		for (String remoteStationIpaddress : remoteStationsIpaddresses.split(MULTI_VALUES_PROPERTY_SEPARATOR)) {
			remoteStationIpaddress = remoteStationIpaddress.trim();
			remoteStationReportSourceFile = remoteStationReportSourceFile.trim();
			String targetFile = localStationReportTargetLocation.trim() + File.separator + remoteStationIpaddress + "_" + remoteStationReportSourceFile; 
			dataCollector.collectDataAtRemoteStation(remoteStationIpaddress, remoteStationReportSourceFile, targetFile);
			parsedAutomationReport = dataParser.parseAutomationReport(targetFile);
			singleSetupCurrentStatusMap = calculateValuesForSingleStationStatus(parsedAutomationReport);
			fillOverallSetupsStatusMap(singleSetupCurrentStatusMap);
		}
	}

	/**
	 * calculating values of a gathered data
	 */
	private Map<CurrentStatusTableField, String> calculateValuesForSingleStationStatus(Map<JsystemSummaryReportField, String> reportData) {
		
		Map<CurrentStatusTableField, String> statusMap = initStatusFieldMap();
		
		String saVersion = reportData.get(JsystemSummaryReportField.SA_CORE_VERSION)+"-"+reportData.get(JsystemSummaryReportField.SA_CORE_VERSION_BUILD);
		String runType = reportData.get(JsystemSummaryReportField.SCENARIO).substring(reportData.get(JsystemSummaryReportField.SCENARIO).indexOf('-')+1);
		String totalTestsInRunInStringFormat = reportData.get(JsystemSummaryReportField.TOTAL_ENABLED_TESTS);
		if (totalTestsInRunInStringFormat == null) {
			totalTestsInRunInStringFormat = reportData.get(JsystemSummaryReportField.TESTS_IN_RUN);
		}
		
		if (saVersion != null && runType != null && totalTestsInRunInStringFormat != null) {
			statusMap.put(CurrentStatusTableField.SA_VERSION, saVersion);
			statusMap.put(CurrentStatusTableField.RUN_TYPE, runType);
		} else {
			return statusMap;
		}
		
		int numberOfTests = 0;
		int numberOfFails = 0;
		double totalTestsInRun = 0;
		
		try {
			numberOfTests = Integer.parseInt(reportData.get(JsystemSummaryReportField.NUMBER_OF_TESTS));
			numberOfFails = Integer.parseInt(reportData.get(JsystemSummaryReportField.NUMBER_OF_FAILS));
			totalTestsInRun = Double.parseDouble(totalTestsInRunInStringFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return statusMap;
		}
		
		String passPercentage = reportData.get(JsystemSummaryReportField.PASS_RATE) + "%";
		String numberOfPassedTests = (numberOfTests - numberOfFails) + " out of " + reportData.get(JsystemSummaryReportField.NUMBER_OF_TESTS);
		double progressPercentageRealValue = (double)(numberOfTests) / totalTestsInRun*100;
		String progressPercentage = String.format("%.2f", progressPercentageRealValue) + "%";
		String runStatus = runStatusGeneralCalculator.calculateRunStatus(progressPercentageRealValue).toString();
		
		statusMap.put(CurrentStatusTableField.PASS_PERCENTAGE, passPercentage);
		statusMap.put(CurrentStatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, numberOfPassedTests);
		statusMap.put(CurrentStatusTableField.PROGRESS_PERCENTAGE, progressPercentage);
		statusMap.put(CurrentStatusTableField.TOTAL_TESTS_IN_RUN, totalTestsInRunInStringFormat);
		statusMap.put(CurrentStatusTableField.RUN_STATUS, runStatus);
//		statusMap.put(CurrentStatusTableField.COMMENTS, "no details");
		
		URL url = null;
		try {
			url = new URL("http://"+reportData.get(JsystemSummaryReportField.STATION));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return statusMap;
		}
		statusMap.put(CurrentStatusTableField.URL, url.toString());
		return statusMap;
	}

}
