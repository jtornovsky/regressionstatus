package com.regressionstatus.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public abstract class AbstractRegressionStatusDataUpdater implements RegressionStatusDataUpdater {

	@Resource(name="overallSetupsCurrentStatusMap")
	protected Map<StatusTableField, List<String>> overallSetupsCurrentStatusMap;
	
	@Bean(name="overallSetupsCurrentStatusMap")
	public Map<StatusTableField, List<String>> initOverallSetupsCurrentStatusMap() {
		Map<StatusTableField, List<String>> localOverallSetupsCurrentStatusMap = new HashMap<>();
		for (StatusTableField statusTableField : StatusTableField.values()) {
			localOverallSetupsCurrentStatusMap.put(statusTableField, new ArrayList<>());
		}
		return localOverallSetupsCurrentStatusMap;
	}

	public Map<StatusTableField, List<String>> getOverallSetupsCurrentStatusMap() {
		return this.overallSetupsCurrentStatusMap;
	}
}
