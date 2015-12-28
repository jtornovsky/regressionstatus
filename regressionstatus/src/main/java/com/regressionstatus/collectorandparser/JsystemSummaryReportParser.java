package com.regressionstatus.collectorandparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component("JsystemSummaryReportParser")
public class JsystemSummaryReportParser implements DataParser {

	@Override
	public Map<ReportField, String> parseAutomationReport(String reportTargetLocation) {

		Map<ReportField, String> automationReport = new HashMap<>();
		
		try {
			Document doc = getHtmlInDocumentFormat(reportTargetLocation);
			for (ReportField reportField : ReportField.values()) {
				  Element nameElement = doc.getElementById(reportField.toString());
//				  nameElement.getNodeValue();
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}

		return automationReport;
	}

	/**
	 * converts textual html file to doc format 
	 * @param htmlFile
	 * @return
	 * @throws Exception
	 */
	private Document getHtmlInDocumentFormat(String htmlFile) throws Exception {

		String formattedHtmlFile = formatHtmlFile(htmlFile);
		File fHtmlFile = new File(formattedHtmlFile); 

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fHtmlFile);
		doc.getDocumentElement().normalize();
		
		return doc;
	}

	/**
	 * reformats the raw html report to get rid from the unparsable characters
	 * @param htmlFile
	 * @return
	 * @throws IOException
	 */
	private String  formatHtmlFile(String htmlFile) throws IOException   {

		List<String> lines = new ArrayList<String>();
		String line = null;

		File sourceHtmlFile = new File(htmlFile);
		FileReader srcFileReader = new FileReader(sourceHtmlFile);
		BufferedReader srcBufferedReader = new BufferedReader(srcFileReader);
		
		String reformattedHtmlFile = htmlFile+".out"; 
		File targetHtmlFile = new File(reformattedHtmlFile);
		FileWriter targetFileWriter = new FileWriter(targetHtmlFile);
		BufferedWriter targetBufferedWriter = new BufferedWriter(targetFileWriter);
		
		while ((line = srcBufferedReader.readLine()) != null) {
			line = line.replaceAll("&", "AA").replaceAll("<p>", "").replaceAll("</p>", "");
			lines.add(line);
		}

		for(String s : lines) {
			targetBufferedWriter.write(s);
		}
		
		srcFileReader.close();
		srcBufferedReader.close();
		targetBufferedWriter.flush();
		targetBufferedWriter.close();
		
		return reformattedHtmlFile;
	}
	
}


