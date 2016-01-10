package com.regressionstatus.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
	
	@Value("${remote.station.property.report.source.location}")
	private String remoteStationReportSourceLocation;
	
	@Value("${local.station.property.report.target.location}")
	private String localStationReportTargetLocation;
	
	@Autowired
	@Qualifier("dataCollectorImpl")
	private DataCollector dataCollector;
	
	@Autowired
	@Qualifier("JsystemSummaryReportParser")
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
			remoteStationReportSourceLocation = remoteStationReportSourceLocation.trim();
			localStationReportTargetLocation = localStationReportTargetLocation.trim();
			dataCollector.collectDataAtRemoteStation(remoteStationIpaddress, remoteStationReportSourceLocation, localStationReportTargetLocation);
			parsedAutomationReport = dataParser.parseAutomationReport(localStationReportTargetLocation);
			singleSetupCurrentStatusMap = calculateValuesForSingleStationStatus(parsedAutomationReport);
			fillOverallSetupsStatusMap(singleSetupCurrentStatusMap);
		}
	}

	/**
	 * calculating values of a gathered data
	 */
	private Map<StatusTableField, String> calculateValuesForSingleStationStatus(Map<ReportField, String> reportData) {
		
		Map<StatusTableField, String> statusMap = new HashMap<>();
		
		String saVersion = reportData.get(ReportField.SA_CORE_VERSION)+"-"+reportData.get(ReportField.SA_CORE_VERSION_BUILD);
		String runType = reportData.get(ReportField.SCENARIO).substring(reportData.get(ReportField.SCENARIO).indexOf('-')+1);
		String passPercentage = reportData.get(ReportField.PASS_RATE) + "%";
		String numberOfPassedTests = Integer.parseInt(reportData.get(ReportField.NUMBER_OF_TESTS)) - Integer.parseInt(reportData.get(ReportField.NUMBER_OF_FAILS)) + " out of " + reportData.get(ReportField.NUMBER_OF_TESTS);
		String progressPercentage = Double.parseDouble(reportData.get(ReportField.NUMBER_OF_TESTS)) / Double.parseDouble(reportData.get(ReportField.NUMBER_OF_TESTS))*100 + "%";
		String totalTestsInRun = reportData.get(ReportField.TOTAL_ENABLED_TESTS);
		String runStatus = calculateRunStatus().name();
		URL url = null;
		try {
			url = new URL("http://"+reportData.get(ReportField.STATION));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		statusMap.put(StatusTableField.SA_VERSION, saVersion);
		statusMap.put(StatusTableField.RUN_TYPE, runType);
		statusMap.put(StatusTableField.PASS_PERCENTAGE, passPercentage);
		statusMap.put(StatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, numberOfPassedTests);
		statusMap.put(StatusTableField.PROGRESS_PERCENTAGE, progressPercentage);
		statusMap.put(StatusTableField.TOTAL_TESTS_IN_RUN, totalTestsInRun);
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
	
	private RunStatus calculateRunStatus() {
		RunStatus runStatus = null;
		// TODO: think about algorithm how to calculate run status
		runStatus = RunStatus.Running;
		return runStatus;
	}


}
