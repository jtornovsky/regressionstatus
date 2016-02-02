package com.regressionstatus.collectorandparser.summaryjson;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.regressionstatus.collectorandparser.DataParser;
import com.regressionstatus.collectorandparser.SummaryReportField;

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
            		fieldValue = jsonObject.get(fieldId);
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


