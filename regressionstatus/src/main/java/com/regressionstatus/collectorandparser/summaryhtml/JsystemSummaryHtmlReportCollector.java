package com.regressionstatus.collectorandparser.summaryhtml;

import org.springframework.stereotype.Component;

import com.regressionstatus.collectorandparser.AbstractDataCollector;

/**
 * Class used to copy jsystem's summary.html report from the remote machine that runs one of regression scenario 
 * to the local machine to prepare before calculating report's statistics 
 * @author jtornovsky
 *
 */
@Component("jsystemSummaryHtmlReportCollector")
public class JsystemSummaryHtmlReportCollector extends AbstractDataCollector {
	
	final String FILE_EXTENSION = ".html";

	/**
	 * brings the summary.html report from the remote machine to the local machine
	 * @param remoteStationIpaddress - remote machine ip address
	 * @param jsystemReportSourceFile - name of a report to fetch ('summary') + .html
	 * @param jsystemReportTargetFile - name of target file onto a local machine to be used to copy a remote report
	 */
	@Override
	public String collectDataAtRemoteStation(String remoteStationIpaddress, String jsystemReportSourceFile, String jsystemReportTargetFile) throws Exception {

		return copySummaryFileFromRemoteToLocal(remoteStationIpaddress, jsystemReportSourceFile, jsystemReportTargetFile, FILE_EXTENSION);
	}
}
