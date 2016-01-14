package com.regressionstatus.data;

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
import com.regressionstatus.collectorandparser.ReportField;

@Component("regressionStatusDataUpdaterImpl")
public class RegressionStatusDataUpdaterImpl extends AbstractRegressionStatusDataUpdater {
	
	private final String MULTI_VALUES_PROPERTY_SEPARATOR = ",";
	
	@Resource(name="singleSetupCurrentStatusMap")
	private Map<StatusTableField, String> singleSetupCurrentStatusMap;
	
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
	
	@Resource(name="parsedAutomationReport")
	private Map<ReportField, String> parsedAutomationReport;	// to hold parsed report to calculate and fill singleSetupCurrentStatusMap
	
	@Bean(name="singleSetupCurrentStatusMap")
	public Map<StatusTableField, String> initSingleSetupCurrentStatusMap() {
		Map<StatusTableField, String> localSingleSetupsCurrentStatusMap = new HashMap<>();
		for (StatusTableField statusTableField : StatusTableField.values()) {
			localSingleSetupsCurrentStatusMap.put(statusTableField, "");
		}
		return localSingleSetupsCurrentStatusMap;
	}
	
	@Bean(name = "parsedAutomationReport")
	public Map<ReportField, String> initParsedAutomationReportMap() {
		Map<ReportField, String> pAutoReportMap = new HashMap<>();
		for (ReportField reportField : ReportField.values()) {
			pAutoReportMap.put(reportField, "");
		}
		return pAutoReportMap;
	}

	@Override
	public void fetchStatusData() {
		for (StatusTableField statusTableField : StatusTableField.values()) {
			singleSetupCurrentStatusMap.put(statusTableField, "");
		}
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
	private Map<StatusTableField, String> calculateValuesForSingleStationStatus(Map<ReportField, String> reportData) {
		
		Map<StatusTableField, String> statusMap = new HashMap<>();
		
		String totalTestsInRunInStringFormat = reportData.get(ReportField.TOTAL_ENABLED_TESTS);
		if (totalTestsInRunInStringFormat == null) {
			totalTestsInRunInStringFormat = reportData.get(ReportField.TESTS_IN_RUN);
		}
		
		int numberOfTests = 0;
		int numberOfFails = 0;
		double totalTestsInRun = 0;
		
		try {
			numberOfTests = Integer.parseInt(reportData.get(ReportField.NUMBER_OF_TESTS));
			numberOfFails = Integer.parseInt(reportData.get(ReportField.NUMBER_OF_FAILS));
			totalTestsInRun = Double.parseDouble(totalTestsInRunInStringFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return statusMap;
		}
		
		String saVersion = reportData.get(ReportField.SA_CORE_VERSION)+"-"+reportData.get(ReportField.SA_CORE_VERSION_BUILD);
		String runType = reportData.get(ReportField.SCENARIO).substring(reportData.get(ReportField.SCENARIO).indexOf('-')+1);
		String passPercentage = reportData.get(ReportField.PASS_RATE) + "%";
		String numberOfPassedTests = (numberOfTests - numberOfFails) + " out of " + reportData.get(ReportField.NUMBER_OF_TESTS);
		double progressPercentageRealValue = (double)(numberOfTests) / totalTestsInRun*100;
		String progressPercentage = String.format("%.2f", progressPercentageRealValue) + "%";
		String runStatus = calculateRunStatus(progressPercentageRealValue);
		
		URL url = null;
		try {
			url = new URL("http://"+reportData.get(ReportField.STATION));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return statusMap;
		}
		
		statusMap.put(StatusTableField.SA_VERSION, saVersion);
		statusMap.put(StatusTableField.RUN_TYPE, runType);
		statusMap.put(StatusTableField.PASS_PERCENTAGE, passPercentage);
		statusMap.put(StatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, numberOfPassedTests);
		statusMap.put(StatusTableField.PROGRESS_PERCENTAGE, progressPercentage);
		statusMap.put(StatusTableField.TOTAL_TESTS_IN_RUN, totalTestsInRunInStringFormat);
		statusMap.put(StatusTableField.RUN_STATUS, runStatus);
		statusMap.put(StatusTableField.URL, url.toString());
		statusMap.put(StatusTableField.DETAILS, "no details");
		
		return statusMap;
	}
	
	/**
	 * holds the possible run statuses of a regression
	 * @author jtornovsky
	 *
	 */
	private enum RunStatus {
		Running,
		Suspicious, // in case of no progress in run for a long time
		Ended,
		Rerun,
		Stopped;
	}
	
	private String calculateRunStatus(double progressPercentageRealValue) {
		RunStatus runStatus = null;
		// TODO: think about algorithm how to calculate run status
		runStatus = RunStatus.Running;
		if (progressPercentageRealValue > 99.99) {
			runStatus = RunStatus.Ended;
		}
		return runStatus.name();
	}


}
