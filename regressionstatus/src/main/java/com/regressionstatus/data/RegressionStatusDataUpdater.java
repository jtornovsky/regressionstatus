package com.regressionstatus.data;

import java.util.List;
import java.util.Map;

/**
 * interface to fetch, calculate and bring report to the controller side
 * this report should be shown at jsp page
 * @author jtornovsky
 *
 */
public interface RegressionStatusDataUpdater {
	
	public final String VALUE_NOT_AVAILABLE = "NA";
	
	/**
	 * fetches status data from each remote machine that runs regression,
	 * calculates results and populates overall map 
	 */
	public void fetchStatusData();
	
	/**
	 * returns map with calculated data for final current report
	 * @return
	 */
	public Map<StatusTableField, List<String>> getOverallSetupsCurrentStatusMap();
}
