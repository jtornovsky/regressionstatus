package com.regressionstatus.data.frontendparameters.current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.regressionstatus.data.current.CurrentStatusTableField;

@Component("urlParametersHandlerDao")
public class UrlParametersHandlerDao implements UrlParametersHandler {
	
	@Resource(name="urlParametersContainer")
	private Map<UrlCommand, List<String>> urlParametersContainer = null;
	
	@Bean(name="urlParametersContainer")
	private Map<UrlCommand, List<String>> initUrlParametersContainer() {
		Map<UrlCommand, List<String>> localUrlParametersContainer = new TreeMap<>(); // tree map to sort keys as their ordinal order
		for (UrlCommand urlCommand : UrlCommand.values()) {
			localUrlParametersContainer.put(urlCommand, new ArrayList<>());
		}
		return localUrlParametersContainer;
	}

	/**
	 * 
	 */
	@Override
	public void fillParametersMap(String parameters) {

		urlParametersContainer.clear(); //deleting previously filled values

		for (String command : parameters.split(UrlCommand.COMMANDS_SEPARATOR)) {
			UrlCommand cmd = UrlCommand.getEnumByString(command.split(UrlCommand.COMMAND_TO_VALUES)[0]);
			if (cmd == null) {
				System.out.println("Unrecognized command: " + command + ". Skipping to the next.");
				continue;
			}
			String[] parametersAsStringArray = command.split(UrlCommand.COMMAND_TO_VALUES)[1].split(UrlCommand.PARAMS_SEPARATOR);
			if (parametersAsStringArray == null) {
				System.out.println("Unrecognized params: " + command + ". Skipping to the next.");
				continue;
			}
			List<String> params = Arrays.asList(parametersAsStringArray);
			if (params != null) {	// if params list is not empty
				urlParametersContainer.put(cmd, params);
			} else {
				System.out.println("Params list of the command: " + command + " is empty. Skipping");
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public List<String> getParameterFromMap(UrlCommand parameterKey) {
		if (urlParametersContainer == null || !urlParametersContainer.containsKey(parameterKey)) {
			return null;
		} 
		return urlParametersContainer.get(parameterKey);
	}

	/**
	 * 
	 */
	@Override
	public Map<UrlCommand, List<String>> getAllParametersFromMap() {
		return urlParametersContainer;
	}
}
