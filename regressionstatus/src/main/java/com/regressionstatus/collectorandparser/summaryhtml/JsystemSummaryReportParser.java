package com.regressionstatus.collectorandparser.summaryhtml;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.regressionstatus.collectorandparser.DataParser;

@Component("jsystemSummaryReportParser")
public class JsystemSummaryReportParser implements DataParser {

	@Override
	public Map<JsystemSummaryReportField, String> parseAutomationReport(String reportTargetLocation) {

		Map<JsystemSummaryReportField, String> automationReport = new HashMap<>();
		
		try {
			automationReport = getDataFromSummaryFile(reportTargetLocation);				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return automationReport;
	}

	/**
	 * retrieves data from the summary.html and puts it into hash map 
	 * @param htmlFile
	 * @return
	 * @throws Exception
	 */
	private Map<JsystemSummaryReportField, String> getDataFromSummaryFile(String htmlFile) throws Exception {

		String formattedHtmlFile = formatHtmlFile(htmlFile);
		File fHtmlFile = new File(formattedHtmlFile); 

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fHtmlFile);
		doc.getDocumentElement().normalize();
		NodeList list = doc.getElementsByTagName("tr");
		Map<JsystemSummaryReportField,String> summaryValues = new HashMap<>();
		
		if (list != null && list.getLength() > 0) {
			NodeList tdList;
			Node tr;
			String fieldId;
			String fieldValue;
			
			for (int i=0; i < list.getLength(); i++) {
				tr = list.item(i);
				tdList = tr.getChildNodes();
				if(tdList.getLength() == 2) { // taking field with only 2 elements
					fieldId = tdList.item(0).getTextContent();
					JsystemSummaryReportField reportKey = JsystemSummaryReportField.getEnumByString(fieldId);
					if (reportKey != null) {
						fieldValue = tdList.item(1).getTextContent();
						summaryValues.put(reportKey, fieldValue);
					}
				}
			}
		}
		return summaryValues;
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


