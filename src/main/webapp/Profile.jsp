<%@ page import="ec.model.user.UserBean" %><%--
  Created by IntelliJ IDEA.
  User: mauri
  Date: 13/06/2024
  Time: 16:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <!-- TODO: aggiungere style e modificare layout-->
    <title>Title</title>
</head>
<body>
<jsp:include page="Header.jsp"/>
    <%
        // TODO: aggiungere controllo per vedere se l'utente è loggato (me l'ha consigliato copilot)
        UserBean user = (UserBean) request.getAttribute("user");
        if (user != null) {

    %>
    <h1>Profilo <%= user.getUsername() %></h1>
    <table border="1">
        <tr>
            <th>Nome</th>
            <th>Cognome</th>
            <th>Email</th>
            <th>Telefono</th>
        </tr>
        <tr>
            <td><%= user.getNome() %></td>
            <td><%= user.getCognome() %></td>
            <td><%= user.getEmail() %></td>
            <td><%= user.getPhoneNumber() %></td>
        </tr>
    </table>


    <!-- TODO: form a scomparsa (dovrebbero apparire solo nel caso in cui l'utente voglia modificare i dati)-->
    <h2>Modifica</h2>
    <form action="updateUser" method="post">
        <input type="hidden" name="option" value="update">
        <input type="hidden" name="username"  value="<%= user.getUsername() %>">

        <label for="nome">Nome:</label><br>
        <input id="nome" name="nome" type="text" maxlength="20" required value=<%= user.getNome()%>><br>

        <label for="cognome">Cognome:</label><br>
        <input id="cognome" name="cognome" type="text" maxlength="20" required value=<%= user.getCognome()%>><br>

        <label for="email">Email:</label><br>
        <input id="email" name="email" type="email" required value=<%= user.getEmail()%>><br>

        <label for="phoneNumber">Telefono:</label><br>
        <input id="phoneNumber" name="phoneNumber" type="tel" required value=<%= user.getPhoneNumber()%>><br>

        <input type="submit" value="Salva modifiche"><input type="reset" value="Reset">
    </form>

    <h2>Cambia Password</h2>
    <form action="updateUser" method="post">

        <input type="hidden" name="option" value="changePassword">
        <input type="hidden" name="username" required value="<%= user.getUsername() %>">

        <label for="newPassword">Nome:</label><br>
        <input id="newPassword" name="newPassword" type="text" maxlength="20" required placeholder="Nuova Password"><br>

        <label for="confirmPassword">Nome:</label><br>
        <input id="confirmPassword" name="confirmPassword" type="text" maxlength="20" required placeholder="Conferma Password"><br>

        <input type="submit" value="Cambia Password"><input type="reset" value="Reset">
    </form>



<%
        }
    %>

</body>
</html>
