<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    boolean shouldForward = false;

    if (request.getSession().getAttribute("userID") != null) {
        shouldForward = true;
    }

    if (request.getSession().getAttribute("signupSuccess") != null) {
        shouldForward = true;
    }

    if (shouldForward) {
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
        <!--form per il login-->
        <form id = "login-form" action="Login" method="post">
            <input type="hidden" id="login-option" name="option" value="login">
            <div class = "form-group">
                <label for = "login-token"> Email</label>
                <input type="text" id = "login-token" name = "login-token" required>
                <span id="login-email-error" class="error-message">Inserisci un indirizzo email valido.</span>
            </div>

            <div class = "form-group">
                <label for = "login-password"> Password</label>
                <input type="password" id="login-password" name="login-password" required autocomplete="current-password">
            </div>

            <div class="form-group">
                <button type="submit">Login</button>
            </div>

            <div class="form-group">
                <a href="#" id="show-signup-link">Create an account</a>
            </div>
        </form>

        <!--form per registrarsi -->
        <form id="signup-form" style="display:none" action="Signup" method="post">
            <input type="hidden" id="signup-option" name="option" value="signup">
            <div class="form-group">
                <label for = "signup-username"> Username:</label>
                <input type="text" id="signup-username" name="signup-username" required pattern="^[a-zA-Z0-9]+$" title="username alfanumerico senza spazi" placeholder="user1234">
                <span id="signup-username-error" class="error-message">Username già in uso.</span>
            </div>

            <div class="form-group">
                <label for = "signup-name"> Name:</label>
                <input type="text" id="signup-name" name="signup-name" required
                       title="nome utente" placeholder="Mario" pattern="^[a-zA-Z\s]+$">
            </div>

            <div class="form-group">
                <label for = "signup-surname"> Surname:</label>
                <input type="text" id="signup-surname" name="signup-surname" required
                       title="cognome utente" placeholder="Del Santo" pattern="^[a-zA-Z\s]+$">
            </div>

            <div class="form-group">
                <label for = "signup-phone"> Numero Telefono</label>
                <input type="tel" id="signup-phone" name="signup-phone" pattern="[0-9]{3}[0-9]{3}[0-9]{4}" placeholder="Formato: 1234567890" required title="Formato valido: 1234567890">
            </div>

            <div class="form-group">
                <label for = "signup-email"> Email:</label>
                <input type="text" id="signup-email" name="signup-email" title="Email valida: email@valid.it" placeholder="email@valid.it" required>
                <span id="signup-email-error" class="error-message">Inserisci un indirizzo email valido.</span>
            </div>

            <div class="form-group">
                <label for = "signup-password"> Password:</label>
                <input type="password" id="signup-password" name="signup-password" required maxlength="20"
                       pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"
                       title = "la password deve avere: minimo 8 caretteri, almeno una lettere maiuscola ed una minuscola"
                       placeholder="pAssword1234">
            </div>
            <div class="form-group">
                <label for = "signup-rep-password"> Repeat Password:</label>
                <input type="password" id="signup-rep-password" name="signup-rep-password" maxlength="20"
                       pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$" title="ripeti password" placeholder="pAssword1234" required>
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
        clearErrors() // così quando cambia da un interfaccia ad un altra si tolgono gli errori
        event.preventDefault(); // Evita il comportamento predefinito del link
        document.getElementById("login-form").style.display = "none";
        document.getElementById("signup-form").style.display = "block";
    });

    document.getElementById("show-login-link").addEventListener("click", function(event) {
        clearErrors(); // così quando cambia da un interfaccia ad un altra si tolgono gli errori
        event.preventDefault(); // Evita il comportamento predefinito del link
        document.getElementById("signup-form").style.display = "none";
        document.getElementById("login-form").style.display = "block";
    });
</script>


</body>
</html>