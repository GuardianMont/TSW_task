function loadOrders() {
    document.getElementById("edit-container").style.display = "none"
    document.getElementById("profile-container").style.display = "none"
    document.getElementById("change-password-container").style.display = "none"
    document.getElementById("ordiniEffettuati").style.display="block"

    document.getElementById("profilo").style.display="inline-block"

    document.getElementById("modifica").style.display="inline-block"
    document.getElementById("cambiaPassword").style.display="inline-block"
    document.getElementById("viewOrdini").style.display="none"
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'CheckoutServlet');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            console.log(response);  // serve per visualizzare in conosole la stringa JSON che mi invia la servlet
            //in modo anche da vedere se le proprietà e le varie info sono state settate correttamente

            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error loading ordini:', response.error);
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

function orderOrdini(){
    var dropdown = document.getElementById('sortingDropdown');
    var selectedValue = dropdown.value;
    console.log('Opzione selezionata:', selectedValue);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'CheckoutServlet');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            console.log(response);  // serve per visualizzare in conosole la stringa JSON che mi invia la servlet
            //in modo anche da vedere se le proprietà e le varie info sono state settate correttamente

            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error('Request failed. Network error.');
    };
    var data = 'opzione=order&order=' + encodeURIComponent(selectedValue);
    xhr.send(data);
}


//uso render Orders per evitare codice ridondante tra le due funzioni
//dato che entrambe visualizzano le medesime informazioni
function renderOrders(ordini) {
    const container = document.getElementById("checkOutOrdini");
    container.innerHTML = '';
    ordini.forEach(function(ordine) {
        console.log(ordine);
        let div = document.createElement('div');
        div.classList.add('ordine-container');
        //ordine container è il nome della classe che uso per definire il css che avvolge gli ordini

        let orderInfo = document.createElement('div');
        orderInfo.classList.add('ordine-info');
        orderInfo.innerHTML = '<div>Ordine effettuato il: ' + ordine.dataOrdine +
            ', Codice Fattura: ' + ordine.ordineFattura + '</div>';
        // per visualizzare il rettangolino solo con info ordini faccio questo div sepratore
        //con ovviamente classe specifica

        //bottone aggiunta per scaricare il pdf
        let buttonFattura = document.createElement("button");
        buttonFattura.textContent = 'Scarica Fattura';
        buttonFattura.addEventListener("click", function() {
            downloadInvoice(ordine.ordineId);
        });


        let address = ordine.address ? ordine.address.via : 'No info';
        let paymentMethod = ordine.paymentMethod ? ordine.paymentMethod.numCarta : 'No info';
        //in pratica se indirizzo o metodo di pagamento per sbaglio non vengono correttamente estratti pongo un No info

        let prezzoTotale = ordine.prezzototale.toFixed(2);
        //mi assicuro di arrotondare il prezzo a due cifre decimali

        let orderDetails = document.createElement('div');
        orderDetails.innerHTML = '<div>Indirizzo: ' + address + '</div>' +
            '<div>Metodo di Pagamento: ' + paymentMethod + '</div>' +
            '<div>Prezzo Totale: ' + prezzoTotale + '€</div>' +
            '<div>Prodotti:</div>';

        let prodottiList = document.createElement('ul');
        if (ordine.cartItems) {
            ordine.cartItems.forEach(function (item) {
                let prezzo = item.prezzo.toFixed(2);
                let prodotto = document.createElement("li");
                prodotto.innerHTML = 'Nome: ' + item.nome +
                    ', Quantità: ' + item.quantity +
                    ', Prezzo: ' + prezzo + '€';
                prodottiList.appendChild(prodotto);
            });
        }
        orderDetails.appendChild(prodottiList);

        div.appendChild(orderInfo);
        div.appendChild(buttonFattura);
        div.appendChild(orderDetails);
        container.appendChild(div);
    });
}

function downloadInvoice(orderId) {
    const link = document.createElement('a');
    link.href = 'CheckoutServlet?orderId=' + orderId + '&opzione=pdf';
    link.download = 'fattura_' + orderId + '.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}