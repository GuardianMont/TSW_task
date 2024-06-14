
document.addEventListener('DOMContentLoaded', function() {
    loadAddresses();
    loadPaymentMethods();
    document.getElementById('addressForm').addEventListener('onsubmit', submitAddressForm);
    document.getElementById('payMethodsForm').addEventListener('onsubmit', submitPaymentForm);
});
   function checkoutForm(){
        let addressSelected = document.querySelector("input[name='selectedAddress']:checked");
        let paymentSelected = document.querySelector("input[name='selectedPayMethod']:checked");

        if (!addressSelected && !paymentSelected ){
            var notification = document.getElementById("notificationAD");
            var notificationText = document.getElementById('notification-textAD');
            notificationText.textContent = "Per proseguire selezionare un indirizzo e un metodo di pagamento";
            notification.style.display = "flex";
            setTimeout(function() {
                notification.style.display = "none";
            }, 5000);
            return  false;
        }

        if (!addressSelected) {
            var notification = document.getElementById("notificationAD");
            var notificationText = document.getElementById('notification-textAD');
            notificationText.textContent = "Per proseguire selezionare un indirizzo";
            notification.style.display = "flex";
            setTimeout(function() {
                notification.style.display = "none";
            }, 5000);
            return  false;
        }

        if (!paymentSelected) {
            var notification = document.getElementById("notificationPM");
            var notificationText = document.getElementById('notification-textPM');
            notificationText.textContent= "Per proseguire selezionare un metodo di pagamento";
            notification.style.display = "flex";
            setTimeout(function() {
                notification.style.display = "none";
            }, 5000);
            return   false;
        }

        return true;
    }

    // Submit address form via AJAX

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
                document.getElementById('new-address-form').classList.add('hidden');
                loadAddresses();
                resetAddressForm();
            } else {
                alert('Errore: ' + response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error('Request failed. Network error.');
    };
    xhr.send(formData);
    }
}

function submitPaymentForm(event) {
    event.preventDefault();
    if (validateFormPM()) {
        var formData = new FormData(document.getElementById('payMethodsForm'));
        console.log("Sending data:", formData);
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
                    document.getElementById('new-payMethod-form').classList.add('hidden');
                    loadPaymentMethods();
                    resetPaymentForm();
                } else {
                    alert('Errore: ' + response.error);
                }
            } else {
                console.error('Request failed. Status:', xhr.status);
            }
        };
        xhr.onerror = function() {
            console.error('Request failed. Network error.');
        };
        xhr.send(formData);
    }
}


function loadAddresses() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'AddressManagement');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
                var addresses = response.data;
                const container = document.getElementById("shipping-addresses");
                container.innerHTML = '';
                addresses.forEach(function(address) {
                    let div = document.createElement('div');
                    div.innerHTML = '<div></div><input type="radio" name="selectedAddress" value="' + address.num_ID + '"> Indirizzo: ' + address.via + ' , num: ' +
                        address.numCiv + '<br> citta: ' + address.citta + ', provincia: ' + address.provincia + ', cap:' + address.cap +
                        '<br>preferenze:' + address.preferenze + '</div>';
                    container.appendChild(div);
                });
            } else {
                console.error('Error loading addresses:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error('Request failed. Network error.');
    };
    var data = 'opzione=show';
    xhr.send(data);
}


function loadPaymentMethods() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'payMethodsManager');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
                var payMethod = response.data;
                const container = document.getElementById("shipping-payment");
                container.innerHTML = '';
                payMethod.forEach(function(payMethod) {
                    let div = document.createElement('div');
                    div.innerHTML =  '<div><input type="radio" name="selectedPayMethod" value="' + payMethod.numId + '"> Numero carta: ' + payMethod.numCarta +
                        '<br> Data scadenza: ' + payMethod.dataScadenza +
                        'Titolare Carta: ' + payMethod.titolareCarta + '</div>';
                    container.appendChild(div);
                });
            } else {
                console.error('Error loading metodo di pagamento:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error('Request failed. Network error.');
    };
    var data = 'opzione=show';
    xhr.send(data);
}

function aggiungiIndirizzo() {
    document.getElementById("new-address-form").classList.remove("hidden");
    document.getElementById("remove-button").classList.remove("hidden");
    document.getElementById("add-button").classList.add("hidden");
}

function removeForm(){
    document.getElementById("new-address-form").classList.add("hidden");
    document.getElementById("remove-button").classList.add("hidden");
    document.getElementById("add-button").classList.remove("hidden");
}
function aggiungiPayMethods() {
    document.getElementById("new-payMethod-form").classList.remove("hidden");
    document.getElementById("remove-pay-form").classList.remove("hidden");
    document.getElementById("add-pay-button").classList.add("hidden");
}

function removePayForm(){
    document.getElementById("new-payMethod-form").classList.add("hidden");
    document.getElementById("remove-pay-form").classList.add("hidden");
    document.getElementById("add-pay-button").classList.remove("hidden");
}

function resetAddressForm() {
    $('#addressForm')[0].reset();
}

function resetPaymentForm() {
    $('#payMethodsForm')[0].reset();
}

function validateSelections(){
    let addressSelected = $("input[name='selectedAddress']:checked").val();
    let paymentSelected = $("input[name='selectedPayMethod']:checked").val();

    if (!addressSelected) {
        alert("Per proseguire selezionare un indirizzo");
        return false;
    }

    if (!paymentSelected) {
        alert("Per proseguire selezionare un metodo di pagamento");
        return false;
    }

    return true;
}