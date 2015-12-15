package com.regressionstatus.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component("regressionStatusDataUpdaterImpl")
public class RegressionStatusDataUpdaterImpl extends AbstractRegressionStatusDataUpdater {
	
	@Resource(name="singleSetupCurrentStatusMap")
	protected Map<StatusTableField, String> singleSetupCurrentStatusMap;
	
	@Bean(name="singleSetupCurrentStatusMap")
	public Map<StatusTableField, String> initSingleSetupCurrentStatusMap() {
		Map<StatusTableField, String> localSingleSetupsCurrentStatusMap = new HashMap<>();
		for (StatusTableField statusTableField : StatusTableField.values()) {
			localSingleSetupsCurrentStatusMap.put(statusTableField, "");
		}
		return localSingleSetupsCurrentStatusMap;
	}

	@Override
	public void fetchStatusData() {
		singleSetupCurrentStatusMap.put(StatusTableField.SA_VERSION, "123");
		singleSetupCurrentStatusMap.put(StatusTableField.RUN_TYPE, "u4");
		singleSetupCurrentStatusMap.put(StatusTableField.PASS_PERCENTAGE, "96"+"%");
		singleSetupCurrentStatusMap.put(StatusTableField.PASSED_TESTS_OUT_OF_RUN_TESTS, "456 out 900");
		singleSetupCurrentStatusMap.put(StatusTableField.PROGRESS_PERCENTAGE, "56" + "%");
		singleSetupCurrentStatusMap.put(StatusTableField.TOTAL_TESTS_IN_RUN, "1024");
		singleSetupCurrentStatusMap.put(StatusTableField.RUN_STATUS, "running");
		singleSetupCurrentStatusMap.put(StatusTableField.DETAILS, "http://");
	}

	@Override
	public void fillStatusData() {
		for (StatusTableField statusTableField : StatusTableField.values()) {
			getOverallSetupsCurrentStatusMap().get(statusTableField).add(singleSetupCurrentStatusMap.get(statusTableField));
		}
	}
}
