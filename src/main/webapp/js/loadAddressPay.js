
document.addEventListener('DOMContentLoaded', function() {
    loadAddresses();
    loadPaymentMethods();
    document.getElementById('addressForm').addEventListener('submit', submitAddressForm);
    document.getElementById('payMethodsForm').addEventListener('submit', submitPaymentForm);
    document.getElementById('check-out-form').addEventListener('submit', function(event) {
        event.preventDefault();
        document.querySelector('.proceed-button').disabled = true; // disabilito il bottone
        //in modo che non si possa premere più volte durante il caricamento dello stesso check out generando problemi
        checkoutForm(event);
    });

    var cartDrawerToggles = document.querySelectorAll('.cart-drawer-toggle');

    cartDrawerToggles.forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            var cartDrawer = document.querySelector('.cart-drawer');
            cartDrawer.classList.toggle('open');
        });
    });

});
   function checkoutForm(event){
        let addressSelected = document.querySelector("input[name='selectedAddress']:checked");
        let paymentSelected = document.querySelector("input[name='selectedPayMethod']:checked");

        if (!addressSelected && !paymentSelected ){
            showAttentionNotifica("Per proseguire selezionare un indirizzo e un metodo di pagamento") ;
            document.querySelector('.proceed-button').disabled = false;
            return  false;
        }

        if (!addressSelected) {
            showAttentionNotifica("Per proseguire selezionare un indirizzo") ;
            document.querySelector('.proceed-button').disabled = false;
            return  false;
        }

        if (!paymentSelected) {
            showAttentionNotifica("Per proseguire selezionare un metodo di pagamento") ;
            document.querySelector('.proceed-button').disabled = false;
            return   false;
        }
       if (addressSelected && paymentSelected) {
           let checkData;
           checkData ={
               address: addressSelected.value,
               paymentMethod: paymentSelected.value
           };

           submitCheckOut(checkData)

       }

    }
function submitCheckOut(checkData) {
    console.log("checkData: ", checkData);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", 'CheckoutServlet');
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
                console.log("Operazione completata con successo");
                showConfirmationMessage(response);
            } else {
                alert('Errore: ' + response.error);
                document.querySelector('.proceed-button').disabled = false;
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
            document.querySelector('.proceed-button').disabled = false;
        }
    };

    xhr.onerror = function() {
        console.error('Request failed. Network error.');
        document.querySelector('.proceed-button').disabled = false;
    };

    var params = new URLSearchParams();
    params.append("address", checkData.address);
    params.append("paymentMethod", checkData.paymentMethod);
    params.append("opzione", "add");
    console.log("Params:", params.toString());
    xhr.send(params.toString());
}

function showConfirmationMessage(data) {
    let confirmationMessage = `
        <div class="confirmation-message">
            <h2>Riepilogo Acquisto</h2>
            <h3>Oggetti acquistati:</h3>
            <ul>
                ${data.cartItems.map(item => `
                    <li>${item.nome} (Quantità: ${item.quantity} Prezzo: ${item.prezzoUnitario} &euro; Sconto: ${item.sconto}%)</li>
                `).join('')}
            </ul>
            <h3>Totale Spesa:  ${data.prezzototale}  &euro;</h3>
            <h4>Metodo di pagamento: </h4>
            <p>
                Numero carta: ${data.paymentMethod.numCarta}<br> 
                Data Scadenza: ${data.paymentMethod.dataScadenza}<br>
                Titolare carta: ${data.paymentMethod.titolareCarta}<br>
                Circuito Pagamento: ${data.paymentMethod.circuito}
            </p>
            <h4>Indirizzo di spedizione: </h4>
            <p>
                    ${data.address.via}, numero civico: ${data.address.numCiv}<br>
                    ${data.address.citta}(${data.address.provincia}),${data.address.cap}<br>
                    Preferenze specificate: ${data.address.Preferenze}
            </p>
            <button onclick="closeConfirmationMessage()">Chiudi</button>
        </div>
    `;
    document.body.insertAdjacentHTML('beforeend', confirmationMessage);
}

function closeConfirmationMessage() {
    document.querySelector('.confirmation-message').remove();
    document.querySelector('.proceed-button').disabled = false;
    //ri do la possibilità di premere il bottone
    window.location.href = 'carrello';
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
                showInfoNotifica("indirizzo inserito correttamente");
                removeForm();
                loadAddresses();
                resetAddressForm();
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
        xhr.onload = function () {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    console.log("Operazione completata con successo");
                    showInfoNotifica("indirizzo inserito correttamente");
                    removePayForm();
                    loadPaymentMethods();
                    resetPaymentForm();
                } else {
                    showAttentionNotifica('Errore: ' + response.error);
                }
            } else {
                console.error('Request failed. Status:', xhr.status);
            }
        };
        xhr.onerror = function () {
            console.error('Request failed. Network error.');
        };
        var params = new URLSearchParams(formData);
        params.append("opzione", "add");
        xhr.send(params.toString());
    }
}

function loadAddresses() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'AddressManagement');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            const container = document.getElementById("shipping-addresses");
            const errorElement = document.getElementById("no-addresses-error");
            if (response.success) {
                var addresses = response.data;
                container.innerHTML = '';
                if (addresses.length === 0) {
                    errorElement.style.display = 'block';
                } else {
                    errorElement.style.display = 'none';
                    addresses.forEach(function(address) {
                        let div = document.createElement('div');
                        div.innerHTML = '<div></div><input type="radio" name="selectedAddress" value="' + address.num_ID + '"> Indirizzo: ' + address.via + ' , num: ' +
                            address.numCiv + '<br> citta: ' + address.citta + ', provincia: ' + address.provincia + ', cap:' + address.cap +
                            '<br>preferenze:' + address.preferenze + '</div>';
                        container.appendChild(div);
                    });
                }
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
    var params = new URLSearchParams();
    params.append("opzione", "show");
    xhr.send(params.toString());
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
    var params = new URLSearchParams();
    params.append("opzione", "show");
    xhr.send(params.toString());

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
