package com.regressionstatus.data.frontendparameters.current;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("urlParametersHandlerDao")
public class UrlParametersHandlerDao implements UrlParametersHandler {

	@Override
	public void fillParametersMap(String parameters) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<String> getParameterFromMap(UrlCommand parameterKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<UrlCommand, List<String>> getAllParametersFromMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getParameterFromMap(UrlCommand parameterKey, String[] defaultParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
