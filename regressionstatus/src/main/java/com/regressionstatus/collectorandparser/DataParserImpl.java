package com.regressionstatus.collectorandparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component("dataParserImpl")
public class DataParserImpl implements DataParser {

	@Override
	public Map<ReportField, String> parseAutomationReport(String reportTargetLocation) {

		try {
			Document doc = getXmlInDocumentFormat(reportTargetLocation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private Document getXmlInDocumentFormat(String xmlFile) throws Exception {

		String formattedXmlFile = formatXmlFile(xmlFile);
		File fXmlFile = new File(formattedXmlFile); 

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		return doc;
	}


	private String  formatXmlFile(String xmlFile) throws IOException   {

		List<String> lines = new ArrayList<String>();
		String line = null;

		File f1 = new File(xmlFile);
		FileReader fr = new FileReader(f1);
		BufferedReader br = new BufferedReader(fr);
		
		String xmlOutFile = xmlFile+".out"; 
		File f2 = new File(xmlOutFile);
		FileWriter fw = new FileWriter(f2);
		BufferedWriter out = new BufferedWriter(fw);
		
		while ((line = br.readLine()) != null) {
			line = line.replaceAll("&", "AA").replaceAll("<p>", "").replaceAll("</p>", "");
			lines.add(line);
		}

		for(String s : lines) {
			out.write(s);
		}
		
		fr.close();
		br.close();
		out.flush();
		out.close();
		
		return xmlOutFile;
	}
	
}


