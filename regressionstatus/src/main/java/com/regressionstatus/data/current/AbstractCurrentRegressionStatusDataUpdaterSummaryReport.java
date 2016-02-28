package com.regressionstatus.data.current;

import java.io.File;
import java.util.ArrayList;
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
import com.regressionstatus.data.current.util.CustomIpaddressesListBuilder;
import com.regressionstatus.data.current.util.DistributedRunCalculator;
import com.regressionstatus.data.current.util.UrlBuilder;
import com.regressionstatus.data.frontendparameters.current.UrlParametersHandler;

public abstract class AbstractCurrentRegressionStatusDataUpdaterSummaryReport implements CurrentRegressionStatusDataUpdater {
	
	public static final String PASSED_TESTS_OUT_OF_RUN_TESTS_SEPARATOR = " out of ";

	@Resource(name="overallSetupsCurrentStatusMap")
	private Map<CurrentStatusTableField, List<String>> overallSetupsCurrentStatusMap;
	
	@Resource(name="singleSetupCurrentStatusMap")
	private Map<CurrentStatusTableField, String> singleSetupCurrentStatusMap;
	
	public static final String MULTI_VALUES_PROPERTY_SEPARATOR = ",";
	
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
	 * fetches jsystem report (json or html) from predefined setups, parses data from those reports
	 * and calculate final reports to be shown in jsp page
	 * @param dataCollector
	 * @param dataParser
	 */
	protected void generalFetchStatusData(DataCollector dataCollector, DataParser dataParser) {

		backUpOldDataAndClearOverallSetupsCurrentStatusMap();

		for (String remoteStationIpaddress : CustomIpaddressesListBuilder.getRemoteStationsIpaddresses(remoteStationsIpaddresses, urlParametersHandler)) { 
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
				if (targetFile == null || parsedAutomationReport == null) {	// in case of corrupted data or file is not available, just put setup's ip in map
					singleSetupCurrentStatusMap.put(CurrentStatusTableField.URL, getUrl(remoteStationIpaddress));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fillOverallSetupsStatusMap(singleSetupCurrentStatusMap);
			}
		}
	}
	
	/**
	 * holds the logic of calculating values to be shown in the final report 
	 * @param reportData - report as appears in jsystem (json or html)
	 * @return final report of an examined setup
	 * @throws Exception
	 */
	abstract protected Map<CurrentStatusTableField, String> calculateValuesForSingleStationStatus(Map<SummaryReportField, String> reportData) throws Exception;

	/**
	 * returns the map that holds the overall current status of all regression setups
	 * @return
	 */
	@Override
	public Map<CurrentStatusTableField, List<String>> getOverallSetupsCurrentStatusMap() {

		overallSetupsCurrentStatusMap = DistributedRunCalculator.calculateOverallSetupsCurrentStatusOfDistributedRun(overallSetupsCurrentStatusMap, urlParametersHandler);
		
		return overallSetupsCurrentStatusMap;
	}
	
	/**
	 * Composes url from a given ip
	 * @param stationIp
	 * @return url in string format
	 * @throws Exception
	 */
	protected String getUrl(String stationIp) throws Exception {	
		String url = UrlBuilder.getUrl(stationIp); 
		if (url == null) {
			return VALUE_NOT_AVAILABLE;
		}
		return url;
	}
	
	/**
	 * 
	 */
	protected void backUpOldDataAndClearOverallSetupsCurrentStatusMap() {
		// TODO backup procedure for the old data
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			overallSetupsCurrentStatusMap.get(statusTableField).clear();
		}
	}
	
	/**
	 * 
	 * @param singleSetupCurrentStatusMap
	 */
	protected void fillOverallSetupsStatusMap(Map<CurrentStatusTableField, String> singleSetupCurrentStatusMap) {
//		backUpOldDataAndClearOverallSetupsCurrentStatusMap();
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			overallSetupsCurrentStatusMap.get(statusTableField).add(singleSetupCurrentStatusMap.get(statusTableField));
		}
	}
}
