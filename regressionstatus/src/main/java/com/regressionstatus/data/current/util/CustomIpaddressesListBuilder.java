package com.regressionstatus.data.current.util;

import java.util.Arrays;
import java.util.List;

import com.regressionstatus.data.current.AbstractCurrentRegressionStatusDataUpdaterSummaryReport;
import com.regressionstatus.data.frontendparameters.current.UrlCommand;
import com.regressionstatus.data.frontendparameters.current.UrlParametersHandler;

/**
 * 
 * @author jtornovsky
 *
 */
public class CustomIpaddressesListBuilder extends AbstractUrlCommandExecuterCommonMethodsHolder {

	/**
	 * retrieves the ip addresses of setups to collect jsystem reports from
	 * @param rawRemoteStationsIpaddresses - default ip addresses appear in app.properties
	 * @return list of all ips of the setups to collect jsystem reports from
	 */
	public static List<String> getRemoteStationsIpaddresses(String rawRemoteStationsIpaddresses, UrlParametersHandler urlParametersHandler) {
		
		List<String> ipAddressesList = urlParametersHandler.getUrlParameterFromMap(UrlCommand.IP);
		List<String> shouldBeWithUsedDefaultIps = urlParametersHandler.getUrlParameterFromMap(UrlCommand.USE_WITH_DEFAULT_IPS);
		List<String> defaultIps = Arrays.asList(rawRemoteStationsIpaddresses.trim().split(AbstractCurrentRegressionStatusDataUpdaterSummaryReport.MULTI_VALUES_PROPERTY_SEPARATOR));
		
		if (ipAddressesList == null || ipAddressesList.size() == 0) {	// no custom ipaddresses, but only default ones
			cleanUpOnReturn(urlParametersHandler, UrlCommand.IP, UrlCommand.USE_WITH_DEFAULT_IPS);
			return defaultIps;
		} 
		
		if (ipAddressesList != null && ipAddressesList.size() > 0 && shouldBeWithUsedDefaultIps != null && shouldBeWithUsedDefaultIps.size() > 0) {
			boolean isDefaultIpsShouldBeUsed = Boolean.parseBoolean(shouldBeWithUsedDefaultIps.get(0));
			if (isDefaultIpsShouldBeUsed) {
				ipAddressesList.addAll(defaultIps);
			}
		}
		
		// after getting all params the map should be cleared from data, not to affect rstatus without parameters
		cleanUpOnReturn(urlParametersHandler, UrlCommand.IP, UrlCommand.USE_WITH_DEFAULT_IPS);
		
		return ipAddressesList;
	}

}
