<%@ page language="java" contentType="text/html; charset=windows-1255"
	pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1255">
<title>${statusPageTitle}</title>
</head>
<body>
	<h1>${statusPageHeader}</h1>
	<table border="2" cellpadding="3" cellspacing="1">
		<thead>
			<tr>
				<th>SA Version</th>
				<th>Run Type</th>
				<th>Pass percentage</th>
				<th>Passed tests out of run tests</th>
				<th>Progress percentage</th>
				<th>Total tests in run</th>
				<th>Run status</th>
				<th>Details(url)</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>2.1.1.0-25</td>
				<td>UMTS IPv4</td>
				<td>96.82%</td>
				<td>457 out of 472</td>
				<td>35.54%</td>
				<td>1328</td>
				<td>Running</td>
				<td>http://</td>
			</tr>
			<tr>
				<td>2.1.1.0-25</td>
				<td>UMTS IPv4</td>
				<td>90.82%</td>
				<td>457 out of 492</td>
				<td>36.54%</td>
				<td>1321</td>
				<td>Stopped</td>
				<td>http://1234</td>
			</tr>
		</tbody>
	</table>
</body>
</html>