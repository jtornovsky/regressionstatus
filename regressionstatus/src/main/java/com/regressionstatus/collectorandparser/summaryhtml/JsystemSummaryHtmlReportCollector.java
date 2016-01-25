package com.regressionstatus.collectorandparser.summaryhtml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;

import com.regressionstatus.collectorandparser.DataCollector;

/**
 * Class used to copy jsystem's summary.html report from the remote machine that runs one of regression scenario 
 * to the local machine to prepare before calculating report's statistics 
 * @author jtornovsky
 *
 */
@Component("jsystemSummaryHtmlReportCollector")
public class JsystemSummaryHtmlReportCollector implements DataCollector {
	
	final String FILE_EXTENSION = ".html";

	/**
	 * brings the summary.html report from the remote machine to the local machine
	 * @param remoteStationIpaddress - remote machine ip address
	 * @param jsystemReportSourceFile - name of a report to fetch ('summary') + .html
	 * @param jsystemReportTargetFile - name of target file onto a local machine to be used to copy a remote report
	 */
	@Override
	public String collectDataAtRemoteStation(String remoteStationIpaddress, String jsystemReportSourceFile, String jsystemReportTargetFile) throws Exception {

		URL jsystemSummaryReportUrl = null;
		
		String targetFile = jsystemReportTargetFile + FILE_EXTENSION;
		String sourceFile = jsystemReportSourceFile + FILE_EXTENSION;
		
		deleteOldReportFile(targetFile);

		// building url to copy summary.html report
		jsystemSummaryReportUrl = new URL("http://" + remoteStationIpaddress + "/" + sourceFile);

		// copying summary.html report from remote to local 
		InputStream in = null;
		try  {
			in = jsystemSummaryReportUrl.openStream();
			Path targetPath = Paths.get(targetFile);
			Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			targetFile = null;
		} finally {
			// in case the stream was opened...
			if (in != null) {
				try {
					// ...close stream here
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return targetFile;
	}
	
	/**
	 * deletes previously fetched summary.html file
	 * @param fileName
	 * @throws Exception
	 */
	private void deleteOldReportFile(String fileName) throws Exception {
		
		File file = new File(fileName);
		if(!file.delete()) {
			System.out.println("Failed to delete: " + fileName);
		}

	}
}
