
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

<div class = "outside-container" style="">
    <div class = "container">

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
                <div class="form-switch">
                    <input type = "checkbox" id="show-signup">
                    <label for="show-signup"> Create an account</label>
                </div>
            </div>
        </form>

        <form id="signup-form" style="display: none" action="LoginSignup" method="post" >
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
                <label for = "signup-email"> Email:</label>
                <input type="text" id="signup-email" name="signup-email" required>
            </div>

            <div class="form-group">
                <label for = "signup-password"> Password:</label>
                <input type="text" id="signup-password" name="signup-password" required>
            </div>
            <div class="form-group">
                <label for = "signup-rep_password"> Repeat Password:</label>
                <input type="text" id="signup-rep_password" name="signup-rep_password" required>
            </div>

            <div class="form-group">
                <button type="submit"> Signup</button>
            </div>

            <div class="form-group">
                <div class="form-switch">
                    <input type="checkbox" id="show-login">
                    <label for="show-login">Already have an account?</label>
                </div>
            </div>

        </form>

    </div>
</div>

<script>
    document.getElementById("show-signup").addEventListener("change", function() {
        document.getElementById("login-form").style.display = this.checked ? "none" : "block";
        document.getElementById("signup-form").style.display = this.checked ? "block" : "none";
    });

    document.getElementById("show-login").addEventListener("change", function() {
        document.getElementById("signup-form").style.display = this.checked ? "none" : "block";
        document.getElementById("login-form").style.display = this.checked ? "block" : "none";
    });
</script>

</body>
</html>