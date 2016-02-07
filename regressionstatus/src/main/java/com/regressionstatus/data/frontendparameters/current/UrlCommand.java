package com.regressionstatus.data.frontendparameters.current;

/**
 * treating url with parameters, like that:
 * http://localhost:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45&192.168.20.107&192.168.20.245;usedefaultips=true
 * http://localhost:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45&192.168.20.107;usedefaultips=false 
 * @author jtornovsky
 *
 */
public enum UrlCommand {

	IP("ip"),
	USE_WITH_DEFAULT_IPS("usedefaultips");
	
	private final String urlCommand;
	
	// usage: see in enum java doc 
	public static final String PARAMS_SEPARATOR = "&";
	public static final String COMMAND_TO_VALUES = "=";
	public static final String COMMANDS_SEPARATOR = ";";

	private UrlCommand(String urlCommand) {
		this.urlCommand = urlCommand;
	}
	
	@Override
	public String toString() {
		return urlCommand;
	}
	
	/**
	 * @param enumStringValue - string value to be converted to enum
	 * @return enum equivalent of a string value
	 */
	public static UrlCommand getEnumByString(String enumStringValue) {
		UrlCommand enumValue = null;
		for (UrlCommand value : UrlCommand.values()) {
			if (value.toString().equalsIgnoreCase(enumStringValue.trim())) {
				enumValue = value;
			}
		}
		return enumValue;
	}
}
