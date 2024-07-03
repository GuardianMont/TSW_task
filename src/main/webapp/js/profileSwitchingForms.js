document.addEventListener('DOMContentLoaded', function() {
    showProfile();
});

function showProfile(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("profile-container").style.display = "block"
    document.getElementById("orderDetail").style.display="none"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("indirizziSpedizione").style.display="none"
    document.getElementById("metodiPagamento").style.display="none"
}

function editProfile(){
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("orderDetail").style.display="none"
    document.getElementById("edit-container").style.display = "block"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("indirizziSpedizione").style.display="none"
    document.getElementById("metodiPagamento").style.display="none"

}

function changePassword(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("orderDetail").style.display="none"
    document.getElementById("change-password-container").style.display = "block"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("indirizziSpedizione").style.display="none"
    document.getElementById("metodiPagamento").style.display="none"
}

function viewOrdini(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("orderDetail").style.display="none"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("ordiniEffettuati").style.display="block"
    document.getElementById("indirizziSpedizione").style.display="none"
    document.getElementById("metodiPagamento").style.display="none"
}

function viewPaymentMethods(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("orderDetail").style.display="none"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("indirizziSpedizione").style.display="none"
    document.getElementById("metodiPagamento").style.display="block"
}

function viewAddresses(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("orderDetail").style.display="none"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("indirizziSpedizione").style.display="block"
    document.getElementById("metodiPagamento").style.display="none"
}

function viewDetail(){
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("orderDetail").style.display="block"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("ordiniEffettuati").style.display="none"
    document.getElementById("indirizziSpedizione").style.display="none"
    document.getElementById("metodiPagamento").style.display="none"
}
