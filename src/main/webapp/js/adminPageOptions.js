function RemoveHiddenUsers() {
    document.getElementById('userView').classList.remove('hide'); // Mostra la sezione degli utenti
    document.getElementById('ordiniEffettuati').classList.add('hide'); // Nasconde la sezione degli ordini
    document.getElementById("orderDetail").classList.add("hide");
}

function RenderUserOrder() {
    document.getElementById('userView').classList.add('hide'); // Nasconde la sezione degli utenti
    document.getElementById('ordiniEffettuati').classList.remove('hide'); // Mostra la sezione degli ordini
    document.getElementById("orderDetail").classList.add("hide");//Nasconde la sezione del dettaglio ordini
}

function UserOrderDetail() {
    document.getElementById('userView').classList.add('hide'); // Nasconde la sezione degli utenti
    document.getElementById('ordiniEffettuati').classList.add('hide'); // Nasconde la sezione degli ordini
    document.getElementById("orderDetail").classList.remove("hide");//Mostra la sezione del dettaglio ordini
}

function viewUsers() {
    RemoveHiddenUsers();
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/TSW_task_war_exploded/UserAdminOption");
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                displayUsers(response.data);
            } else {
                console.error('Error loading utenti:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = "opzione=viewUsers";
    xhr.send(data);
}

function displayUsers(users) {
    const UserTable = document.getElementById('UtentiInfo');
    UserTable.innerHTML = ''; // Svuota la tabella prima di aggiungere nuove righe

    document.querySelector('.userView thead').classList.remove('hide-elements'); // Mostra l'intestazione della tabella

    users.forEach(user => {
        const row = document.createElement('tr');
        const role = user.isAdmin ? 'admin' : 'utente';
        row.innerHTML = `
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.nome}</td>
            <td>${user.cognome}</td>
            <td>${user.phoneNumber}</td>
            <td>${role}</td>
            <td><button onclick="loadOrders('${user.username}')">View Orders</button></td>`;
        UserTable.appendChild(row);
    });
}

function loadOrders(userId) {
    RenderUserOrder();
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=show&userAdmin=' + encodeURIComponent(userId);
    xhr.send(data);
}

function viewOrders() {
    RenderUserOrder();
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error loading ordini:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=viewAll';
    xhr.send(data);
}

function renderOrders(ordini) {
    const container = document.getElementById("checkOutOrdini");
    container.innerHTML = '';
    ordini.forEach(ordine => {
        console.log(ordine);
        const div = document.createElement('div');
        div.classList.add('ordine-container');

        const orderInfo = document.createElement('div');
        orderInfo.classList.add('ordine-info');
        orderInfo.innerHTML = `<div>Ordine effettuato il: ${ordine.dataOrdine}, Codice Fattura: ${ordine.ordineFattura}</div>`;

        const buttonFattura = document.createElement("button");
        const buttonDettagli = document.createElement("button");
        buttonFattura.textContent = 'Scarica Fattura';
        buttonFattura.addEventListener("click", function() {
            downloadInvoice(ordine.ordineId);
        });
        buttonDettagli.textContent = "Dettaglio Ordine";
        buttonDettagli.addEventListener("click", function() {
            detaglioOrderUser(ordine.ordineId);
        });

        const address = ordine.address ? ordine.address.via : 'No info';
        const paymentMethod = ordine.paymentMethod ? ordine.paymentMethod.numCarta : 'No info';
        const prezzoTotale = ordine.prezzototale.toFixed(2);

        const orderDetails = document.createElement('div');
        orderDetails.innerHTML = `<div>Indirizzo: ${address}</div>
            <div>Metodo di Pagamento: ${paymentMethod}</div>
            <div>Prezzo Totale: ${prezzoTotale}€</div>
            <div>Prodotti:</div>`;

        const prodottiList = document.createElement('ul');
        if (ordine.cartItems) {
            ordine.cartItems.forEach(item => {
                const prezzo = item.prezzoUnitario.toFixed(2);
                const prodotto = document.createElement("li");
                prodotto.innerHTML = `Nome: ${item.nome}, Quantità: ${item.quantity}, Prezzo: ${prezzo}€`;
                prodottiList.appendChild(prodotto);
            });
        }
        orderDetails.appendChild(prodottiList);

        div.appendChild(orderInfo);
        div.appendChild(buttonFattura);
        div.appendChild(buttonDettagli);
        div.appendChild(orderDetails);
        container.appendChild(div);
    });
}

function filterOrdersByDate() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/AdminViewOrders');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);
            if (response.success) {
                renderOrders(response.data);
            } else {
                console.error('Error filtering orders:', response.error);
            }
        } else {
            console.error('Request failed. Status:', xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error('Request failed. Network error.');
    };
    const data = 'opzione=filterByDate&startDate=' + startDate + '&endDate=' + endDate;
    xhr.send(data);
}

function detaglioOrderUser(orderId) {
    UserOrderDetail();
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/TSW_task_war_exploded/DetailOrder');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            console.log(response);  // serve per visualizzare in conosole la stringa JSON che mi invia la servlet
            //in modo anche da vedere se le proprietà e le varie info sono state settate correttamente

            if (response.success) {
                //chiamo display per rappresentare le informazioni
                displayOrderDetails(response);
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
    var data = 'opzione=detail&orderId=' + encodeURIComponent(orderId);
    xhr.send(data);
}