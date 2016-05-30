<%@ page language="java" contentType="text/html; charset=windows-1255"  pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<title>Help Page</title>
<link rel="shortcut icon" type="image/png"	href="/regressionstatus/images/favicon.png">
<link rel="shortcut icon" type="image/png" href="images/favicon.png">
<!-- <img src="https://ssl.gstatic.com/ui/v1/icons/mail/images/favicon5.ico" /> -->
<!-- <link rel="shortcut icon" href="https://ssl.gstatic.com/ui/v1/icons/mail/images/favicon5.ico" type="image/x-icon"> -->
</head>
<body>
<center><h1>Online regression status help page</h1></center>

<h2> * Regular (read-only) working mode:</h2>

To only see current status with default regression setups open url:</br>
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
</br>

<h2> * Binding calculations of 2 and more setups (distribute run)</h2> 

 - To calculate 2 or more setups use ' (apostrophe)</br> 
For instance, to bind 192.168.20.245 and 192.168.20.107, use the following url to see the their overall status</br>
<a href="http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107" target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107</a><br>
 - To calculate 3 (and even more) setups, just add desired ip followed by ' (apostrophe)</br>
For instance, to bind 192.168.20.245, 192.168.20.107 and 192.168.20.119, use the following url to see the their overall status</br>
<a href="http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107'192.168.20.119" target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107'192.168.20.119</a><br>
 - To chain more than 2 setups for separated calculation, use & (ampersand).
For instance, to bind 192.168.20.245 and 192.168.20.107 with 192.168.20.119 and 192.168.30.45,</br> 
use the following url to see the their overall status</br> 
<a href="http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107&192.168.20.119'192.168.30.45" target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107&192.168.20.119'192.168.30.45</a><br>
</br>

<h2> * Mailing a regression status</h2>
When you're on the regression duty, you need to report a current status twice a weekend.</br> 
As a status shown in a regular link (see above) does not comply to the mailing table format, you might be want to convert it to such one. </br>
Using the 'printstatus=true' command does the work.</br>
 - Print version of regular status:</br>
<a href='http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/printstatus=true' target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/printstatus=true</a><br>
 - Print version combined with another commands:</br>
<a href="http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107;printstatus=true" target=\"_blank\">http://192.168.20.251:8080/regressionstatus/showCurrentStatus/rstatus/bind=192.168.20.245'192.168.20.107;printstatus=true</a><br> 
Now, what remains to do, it's only copy-paste a content of a web page into a mail body and send it out.</br>
</br>

<h2> * Notes</h2>
Don't forget to use ';' to separate commands in an url.</br> 
In case of any input error, the default regression status page will be shown.</br>
Every single ip could be used only once in a bind calculation.</br> 
In case of not valid values in the parameters one of setups (such as NA), bound entry won't be shown in the status page.</br>
</br>
</body>
</html>