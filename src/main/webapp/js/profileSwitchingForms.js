

function showProfile(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("profile-container").style.display = "block"
}

function editProfile(){
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("edit-container").style.display = "block"
}

function changePassword(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("change-password-container").style.display = "block"
}