package com.regressionstatus.data.current.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlBuilder {

	private static final String URL_PREFIX = "http://";
	
	/**
	 * Composes url from a given ip
	 * @param stationIp
	 * @return url in string format, or null if failed to compose
	 * @throws Exception
	 */
	public static String getUrl(String stationIp) throws Exception {
		URL url = null;
		try {
			url = new URL(URL_PREFIX + stationIp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;	// if values above are null, no sense to do further calculations, returning null
		}
		return url.toString();
	}
}
