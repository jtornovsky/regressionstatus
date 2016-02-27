package com.regressionstatus.data.current;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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
import com.regressionstatus.collectorandparser.summaryhtml.JsystemSummaryHtmlReportField;
import com.regressionstatus.data.current.runstatus.RunStatusCalculator;
import com.regressionstatus.data.frontendparameters.current.UrlCommand;
import com.regressionstatus.data.frontendparameters.current.UrlParametersHandler;

public abstract class AbstractCurrentRegressionStatusDataUpdaterSummaryReport implements CurrentRegressionStatusDataUpdater {
	
	private final String URL_PREFIX = "http://";
	
	protected final String PASSED_TESTS_OUT_OF_RUN_TESTS_SEPARATOR = " out of ";

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
	 * fetches jsystem report (json or html) from predefined setups, parses data from those reports
	 * and calculate final reports to be shown in jsp page
	 * @param dataCollector
	 * @param dataParser
	 */
	protected void generalFetchStatusData(DataCollector dataCollector, DataParser dataParser) {

		backUpOldDataAndClearOverallSetupsCurrentStatusMap();

		for (String remoteStationIpaddress : getRemoteStationsIpaddresses(remoteStationsIpaddresses)) { 
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
	 * retrieves the ip addresses of setups to collect jsystem reports from
	 * @param rawRemoteStationsIpaddresses - default ip addresses appear in app.properties
	 * @return list of all ips of the setups to collect jsystem reports from
	 */
	private List<String> getRemoteStationsIpaddresses(String rawRemoteStationsIpaddresses) {
		List<String> ipAddressesList = urlParametersHandler.getUrlParameterFromMap(UrlCommand.IP);
		List<String> shouldBeWithUsedDefaultIps = urlParametersHandler.getUrlParameterFromMap(UrlCommand.USE_WITH_DEFAULT_IPS);
		List<String> defaultIps = Arrays.asList(rawRemoteStationsIpaddresses.trim().split(MULTI_VALUES_PROPERTY_SEPARATOR));
		
		if (ipAddressesList == null || ipAddressesList.size() == 0) {	// no custom ipaddresses, but only default ones
			return defaultIps;
		} 
		
		if (ipAddressesList != null && ipAddressesList.size() > 0 && shouldBeWithUsedDefaultIps != null && shouldBeWithUsedDefaultIps.size() > 0) {
			boolean isDefaultIpsShouldBeUsed = Boolean.parseBoolean(shouldBeWithUsedDefaultIps.get(0));
			if (isDefaultIpsShouldBeUsed) {
				ipAddressesList.addAll(defaultIps);
			}
		}
		
		// after getting all params the map should be cleared from data, not to affect rstatus without parameters
		urlParametersHandler.clearUrlParameterCommand(UrlCommand.IP);
		urlParametersHandler.clearUrlParameterCommand(UrlCommand.USE_WITH_DEFAULT_IPS);
		
		return ipAddressesList;
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
		
		List<String> boundIpAddressesGroups = urlParametersHandler.getUrlParameterFromMap(UrlCommand.BIND);
		
		if (boundIpAddressesGroups != null && boundIpAddressesGroups.size() > 0) {	// calculate grouped setups if any
			this.overallSetupsCurrentStatusMap = calculateOverallSetupsCurrentStatusMapWithBoundedGroups(boundIpAddressesGroups, overallSetupsCurrentStatusMap);
		} 
		
		return this.overallSetupsCurrentStatusMap;
	}
	
	/**
	 * calculates the bound groups
	 * used to calculate distributed runs
	 * @param boundIpAddressesGroups - bound groups retrieved from the url command line
	 * @return
	 */
	private Map<CurrentStatusTableField, List<String>> calculateOverallSetupsCurrentStatusMapWithBoundedGroups(List<String> boundIpAddressesGroups, Map<CurrentStatusTableField, List<String>> overallBoundIpAddressesGroups) {
		
		Map<CurrentStatusTableField, List<String>> boundIpAddrsMap = new HashMap<>();
		
		for (String boundIpAddressesGroup : boundIpAddressesGroups) {
			// dividing groups into single bound group
			String[] boundIps = boundIpAddressesGroup.split(UrlCommand.BIND_COMMAND_PARAMETERS_SEPARATOR);
			// collect data of bound group
			for (String ip : boundIps) {
				// move all bound group from overallSetupsCurrentStatusMap to boundIpAddrsMap
				int ipEntryIndex = -1;
				try {
					ipEntryIndex = getIndexInListPerEntry(overallBoundIpAddressesGroups, CurrentStatusTableField.URL, getUrl(ip));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (ipEntryIndex != -1) {
					moveEntryFromMapToMap(overallBoundIpAddressesGroups, boundIpAddrsMap, ipEntryIndex);
				} else {
					// ip not found
					continue;
				}
			}
			
			// calculate totals of collected data of bound group
			String boundValuesSeparator = "#";
			String saVersion = "";
			String runType = "";
			int passedTests = 0;
			int runTests = 0;
			int totalTestsInRun = 0;
			String runStatus = "";
			String url = "";
			
			for (CurrentStatusTableField currentStatusTableField : CurrentStatusTableField.values()) {
				
				switch (currentStatusTableField) {
				
				case PASSED_TESTS_OUT_OF_RUN_TESTS:
					for (String sValue : boundIpAddrsMap.get(currentStatusTableField)) {
						String[] pTestOutOfRtests = sValue.split(PASSED_TESTS_OUT_OF_RUN_TESTS_SEPARATOR);
						try {
							passedTests += Integer.parseInt(pTestOutOfRtests[0]);
							runTests += Integer.parseInt(pTestOutOfRtests[1]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
					break;
					
				case RUN_TYPE:
					runType = addValueToString(boundIpAddrsMap.get(currentStatusTableField), boundValuesSeparator);
					break;
					
				case SA_VERSION:
					saVersion = addValueToString(boundIpAddrsMap.get(currentStatusTableField), boundValuesSeparator);
					break;
					
				case URL:
					url = addValueToString(boundIpAddrsMap.get(currentStatusTableField), boundValuesSeparator);
					break;
					
				case RUN_STATUS:
					runStatus = addValueToString(boundIpAddrsMap.get(currentStatusTableField), boundValuesSeparator);
					break;
					
				case TOTAL_TESTS_IN_RUN:
					for (String sValue : boundIpAddrsMap.get(currentStatusTableField)) {
						try {
							totalTestsInRun += Integer.parseInt(sValue);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					break;
					
				default:
					break;
				}
			}
			
			// populate map with a calculated values
			for (CurrentStatusTableField currentStatusTableField : CurrentStatusTableField.values()) {
				switch (currentStatusTableField) {
				case PROGRESS_PERCENTAGE:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(String.format("%.2f", (double)runTests/(double)totalTestsInRun*100) + "%");
					break;
					
				case PASSED_TESTS_OUT_OF_RUN_TESTS:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(passedTests+PASSED_TESTS_OUT_OF_RUN_TESTS_SEPARATOR+runTests);
					break;
					
				case PASS_PERCENTAGE:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(String.format("%.2f", (double)passedTests/(double)runTests*100) + "%");
					break;
					
				case RUN_TYPE:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(runType);
					break;
					
				case SA_VERSION:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(saVersion);
					break;
					
				case URL:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(url);
					break;
					
				case RUN_STATUS:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(runStatus);
					break;
					
				case TOTAL_TESTS_IN_RUN:
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(String.valueOf(totalTestsInRun));
					break;
					
				default:
					break;
				}
			}
			
			// clear boundIpAddrsMap for the calculation of values of the next bound group
			boundIpAddrsMap.clear();
		}
		
		// after getting all params the map should be cleared from data, not to affect rstatus without parameters
		urlParametersHandler.clearUrlParameterCommand(UrlCommand.BIND);
		
		return overallBoundIpAddressesGroups;

	}
	
	/**
	 * 
	 * @param fieldValuesList
	 * @param separator
	 * @return
	 */
	private String addValueToString(List<String> fieldValuesList, String separator) {
		String valuesAccumulator = "";
		for (String sValue : fieldValuesList) {
			valuesAccumulator += sValue+separator;
		}
		return valuesAccumulator.replaceAll(separator+"$", "");
	}
	
	/**
	 * moves the whole record (line) from the one map to another
	 * @param srcMap - source map
	 * @param dstMap - destination map the record will be moved into
	 * @param entryIndexInSrcMap - record's index in the source map
	 */
	private void moveEntryFromMapToMap(Map<CurrentStatusTableField, List<String>> srcMap, Map<CurrentStatusTableField, List<String>> dstMap, int entryIndexInSrcMap) {

		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			List<String> tmpEntryList = new ArrayList<>();
			try {
				if (dstMap.containsKey(statusTableField)) {
					tmpEntryList.addAll(dstMap.get(statusTableField));
				}
				tmpEntryList.add(srcMap.get(statusTableField).remove(entryIndexInSrcMap));
				dstMap.put(statusTableField,tmpEntryList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * looks up for a desired entry in a field of the given map 
	 * @param statusMap - map for lookup
	 * @param currentStatusTableField - field for lookup
	 * @param entry - entry to be found
	 * @return entry's index in an list, or -1 if not found
	 */
	private int getIndexInListPerEntry(Map<CurrentStatusTableField, List<String>> statusMap, CurrentStatusTableField currentStatusTableField, String entry) {
		int index = -1;
		List<String> entryList = statusMap.get(currentStatusTableField);
		for (String lEntry : entryList) {
			index++;
			if (lEntry.equalsIgnoreCase(entry)) {
				return index;
			}
		}
		return -1;	// entry not found
	}
	
	/**
	 * Composes url from a given ip
	 * @param stationIp
	 * @return url in string format
	 * @throws Exception
	 */
	protected String getUrl(String stationIp) throws Exception {
		URL url = null;
		try {
			url = new URL(URL_PREFIX + stationIp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return VALUE_NOT_AVAILABLE;	// if values above are null, no sense to do further calculations, returning NA
		}
		return url.toString();
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
