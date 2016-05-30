<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.IOException"%>
<%@page	import="com.regressionstatus.data.current.CurrentStatusTableField"%>
<%@page	import="com.regressionstatus.data.current.CurrentRegressionStatusDataUpdater"%>
<%@page	import="com.regressionstatus.data.current.util.DistributedRunCalculator"%>
<%@page language="java" contentType="text/html; charset=windows-1255" pageEncoding="windows-1255"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1255">
<title>${statusPageTitle}</title>
<!-- <img src="/regressionstatus/images/favicon.png" /></br> -->
<link rel="shortcut icon" type="image/png"
	href="/regressionstatus/images/favicon.png">
</head>
<body>
	<%
		java.util.Date currentDate = new java.util.Date();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String regressionCurrentStatusWebpageAutoRefreshInterval = (String) request.getAttribute("regressionCurrentStatusWebpageAutoRefreshInterval");
		response.addHeader("Refresh", regressionCurrentStatusWebpageAutoRefreshInterval);
		
		Map<CurrentStatusTableField, List<String>> dataMap = (Map<CurrentStatusTableField, List<String>>) request.getAttribute("dataMap");
		
		double regressionRunPassPercentageRedLevel = 0.0;
		double regressionRunPassPercentageGreenLevel = 0.0;

		try {
			regressionRunPassPercentageRedLevel = Double.parseDouble((String)request.getAttribute("regressionRunPassPercentageRedLevel"));
			regressionRunPassPercentageGreenLevel = Double.parseDouble((String)request.getAttribute("regressionRunPassPercentageGreenLevel"));

		} catch(NumberFormatException e) {
			// don't throw anything
		}
		
		String statusPageHeader = (String) request.getAttribute("statusPageHeader");
		boolean isDisplayInPrintVersion = false;
		Boolean retValue = (Boolean)request.getAttribute("isDisplayInPrintVersion");
		if (retValue != null) {
			isDisplayInPrintVersion = retValue.booleanValue();
		}
		printStatusPageBackgrounds(out, isDisplayInPrintVersion, regressionCurrentStatusWebpageAutoRefreshInterval, statusPageHeader);
	%>
	<table border="2" cellpadding="3" cellspacing="1">
		<thead>
			<%
			 	printStatusTableHeader(out, isDisplayInPrintVersion, dataMap);
			%>
		</thead>
		<tbody>
			<%
				printStatusTableBody(out, isDisplayInPrintVersion, dataMap, regressionRunPassPercentageRedLevel, regressionRunPassPercentageGreenLevel);
			%>
		</tbody>
	</table>
	
	<%!
	private void printStatusPageBackgrounds(JspWriter out, boolean isInPrintVersion, String regressionCurrentStatusWebpageAutoRefreshInterval, String statusPageHeader) throws IOException {

		java.util.Date currentDate = new java.util.Date();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		if (!isInPrintVersion) {
			out.println("Page auto refreshed every " + regressionCurrentStatusWebpageAutoRefreshInterval + " seconds.");
			out.println("</br>");
			out.println("Last refreshed at " + format.format(currentDate));
			out.println("</br>");
			out.println("</br>");
			out.println("</br>");
			out.println("<h1>" + statusPageHeader + "</h1>");
		} else {
			out.println("</br>");
			out.println("</br>");
			out.println("Hi,");
			out.println("</br>");
			out.println("Regression status (" + format.format(currentDate) + ")");
			out.println("</br>");
		}
		out.println("</br>");
	}
	
	private void printStatusTableHeader(JspWriter out, boolean isInPrintVersion, Map<CurrentStatusTableField, List<String>> dataMap) throws IOException {
		
		String headerBackgroundColor = "white";
		
		if (isInPrintVersion) {
			headerBackgroundColor = "grey";
		}
		
		out.println("<tr>");
		for (CurrentStatusTableField field : dataMap.keySet()) {
			switch (field) {
			case URL : 
				if (isInPrintVersion) {
					// Urls aren't displayed in the printed version of status
					break;
				}
			default :
				out.println("<th style='background-color: " + headerBackgroundColor + "'>" + field.toString() + "</th>");	
			}
		}
		out.println("</tr>");
	}
	
	private void printStatusTableBody(JspWriter out, boolean isInPrintVersion, Map<CurrentStatusTableField, List<String>> dataMap, 
			double regressionRunPassPercentageRedLevel, double regressionRunPassPercentageGreenLevel) throws IOException {
		
		int size = dataMap.get(CurrentStatusTableField.SA_VERSION).size();
		String fieldToShow = null;
		String[] splitFields;
		
		String cellBackgroundColor = "white";

		for (int i = 0; i < size; i++) {
			
			out.println("<tr>");
			
			for (CurrentStatusTableField status : dataMap.keySet()) {
				fieldToShow = dataMap.get(status).get(i);
				splitFields = fieldToShow.split(DistributedRunCalculator.BOUND_VALUES_SEPARATOR);
				
				switch (status) {
				
				case URL:
					if (isInPrintVersion) {
						// Urls aren't displayed in the printed version of status
						fieldToShow = "";
						break;
					} 

					if (!fieldToShow.toString().equals(CurrentRegressionStatusDataUpdater.VALUE_NOT_AVAILABLE)) {
						fieldToShow = "<td>";
						for (String url : splitFields) {
							fieldToShow += "<a href='" + url + "' target=\"_blank\">" + url + "</a><br>";
						}
						fieldToShow += "</td>";
						break;
					}
					
				case PASS_PERCENTAGE:
					if (regressionRunPassPercentageRedLevel > 0.0 && regressionRunPassPercentageGreenLevel > 0.0 && !isInPrintVersion) {	// checking levels validity
						fieldToShow = "<td ";
						for (String passPercentageInStringFormat : splitFields) {
							if (passPercentageInStringFormat.equals(CurrentRegressionStatusDataUpdater.VALUE_NOT_AVAILABLE)) {
								fieldToShow += " style='background-color: " + cellBackgroundColor + "'>" + passPercentageInStringFormat;
								continue;
							}
							double passPercentage = 0.0;
							try {
								passPercentage = Double.parseDouble(passPercentageInStringFormat);
							} catch(NumberFormatException e) {
								fieldToShow += " style='background-color: " + cellBackgroundColor + "'>" + passPercentageInStringFormat;
								continue;
							}
							if (passPercentage <= regressionRunPassPercentageRedLevel) {
								cellBackgroundColor = "red";
							} else if (passPercentage >= regressionRunPassPercentageGreenLevel) {
								cellBackgroundColor = "green";
							}
							fieldToShow += " style='background-color: " + cellBackgroundColor + "'>" + passPercentageInStringFormat + "%";
						}
						fieldToShow += "<br></td>";
						break;
					}
					
				default:
					fieldToShow = "<td>";
					for (String field : splitFields) {
						fieldToShow += field + "<br>";
					}
					fieldToShow += "</td>";
				}
				out.println(fieldToShow);
			}
			out.println("</tr>");
		}
	}
	%>
	
</body>
</html>