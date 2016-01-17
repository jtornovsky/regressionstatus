package com.regressionstatus.data.current;

import java.util.List;
import java.util.Map;

/**
 * interface to fetch, calculate and bring report to the controller side
 * this report should be shown at jsp page
 * @author jtornovsky
 *
 */
public interface CurrentRegressionStatusDataUpdater {
	
	public final String VALUE_NOT_AVAILABLE = "NA";
	
	/**
	 * fetches status data from each remote machine that runs regression,
	 * calculates results and populates overall map 
	 */
	public void fetchStatusData();
	
	/**
	 * @return map with calculated data for final report of the current regression status 
	 */
	public Map<CurrentStatusTableField, List<String>> getOverallSetupsCurrentStatusMap();
}
