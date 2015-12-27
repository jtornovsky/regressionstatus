package com.regressionstatus.collectorandparser;

public interface DataCollector {

	public String collectDataAtRemoteStation(String remoteStationIpaddress, String jsystemReportSourceLocation, String jsystemReportTargetLocation);
}
