<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<title>Help Page</title>
<img src="images/favicon.png" />
<link rel="shortcut icon" type="image/png" href="images/favicon.png">
<!-- <img src="https://ssl.gstatic.com/ui/v1/icons/mail/images/favicon5.ico" /> -->
<!-- <link rel="shortcut icon" href="https://ssl.gstatic.com/ui/v1/icons/mail/images/favicon5.ico" type="image/x-icon"> -->
</head>
<body>
<h1>Online regression status help page</h1>

<h2> * Regular (read-only) working mode:</h2>

To only see current status open url:</br>
<a href='http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus' target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus</a><br>
</br>

<h2> * Dynamic configuration option via Url</h2>
You can add a custom ips (like a smoke) and chain them each to other using & (ampersand).</br> 
Chaining is optional you don't have to, if there is only 1 ip, for example:</br>
ip=192.168.30.45&192.168.20.107 - custom and chained ips defined by user</br>
usedefaultips=true - default ips will be used with a custom ones</br>
usedefaultips=false - default ips won't be used, only a custom ips, that defined in url</br>
For instance:</br>
<a href='http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45&192.168.20.107&192.168.20.245;usedefaultips=true' target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45&192.168.20.107&192.168.20.245;usedefaultips=true</a><br>
<a href='http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45;usedefaultips=false' target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45;usedefaultips=false</a><br>
<br>Don't forget to use ';' to separate commands in an url.</br> 
In case of any input error, the default regression status page will be shown.</br>
</br>

<h2> * Binding calculations of 2 and more setups (distribute run)</h2> 

To calculate 2 or more setups use ' (apostrophe)</br> 
For instance, to bind 192.168.20.245 and 192.168.20.107, use the following url to see the their overall status</br>
<a href='http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245\'192.168.20.107' target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107</a><br>
In case of not valid values in the parameters one of setups (such as NA), bound entry won't be shown in the status page.</br>
</br>
</body>
</html>