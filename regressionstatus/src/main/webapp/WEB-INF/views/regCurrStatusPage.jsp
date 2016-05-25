<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page
	import="com.regressionstatus.data.current.CurrentStatusTableField"%>
<%@page
	import="com.regressionstatus.data.current.CurrentRegressionStatusDataUpdater"%>
<%@page
	import="com.regressionstatus.data.current.util.DistributedRunCalculator"%>
<%@page language="java" contentType="text/html; charset=windows-1255"
	pageEncoding="windows-1255"%>
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
		String regressionCurrentStatusWebpageAutoRefreshInterval = (String) request
				.getAttribute("regressionCurrentStatusWebpageAutoRefreshInterval");
		response.addHeader("Refresh", regressionCurrentStatusWebpageAutoRefreshInterval);
	%>
	Page auto refreshed every
	<%=(String) request.getAttribute("regressionCurrentStatusWebpageAutoRefreshInterval")%>
	seconds.
	</br> Last refreshed at
	<%=new java.util.Date()%>.
	</br>
	<br></br>
	<br></br>
	<h1>${statusPageHeader}</h1>
	<table border="2" cellpadding="3" cellspacing="1">
		<thead>
			<tr>
				<c:forEach items="${dataMap}" var="mapEntry">
					<th style="background-color:grey">${mapEntry.key.toString()}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<%
				Map<CurrentStatusTableField, List<String>> resp = (Map<CurrentStatusTableField, List<String>>) request.getAttribute("dataMap");
				int size = resp.get(CurrentStatusTableField.SA_VERSION).size();
				String fieldToShow = null;
				String[] splitFields;

				for (int i = 0; i < size; i++) {
					out.println("<tr>");
					for (CurrentStatusTableField status : resp.keySet()) {
						fieldToShow = resp.get(status).get(i);
						switch (status) {
						case URL:
							if (!fieldToShow.toString().equals(CurrentRegressionStatusDataUpdater.VALUE_NOT_AVAILABLE)) {
								splitFields = fieldToShow.split(DistributedRunCalculator.BOUND_VALUES_SEPARATOR);
								fieldToShow = "<td>";
								for (String url : splitFields) {
									fieldToShow += "<a href='" + url + "' target=\"_blank\">" + url + "</a><br>";
								}
								fieldToShow += "</td>";
								break;
							}
						default:
							splitFields = fieldToShow.split(DistributedRunCalculator.BOUND_VALUES_SEPARATOR);
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
			%>
		</tbody>
	</table>
</body>
</html>