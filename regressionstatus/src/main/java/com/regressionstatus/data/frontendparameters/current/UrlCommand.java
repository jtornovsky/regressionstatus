package com.regressionstatus.data.frontendparameters.current;

/**
 * treating url with parameters, like that:
 * http://localhost:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.26.30.157&192.168.10.16;usedefaultips=false;
 * @author jtornovsky
 *
 */
public enum UrlCommand {

	IP("ip"),
	USE_DEFAULT_IPS("usedefaultips");
	
	private final String urlCommand;
	
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
	
	public static UrlCommand getEnumByString(String enumStringValue) {
		UrlCommand enumValue = null;
		for (UrlCommand value : UrlCommand.values()) {
			if (value.toString().equalsIgnoreCase(enumStringValue)) {
				enumValue = value;
			}
		}
		return enumValue;
	}
}