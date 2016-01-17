<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.regressionstatus.data.current.CurrentStatusTableField"%>
<%@page language="java" contentType="text/html; charset=windows-1255" pageEncoding="windows-1255"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1255">
<title>${statusPageTitle}</title>
</head>
<body>
	<%
	String regressionCurrentStatusWebpageAutoRefreshInterval = (String)request.getAttribute("regressionCurrentStatusWebpageAutoRefreshInterval");
	response.addHeader("Refresh",regressionCurrentStatusWebpageAutoRefreshInterval);
	%>
	Page auto refreshed every <%= (String)request.getAttribute("regressionCurrentStatusWebpageAutoRefreshInterval") %> seconds.</br> 
	Last refreshed at <%= new java.util.Date()%>.</br><br></br><br></br>
	<h1>${statusPageHeader}</h1>
	<table border="2" cellpadding="3" cellspacing="1">
		<thead>
			<tr>
				<c:forEach items="${dataMap}" var="mapEntry">
					<th>${mapEntry.key.toString()}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<%
				Map<CurrentStatusTableField, List<String>> resp = (Map<CurrentStatusTableField, List<String>>) request.getAttribute("dataMap");
				int size = resp.get(CurrentStatusTableField.SA_VERSION).size();
				String fieldToShow = null;
				for (int i = 0; i < size; i++) {
					out.println("<tr>");
					for (CurrentStatusTableField status : resp.keySet()) {
						fieldToShow = resp.get(status).get(i);
						if(status.equals(CurrentStatusTableField.URL)) {
							fieldToShow = "<td><a href='" + resp.get(status).get(i) + "'>" + resp.get(status).get(i) + "</a></td>";
						}
						else {
							fieldToShow = "<td>" + resp.get(status).get(i) + "</td>"; 
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