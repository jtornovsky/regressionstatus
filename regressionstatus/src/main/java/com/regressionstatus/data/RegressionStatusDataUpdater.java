package com.regressionstatus.data;

import java.util.List;
import java.util.Map;

public interface RegressionStatusDataUpdater {
	
	/**
	 * fetches status data from dev machine
	 */
	public void fetchStatusData();
	
//	/**
//	 * populate overall status map
//	 */
//	public void fillStatusData();
	
	/**
	 * 
	 * @return
	 */
	public Map<StatusTableField, List<String>> getOverallSetupsCurrentStatusMap();
}
