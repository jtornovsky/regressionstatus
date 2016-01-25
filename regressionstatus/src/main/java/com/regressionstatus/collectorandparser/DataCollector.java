package com.regressionstatus.collectorandparser;

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
