package com.regressionstatus.data.frontendparameters.current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * class works with external parameters provided via url
 * @author jtornovsky
 *
 */
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
	 * retrieves and fills a map with parameters from URL  
	 * @param parameters - parameters in string format taken from url
	 */
	@Override
	public void fillUrlParametersMap(String parameters) {

		clearUrlParametersMap();

		for (String command : parameters.split(UrlCommand.COMMANDS_SEPARATOR)) {
			try {
				UrlCommand cmd = UrlCommand.getEnumByString(command.trim().split(UrlCommand.COMMAND_TO_VALUES)[0]);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * validates the correctness of values of the provided commands
	 * @param cmd - command
	 * @param cmdParams - command's parameters
	 * @return - list of validated parameters
	 */
	private List<String> validateParams(UrlCommand cmd, List<String> cmdParams) throws Exception {
		
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
					System.out.println("Illegal ip address: " + cmdParam + ". Skipping");
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
				
			case BIND:
				String boundIpsGroup = "";
				for (String boundIp : cmdParam.split(UrlCommand.BIND_COMMAND_PARAMETERS_SEPARATOR)) {
					ipAddr = boundIp.trim();
					if (InetAddressValidator.getInstance().isValid(ipAddr)) {
						boundIpsGroup += ipAddr+UrlCommand.BIND_COMMAND_PARAMETERS_SEPARATOR;
					} else {
						System.out.println("Illegal ip address: " + boundIp + ". Skipping");
					}
				}
				cmdValidatedParams.add(boundIpsGroup);
				break;
				
			default:
				// nothing to do
				break;
			}
		}
		return cmdValidatedParams;
	}

	/**
	 * @param parameterKey - command as appears in UrlCommand enum
	 * @return parameters of the given command
	 */
	@Override
	public List<String> getUrlParameterFromMap(UrlCommand parameterKey) {
		if (urlParametersContainer == null || !urlParametersContainer.containsKey(parameterKey)) {
			return null;
		} 
		return urlParametersContainer.get(parameterKey);
	}

	/**
	 * @return entire map with commands and their parameters, which were provided by url
	 */
	@Override
	public Map<UrlCommand, List<String>> getAllUrlParametersFromMap() {
		return urlParametersContainer;
	}

	/**
	 * after getting all params the map should be cleared from data, not to affect rstatus without parameters
	 */
	@Override
	public void clearUrlParametersMap() {
		urlParametersContainer.clear(); //deleting previously filled values
	}
}
