package com.regressionstatus.data.frontendparameters.current;

import java.util.List;
import java.util.Map;

public interface UrlParametersHandler {

	public void fillParametersMap(String parameters);
	public List<String> getParameterFromMap(UrlCommand parameterKey);
	public Map<UrlCommand, List<String>> getAllParametersFromMap();
}
