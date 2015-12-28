package com.regressionstatus.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
			calculateValuesForSingleStationStatus();
		}
		
		singleSetupCurrentStatusMap.put(StatusTableField.SA_VERSION, "123");
		singleSetupCurrentStatusMap.put(StatusTableField.RUN_TYPE, "u4");
		singleSetupCurrentStatusMap.put(StatusTableField.PASS_PERCENTAGE, "96"+"%");
		singleSetupCurrentStatusMap.put(StatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, "456 out 900");
		singleSetupCurrentStatusMap.put(StatusTableField.PROGRESS_PERCENTAGE, "56" + "%");
		singleSetupCurrentStatusMap.put(StatusTableField.TOTAL_TESTS_IN_RUN, "1024");
		singleSetupCurrentStatusMap.put(StatusTableField.RUN_STATUS, "running");
		singleSetupCurrentStatusMap.put(StatusTableField.DETAILS, "http://");
		fillOverallSetupsStatusMap(singleSetupCurrentStatusMap);
		
		singleSetupCurrentStatusMap.put(StatusTableField.SA_VERSION, "1223");
		singleSetupCurrentStatusMap.put(StatusTableField.RUN_TYPE, "u43");
		singleSetupCurrentStatusMap.put(StatusTableField.PASS_PERCENTAGE, "95"+"%");
		singleSetupCurrentStatusMap.put(StatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, "476 out 900");
		singleSetupCurrentStatusMap.put(StatusTableField.PROGRESS_PERCENTAGE, "76" + "%");
		singleSetupCurrentStatusMap.put(StatusTableField.TOTAL_TESTS_IN_RUN, "1054");
		singleSetupCurrentStatusMap.put(StatusTableField.RUN_STATUS, "stopped");
		singleSetupCurrentStatusMap.put(StatusTableField.DETAILS, "http://123");
		fillOverallSetupsStatusMap(singleSetupCurrentStatusMap);
	}

	private void calculateValuesForSingleStationStatus() {
		// TODO 
		// singleSetupCurrentStatusMap = parsedAutomationReport // after required calculations
	}


}
