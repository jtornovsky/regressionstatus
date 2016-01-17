package com.regressionstatus.data.current.runstatus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("runStatusGeneralCalculator")
public class RunStatusGeneralCalculator implements RunStatusCalculator {
	
	@Value("${regression.run.status.allowed.time.of.progress.unchanged}")
	private String regressionRunStatusAllowedTimeOfProgressUnchanged;
	
	private double prevProgressPercentageRealValue = -1;
	private long prevTimeStamp = -1;

	public RunStatus calculateRunStatus(double progressPercentageRealValue) {
		
		RunStatus runStatus = null;
		long regressionRunStatusAllowedTimeOfProgressUnchangedInLong = Long.parseLong(regressionRunStatusAllowedTimeOfProgressUnchanged);
		// TODO: think about algorithm how to calculate run status
		runStatus = RunStatus.RUNNING;
		if (progressPercentageRealValue > 99.99) {
			runStatus = RunStatus.ENDED;
		}
		prevTimeStamp = System.currentTimeMillis();
		return runStatus;
	}
}
