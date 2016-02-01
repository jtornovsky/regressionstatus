package com.regressionstatus.collectorandparser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class AbstractDataCollector implements DataCollector {

	/**
	 * brings the summary report from the remote machine to the local machine
	 * @param remoteStationIpaddress - remote machine ip address
	 * @param jsystemReportSourceFile - name of a report to fetch ('summary') + .json
	 * @param jsystemReportTargetFile - name of target file onto a local machine to be used to copy a remote report
	 * @param fileExtension - file extension to build html or json summary file to collect 
	 * @throws Exception 
	 */
	protected String copySummaryFileFromRemoteToLocal(String remoteStationIpaddress, String jsystemReportSourceFile, String jsystemReportTargetFile, String fileExtension) throws Exception {
		
		URL jsystemSummaryReportUrl = null;
		
		String targetFile = jsystemReportTargetFile + fileExtension;
		String sourceFile = jsystemReportSourceFile + fileExtension;
		
		deleteOldReportFile(targetFile);
		
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
