
document.addEventListener('DOMContentLoaded', function() {

    //prendo i campi del form di modifica profilo
    const editProfileForm = document.getElementById('edit-profile-form');
    const editProfileName = document.getElementById('edit-nome');
    const editProfileSurname = document.getElementById('edit-cognome');
    const editProfileEmail = document.getElementById('edit-email');
    const editProfilePhone = document.getElementById('edit-phonenumber');

    //prendo i campi del form di cambio password
    const changePasswordForm = document.getElementById('change-password-form');
    const changePasswordNew = document.getElementById('change-password-new');
    const changePasswordConfirm = document.getElementById('change-password-confirm');

    //prendo i campi di errore
    const editProfileError = document.getElementById("edit-error");
    const changePasswordError = document.getElementById("change-password-error");

    //validazione modifica profilo
    editProfileForm.addEventListener('submit', function(event) {
        let isValid = true;
        editProfileError.style.display = 'none';
        if (editProfileName.value === "" || editProfileSurname.value === "" || editProfileEmail.value === "" || editProfilePhone.value === "") {
            //caso se uno dei campi è vuoto
            editProfileError.style.display = 'block';
            editProfileError.textContent = 'Compila tutti i campi.';
            isValid = false;
        }else if(!validateEmail(editProfileEmail.value)){
            //caso in cui l'email non sia valida
            editProfileError.style.display = 'block';
            editProfileError.textContent = 'Inserisci un indirizzo email valido.';
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
        }else if(changePasswordNew.value !== changePasswordConfirm.value){
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

function validateEmail(email) {
    return /^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(email);
}