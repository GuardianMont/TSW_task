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
        //validation email?
    });

    signupForm.addEventListener('submit', function(event) {
        const email = document.getElementById('signup-email');
        const password = document.getElementById('signup-password');
        const repPassword = document.getElementById('signup-rep-password');
        const emailError = document.getElementById('signup-email-error');
        const passwordError = document.getElementById('signup-password-error');
        const repPasswordError = document.getElementById('signup-rep-password-error');

        email.classList.remove('error');
        password.classList.remove('error');
        repPassword.classList.remove('error');
        emailError.style.display = 'none';
        passwordError.style.display = 'none';
        repPasswordError.style.display = 'none';

        let isValid = true;

        if (!validateEmail(email.value)) {
            email.classList.add('error');
            emailError.style.display = 'block';
            isValid = false;
        }
        if (password.value !== repPassword.value) {
            password.classList.add('error');
            repPassword.classList.add('error');
            passwordError.style.display = 'block';
            repPasswordError.style.display = 'block';
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
});