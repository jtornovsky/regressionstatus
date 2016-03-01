package com.regressionstatus.data.current.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.regressionstatus.data.current.AbstractCurrentRegressionStatusDataUpdaterSummaryReport;
import com.regressionstatus.data.current.CurrentStatusTableField;
import com.regressionstatus.data.frontendparameters.current.UrlCommand;
import com.regressionstatus.data.frontendparameters.current.UrlParametersHandler;

/**
 * 
 * @author jtornovsky
 *
 */
public class DistributedRunCalculator extends AbstractUrlCommandExecuterCommonMethodsHolder {
	
	public static String BOUND_VALUES_SEPARATOR = "#";

	/**
	 * returns the map that holds the overall current status of all regression setups
	 * @return
	 */
	public static Map<CurrentStatusTableField, List<String>> calculateOverallSetupsCurrentStatusOfDistributedRun(Map<CurrentStatusTableField, List<String>> overallSetupsCurrentStatusMap, UrlParametersHandler urlParametersHandler) {
		
		List<String> boundIpAddressesGroups = urlParametersHandler.getUrlParameterFromMap(UrlCommand.BIND);
		
		if (boundIpAddressesGroups != null && boundIpAddressesGroups.size() > 0) {	// calculate bound setups if any
			try {
				overallSetupsCurrentStatusMap = calculateOverallSetupsCurrentStatusMapWithBoundedGroups(boundIpAddressesGroups, overallSetupsCurrentStatusMap, urlParametersHandler);
			} catch (Exception e) {
				e.printStackTrace();
				// after getting all params the map should be cleared from data, not to affect rstatus without parameters
				cleanUpOnReturn(urlParametersHandler, UrlCommand.BIND);
				return overallSetupsCurrentStatusMap;
			} 
		}
		return overallSetupsCurrentStatusMap;
	}
	
	/**
	 * calculates the bound groups
	 * used to calculate distributed runs
	 * @param boundIpAddressesGroups - bound groups retrieved from the url command line
	 * @return
	 */
	private static Map<CurrentStatusTableField, List<String>> calculateOverallSetupsCurrentStatusMapWithBoundedGroups(List<String> boundIpAddressesGroups, Map<CurrentStatusTableField, List<String>> overallSetupsCurrentStatusMap, UrlParametersHandler urlParametersHandler) throws Exception {
		
		Map<CurrentStatusTableField, List<String>> boundIpAddrsMap = new HashMap<>();
		
		for (String boundIpAddressesGroup : boundIpAddressesGroups) {
			// dividing groups into single bound group
			String[] boundIps = boundIpAddressesGroup.split(UrlCommand.BIND_COMMAND_PARAMETERS_SEPARATOR);
			// collect data of bound group
			for (String ip : boundIps) {
				// move all bound group from overallSetupsCurrentStatusMap to boundIpAddrsMap
				int ipEntryIndex = -1;
				try {
					ipEntryIndex = getIndexInListPerEntry(overallSetupsCurrentStatusMap, CurrentStatusTableField.URL, UrlBuilder.getUrl(ip));
				} catch (Exception e) {
					e.printStackTrace();
					// clear boundIpAddrsMap for the calculation of values of the next bound group							
					boundIpAddrsMap.clear();
					// after getting all params the map should be cleared from data, not to affect rstatus without parameters
					cleanUpOnReturn(urlParametersHandler, UrlCommand.BIND);
					return overallSetupsCurrentStatusMap;
				}
				if (ipEntryIndex != -1) {
					moveEntryFromMapToMap(overallSetupsCurrentStatusMap, boundIpAddrsMap, ipEntryIndex);
				} else {
					// ip not found
					continue;
				}
			}
			
			// calculate totals of collected data of bound group
			String saVersion = "";
			String runType = "";
			int passedTests = 0;
			int runTests = 0;
			int totalTestsInRun = 0;
			String runStatus = "";
			String url = "";
			
			for (CurrentStatusTableField currentStatusTableField : CurrentStatusTableField.values()) {
				
				if (!boundIpAddrsMap.containsKey(currentStatusTableField) || boundIpAddrsMap.get(currentStatusTableField) == null) {
					// clear boundIpAddrsMap for the calculation of values of the next bound group							
					boundIpAddrsMap.clear();
					// after getting all params the map should be cleared from data, not to affect rstatus without parameters
					cleanUpOnReturn(urlParametersHandler, UrlCommand.BIND);
					return overallSetupsCurrentStatusMap;
				}

				switch (currentStatusTableField) {

				case PASSED_TESTS_OUT_OF_RUN_TESTS:
					for (String sValue : boundIpAddrsMap.get(currentStatusTableField)) {
						String[] pTestOutOfRtests = sValue.split(AbstractCurrentRegressionStatusDataUpdaterSummaryReport.PASSED_TESTS_OUT_OF_RUN_TESTS_SEPARATOR);
						try {
							passedTests += Integer.parseInt(pTestOutOfRtests[0]);
							runTests += Integer.parseInt(pTestOutOfRtests[1]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							// clear boundIpAddrsMap for the calculation of values of the next bound group							
							boundIpAddrsMap.clear();
							// after getting all params the map should be cleared from data, not to affect rstatus without parameters
							cleanUpOnReturn(urlParametersHandler, UrlCommand.BIND);
							return overallSetupsCurrentStatusMap;
						}
					}
					break;

				case RUN_TYPE:
					runType = addValueToString(boundIpAddrsMap.get(currentStatusTableField), BOUND_VALUES_SEPARATOR);
					break;

				case SA_VERSION:
					saVersion = addValueToString(boundIpAddrsMap.get(currentStatusTableField), BOUND_VALUES_SEPARATOR);
					break;

				case URL:
					url = addValueToString(boundIpAddrsMap.get(currentStatusTableField), BOUND_VALUES_SEPARATOR);
					break;

				case RUN_STATUS:
					runStatus = addValueToString(boundIpAddrsMap.get(currentStatusTableField), BOUND_VALUES_SEPARATOR);
					break;

				case TOTAL_TESTS_IN_RUN:
					for (String sValue : boundIpAddrsMap.get(currentStatusTableField)) {
						try {
							totalTestsInRun += Integer.parseInt(sValue);
						} catch(Exception e) {
							e.printStackTrace();
							// clear boundIpAddrsMap for the calculation of values of the next bound group							
							boundIpAddrsMap.clear();
							// after getting all params the map should be cleared from data, not to affect rstatus without parameters
							cleanUpOnReturn(urlParametersHandler, UrlCommand.BIND);
							return overallSetupsCurrentStatusMap;
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
					overallSetupsCurrentStatusMap.get(currentStatusTableField).add(passedTests+AbstractCurrentRegressionStatusDataUpdaterSummaryReport.PASSED_TESTS_OUT_OF_RUN_TESTS_SEPARATOR+runTests);
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
		cleanUpOnReturn(urlParametersHandler, UrlCommand.BIND);
		
		return overallSetupsCurrentStatusMap;
	}
}
