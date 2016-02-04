package com.regressionstatus.data.frontendparameters.current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("urlParametersHandlerDao")
public class UrlParametersHandlerDao implements UrlParametersHandler {
	
	private Map<UrlCommand, List<String>> urlParametersContainer = null;

	/**
	 * 
	 */
	@Override
	public void fillParametersMap(String parameters) {
		urlParametersContainer = new HashMap<>();
		for (UrlCommand urlCommand : UrlCommand.values()) {
			urlParametersContainer.put(urlCommand, new ArrayList<>());
		}
		for (String command : parameters.split(UrlCommand.COMMANDS_SEPARATOR)) {
			UrlCommand cmd = UrlCommand.getEnumByString(command);
			if (cmd == null) {
				System.out.println("Unrecognized command: " + command + ". Skipping to the next.");
				continue;
			}
			for (String commandParam : command.split(UrlCommand.COMMAND_TO_VALUES)) {
				List<String> params = Arrays.asList(commandParam, UrlCommand.PARAMS_SEPARATOR);
				if (params != null) {	// if params list is not empty
					urlParametersContainer.get(cmd).addAll(params);
				} else {
					System.out.println("Params list of the command: " + command + " is empty. Skipping");
				}
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public List<String> getParameterFromMap(UrlCommand parameterKey) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	@Override
	public Map<UrlCommand, List<String>> getAllParametersFromMap() {
		return urlParametersContainer;
	}
}
