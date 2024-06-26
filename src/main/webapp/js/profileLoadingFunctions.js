
function loadPaymentMethods() {
    viewPaymentMethods()
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
                    div.innerHTML =  '<div>Numero carta: ' + payMethod.numCarta +
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

function loadAddresses() {
    viewAddresses()
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
                addresses.forEach(function(address) {
                    let div = document.createElement('div');
                    div.innerHTML = '<div></div> Indirizzo: ' + address.via + ' , num: ' +
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