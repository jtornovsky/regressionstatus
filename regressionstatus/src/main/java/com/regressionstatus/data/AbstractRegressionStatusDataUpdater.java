package com.regressionstatus.data;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRegressionStatusDataUpdater implements RegressionStatusDataUpdater {

	@Resource
	protected Map<StatusTableField, List<String>> overallSetupsCurrentStatusMap;

//	public AbstractRegressionStatusDataUpdater() {
//		if (overallSetupsCurrentStatusMap == null) {
////			overallSetupsCurrentStatusMap = new HashMap<>();
//			for (StatusTableField statusTableField : StatusTableField.values()) {
//				overallSetupsCurrentStatusMap.put(statusTableField, new ArrayList<>());
//			}
//		}
//	}

	public Map<StatusTableField, List<String>> getOverallSetupsCurrentStatusMap() {
		return overallSetupsCurrentStatusMap;
	}
}
