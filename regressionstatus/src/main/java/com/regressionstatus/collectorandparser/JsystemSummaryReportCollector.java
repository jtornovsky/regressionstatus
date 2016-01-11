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

@Component("jsystemSummaryReportCollector")
public class JsystemSummaryReportCollector implements DataCollector {

	@Override
	public void collectDataAtRemoteStation(String remoteStationIpaddress, String jsystemReportSourceFile, String jsystemReportTargetFile) {

		URL jsystemSummaryReportUrl = null;
		try {
			jsystemSummaryReportUrl = new URL("http://" + remoteStationIpaddress + "/" + jsystemReportSourceFile);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try (InputStream in = jsystemSummaryReportUrl.openStream()) {
			Path targetPath = Paths.get(jsystemReportTargetFile);
		    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
