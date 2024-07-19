
function loadPaymentMethods() {
    console.log("Loading payment methods...");
    viewPaymentMethods(); // Funzione viewPaymentMethods presumibilmente visualizza qualcosa nell'interfaccia utente
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'payMethodsManager');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        console.log("Response received:", xhr.responseText);
        if (xhr.status === 200) {
            try {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    var payMethods = response.data;
                    console.log("Payment methods data:", payMethods);
                    const container = document.getElementById("shipping-payment");
                    if (container) {
                        container.innerHTML = '';
                        if (payMethods.length === 0) {
                            container.textContent = 'Nessun metodo di pagamento disponibile';
                        } else {
                            payMethods.forEach(function(payMethod) {
                                var div = document.createElement('div');
                                var innerDiv = document.createElement('div');
                                innerDiv.textContent = 'Numero carta: ' + payMethod.numCarta +
                                    '\nData scadenza: ' + payMethod.dataScadenza +
                                    '\nTitolare Carta: ' + payMethod.titolareCarta;
                                div.appendChild(innerDiv);

                                var deleteButton = document.createElement('button');
                                deleteButton.textContent = 'Elimina';
                                deleteButton.onclick = function() {
                                    confirmDeletePaymentMethod(payMethod.numId);
                                };
                                div.appendChild(deleteButton);

                                container.appendChild(div);
                            });
                        }
                    } else {
                        console.error("Container element not found");
                    }
                } else {
                    console.error('Error loading payment methods:', response.error);
                }
            } catch (e) {
                console.error('Error parsing JSON response:', e);
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


function loadAddresses() {
    viewAddresses(); // Funzione viewAddresses presumibilmente visualizza qualcosa nell'interfaccia utente
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'AddressManagement');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
                var addresses = response.data;
                const container = document.getElementById("shipping-address");
                container.innerHTML = '';
                if (addresses.length === 0) {
                    container.textContent = 'Nessun indirizzo disponibile';
                } else {
                    addresses.forEach(function(address) {
                        var div = document.createElement('div');
                        var innerDiv = document.createElement('div');
                        innerDiv.textContent = 'Indirizzo: ' + address.via + ', num: ' +
                            address.numCiv + '\nCittà: ' + address.citta + ', Provincia: ' + address.provincia + ', CAP: ' + address.cap +
                            '\nPreferenze: ' + address.preferenze;
                        div.appendChild(innerDiv);

                        var deleteButton = document.createElement('button');
                        deleteButton.textContent = 'Elimina';
                        deleteButton.onclick = function() {
                            confirmDeleteAddress(address.num_ID);
                        };
                        div.appendChild(deleteButton);

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
    var data = 'opzione=show';
    xhr.send(data);
}


function deletePaymentMethod(numId) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'payMethodsManager');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
                // Richiama la funzione per ricaricare i metodi di pagamento aggiornati dopo la cancellazione
                loadPaymentMethods();
                showInfoNotifica('Metodo di pagamento eliminato con successo.');
            } else {
                console.error('Errore durante l\'eliminazione del metodo di pagamento:', response.error);
                showAttentionNotifica('Errore durante l\'eliminazione del metodo di pagamento.');
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
            showAttentionNotifica('Errore durante l\'eliminazione del metodo di pagamento. Codice errore: ' + xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error('Request failed. Network error.');
        showAttentionNotifica('Errore di rete durante l\'eliminazione del metodo di pagamento.');
    };
    var data = 'opzione=delete&numId=' + encodeURIComponent(numId);
    xhr.send(data);
}

function deleteAddress(numId) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'AddressManagement');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
                // Richiama la funzione per ricaricare i metodi di pagamento aggiornati dopo la cancellazione
                loadAddresses();
                showInfoNotifica('Indirizzo eliminato con successo.');
            } else {
                console.error('Errore durante l\'eliminazione dell\'indirizzo:', response.error);
                showAttentionNotifica('Errore durante l\'eliminazione dell\'indirizzo.');
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
            showAttentionNotifica('Errore durante l\'eliminazione dell\'indirizzo. Codice errore: ' + xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error('Request failed. Network error.');
        showAttentionNotifica('Errore di rete durante l\'eliminazione dell\'indirizzo.');
    };
    var data = 'opzione=delete&numId=' + encodeURIComponent(numId);
    xhr.send(data);
}

function confirmDeletePaymentMethod(paymentMethodId) {
    Swal.fire({
        title: 'Sei sicuro?',
        text: "Non potrai annullare questa azione!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sì, elimina!',
        cancelButtonText: 'Annulla'
    }).then((result) => {
        if (result.isConfirmed) {
            deletePaymentMethod(paymentMethodId);
        }
    });
}

function confirmDeleteAddress(addressId) {
    Swal.fire({
        title: 'Sei sicuro?',
        text: "Non potrai annullare questa azione!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sì, elimina!',
        cancelButtonText: 'Annulla'
    }).then((result) => {
        if (result.isConfirmed) {
            deleteAddress(addressId);
        }
    });
}