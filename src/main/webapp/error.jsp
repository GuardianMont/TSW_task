<!-- error.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Errore</title>
</head>
<body>
<h1>Errore</h1>
<p><%= request.getAttribute("errorMessage") %></p>
</body>
</html>