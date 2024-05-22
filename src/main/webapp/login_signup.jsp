
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (request.getSession().getAttribute("userID")!=null){
        RequestDispatcher dispatcher = request.getRequestDispatcher("/ProductView.jsp");
        dispatcher.forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Login_Signup</title>
    <link rel = "stylesheet" href="Login_Signup.css">
</head>
<body>

<jsp:include page="Header.jsp"/>

<div class = "outside-container" style="">
    <div class = "container">
        <% String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <p style="color: red;"><%= errorMessage %></p>
        <% } %>
        <form id = "login-form" action="LoginSignup" method="post">
            <input type="hidden" id="login-option" name="option" value="login">
            <div class = "form-group">
                <label for = "login-token"> Email</label>
                <input type="text" id = "login-token" name = "login-token" required placeholder="rossi@gmail.com">
            </div>

            <div class = "form-group">
                <label for = "login-password"> Password</label>
                <input type="password" id="login-password" name="login-password" required placeholder="Abc1234@">
            </div>

            <div class="form-group">
                <button type="submit">Login</button>
            </div>

            <div class="form-group">
                <a href="#" id="show-signup-link">Create an account</a>
            </div>
        </form>

        <form id="signup-form" style="display: none" action="LoginSignup" method="post" >
            <input type="hidden" id="signup-option" name="option" value="signup">
            <div class="form-group">
                <label for = "signup-username"> Username:</label>
                <input type="text" id="signup-username" name="signup-username" required placeholder="Mario.zurolo">
            </div>

            <div class="form-group">
                <label for = "signup-name"> Name:</label>
                <input type="text" id="signup-name" name="signup-name" required placeholder="Mario">
            </div>

            <div class="form-group">
                <label for = "signup-surname"> Surname:</label>
                <input type="text" id="signup-surname" name="signup-surname" required placeholder="Zurolo">
            </div>

            <div class="form-group">
                <label for = "signup-phone"> Numero Telefono</label>
                <input type="tel" id="signup-phone" name="signup-phone" pattern="[0-9]{3}[0-9]{3}[0-9]{4}" placeholder="Formato: 1234567890" required>
            </div>

            <div class="form-group">
                <label for = "signup-email"> Email:</label>
                <input type="text" id="signup-email" name="signup-email" required placeholder="rossi@gmail.com">
            </div>

            <div class="form-group">
                <label for = "signup-password"> Password:</label>
                <input type="text" id="signup-password" name="signup-password" required placeholder="Abc123@">
            </div>
            <div class="form-group">
                <label for = "signup-rep-password"> Repeat Password:</label>
                <input type="text" id="signup-rep-password" name="signup-rep-password" required placeholder="Abc123@">
            </div>

            <div class="form-group">
                <button type="submit"> Signup</button>
            </div>

            <div class="form-group">
                <a href="#" id="show-login-link">Already have an account?</a>
            </div>

        </form>

    </div>
</div>

<jsp:include page="Footer.jsp"/>

<script>
    document.getElementById("show-signup-link").addEventListener("click", function(event) {
        event.preventDefault(); // Evita il comportamento predefinito del link
        document.getElementById("login-form").style.display = "none";
        document.getElementById("signup-form").style.display = "block";
    });

    document.getElementById("show-login-link").addEventListener("click", function(event) {
        event.preventDefault(); // Evita il comportamento predefinito del link
        document.getElementById("signup-form").style.display = "none";
        document.getElementById("login-form").style.display = "block";
    });
</script>


</body>
</html>