package com.regressionstatus.collectorandparser;

/**
 * interface responsible to fetch data from the remote server to the local machine
 * @author jtornovsky
 *
 */
public interface DataCollector {

	/**
	 * 
	 * @param remoteStationIpaddress
	 * @param remoteStationReportSourceFile
	 * @param localStationReportTargetFile
	 * @return - created file name
	 * @throws Exception
	 */
	public String collectDataAtRemoteStation(String remoteStationIpaddress, String remoteStationReportSourceFile, String localStationReportTargetFile) throws Exception;
}
