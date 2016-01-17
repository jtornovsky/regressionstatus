/**
 * 
 */
package com.regressionstatus.data.current.runstatus;

/**
 * Calculates run status of the examined regression machine
 * @author jtornovsky
 *
 */
public interface RunStatusCalculator {

	public RunStatus calculateRunStatus(double progressPercentageRealValue);
}
