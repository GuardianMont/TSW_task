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

    <!-- TODO: modificare style e layout-->
    <title>Profilo</title>
    <link rel="stylesheet" type="text/css" href="css/Profile.css">
</head>
<body>
<jsp:include page="Header.jsp"/>
<script src="js/profileSwitchingForms.js"></script>
    <%
        UserBean user = (UserBean) request.getAttribute("user");
        String sessionUser = (String) request.getSession().getAttribute("userId");

        if (user != null && sessionUser != null && sessionUser.equals(user.getUsername())){
    %>
<div class="container main-container" id = "main-container">
    <div class="container profile-container" id="profile-container">
        <h3>Profilo <%= user.getUsername() %></h3>
        <p>Nome: <%= user.getNome() %></p>
        <p>Cognome: <%= user.getCognome() %></p>
        <p>Email: <%= user.getEmail() %></p>
        <p>Telefono: <%= user.getPhoneNumber() %></p>
    </div>

    <div class="container edit-container" id="edit-container">
        <p id="edit-error" class="edit-error"></p>
        <h3>Modifica</h3>
        <form id="edit-profile-form" action="updateUser" method="post" id="edit-form">
            <div class="edit-form-hidden">
                <input type="hidden" name="option" value="update">
                <input type="hidden" name="username"  value="<%= user.getUsername() %>">
            </div>
            <div class="edit-form-group">
                <label for="edit-nome">Nome:</label><br>
                <input id="edit-nome" name="nome" type="text" maxlength="20" required value=<%= user.getNome()%>><br>
                <span id="edit-nome-error" class="error-message">Non ci possono essere numeri o simboli.</span>
            </div>
            <div class="edit-form-group">
                <label for="edit-cognome">Cognome:</label><br>
                <input id="edit-cognome" name="cognome" type="text" maxlength="20" required value=<%= user.getCognome()%>><br>
                <span id="edit-cognome-error" class="error-message">Non ci possono essere numeri o simboli.</span>
            </div>
            <div class="edit-form-group">
                <label for="edit-email">Email:</label><br>
                <input id="edit-email" name="email" type="email" required value=<%= user.getEmail()%>><br>
                <span id="edit-email-error" class="error-message">Email non valida.</span>
            </div>
            <div class="edit-form-group">
                <label for="edit-phonenumber">Telefono:</label><br>
                <input id="edit-phonenumber" name="phoneNumber" type="tel" required value=<%= user.getPhoneNumber()%>><br>
                <span id="edit-phonenumber-error" class="error-message">Numero di telefono non valido.</span>
            </div>
            <div class="edit-form-group">
                <input type="submit" id="edit-submit" value="Salva modifiche"><input type="reset" value="Reset">
            </div>
        </form>
    </div>

    <div class="container change-password-container" id="change-password-container">
        <p id="change-password-error" class="change-password-error"></p>
        <h3>Cambia Password</h3>
        <form id="change-password-form" action="updateUser" method="post">
            <div class="change-password-form-hidden">
                <input type="hidden" name="option" value="changePassword">
                <input type="hidden" name="username" required value="<%= user.getUsername() %>">
            </div>
            <div class="change-password-form-group">
                <label for="change-password-new">Inserisci Password:</label><br>
                <input id="change-password-new" name="newPassword" type="text" maxlength="20" required placeholder="Nuova Password"><br>
                <span id="change-password-new-error" class="error-message">Sono richiesti almeno 8 caratteri, almeno una lettera maiuscola e un numero.</span>
            </div>
            <div class="change-password-form-group">
                <label for="change-password-confirm">Ripeti Password:</label><br>
                <input id="change-password-confirm" name="confirmPassword" type="text" maxlength="20" required placeholder="Conferma Password"><br>
                <span id="change-password-confirm-error" class="error-message">La password deve essere la stessa.</span>
            </div>
            <div class="change-password-form-submit">
                <input type="submit" value="Cambia Password"><input type="reset" value="Reset">
            </div>
        </form>
    </div>

    <div class = "container button-container">
        <button class="edit-button" onclick="showProfile()">Profilo</button>
        <button class="edit-button" onclick="editProfile()">Modifica</button>
        <button class="edit-button" onclick="changePassword()">Cambia Password</button>
    </div>
</div>
<%
}else{
%>
<div class="container" id="not-logged-message">
    <h3>Devi essere loggato per visualizzare il profilo, coglione!</h3>
    <p>
        Ma come cazzo hai fatto ad arrivare in sta pagina poi...<br>
        Ti ci sei messo proprio d'impegno, eh?
    </p>
</div>
<%
    }
%>
<script src="js/validationEditProfile.js"></script>
<jsp:include page="Footer.jsp"/>
</body>
</html>
