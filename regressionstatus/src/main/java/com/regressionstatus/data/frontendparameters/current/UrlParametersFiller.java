package com.regressionstatus.data.frontendparameters.current;

import java.util.List;
import java.util.Map;

public interface UrlParametersFiller {

	public Map<UrlCommands, List<String>> fillParametersMap(String parameters);
	public List<String> getParameterFromMap(UrlCommands parameterKey);
	public Map<UrlCommands, List<String>> getAllParametersFromMap();
}
