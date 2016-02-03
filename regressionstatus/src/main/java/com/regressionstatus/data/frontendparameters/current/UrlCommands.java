package com.regressionstatus.data.frontendparameters.current;

/**
 * treating url with parameters, like that:
 * http://localhost:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.26.30.157&192.168.10.16;usedefaultips=false;
 * @author jtornovsky
 *
 */
public enum UrlCommands {

	IP("ip"),
	USE_DEFAULT_IPS("usedefaultips"),
	/////////////// for the future use
	SHOW_IP("showip"),
	ADD_IP("addip"),
	REMOVE_IP("removeip"),
	REMOVE_ALL_IPS("removeallips");
	
	private final String urlCommand;
	
	public static final String COMMAND_VALUES_SEPARATOR = "&";
	public static final String COMMAND_TO_VALUES = "=";
	public static final String COMMANDS_SEPARATOR = ";";

	private UrlCommands(String urlCommand) {
		this.urlCommand = urlCommand;
	}
	
	@Override
	public String toString() {
		return urlCommand;
	}
	
	public static UrlCommands getEnumByString(String enumStringValue) {
		UrlCommands enumValue = null;
		for (UrlCommands value : UrlCommands.values()) {
			if (value.toString().equalsIgnoreCase(enumStringValue)) {
				enumValue = value;
			}
		}
		return enumValue;
	}
}
