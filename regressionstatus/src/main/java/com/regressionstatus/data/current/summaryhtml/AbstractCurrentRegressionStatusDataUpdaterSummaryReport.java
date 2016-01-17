package com.regressionstatus.data.current.summaryhtml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.regressionstatus.collectorandparser.summaryhtml.JsystemSummaryReportField;
import com.regressionstatus.data.current.CurrentRegressionStatusDataUpdater;
import com.regressionstatus.data.current.CurrentStatusTableField;

public abstract class AbstractCurrentRegressionStatusDataUpdaterSummaryReport implements CurrentRegressionStatusDataUpdater {

	@Resource(name="overallSetupsCurrentStatusMap")
	protected Map<CurrentStatusTableField, List<String>> overallSetupsCurrentStatusMap;
	
	@Bean(name="overallSetupsCurrentStatusMap")
	public Map<CurrentStatusTableField, List<String>> initOverallSetupsCurrentStatusMap() {
		Map<CurrentStatusTableField, List<String>> localOverallSetupsCurrentStatusMap = new TreeMap<>(); // tree map to sort keys as their ordinal order
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			localOverallSetupsCurrentStatusMap.put(statusTableField, new ArrayList<>());
		}
		return localOverallSetupsCurrentStatusMap;
	}

	public Map<CurrentStatusTableField, List<String>> getOverallSetupsCurrentStatusMap() {
		return this.overallSetupsCurrentStatusMap;
	}
	
	protected void backUpOldDataAndClearOverallSetupsCurrentStatusMap() {
		// TODO backup procedure for the old data
		
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			overallSetupsCurrentStatusMap.get(statusTableField).clear();
//			overallSetupsCurrentStatusMap.get(statusTableField).add(VALUE_NOT_AVAILABLE);
		}
	}
	
	protected void fillOverallSetupsStatusMap(Map<CurrentStatusTableField, String> singleSetupCurrentStatusMap) {
//		backUpOldDataAndClearOverallSetupsCurrentStatusMap();
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			getOverallSetupsCurrentStatusMap().get(statusTableField).add(singleSetupCurrentStatusMap.get(statusTableField));
		}
	}
	
	protected Map<CurrentStatusTableField, String> initStatusFieldMap() {
		
		Map<CurrentStatusTableField, String> map = new HashMap<>();
		for (CurrentStatusTableField statusTableField : CurrentStatusTableField.values()) {
			map.put(statusTableField, VALUE_NOT_AVAILABLE);
		}
		return map;
	}
}
