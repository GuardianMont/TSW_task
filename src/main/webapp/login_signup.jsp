
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
    <link rel = "stylesheet" href="css/Login_Signup.css">
    <script src="js/validationLoginSignup.js"></script>
    <style>
        .error {
            color: red;
            border-color: red;
        }
        .error-message {
            color: red;
            display: none;
        }
    </style>
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
                <input type="text" id = "login-token" name = "login-token" required>
            </div>

            <div class = "form-group">
                <label for = "login-password"> Password</label>
                <input type="password" id="login-password" name="login-password" required>
            </div>

            <div class="form-group">
                <button type="submit">Login</button>
            </div>

            <div class="form-group">
                <a href="#" id="show-signup-link">Create an account</a>
            </div>
        </form>

        <form id="signup-form" style="display:none" action="LoginSignup" method="post" >
            <input type="hidden" id="signup-option" name="option" value="signup">
            <div class="form-group">
                <label for = "signup-username"> Username:</label>
                <input type="text" id="signup-username" name="signup-username" required>
            </div>

            <div class="form-group">
                <label for = "signup-name"> Name:</label>
                <input type="text" id="signup-name" name="signup-name" required>
            </div>

            <div class="form-group">
                <label for = "signup-surname"> Surname:</label>
                <input type="text" id="signup-surname" name="signup-surname" required>
            </div>

            <div class="form-group">
                <label for = "signup-phone"> Numero Telefono</label>
                <input type="tel" id="signup-phone" name="signup-phone" pattern="[0-9]{3}[0-9]{3}[0-9]{4}" placeholder="Formato: 1234567890" required>
            </div>

            <div class="form-group">
                <label for = "signup-email"> Email:</label>
                <input type="text" id="signup-email" name="signup-email" required>
                <span id="signup-email-error" class="error-message">Inserisci un indirizzo email valido.</span>
            </div>

            <div class="form-group">
                <label for = "signup-password"> Password:</label>
                <input type="text" id="signup-password" name="signup-password" required>
            </div>
            <div class="form-group">
                <label for = "signup-rep-password"> Repeat Password:</label>
                <input type="text" id="signup-rep-password" name="signup-rep-password" required>
                <span id="signup-password-error" class="error-message">Le password non coincidono.</span>
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