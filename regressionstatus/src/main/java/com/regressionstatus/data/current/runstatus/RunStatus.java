package com.regressionstatus.data.current.runstatus;

/**
 * holds the possible run statuses of a regression
 * @author jtornovsky
 *
 */
public enum RunStatus {
	
	RUNNING("Running"),
	SUSPICIOUS("Suspicious"), // in case of no progress in run for a long time
	NOT_RUNNING("Not running"),
	ENDED("Ended"),
	RERUN("Rerun"),
	STOPPED("Stopped");
	
	private final String runStatus;
	
	private RunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	
	@Override
	public String toString() {
		return runStatus;
	}
}
