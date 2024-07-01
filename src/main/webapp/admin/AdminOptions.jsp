<%--
  Created by IntelliJ IDEA.
  User: mauri
  Date: 27/06/2024
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>AdminOptions</title>
</head>
<body>
<jsp:include page="../Header.jsp"/>

<%
    if (session.getAttribute("user") == null || session.getAttribute("isAdmin") == null || !(Boolean)session.getAttribute("isAdmin")) {
        response.sendRedirect("Homepage.jsp");
    }
%>
<div>
    <h1>Admin Options</h1>
    <div id="admin-options" class="button-container">
        <button onclick="viewOrders()">View Orders</button>
        <button onclick="viewUsers()">View Users</button>
    </div>
</div>

<jsp:include page="../Footer.jsp"/>
</body>
</html>