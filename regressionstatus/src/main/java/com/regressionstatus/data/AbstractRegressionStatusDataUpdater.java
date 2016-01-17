package com.regressionstatus.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.regressionstatus.collectorandparser.ReportField;

public abstract class AbstractRegressionStatusDataUpdater implements RegressionStatusDataUpdater {

	@Resource(name="overallSetupsCurrentStatusMap")
	protected Map<StatusTableField, List<String>> overallSetupsCurrentStatusMap;
	
	@Bean(name="overallSetupsCurrentStatusMap")
	public Map<StatusTableField, List<String>> initOverallSetupsCurrentStatusMap() {
		Map<StatusTableField, List<String>> localOverallSetupsCurrentStatusMap = new TreeMap<>(); // tree map to sort keys as their ordinal order
		for (StatusTableField statusTableField : StatusTableField.values()) {
			localOverallSetupsCurrentStatusMap.put(statusTableField, new ArrayList<>());
		}
		return localOverallSetupsCurrentStatusMap;
	}

	public Map<StatusTableField, List<String>> getOverallSetupsCurrentStatusMap() {
		return this.overallSetupsCurrentStatusMap;
	}
	
	protected void backUpOldDataAndClearOverallSetupsCurrentStatusMap() {
		// TODO backup procedure for the old data
		
		for (StatusTableField statusTableField : StatusTableField.values()) {
			overallSetupsCurrentStatusMap.get(statusTableField).clear();
//			overallSetupsCurrentStatusMap.get(statusTableField).add(VALUE_NOT_AVAILABLE);
		}
	}
	
	protected void fillOverallSetupsStatusMap(Map<StatusTableField, String> singleSetupCurrentStatusMap) {
//		backUpOldDataAndClearOverallSetupsCurrentStatusMap();
		for (StatusTableField statusTableField : StatusTableField.values()) {
			getOverallSetupsCurrentStatusMap().get(statusTableField).add(singleSetupCurrentStatusMap.get(statusTableField));
		}
	}
	
	protected Map<StatusTableField, String> initStatusFieldMap() {
		
		Map<StatusTableField, String> map = new HashMap<>();
		for (StatusTableField statusTableField : StatusTableField.values()) {
			map.put(statusTableField, VALUE_NOT_AVAILABLE);
		}
		return map;
	}
}
