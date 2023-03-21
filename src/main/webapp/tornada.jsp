<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sortida_Web</title>
</head>

<body>

	<%
	String miss = request.getAttribute("miss").toString();
	%>
	<h3>
		Missatge de tornada:
		<%=miss%></h3>
	<p>
	<p>
</body>

</html>