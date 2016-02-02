package com.regressionstatus.data.current.runstatus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("runStatusGeneralCalculator")
public class RunStatusGeneralCalculator implements RunStatusCalculator {
	
	@Value("${regression.run.status.allowed.time.of.progress.unchanged}")
	private String regressionRunStatusAllowedTimeOfProgressUnchanged;
	
	private double prevProgressPercentageRealValue = 0.0001;
	private long prevTimeStamp = -1;

	@Override
	public RunStatus calculateRunStatus(double progressPercentageRealValue) {
		
		RunStatus runStatus = null;
		long currentTimeStamp = System.currentTimeMillis()/1000;
		

		// TODO: think about algorithm how to calculate run status
		runStatus = RunStatus.RUNNING;
		if (progressPercentageRealValue > 99.99) {
			return RunStatus.ENDED; // no sense for the further calculations as a run cycle of an examined setup has ended
		}
		
//		long regressionRunStatusAllowedTimeOfProgressUnchangedInLong = -1;
//		try {
//			regressionRunStatusAllowedTimeOfProgressUnchangedInLong = Long.parseLong(regressionRunStatusAllowedTimeOfProgressUnchanged);
//		} catch(NumberFormatException e) {
//			e.printStackTrace();
//			return runStatus;	// cannot make further calculations as a parameter is wrong, returning status as it is so far
//		}
//		
//		// checking if the progress percentage doesn't get changed for a more than allowed time
//		if ((currentTimeStamp - prevTimeStamp) > regressionRunStatusAllowedTimeOfProgressUnchangedInLong) {
//			if ((progressPercentageRealValue - prevProgressPercentageRealValue) < 0.001) {   
//				runStatus = RunStatus.NOT_RUNNING;
//			}
//			prevTimeStamp = currentTimeStamp;
//			prevProgressPercentageRealValue = progressPercentageRealValue;
//		}

		return runStatus;
	}
}
