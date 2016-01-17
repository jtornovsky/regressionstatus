package com.regressionstatus.collectorandparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;

/**
 * Class used to copy jsystem's summary.html report from the remote machine that runs one of regression scenario 
 * to the local machine to prepare before calculating report's statistics 
 * @author jtornovsky
 *
 */
@Component("jsystemSummaryReportCollector")
public class JsystemSummaryReportCollector implements DataCollector {

	@Override
	public void collectDataAtRemoteStation(String remoteStationIpaddress, String jsystemReportSourceFile, String jsystemReportTargetFile) {

		URL jsystemSummaryReportUrl = null;
		
		// building url to copy summary.html report
		try {
			jsystemSummaryReportUrl = new URL("http://" + remoteStationIpaddress + "/" + jsystemReportSourceFile);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		// copying summary.html report from remote to local 
		InputStream in = null;
		try  {
			in = jsystemSummaryReportUrl.openStream();
			Path targetPath = Paths.get(jsystemReportTargetFile);
		    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			 // Close stream here
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
