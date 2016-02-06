package com.regressionstatus.data.current;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.regressionstatus.collectorandparser.DataCollector;
import com.regressionstatus.collectorandparser.DataParser;
import com.regressionstatus.collectorandparser.SummaryReportField;
import com.regressionstatus.data.current.runstatus.RunStatusCalculator;
import com.regressionstatus.data.frontendparameters.current.UrlCommand;
import com.regressionstatus.data.frontendparameters.current.UrlParametersHandler;

public abstract class AbstractCurrentRegressionStatusDataUpdaterSummaryReport implements CurrentRegressionStatusDataUpdater {

	@Resource(name="overallSetupsCurrentStatusMap")
	private Map<CurrentStatusTableField, List<String>> overallSetupsCurrentStatusMap;
	
	@Resource(name="singleSetupCurrentStatusMap")
	private Map<CurrentStatusTableField, String> singleSetupCurrentStatusMap;
	
	protected final String MULTI_VALUES_PROPERTY_SEPARATOR = ",";
	
	@Value("${remote.station.property.ipdresses}")
	protected String remoteStationsIpaddresses;
	
	@Value("${remote.station.property.report.source.summary.file}")
	protected String remoteStationReportSourceFile;
	
	@Value("${local.station.property.report.target.summary.file.location}")
	protected String localStationReportTargetLocation;

	@Autowired
	@Qualifier("runStatusGeneralCalculator")
	protected RunStatusCalculator runStatusGeneralCalculator;
	
	@Autowired
	@Qualifier("urlParametersHandlerDao")
	private UrlParametersHandler urlParametersHandler;
	
	protected Map<SummaryReportField, String> parsedAutomationReport = null;	// to hold parsed report to calculate and fill singleSetupCurrentStatusMap
	
	/**
	 * 
	 * @return
	 */
	@Bean(name="overallSetupsCurrentStatusMap")
	private Map<CurrentStatusTableField, List<String>> initOverallSetupsCurrentStatusMap() {
		Map<CurrentStatusTableField, List<String>> localOverallSetupsCurrentStatusMap = new TreeMap<>(); // tree map to sort keys as their ordinal order
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			localOverallSetupsCurrentStatusMap.put(statusTableField, new ArrayList<>());
		}
		return localOverallSetupsCurrentStatusMap;
	}
		
	/**
	 * 
	 * @return
	 */
	@Bean(name="singleSetupCurrentStatusMap")
	protected Map<CurrentStatusTableField, String> initSingleSetupCurrentStatusMap() {
		Map<CurrentStatusTableField, String> localSingleSetupsCurrentStatusMap = new HashMap<>();
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			localSingleSetupsCurrentStatusMap.put(statusTableField, VALUE_NOT_AVAILABLE);
		}
		return localSingleSetupsCurrentStatusMap;
	}
	
	/**
	 * 
	 * @param dataCollector
	 * @param dataParser
	 */
	protected void generalFetchStatusData(DataCollector dataCollector, DataParser dataParser) {

		backUpOldDataAndClearOverallSetupsCurrentStatusMap();

		for (String remoteStationIpaddress : getRemoteStationsIpaddresses(remoteStationsIpaddresses)) { 
//		for (String remoteStationIpaddress : remoteStationsIpaddresses.split(MULTI_VALUES_PROPERTY_SEPARATOR)) {

			singleSetupCurrentStatusMap =  initSingleSetupCurrentStatusMap();
			try {
				remoteStationIpaddress = remoteStationIpaddress.trim();
				remoteStationReportSourceFile = remoteStationReportSourceFile.trim(); 
				String targetFile = localStationReportTargetLocation.trim() + File.separator + remoteStationIpaddress + "_" + remoteStationReportSourceFile;
				targetFile = dataCollector.collectDataAtRemoteStation(remoteStationIpaddress, remoteStationReportSourceFile, targetFile);
				if (targetFile != null) {	// failed to collect summary file from the remote machine
					parsedAutomationReport = dataParser.parseAutomationReport(targetFile);
					if (parsedAutomationReport != null) {	// wasn't retrieved any data from the examined report file 
						singleSetupCurrentStatusMap = calculateValuesForSingleStationStatus(parsedAutomationReport);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fillOverallSetupsStatusMap(singleSetupCurrentStatusMap);
			}
		}
	}
	
	/**
	 * 
	 * @param urlCommandIp
	 * @param rawRemoteStationsIpaddresses
	 * @return
	 */
	private List<String> getRemoteStationsIpaddresses(String rawRemoteStationsIpaddresses) {
		List<String> ipAddressesList = urlParametersHandler.getParameterFromMap(UrlCommand.IP);
		List<String> shouldBeWithUsedDefaultIps = urlParametersHandler.getParameterFromMap(UrlCommand.USE_WITH_DEFAULT_IPS);
		List<String> defaultIps = Arrays.asList(rawRemoteStationsIpaddresses.trim().split(MULTI_VALUES_PROPERTY_SEPARATOR));
		
		if (ipAddressesList == null) {	// no custom ipaddresses, but only default ones
			return defaultIps;
		} 
		
		if (ipAddressesList != null && shouldBeWithUsedDefaultIps != null) {
			boolean isDefaultIpsShouldBeUsed = Boolean.parseBoolean(shouldBeWithUsedDefaultIps.get(0));
			if (isDefaultIpsShouldBeUsed) {
				ipAddressesList.addAll(defaultIps);
			}
		}
		return ipAddressesList;
	}
	
	/**
	 * 
	 * @param reportData
	 * @return
	 * @throws Exception
	 */
	abstract protected Map<CurrentStatusTableField, String> calculateValuesForSingleStationStatus(Map<SummaryReportField, String> reportData) throws Exception;

	@Override
	public Map<CurrentStatusTableField, List<String>> getOverallSetupsCurrentStatusMap() {
		return this.overallSetupsCurrentStatusMap;
	}
	
	protected void backUpOldDataAndClearOverallSetupsCurrentStatusMap() {
		// TODO backup procedure for the old data
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			overallSetupsCurrentStatusMap.get(statusTableField).clear();
		}
	}
	
	protected void fillOverallSetupsStatusMap(Map<CurrentStatusTableField, String> singleSetupCurrentStatusMap) {
//		backUpOldDataAndClearOverallSetupsCurrentStatusMap();
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			getOverallSetupsCurrentStatusMap().get(statusTableField).add(singleSetupCurrentStatusMap.get(statusTableField));
		}
	}

}
