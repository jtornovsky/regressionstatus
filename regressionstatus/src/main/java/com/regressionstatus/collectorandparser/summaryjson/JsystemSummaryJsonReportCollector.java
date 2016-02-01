package com.regressionstatus.collectorandparser.summaryjson;

import org.springframework.stereotype.Component;

import com.regressionstatus.collectorandparser.AbstractDataCollector;

/**
 * Class used to copy jsystem's summary.html report from the remote machine that runs one of regression scenario 
 * to the local machine to prepare before calculating report's statistics 
 * @author jtornovsky
 *
 */
@Component("jsystemSummaryJsonReportCollector")
public class JsystemSummaryJsonReportCollector extends AbstractDataCollector {
	
	final String FILE_EXTENSION = ".json";

	/**
	 * brings the summary.json report from the remote machine to the local machine
	 * @param remoteStationIpaddress - remote machine ip address
	 * @param jsystemReportSourceFile - name of a report to fetch ('summary') + .json
	 * @param jsystemReportTargetFile - name of target file onto a local machine to be used to copy a remote report
	 */
	@Override
	public String collectDataAtRemoteStation(String remoteStationIpaddress, String jsystemReportSourceFile, String jsystemReportTargetFile) throws Exception {
	
		return copySummaryFileFromRemoteToLocal(remoteStationIpaddress, jsystemReportSourceFile, jsystemReportTargetFile, FILE_EXTENSION);
	}
}
