package com.regressionstatus.collectorandparser;

public interface DataCollector {

	public void collectDataAtRemoteStation(String remoteStationIpaddress, String remoteStationReportSourceFile, String localStationReportTargetFile);
}
