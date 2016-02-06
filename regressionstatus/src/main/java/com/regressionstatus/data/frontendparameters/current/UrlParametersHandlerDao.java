package com.regressionstatus.data.frontendparameters.current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.validator.routines.InetAddressValidator;
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
			List<String> params = validateParams(cmd, Arrays.asList(parametersAsStringArray));
			if (params != null) {	// if params list is not empty
				urlParametersContainer.put(cmd, params);
			} 
		}
	}
	
	private List<String> validateParams(UrlCommand cmd, List<String> cmdParams) {
		
		if (cmdParams == null) {
			System.out.println("Params list of the command: " + cmd.toString() + " is empty. Skipping");
			return null;
		}
		
		List<String> cmdValidatedParams = new ArrayList<>();
		String ipAddr = null;
		String boolValue = null;
		
		for (String cmdParam : cmdParams) {
			switch (cmd) {
			case IP:
				ipAddr = cmdParam.trim();
				if (InetAddressValidator.getInstance().isValid(ipAddr)) {
					cmdValidatedParams.add(ipAddr);
				} else {
					System.out.println("Illegal ip adress: " + cmdParam + ". Skipping");
				}
				break;
			case USE_WITH_DEFAULT_IPS:
				boolValue = cmdParam.toLowerCase().trim();
				if (boolValue.equals("false") || boolValue.equals("true")) {
					cmdValidatedParams.add(boolValue);
				} else {
					System.out.println("Illegal boolean value: " + cmdParam + ". Skipping");
				}
				break;
			default:
				// nothing to do
				break;
			}
		}
		return cmdValidatedParams;
	}


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
