<%-- <%@ page isErrorPage="true"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error Page</title>
</head>
<body background="<%=request.getContextPath()%>/resources/images/featured.jpg">

	<br>
	<center>
		<!-- <img src="/resources/images/error-code-18.jpeg" width="320" alt="error"> -->
		<h1>Some error occurred!!!
		
	<br> </h1><img src="<%=request.getContextPath()%>/resources/images/${title}.png"/>
	<h2><br> ${description}</h2>
	</center>
<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>		
</body>
</html>