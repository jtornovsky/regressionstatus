package com.regressionstatus.data;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("regressionStatusDataUpdaterImpl")
public class RegressionStatusDataUpdaterImpl extends AbstractRegressionStatusDataUpdater {
	
	@Resource
	protected Map<StatusTableField, String> singleSetupCurrentStatusMap;

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
//			List<String> values = getOverallSetupsCurrentStatusMap().get(statusTableField);
//			values.add(singleSetupCurrentStatusMap.get(statusTableField));
			getOverallSetupsCurrentStatusMap().get(statusTableField).add(singleSetupCurrentStatusMap.get(statusTableField));
		}

	}

}
