package com.regressionstatus.data;

public interface RegressionStatusDataUpdater {
	
	/**
	 * fetches status data from dev machine
	 */
	public void fetchStatusData();
	
	/**
	 * populate overall status map
	 */
	public void fillStatusData();
}
