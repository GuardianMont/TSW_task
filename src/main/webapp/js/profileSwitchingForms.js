document.addEventListener('DOMContentLoaded', function() {
    // Ensure that showProfile function is defined and works correctly
    showProfile();

    // Adding an event listener to the form with id 'new-address-form'
    var addressForm = document.getElementById('new-address-form');
    if (addressForm) {
        addressForm.addEventListener('submit', submitAddressForm);
    } else {
        console.error('Element with id "new-address-form" not found');
    }
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

function toggleAddressForm() {
    var form = document.getElementById('new-address-form');
    var removeButton = document.getElementById('remove-button');
    var addButton = document.getElementById("add-address");
    if (form.classList.contains('hidden')) {
        form.classList.remove('hidden');
        removeButton.classList.remove('hidden');
        addButton.classList.add("hidden");
    } else {
        form.classList.add('hidden');
        removeButton.classList.add('hidden');
        addButton.classList.remove("hidden");
    }
}

function submitAddressForm(event) {
    event.preventDefault();
    if (validateFormAd()) {
        var formData = new FormData(document.getElementById('addressForm'));
        console.log("Sending data:");
        formData.forEach((value, key) => {
            console.log(key + ": " + value);
        });
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'AddressManagement');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function() {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    console.log("Operazione completata con successo");
                    showInfoNotifica("indirizzo inserito correttamente");
                    toggleAddressForm();
                    loadAddresses();
                } else {
                    showAttentionNotifica('Errore: ' + response.error);
                }
            } else {
                console.error('Request failed. Status:', xhr.status);
            }
        };
        xhr.onerror = function() {
            console.error('Request failed. Network error.');
        };
        var params = new URLSearchParams(formData);
        params.append("opzione", "add");
        xhr.send(params.toString());
    }
}

function togglePayMethodForm() {
    var form = document.getElementById('new-payMethod-form');
    var removeButton = document.getElementById('remove-pay-form');
    var addButton = document.getElementById("add-pay-button");
    if (form.classList.contains('hidden')) {
        form.classList.remove('hidden');
        removeButton.classList.remove('hidden');
        addButton.classList.add("hidden");
    } else {
        form.classList.add('hidden');
        removeButton.classList.add('hidden');
        addButton.classList.remove("hidden");
    }
}

function submitPayMethodForm(event) {
    event.preventDefault();
    if (validateFormPM()) {
        var formData = new FormData(document.getElementById('payMethodsForm'));
        console.log("Sending data:");
        formData.forEach((value, key) => {
            console.log(key + ": " + value);
        });
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'payMethodsManager');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function() {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    console.log("Operazione completata con successo");
                    showInfoNotifica("metodo di pagamento inserito correttamente");
                    toggleAddressForm();
                    loadAddresses();
                } else {
                    showAttentionNotifica('Errore: ' + response.error);
                }
            } else {
                console.error('Request failed. Status:', xhr.status);
            }
        };
        xhr.onerror = function() {
            console.error('Request failed. Network error.');
        };
        var params = new URLSearchParams(formData);
        params.append("opzione", "add");
        xhr.send(params.toString());
    }
}