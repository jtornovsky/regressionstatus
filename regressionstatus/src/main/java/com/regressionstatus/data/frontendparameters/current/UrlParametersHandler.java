package com.regressionstatus.data.frontendparameters.current;

import java.util.List;
import java.util.Map;

/**
 * interface gives an api to work with parameters which provided via url 
 * @author jtornovsky
 *
 */
public interface UrlParametersHandler {

	/**
	 * retrieves and fills a map with parameters from URL  
	 * @param parameters - parameters in string format taken from url
	 */
	public void fillUrlParametersMap(String parameters);
	
	/**
	 * @param parameterKey - command as appears in UrlCommand enum
	 * @return parameters of the given command
	 */
	public List<String> getUrlParameterFromMap(UrlCommand parameterKey);
	
	/**
	 * @return entire map with commands and their parameters, which were provided by url
	 */
	public Map<UrlCommand, List<String>> getAllUrlParametersFromMap();
	
	/**
	 * after getting all params the map should be cleared from data, not to affect rstatus without parameters
	 */
	public void clearUrlParametersMap();
}
