package com.regressionstatus.data.current.runstatus;

/**
 * holds the possible run statuses of a regression
 * @author jtornovsky
 *
 */
public enum RunStatus {
	
	RUNNING("Running"),
	NOT_RUNNING("Not running"),
	ENDED("Ended");
	
	private final String runStatus;
	
	private RunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	
	@Override
	public String toString() {
		return runStatus;
	}
}
