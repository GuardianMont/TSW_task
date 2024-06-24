document.addEventListener('DOMContentLoaded', function() {

    //prendo i campi di errore
    const editProfileError = document.getElementById("edit-error");
    const changePasswordError = document.getElementById("change-password-error");

    //prendo i campi del form di modifica profilo
    const editProfileForm = document.getElementById('edit-profile-form');
    const editProfileName = document.getElementById('edit-nome');
    const editProfileNameError = document.getElementById('edit-nome-error');
    const editProfileSurname = document.getElementById('edit-cognome');
    const editProfileSurnameError = document.getElementById('edit-cognome-error');
    const editProfileEmail = document.getElementById('edit-email');
    const editProfileEmailError = document.getElementById('edit-email-error');
    const editProfilePhone = document.getElementById('edit-phonenumber');
    const editProfilePhoneError = document.getElementById('edit-phonenumber-error');

    //prendo i campi del form di cambio password
    const changePasswordForm = document.getElementById('change-password-form');
    const changePasswordNew = document.getElementById('change-password-new');
    const changePasswordNewError = document.getElementById('change-password-new-error');
    const changePasswordConfirm = document.getElementById('change-password-confirm');
    const changePasswordConfirmError = document.getElementById('change-password-confirm-error');

    //validazione nome in scrittura
    editProfileName.addEventListener('change', function() {
        if(validateNameSurname(editProfileName.value)) {
            editProfileNameError.style.display = 'none';
        }else {
            editProfileNameError.style.display = 'block';
        }
    });

    //validazione cognome in scrittura
    editProfileSurname.addEventListener('change', function() {
        if(validateNameSurname(editProfileSurname.value)) {
            editProfileSurnameError.style.display = 'none';
        }else {
            editProfileSurnameError.style.display = 'block';
        }
    });

    //validazione email in scrittura
    editProfileEmail.addEventListener('change', function() {
        if(validateEmail(editProfileEmail.value)) {
            editProfileEmailError.style.display = 'none';
        }else {
            editProfileEmailError.style.display = 'block';
        }
    });

    //validazione telefono in scrittura
    editProfilePhone.addEventListener('change', function() {
        if(validatePhone(editProfilePhone.value)) {
            editProfilePhoneError.style.display = 'none';
        }else {
            editProfilePhoneError.style.display = 'block';
        }
    });

    //validazione new password in scrittura
    changePasswordNew.addEventListener('change', function() {
        if(validatePassword(changePasswordNew.value)) {
            changePasswordNewError.style.display = 'none';
        }else {
            changePasswordNewError.style.display = 'block';
        }
    });
    //validazione conferma password in scrittura
    changePasswordConfirm.addEventListener('change', function() {
       if (changePasswordConfirm.value === '' || (changePasswordNew.value === changePasswordConfirm.value)) {
           changePasswordConfirmError.style.display = 'none';
       }else{
           changePasswordConfirmError.style.display = 'block';
       }
    });

    //validazione modifica profilo
    editProfileForm.addEventListener('submit', function(event) {

        let isValid = true;
        editProfileError.style.display = 'none';

        if (editProfileName.value === "" || editProfileSurname.value === "" || editProfileEmail.value === "" || editProfilePhone.value === "") {
            //caso se uno dei campi è vuoto
            editProfileError.style.display = 'block';
            editProfileError.textContent = 'Compila tutti i campi.';
            isValid = false;
        }else if(!validateNameSurname(editProfileName.value) || !validateNameSurname(editProfileSurname.value)) {
            //caso in cui il nome o il cognome non siano validi
            editProfileError.style.display = 'block';
            editProfileError.textContent = 'Inserisci un nome e un cognome validi.';
            isValid = false;
        }else if(!validateEmail(editProfileEmail.value)){
            //caso in cui l'email non sia valida
            editProfileError.style.display = 'block';
            editProfileError.textContent = 'Inserisci un indirizzo email valido.';
            isValid = false;
        }else if(!validatePhone(editProfilePhone.value)){
            //caso in cui il telefono non sia valido
            editProfileError.style.display = 'block';
            editProfileError.textContent = 'Inserisci un numero di telefono valido.';
            isValid = false;
        }

        //se non è valido, non invio il form
        if (!isValid) {
            event.preventDefault();
        }
    });

    changePasswordForm.addEventListener('submit', function(event) {
        let isValid = true;
        changePasswordError.style.display = 'none';

        if(changePasswordNew.value === "" || changePasswordConfirm.value === "") {
            //caso in cui uno dei campi è vuoto
            changePasswordError.style.display = 'block';
            changePasswordError.textContent = 'Compila tutti i campi.';
            isValid = false;
        }else if (!validatePassword(changePasswordNew.value)) {
            //caso in cui la password non è valida
            changePasswordError.style.display = 'block';
            changePasswordError.textContent = 'La password deve essere di almeno 8 caratteri.';
            isValid = false;
        } else if(changePasswordNew.value !== changePasswordConfirm.value){
            //caso in cui le due password non coincidono
            changePasswordError.style.display = 'block';
            changePasswordError.textContent = 'Le password non coincidono.';
            isValid = false;
        }

        //se non è valido, non invio il form
        if (!isValid) {
            event.preventDefault();
        }
    });
});