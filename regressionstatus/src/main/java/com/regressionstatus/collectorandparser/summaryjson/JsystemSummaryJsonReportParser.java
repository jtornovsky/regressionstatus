package com.regressionstatus.collectorandparser.summaryjson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.regressionstatus.collectorandparser.DataParser;
import com.regressionstatus.collectorandparser.SummaryReportField;
import com.regressionstatus.collectorandparser.summaryjson.JsystemSummaryJsonReportField;

/**
 * Class parses jsystem summary.html
 * parsed data populated and returned as an hash map 
 * @author jtornovsky
 *
 */
@Component("jsystemSummaryJsonReportParser")
public class JsystemSummaryJsonReportParser implements DataParser {

	/**
	 * parses jsystem summary.json report to an hash map 
	 */
	@Override
	public Map<SummaryReportField, String> parseAutomationReport(String reportTargetLocation) {

		Map<SummaryReportField, String> automationReport = null;
		
		try {
			automationReport = getDataFromSummaryFile(reportTargetLocation);				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return automationReport;
	}

	/**
	 * retrieves data from the summary.html and puts it into hash map 
	 * @param jsonFile
	 * @return
	 * @throws Exception
	 */
	private Map<SummaryReportField, String> getDataFromSummaryFile(String jsonFile) throws Exception {

		JSONParser jsonParser = new JSONParser();
		Map<SummaryReportField,String> summaryValues = new HashMap<>();
		FileReader jsonFileReader = new FileReader(jsonFile);
		
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonFileReader);
 
            @SuppressWarnings("unchecked")
			Iterator<String> iterator = jsonObject.keySet().iterator();
            
			String fieldId = null;
			Object fieldValue = null;
			
            while (iterator.hasNext()) {
            	fieldId = iterator.next().toString();
            	JsystemSummaryJsonReportField reportKey = JsystemSummaryJsonReportField.getEnumByString(fieldId);
            	if (reportKey != null) {
            		fieldValue = (Object) jsonObject.get(fieldId);
            		summaryValues.put(reportKey, String.valueOf(fieldValue));
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonFileReader.close();
            return null;
        } 
       	jsonFileReader.close();
        return summaryValues;
	}
}


