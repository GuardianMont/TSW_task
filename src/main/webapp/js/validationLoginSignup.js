document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');
    const showSignupLink = document.getElementById('show-signup-link');
    const showLoginLink = document.getElementById('show-login-link');

    showSignupLink.addEventListener('click', function(event) {
        event.preventDefault();
        loginForm.style.display = 'none';
        signupForm.style.display = 'block';
    });

    showLoginLink.addEventListener('click', function(event) {
        event.preventDefault();
        signupForm.style.display = 'none';
        loginForm.style.display = 'block';
    });

    loginForm.addEventListener('submit', function(event) {
        const email = document.getElementById('login-token').value;
        const password = document.getElementById('login-password').value;
        const emailError = document.getElementById('login-email-error');
        emailError.style.display = 'none';
        let isValid = true;

        if (email === "" || password === "") {
            emailError.style.display = 'block';
            emailError.textContent = 'Compila tutti i campi.';
            isValid = false;
        }else if (email.includes("@")) {
            if ( !validateEmail(email)) {
                emailError.style.display = 'block';
                emailError.textContent = 'Inserisci un indirizzo email valido.';
                isValid = false;
            }
        }else if (email.includes(" ")){
            emailError.style.display = 'block';
            emailError.textContent = 'Inserisci un indirizzo email valido.';
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault();
        }
    });

    document.getElementById('signup-email').addEventListener('blur', function() {
        checkEmailAvailability(this.value);
    });
    document.getElementById('signup-username').addEventListener('blur', function() {
        let error_user = document.getElementById("signup-username-error");

        if (this.value.includes(" ")) {
            error_user.style.display = "block";
            error_user.textContent = "L'username non può contenere spazi";
            return;
        } else {
            error_user.style.display = "none";
        }

        checkUsernameAvailability(this.value);
    });

    document.getElementById("signup-form").addEventListener("submit", function(event) {
        const email = document.getElementById('signup-email');
        const password = document.getElementById('signup-password');
        const repPassword = document.getElementById('signup-rep-password');
        const emailError = document.getElementById('signup-email-error');
        const passwordError = document.getElementById('signup-password-error');

        email.classList.remove('error');
        password.classList.remove('error');
        repPassword.classList.remove('error');
        emailError.style.display = 'none';
        passwordError.style.display = 'none';

        let isValid = true;

        if (!validateEmail(email.value)) {
            email.classList.add('error');
            emailError.style.display = 'block';
            emailError.textContent = 'Inserisci un indirizzo email valido.';
            isValid = false;
        }
        if (!validatePassword(password) || !validatePassword(repPassword)){
            password.classList.add('error');
            repPassword.classList.add('error');
            passwordError.style.display = 'block';
            passwordError.textContent = 'rispettare il pattern password';
            isValid = false;
        }
        if (password.value !== repPassword.value) {
            password.classList.add('error');
            repPassword.classList.add('error');
            passwordError.style.display = 'block';
            passwordError.textContent = 'Le password non coincidono.';
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault();
        }
    });

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    function validatePassword(pass){
        const pat =/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
        return pass.test(pass);
    }

    function checkEmailAvailability(email) {
        //verifica se esiste un utente con la stessa mail
        //in caso positivo non consente la registrazione
        const xhr = new XMLHttpRequest();
        const params = `email=${encodeURIComponent(email)}&opzione=checkEmail`;

        xhr.open('POST', 'checkAvailable', true);
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                // ready=state=$ significa che l'operazione è stata completata
                //status invece è un valore intero che indica l'esito della rihiesta: 200 indica il successo
                const response = JSON.parse(xhr.responseText);
                // responseText contiene la risposta solo nel caso di interazione ultimata
                const emailError = document.getElementById('signup-email-error');

                if (!response.isAvailable) {
                    emailError.style.display = 'block';
                    emailError.textContent = 'Email già in uso.';
                } else {
                    emailError.style.display = 'none';
                }
            }
        };

        xhr.send(params);
    }


    function checkUsernameAvailability(username) {
        //verifica che non vi siano due utenti con il
        //medesimo username
        const xhr = new XMLHttpRequest();
        const params = `username=${encodeURIComponent(username)}&opzione=checkUser`;

        xhr.open('POST', 'checkAvailable', true);
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                const usernameError = document.getElementById('signup-username-error');

                if (!response.isAvailable) {
                    usernameError.style.display = 'block';
                    usernameError.textContent = 'Nome utente già in uso.';
                } else {
                    usernameError.style.display = 'none';
                }
            }
        };

        xhr.send(params);
    }

});

function clearErrors() {
    var errors = document.querySelectorAll(".error");
    errors.forEach(function(element) {
        element.classList.remove("error");
    });

    var errorMessages = document.querySelectorAll(".error-message");
    errorMessages.forEach(function(element) {
        element.style.display = "none";
    });
}