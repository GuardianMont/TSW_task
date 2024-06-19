document.addEventListener('DOMContentLoaded', function() {
    showProfile();
});

function showProfile(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("profile-container").style.display = "block"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("profilo").style.display="none"
    document.getElementById("modifica").style.display="inline-block"
    document.getElementById("cambiaPassword").style.display="inline-block"
    document.getElementById("viewOrdini").style.display="inline-block"
}

function editProfile(){
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("edit-container").style.display = "block"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("profilo").style.display="inline-block"
    document.getElementById("modifica").style.display="none"
    document.getElementById("cambiaPassword").style.display="inline-block"
    document.getElementById("viewOrdini").style.display="inline-block"
}

function changePassword(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("change-password-container").style.display = "block"
    document.getElementById("profilo").style.display="inline-block"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("modifica").style.display="inline-block"
    document.getElementById("cambiaPassword").style.display="none"
    document.getElementById("viewOrdini").style.display="inline-block"
}
